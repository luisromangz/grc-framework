
package com.greenriver.commons.log.spring;

import com.greenriver.commons.log.Log4jLogger;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 *
 * @author luis
 */
public class Log4jLoggingAdvisor extends Log4jLogger
        implements LoggingAdvisor {

    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        Logger logger = getLogger();
        logger.debug(String.format("Entering %s::%s",
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().toShortString()));
        try {
            return pjp.proceed();
        } catch (Throwable e) {

            // Ignored classes are not logged.
            if(!getIgnoredExceptions().contains(e.getClass().getName())) {
                StringWriter writer = new StringWriter();

                e.printStackTrace(new PrintWriter(writer));

                String message = writer.toString();
                logger.error(message);
               
            }

            throw e;
        }
    }

}
