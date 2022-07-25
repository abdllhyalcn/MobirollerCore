package com.mobiroller.core.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Specifies FragmentLevel object
 *
 * Created by ealtaca on 30.05.2017.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {}
