package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.helpers.header.HeaderConfigurer;
import com.greenriver.commons.web.helpers.header.HeaderConfigurerClient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * Implements the controller for the notice that tells the user that
 * the page is loading.
 * @author luis
 */
public class LoadingNoticeController extends ParameterizableViewController
    implements HeaderConfigurerClient {

    public LoadingNoticeController() {

    }

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        String id = request.getParameter("id");
        String loadingMessage = request.getParameter("loadingMessage");
        String startHidden = request.getParameter("startHidden");
        
        if(id == null || id.isEmpty()) {
            id="loadingHidder";
        }

        if(loadingMessage == null || loadingMessage.isEmpty()) {
            loadingMessage="Cargando";
        }

        if(startHidden == null || startHidden.isEmpty()) {
            startHidden="";
        }

        mav.addObject("id",id);
        mav.addObject("loadingMessage", loadingMessage);
        mav.addObject("startHidden", startHidden.equals("true"));

        return mav;
    }

    private HeaderConfigurer headerConfigurer;
    @Override
    public void setHeaderConfigurer(HeaderConfigurer bean) {
        headerConfigurer = bean;
    }

    @Override
    public HeaderConfigurer getHeaderConfigurer() {
        return headerConfigurer;
    }

}