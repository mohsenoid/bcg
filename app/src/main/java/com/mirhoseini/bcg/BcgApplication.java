package com.mirhoseini.bcg;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mirhoseini.bcg.user.login.AppLoginModule;
import com.mirhoseini.bcg.user.login.LoginSubComponent;
import com.mirhoseini.bcg.user.profile.AppProfileModule;
import com.mirhoseini.bcg.user.profile.ProfileSubComponent;

/**
 * Created by Mohsen on 07/01/2017.
 */

public abstract class BcgApplication extends Application {

    private static ApplicationComponent component;
    private LoginSubComponent loginSubComponent;
    private AppLoginModule loginModule;
    private ProfileSubComponent profileSubComponent;
    private AppProfileModule profileModule;

    public static ApplicationComponent getComponent() {
        return component;
    }

    public static BcgApplication get(Context context) {
        return (BcgApplication) context.getApplicationContext();
    }

    protected AndroidModule getAndroidModule() {
        return new AndroidModule(this);
    }

    public LoginSubComponent getLoginSubComponent() {
        if (null == loginSubComponent)
            createLoginSubComponent();

        return loginSubComponent;
    }

    public LoginSubComponent createLoginSubComponent() {
        loginSubComponent = component.plus(getLoginModule());
        return loginSubComponent;
    }

    public AppLoginModule getLoginModule() {
        if (null == loginModule)
            createLoginModule();

        return loginModule;
    }

    private AppLoginModule createLoginModule() {
        loginModule = new AppLoginModule();

        return loginModule;
    }

    public void releaseLoginSubComponent() {
        loginModule = null;
        loginSubComponent = null;
    }

    public ProfileSubComponent getProfileSubComponent() {
        if (null == profileSubComponent)
            createProfileSubComponent();

        return profileSubComponent;
    }

    public ProfileSubComponent createProfileSubComponent() {
        profileSubComponent = component.plus(getProfileModule());
        return profileSubComponent;
    }

    public AppProfileModule getProfileModule() {
        if (null == profileModule)
            createProfileModule();

        return profileModule;
    }

    private AppProfileModule createProfileModule() {
        profileModule = new AppProfileModule();

        return profileModule;
    }

    public void releaseProfileSubComponent() {
        profileModule = null;
        profileSubComponent = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initApplication();

        component = createComponent();
    }

    public ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .androidModule(getAndroidModule())
                .build();
    }

    public abstract void initApplication();

}
