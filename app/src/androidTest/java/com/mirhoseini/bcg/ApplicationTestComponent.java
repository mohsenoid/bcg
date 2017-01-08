package com.mirhoseini.bcg;


import com.mirhoseini.bcg.domain.ApiModule;
import com.mirhoseini.bcg.domain.ClientModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mohsen on 08/01/2017.
 */

@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class,
        ApiModule.class,
        ClientModule.class
})
public interface ApplicationTestComponent extends ApplicationComponent {

    void inject(MainActivityTest activity);

}