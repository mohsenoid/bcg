package com.mirhoseini.bcg;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by Mohsen on 08/01/2017.
 */

public class BcgTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader classLoader, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        // replace Application class with mock one
        return super.newApplication(classLoader, BcgTestApplication.class.getName(), context);
    }

}
