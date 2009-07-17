package com.greenriver.commons.mvc.interceptors;

import com.greenriver.commons.data.transactions.TransactionManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author luis
 */
public class TransactionInterceptor implements HandlerInterceptor {

    private TransactionManager transactionManager;

    /**
     * @return the transactionManager
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;

    }

    /**
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        transactionManager.begin();
        return true;
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
        // Nothing to do here
    }

    public void afterCompletion(
            HttpServletRequest arg0,
            HttpServletResponse arg1,
            Object arg2,
            Exception exception) throws Exception {
        if(exception != null) {
            transactionManager.rollback();
        } else {
            transactionManager.commit();
        }
    }
}
