package com.greenriver.commons.log.spring;
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * Contract for advisors to log authentication activity when using
 * Spring Security jars.
 * @author luis
 */
public interface AuthenticationLoggingAdvisor {

    /**
     * Wraps a call to Spring Security's
     * AuthenticationProvider.authenticate().
     */
    Object logAuth(ProceedingJoinPoint call) throws Throwable;

}
