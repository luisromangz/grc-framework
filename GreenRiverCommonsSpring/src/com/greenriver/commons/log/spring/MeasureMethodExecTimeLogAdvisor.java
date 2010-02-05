
package com.greenriver.commons.log.spring;

import com.greenriver.commons.log.Log4jLogger;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Profiler advisor that measures the execution time of a method and logs it
 * @author Miguel Angel
 */
public class MeasureMethodExecTimeLogAdvisor 
        extends Log4jLogger
        implements LoggingAdvisor {

    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        long execTime = System.currentTimeMillis();

        try {
            result = pjp.proceed();
        } finally {
            execTime = System.currentTimeMillis() - execTime;
            Logger logger = this.getLogger();
            logger.debug(String.format(
                    "Profiled method %1$s::%2$s, execution time: %3$s ms.",
                    pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().toShortString(),
                    execTime));
        }

        return result;
    }

    @Override
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        return this.profile(pjp);
    }
}
