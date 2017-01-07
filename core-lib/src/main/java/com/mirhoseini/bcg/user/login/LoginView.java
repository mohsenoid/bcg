package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.base.BaseView;
import com.mirhoseini.bcg.user.profile.ProfileException;

/**
 * Created by Mohsen on 06/01/2017.
 */

interface LoginView extends BaseView {

    void showProgress();

    void hideProgress();

    void userLoggedIn();

    void showRetryMessage(Throwable throwable);

    void showInvalidUsernamePassword(LoginException loginException);

    void showInvalidProfile(ProfileException profileException);
}
