package com.mirhoseini.bcg.util;

import android.util.Base64;

import javax.inject.Inject;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class SecurityImpl implements Security {

    @Inject
    public SecurityImpl() {
    }

    @Override
    public String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
