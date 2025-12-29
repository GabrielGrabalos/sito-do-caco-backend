package com.project.api.aspect;

import com.project.api.annotation.RateLimiter;
import com.project.api.exception.RateLimitExceededException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitingAspect {

    // A map to store Guava RateLimiters for each unique client + key
    private final ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter> limiters = new ConcurrentHashMap<>();

    /**
     * This pointcut intercepts any public method that either:
     * - has a RateLimiter annotation, or
     * - is declared in a class annotated with RateLimiter.
     */
    @Before("execution(public * *(..)) && " +
            "(@annotation(com.liberis.api.annotation.RateLimiter) || @within(com.liberis.api.annotation.RateLimiter))")
    public void rateLimitCheck(JoinPoint joinPoint) {
        // Determine the effective RateLimiter configuration (method overrides class)
        RateLimiter rateLimiterConfig = getEffectiveRateLimiter(joinPoint);
        if (rateLimiterConfig == null) {
            // Should not happen as our pointcut ensures at least one annotation exists.
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        String clientKey = getClientKey(request);
        // Build a composite key: if method annotation is present, use method signature;
        // otherwise use the controller (class) name.
        String scopeKey = getScopeKey(joinPoint, rateLimiterConfig);
        String compositeKey = clientKey + ":" + scopeKey;

        double permitsPerSecond = calculatePermitsPerSecond(rateLimiterConfig);

        com.google.common.util.concurrent.RateLimiter limiter = limiters.computeIfAbsent(compositeKey,
                key -> com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond)
        );

        if (!limiter.tryAcquire()) {
            throw new RateLimitExceededException(rateLimiterConfig.message());
        }
    }

    /**
     * Determine the effective RateLimiter annotation for the join point.
     * Method-level annotation takes precedence over the controller-level annotation.
     */
    private RateLimiter getEffectiveRateLimiter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // First check if the method itself has a RateLimiter annotation.
        RateLimiter methodAnnotation = method.getAnnotation(RateLimiter.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        // Otherwise, check if the declaring class (e.g. controller) is annotated.
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.getAnnotation(RateLimiter.class);
    }

    /**
     * Returns a scope key based on where the RateLimiter configuration was found.
     * This key is used to uniquely identify the rate limiter in our map.
     */
    private String getScopeKey(JoinPoint joinPoint, RateLimiter config) {
        // Check if the method is annotated.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getAnnotation(RateLimiter.class) != null) {
            // Method-level configuration: use the method signature as key.
            return method.toGenericString();
        } else {
            // Controller-level configuration: use the class name as key.
            return joinPoint.getTarget().getClass().getName();
        }
    }

    /**
     * Calculates the permits per second based on the configured requests per time window.
     */
    private double calculatePermitsPerSecond(RateLimiter rateLimiter) {
        // Convert requests per minute (or per X minutes) into permits per second.
        return rateLimiter.requests() / (60.0 * rateLimiter.time());
    }

    /**
     * Retrieves a key representing the client making the request.
     * For production use, consider using headers like X-Forwarded-For.
     */
    private String getClientKey(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
