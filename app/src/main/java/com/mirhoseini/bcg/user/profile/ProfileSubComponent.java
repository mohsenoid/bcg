package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.user.UserScope;

import dagger.Subcomponent;

/**
 * Created by Mohsen on 07/01/2017.
 */

@UserScope
@Subcomponent(modules = {
        AppProfileModule.class
})
public interface ProfileSubComponent {

    void inject(ProfileFragment fragment);

}
