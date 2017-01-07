package com.mirhoseini.bcg.user.profile;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mohsen on 07/01/2017.
 */

@Module
public class AppProfileModule extends ProfileModule {

    private ProfileFragment fragment;

    void bind(ProfileFragment fragment) {
        this.fragment = fragment;
    }

    void unbind() {
        this.fragment = null;
    }

    @Provides
    ProfileView provideProfileView() {
        return fragment;
    }

}
