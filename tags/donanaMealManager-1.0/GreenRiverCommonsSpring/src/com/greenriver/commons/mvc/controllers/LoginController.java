package com.greenriver.commons.mvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public class LoginController extends ConfigurablePageController{

    public LoginController() {
    }

    @Override
    public void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            ModelAndView modelAndView) throws Exception {
        
        super.customHandleRequest(request, response, modelAndView);
        

        boolean authError =  String.valueOf(
                request.getParameter("login_error")).equals("1");
        boolean accessDenied =  String.valueOf(
                request.getParameter("access_denied")).equals("1");
        boolean sessionExpired =  String.valueOf(
                request.getParameter("session_expired")).equals("1");

        modelAndView.addObject("error",  authError);
        modelAndView.addObject("accessDenied",  accessDenied);
        modelAndView.addObject("sessionExpired", sessionExpired);
    }
}