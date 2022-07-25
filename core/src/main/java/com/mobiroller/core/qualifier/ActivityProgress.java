package com.mobiroller.core.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Make differ MainComponent ProgressHelper
 * and FragmentComponent ProgressHelper
 * with this @ActivityProgress annotation
 *
 * Created by ealtaca on 30.05.2017.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityProgress {
}
