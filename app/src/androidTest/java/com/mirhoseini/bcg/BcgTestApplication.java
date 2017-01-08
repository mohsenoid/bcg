package com.mirhoseini.bcg;

import android.support.annotation.NonNull;

import com.mirhoseini.bcg.domain.ApiTestModule;

/**
 * Created by Mohsen on 08/01/2017.
 */

public class BcgTestApplication extends BcgApplicationImpl {

    @Override
    public ApplicationTestComponent createComponent() {
        return DaggerApplicationTestComponent
                .builder()
                .androidModule(getAndroidModule())
                // replace Api Module with Mocked one
                .apiModule(getMockedApiModule())
                .build();
    }

    @NonNull
    private ApiTestModule getMockedApiModule() {
        return new ApiTestModule();
    }

}
