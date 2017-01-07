package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.domain.model.LoginResponse;

/**
 * Created by Mohsen on 06/01/2017.
 */

class LoginException extends Exception {
    private final LoginResponse loginResponse;

    // TODO: 07/01/2017 Login Response can contain more information about error cause
    LoginException(LoginResponse loginResponse) {

        this.loginResponse = loginResponse;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }
}
