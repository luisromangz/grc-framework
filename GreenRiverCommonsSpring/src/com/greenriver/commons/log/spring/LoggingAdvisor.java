package com.greenriver.commons.log.spring;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 *
 * @author luis
 */
public interface LoggingAdvisor {

    public Object log(ProceedingJoinPoint pjp) throws Throwable;

}
