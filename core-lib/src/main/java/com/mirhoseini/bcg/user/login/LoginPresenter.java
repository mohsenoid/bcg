package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.base.BasePresenter;

/**
 * Created by Mohsen on 06/01/2017.
 */

interface LoginPresenter extends BasePresenter<LoginView> {

    void login(String email, String password);

    boolean isEmailValid(String email);

    boolean isPasswordValid(String password);

}
