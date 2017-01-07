package com.mirhoseini.bcg.user.login;

import com.mirhoseini.bcg.user.UserScope;

import dagger.Subcomponent;

/**
 * Created by Mohsen on 07/01/2017.
 */

@UserScope
@Subcomponent(modules = {
        AppLoginModule.class
})
public interface LoginSubComponent {

    void inject(LoginActivity activity);

}
