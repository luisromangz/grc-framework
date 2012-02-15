/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.web.services.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * This interface defines and advisor to handle unexpected service failures,
 * so the GUI doesn't keep waiting forever.
 * 
 * @author luisro
 */
public interface ServiceUnexpectedFailureAdvisor {
    public Object watch(ProceedingJoinPoint pjp);
}
