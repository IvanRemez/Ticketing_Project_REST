package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    // ^^ can be replaced by @Slf4j Annotation at Class level

    private String getUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount userDetails = (SimpleKeycloakAccount) authentication.getDetails();
    // ^^ used to access logged-in User details from Security (+ Keycloak)

        return userDetails.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || " +
            "execution(* com.cydeo.controller.TaskController.*(..))")
    public void anyProjectOrTaskControllerPC() {}

    @Before("anyProjectOrTaskControllerPC()")
    public void beforeAnyProjectOrTaskControllerAdvice(JoinPoint joinPoint) {

        log.info("Before -> Method: {}, User: {}",
                joinPoint.getSignature().toShortString(), getUserName());
    }

    @AfterReturning(pointcut = "anyProjectOrTaskControllerPC()", returning = "results")
    public void afterReturningAnyProjectOrTaskControllerAdvice(JoinPoint joinPoint,
                                                               Object results) {
        log.info("After Returning -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(), getUserName(),
                results.toString());
    }

    @AfterThrowing(pointcut = "anyProjectOrTaskControllerPC()", throwing = "exception")
    public void afterReturningAnyProjectOrTaskControllerAdvice(JoinPoint joinPoint,
                                                               Exception exception) {
        log.info("After Throwing -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(), getUserName(),
                exception.getMessage());
    }

}
