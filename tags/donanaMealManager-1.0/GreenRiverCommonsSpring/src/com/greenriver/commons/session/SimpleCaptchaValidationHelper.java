/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.session;

import com.greenriver.commons.data.fieldProperties.CaptchaValidationHelper;
import javax.servlet.http.HttpSession;
import nl.captcha.Captcha;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implements captcha validation helper retrieving the value from the session.
 *
 * @author luisro
 */
public class SimpleCaptchaValidationHelper implements CaptchaValidationHelper {
    
    @Override
    public String getCaptchaValue() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true);        
        Captcha captcha  = (Captcha) session.getAttribute(Captcha.NAME);
        return captcha.getAnswer();
    }
}
