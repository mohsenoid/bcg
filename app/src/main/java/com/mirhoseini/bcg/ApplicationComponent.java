package com.mirhoseini.bcg;

import com.mirhoseini.bcg.domain.ApiModule;
import com.mirhoseini.bcg.domain.ClientModule;
import com.mirhoseini.bcg.user.login.AppLoginModule;
import com.mirhoseini.bcg.user.login.LoginSubComponent;
import com.mirhoseini.bcg.user.profile.AppProfileModule;
import com.mirhoseini.bcg.user.profile.ProfileSubComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mohsen on 07/01/2017.
 */

@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class,
        ApiModule.class,
        ClientModule.class
})
public interface ApplicationComponent {

    void inject(MainActivity activity);

    LoginSubComponent plus(AppLoginModule module);

    ProfileSubComponent plus(AppProfileModule module);

}