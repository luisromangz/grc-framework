package com.greenriver.commons.log.spring;

import com.greenriver.commons.log.Log4jLogger;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.Authentication;
import org.springframework.security.BadCredentialsException;
import org.springframework.util.StringUtils;

/**
 * This class both extends Log4jLogger and implements AuthenticationLoggerAdvisor,
 * thus providing a Log4j based system to log a webapp's login process in an
 * aspect oriented way.
 *
 * @author luis
 */
public class Log4jAuthenticationLoggingAdvisor extends Log4jLogger
        implements AuthenticationLoggingAdvisor {

    /**
     * Wraps a call to Spring Security's
     * AuthenticationProvider.authenticate().
     */
    public Object logAuth(ProceedingJoinPoint call) throws Throwable {

        Logger authLog = this.getLogger();

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
            authLog.info("auth=fail;user=" + user + ";cause=" + bce.toString());
            throw bce;
        } catch (Exception e) {
            // A real error happened
            if (!getIgnoredExceptions().contains(e.getClass().getName())) {
                // If the exception is not ignored, we log it.
                authLog.error(
                        "auth=fail;user=" + user + ";cause=" + e.toString());
            }
            throw e;

        }

        if (result != null) {
            authLog.info("auth=pass;user=" + user);
        }

        return result;
    }
}

