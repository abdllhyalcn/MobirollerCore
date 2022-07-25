package com.mobiroller.core.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Specifies ApplicationLevel object
 *
 * Created by ealtaca on 29.05.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationLevel {}