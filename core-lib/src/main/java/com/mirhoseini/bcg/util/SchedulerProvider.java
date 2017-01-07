package com.mirhoseini.bcg.util;

import rx.Scheduler;

/**
 * Created by Mohsen on 06/01/2017.
 */

public interface SchedulerProvider {

    Scheduler mainThread();

    Scheduler backgroundThread();

}
