package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.user.UserInteractor;
import com.mirhoseini.bcg.user.profile.ProfileException;
import com.mirhoseini.bcg.util.Constants;
import com.mirhoseini.bcg.util.Mapper;
import com.mirhoseini.bcg.util.SchedulerProvider;
import com.mirhoseini.bcg.util.StateManager;

import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.subscriptions.Subscriptions;

/**
 * Created by Mohsen on 06/01/2017.
 */

class LoginPresenterImpl implements LoginPresenter {

    private static final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private final SchedulerProvider scheduler;
    private final UserInteractor interactor;
    private final StateManager stateManager;

    private LoginView view;
    private Subscription subscription = Subscriptions.empty();

    @Inject
    public LoginPresenterImpl(SchedulerProvider scheduler, UserInteractor interactor, StateManager stateManager) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        this.stateManager = stateManager;
    }

    @Override
    public void bind(LoginView view) {
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (null != view) {
            view.showProgress();
        }

        subscription = interactor.login(email, password)
                // check if login result code is OK
                .map(loginResponseResponse -> {
                    if (loginResponseResponse.isSuccessful()
                            && null != loginResponseResponse.body())
                        return loginResponseResponse.body();
                    else
                        throw Exceptions.propagate(new LoginException(loginResponseResponse.body()));
                })
                // cache token data
                .doOnNext(loginResponse -> stateManager.saveUserToken(loginResponse.getToken()))
                // cache user id
                .doOnNext(loginResponse -> stateManager.saveUserId(loginResponse.getUserId()))
                // get user profile
                .flatMap(loginResponse -> interactor.getProfile(loginResponse.getToken(), loginResponse.getUserId()))
                // check if get profile result code is OK
                .map(profileResponseResponse -> {
                    if (profileResponseResponse.isSuccessful()
                            && null != profileResponseResponse.body())
                        return profileResponseResponse.body();
                    else
                        throw Exceptions.propagate(new ProfileException(profileResponseResponse.body()));
                })
                // map profile response to profile model
                .map(Mapper::mapLoginResponseToUserModel)
                // cache profile data
                .doOnNext(stateManager::saveUserProfile)
                .observeOn(scheduler.mainThread())
                .subscribe(user -> {
                            if (null != view) {
                                view.hideProgress();
                                view.userLoggedIn();
                            }
                        },
                        // handle exceptions
                        throwable -> {
                            stateManager.clearUser();

                            if (null != view) {
                                view.hideProgress();

                                if (stateManager.isConnect()) {
                                    if (throwable instanceof LoginException)
                                        view.showInvalidUsernamePassword((LoginException) throwable);
                                    else if (throwable instanceof ProfileException)
                                        view.showInvalidProfile((ProfileException) throwable);
                                    else
                                        view.showRetryMessage(throwable);

                                } else {
                                    view.showOfflineMessage(true);
                                }
                            }
                        });
    }

    @Override
    public boolean isEmailValid(String email) {
        return null != email && !email.isEmpty() && email.contains("@") && emailPattern.matcher(email).matches();
    }

    @Override
    public boolean isPasswordValid(String password) {
        return null != password && !password.isEmpty() && password.length() > Constants.EMAIL_MIN_LENGTH;
    }

    @Override
    public void unbind() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();

        interactor.unbind();

        view = null;
    }

}
