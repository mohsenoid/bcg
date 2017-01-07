package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.domain.model.AvatarResponse;
import com.mirhoseini.bcg.user.UserInteractor;
import com.mirhoseini.bcg.util.Constants;
import com.mirhoseini.bcg.util.SchedulerProvider;
import com.mirhoseini.bcg.util.Security;
import com.mirhoseini.bcg.util.StateManager;

import javax.inject.Inject;

import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.subscriptions.Subscriptions;

/**
 * Created by Mohsen on 06/01/2017.
 */

class ProfilePresenterImpl implements ProfilePresenter {

    private final SchedulerProvider scheduler;
    @Inject
    UserInteractor interactor;
    @Inject
    StateManager stateManager;
    @Inject
    Security security;
    private ProfileView view;
    private Subscription subscription = Subscriptions.empty();

    @Inject
    public ProfilePresenterImpl(SchedulerProvider scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void bind(ProfileView view) {
        this.view = view;
    }


    @Override
    public void setAvatar(byte[] avatarByteArray) {
        if (null != view) {
            view.showAvatarProgress();
        }

        if (!isAvatarFileSizeValid(avatarByteArray)) {
            if (null != view) {
                view.hideAvatarProgress();
                view.showAvatarSizeError();
            }
            return;
        }

        // encode avatar file to Base64
        String avatarBase64Bytes = security.toBase64(avatarByteArray);

        subscription = interactor.setAvatar(stateManager.loadUserToken(), stateManager.loadUserId(), avatarBase64Bytes)
                // check if set avatar result code is OK
                .map(avatarResponseResponse -> {
                    if (avatarResponseResponse.isSuccessful()
                            && null != avatarResponseResponse.body())
                        return avatarResponseResponse.body();
                    else
                        throw Exceptions.propagate(new AvatarException(avatarResponseResponse.body()));
                })
                .map(AvatarResponse::getAvatarUrl)
                .doOnNext(stateManager::updateAvatarUrl)
                .observeOn(scheduler.mainThread())
                .subscribe(avatarUrl -> {
                            if (null != view) {
                                view.hideAvatarProgress();
                                view.updateProfile();
                            }
                        },
                        // handle exceptions
                        throwable -> {
                            if (null != view) {
                                view.hideAvatarProgress();

                                if (stateManager.isConnect()) {
                                    if (throwable instanceof AvatarException)
                                        view.showAvatarError((AvatarException) throwable);
                                    else
                                        view.showRetryMessage(throwable);

                                } else {
                                    view.showOfflineMessage(true);
                                }
                            }
                        });
    }

    @Override
    public boolean isAvatarFileSizeValid(byte[] avatarByteArray) {
        return avatarByteArray.length <= Constants.AVATAR_MAX_FILE_SIZE;
    }

    @Override
    public void unbind() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();

        interactor.unbind();

        view = null;
    }

}
