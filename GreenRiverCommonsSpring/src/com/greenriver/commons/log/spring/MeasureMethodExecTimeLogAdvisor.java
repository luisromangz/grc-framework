
package com.greenriver.commons.log.spring;

import com.greenriver.commons.Strings;
import com.greenriver.commons.log.Log4jLogger;
import java.util.ArrayList;
import java.util.List;
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
                    "Profiled %1$s::%2$s(%3$s), execution: %4$s ms.",
                    pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().toShortString(),
                    getArgsAsString(pjp.getArgs()),
                    execTime));

        }

        return result;
    }

    private String getArgsAsString(Object [] args) {
        List<String> result = new ArrayList<String>();
        for (Object arg : args) {
            if (arg == null) {
                result.add("NULL");
            } else {
                result.add(arg.getClass().getSimpleName());
            }
        }
        return Strings.join(result, ", ");
    }

    @Override
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        return this.profile(pjp);
    }
}
