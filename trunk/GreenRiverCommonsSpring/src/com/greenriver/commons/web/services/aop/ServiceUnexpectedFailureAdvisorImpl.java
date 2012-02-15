package com.greenriver.commons.web.services.aop;

import com.greenriver.commons.web.services.Result;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 
 * @author luisro
 */
public class ServiceUnexpectedFailureAdvisorImpl
        implements ServiceUnexpectedFailureAdvisor {

    @Override
    public Object watch(ProceedingJoinPoint pjp) {
        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable ex) {
            // Unhandled failure catched!
            Logger.getLogger(this.getClass()).info("Unexpeted failure catched!");
            Result sr = new Result();
            sr.addErrorMessage("Error desconocido.");
            sr.setSuccess(false);
            return sr;
        }

        return result;
    }
}
