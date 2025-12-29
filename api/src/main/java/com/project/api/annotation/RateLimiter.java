package com.project.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})  // Allow on methods and types (controllers)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    int requests() default 10;         // Requests per minute
    int time() default 1;              // Time window in minutes
    String message() default "Too many requests. Please try again later.";
}
