package com.software.eric.coolweather.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by eric on 2018/11/23
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {}
