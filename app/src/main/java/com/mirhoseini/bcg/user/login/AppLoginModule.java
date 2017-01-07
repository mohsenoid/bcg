package com.mirhoseini.bcg.user.login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mohsen on 07/01/2017.
 */

@Module
public class AppLoginModule extends LoginModule {

    private LoginActivity activity;

    void bind(LoginActivity activity) {
        this.activity = activity;
    }

    void unbind() {
        this.activity = null;
    }

    @Provides
    LoginView provideLoginView() {
        return activity;
    }

}
