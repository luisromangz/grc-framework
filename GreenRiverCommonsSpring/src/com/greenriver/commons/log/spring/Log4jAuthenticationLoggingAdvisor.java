package com.greenriver.commons.log.spring;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.Authentication;
import org.springframework.security.BadCredentialsException;
import org.springframework.util.StringUtils;

/**
 *
 * @author luis
 */
public class Log4jAuthenticationLoggingAdvisor implements AuthenticationLoggingAdvisor {

    private String componentName;

    /**
     * Wraps a call to Spring Security's
     * AuthenticationProvider.authenticate().
     */
    public Object logAuth(ProceedingJoinPoint call) throws Throwable {

        Logger authLog;
        if (StringUtils.hasText(componentName)) {
            authLog = Logger.getLogger(componentName);
        } else {
            authLog = Logger.getRootLogger();
        }

        Authentication result;
        String user = "UNKNOWN";

        try {
            Authentication auth = (Authentication) call.getArgs()[0];
            user = auth.getName();
        } catch (Exception e) {
            // ignore
        }

        try {
            result = (Authentication) call.proceed();
        } catch (BadCredentialsException bce) {
            // The user doesn't exists of didn't write its password correctly,
            // no reason to complain about.
            authLog.info("auth=fail;user=" + user+";cause="+bce.toString());
            throw bce;
        } catch (Exception e) {
            // A real error happened
            authLog.error("auth=fail;user=" + user+";cause="+e.toString());
            throw e;
        }

        if (result != null) {
            authLog.info("auth=pass;user=" + user);
        }

        return result;
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
}

