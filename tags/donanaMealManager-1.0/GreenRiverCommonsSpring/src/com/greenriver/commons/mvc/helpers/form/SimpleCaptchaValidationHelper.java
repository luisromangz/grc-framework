/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.mvc.helpers.form;

import com.greenriver.commons.data.fieldProperties.CaptchaValidationHelper;
import com.greenriver.commons.session.SessionHelper;
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
        HttpSession session = SessionHelper.getSession();     
        Captcha captcha  = (Captcha) session.getAttribute(Captcha.NAME);
        return captcha.getAnswer();
    }
}
