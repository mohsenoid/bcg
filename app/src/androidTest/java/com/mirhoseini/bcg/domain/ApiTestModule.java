package com.mirhoseini.bcg.domain;

import com.mirhoseini.bcg.domain.client.Api;

import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

/**
 * Created by Mohsen on 08/01/2017.
 */

public class ApiTestModule extends ApiModule {

    @Override
    public Api provideApiService(Retrofit retrofit) {
        // replace Api with Mocked one
        return mock(Api.class);
    }

}
