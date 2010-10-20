
package com.greenriver.commons.log;

import com.greenriver.commons.Strings;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This is the base class for all Log4j loggers.
 * @author luis
 */
public class Log4jLogger {

    /**
     * The Log4j component used by this logger.
     */
    private String componentName;
    private List<String> ignoredExceptions;

    public Log4jLogger() {
        this.ignoredExceptions = new ArrayList<String>();
    }

    /**
     * Gets the Log4j's component name being used.
     * @return
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the Log4j's component name the logger will use.
     * @param componentName
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Returns the Log4j's logger used given the component name set, if any.
     * @return
     */
    public Logger getLogger() {
        Logger logger  = null;
        if (!Strings.isNullOrEmpty(this.getComponentName())) {
            logger = Logger.getLogger(this.getComponentName());
        } else {
            logger = Logger.getRootLogger();
        }
        return logger;
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
