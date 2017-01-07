package com.mirhoseini.bcg.user;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Mohsen on 06/01/2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}
