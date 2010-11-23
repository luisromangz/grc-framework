package com.greenriver.commons.interceptors;

import com.greenriver.commons.Exceptions;
import com.greenriver.commons.log.Log4jLogger;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public class LoggingInterceptor extends Log4jLogger
        implements HandlerInterceptor {

    /**
     * Method included just to comply with the <c>HandlerInterceptor</c>
     * interface.
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest arg0, 
            HttpServletResponse arg1,
            Object arg2, ModelAndView arg3) throws Exception {
        // Nothing to do here
    }

    @Override
    public void afterCompletion(
            HttpServletRequest arg0,
            HttpServletResponse arg1,
            Object arg2,
            Exception exception) throws Exception {
        if (exception != null 
                && !getIgnoredExceptions().contains(
                    exception.getClass().getName())) {
            
            this.getLogger().error(Exceptions.formatException(exception, new Date()));
        } 
    }

    @Override
    public boolean preHandle(
            HttpServletRequest arg0,
            HttpServletResponse arg1,
            Object arg2) throws Exception {

        this.getLogger().debug(String.format("Requesting %s", arg0.getServletPath()));
        return true;
    }
}
