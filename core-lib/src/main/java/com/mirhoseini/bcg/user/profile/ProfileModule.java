package com.mirhoseini.bcg.user.profile;

import com.mirhoseini.bcg.user.UserInteractor;
import com.mirhoseini.bcg.user.UserInteractorImpl;
import com.mirhoseini.bcg.user.UserScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mohsen on 06/01/2017.
 */

@Module
public class ProfileModule {

    @Provides
    @UserScope
    public UserInteractor provideInteractor(UserInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @UserScope
    public ProfilePresenter providePresenter(ProfilePresenterImpl presenter) {
        return presenter;
    }

}
