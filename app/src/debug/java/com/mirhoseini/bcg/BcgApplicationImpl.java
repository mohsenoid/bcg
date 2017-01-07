package com.mirhoseini.bcg;

import timber.log.Timber;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class BcgApplicationImpl extends BcgApplication {

    @Override
    public void initApplication() {

        // initialize Timber in debug version to log
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                // adding line number to logs
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });

    }
}
