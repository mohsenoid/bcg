package com.mirhoseini.bcg.user;

import com.mirhoseini.bcg.domain.client.Api;
import com.mirhoseini.bcg.domain.model.AvatarResponse;
import com.mirhoseini.bcg.domain.model.LoginResponse;
import com.mirhoseini.bcg.domain.model.ProfileResponse;
import com.mirhoseini.bcg.util.SchedulerProvider;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

/**
 * Created by Mohsen on 06/01/2017.
 */

@UserScope
public class UserInteractorImpl implements UserInteractor {

    private final Api api;
    private final SchedulerProvider scheduler;

    private ReplaySubject<Response<LoginResponse>> loginSubject;
    private Subscription loginSubscription;

    private ReplaySubject<Response<ProfileResponse>> profileSubject;
    private Subscription profileSubscription;

    private ReplaySubject<Response<AvatarResponse>> avatarSubject;
    private Subscription avatarSubscription;

    @Inject
    public UserInteractorImpl(Api api, SchedulerProvider scheduler) {
        this.api = api;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<Response<LoginResponse>> login(String email, String password) {
        if (loginSubscription == null || loginSubscription.isUnsubscribed()) {
            loginSubject = ReplaySubject.create();

            loginSubscription = api.login(email, password)
                    .subscribeOn(scheduler.backgroundThread())
                    .subscribe(loginSubject);
        }

        return loginSubject.asObservable();
    }

    @Override
    public Observable<Response<ProfileResponse>> getProfile(String token, int userId) {
        if (profileSubscription == null || profileSubscription.isUnsubscribed()) {
            profileSubject = ReplaySubject.create();

            profileSubscription = api.getProfile(token, userId)
                    .subscribeOn(scheduler.backgroundThread())
                    .subscribe(profileSubject);
        }

        return profileSubject.asObservable();
    }

    @Override
    public Observable<Response<AvatarResponse>> setAvatar(String token, int userId, String avatarBase64) {
        if (avatarSubscription == null || avatarSubscription.isUnsubscribed()) {
            avatarSubject = ReplaySubject.create();

            avatarSubscription = api.setAvatar(token, userId, avatarBase64)
                    .subscribeOn(scheduler.backgroundThread())
                    .subscribe(avatarSubject);
        }

        return avatarSubject.asObservable();
    }


    @Override
    public void unbind() {
        if (loginSubscription != null && !loginSubscription.isUnsubscribed())
            loginSubscription.unsubscribe();
    }

}
