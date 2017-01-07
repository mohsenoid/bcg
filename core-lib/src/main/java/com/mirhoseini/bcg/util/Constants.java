package com.mirhoseini.bcg.util;

/**
 * Created by Mohsen on 06/01/2017.
 */

public abstract class Constants {

    public static final String BASE_URL = "http://91.109.18.74/";

    public static final int NETWORK_CONNECTION_TIMEOUT = 30; // 30 sec
    public static final long CACHE_SIZE = 10 * 1000 * 1000; // 10 MB
    public static final int CACHE_MAX_AGE = 2; // 2 min
    public static final int CACHE_MAX_STALE = 7; // 7 day

    public static final int EMAIL_MIN_LENGTH = 3;
    public static final int AVATAR_MAX_FILE_SIZE = 1 * 1000 * 1000; // 1 MB
}
