package com.cydeo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // where we want to put Annotation
@Retention(RetentionPolicy.RUNTIME) // will be active at Runtime
public @interface DefaultExceptionMessage {

    String defaultMessage() default "";

}