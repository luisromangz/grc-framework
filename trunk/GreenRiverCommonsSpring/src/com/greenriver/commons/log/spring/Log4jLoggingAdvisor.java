
package com.greenriver.commons.log.spring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StringUtils;

/**
 *
 * @author luis
 */
public class Log4jLoggingAdvisor implements LoggingAdvisor {

    private String componentName;

    private List<String> ignoredExceptions = new ArrayList<String>();

    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        Logger logger = getLogger();
        logger.debug(String.format("Entering %s::%s",
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().toShortString()));
        try {
            return pjp.proceed();
        } catch (Throwable e) {

            // Ignored classes are not logged.
            if(!ignoredExceptions.contains(e.getClass().getName())) {
                StringWriter writer = new StringWriter();

                e.printStackTrace(new PrintWriter(writer));

                String message = writer.toString();
                logger.error(message);
               
            }

            throw e;
        }
    }

    private Logger getLogger() {
        if(!StringUtils.hasText(componentName)) {
            return Logger.getRootLogger();
        }

        return Logger.getLogger(componentName);
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the ignoredExceptions
     */
    public List<String> getIgnoredExceptions() {
        return ignoredExceptions;
    }

    /**
     * @param ignoredExceptions the ignoredExceptions to set
     */
    public void setIgnoredExceptions(List<String> ignoredExceptions) {
        this.ignoredExceptions = ignoredExceptions;
    }
}
