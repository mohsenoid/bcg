package com.mirhoseini.bcg.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mirhoseini.bcg.ApplicationComponent;
import com.mirhoseini.bcg.BcgApplication;

/**
 * Created by Mohsen on 07/01/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies(BcgApplication.get(this), BcgApplication.getComponent());

        // can be used for general purpose in all Activities of Application
    }

    protected abstract void injectDependencies(BcgApplication application, ApplicationComponent component);

    @Override
    public void finish() {
        super.finish();

        releaseSubComponents(BcgApplication.get(this));
    }

    protected abstract void releaseSubComponents(BcgApplication application);

}
