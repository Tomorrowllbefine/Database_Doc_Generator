package com.kk.databasedocgenerator.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义注解 用于在Swagger中不显示
 * @author: mmkk
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME  )
public @interface NotIncludeSwagger  {}