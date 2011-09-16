package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.PageConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public class LoginController 
        extends DojoHandledPageController {

    public LoginController() {
        this.setDojoControllerModule("grc.web.LoginPageController");
    }

    @Override
    public void customConfiguration(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {

        super.customConfiguration(request, response, configuration, mav);
        
        boolean authError = String.valueOf(
                request.getParameter("login_error")).equals("1");
        boolean accessDenied = String.valueOf(
                request.getParameter("access_denied")).equals("1");
        boolean sessionExpired = String.valueOf(
                request.getParameter("session_expired")).equals("1");

        mav.addObject("error", authError);
        mav.addObject("accessDenied", accessDenied);
        mav.addObject("sessionExpired", sessionExpired);
    }

}
