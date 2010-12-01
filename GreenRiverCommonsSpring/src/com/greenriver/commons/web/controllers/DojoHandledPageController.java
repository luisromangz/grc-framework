package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.DojoHandled;
import com.greenriver.commons.web.configuration.PageConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luisro
 */
public class DojoHandledPageController 
    extends ConfigurablePageController
    implements DojoHandled {
    
    private String dojoControllerModule = "grc.controller.PageController";

    @Override
    public void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {
        super.customHandleRequest(request, response, configuration, mav);

        configuration.addDojoModule(dojoControllerModule);

        mav.addObject("dojoControllerSetup", this.getDojoControllerSetup());
    }



    public String getDojoControllerSetup() {
        return "new "+getDojoControllerModule()+"();";
    }

    // <editor-fold defaultstate="collapsed" desc="Getter and setters">
    /**
     * @return the dojoControllerModule
     */
    public String getDojoControllerModule() {
        return dojoControllerModule;
    }

    /**
     * @param dojoControllerModule the dojoControllerModule to set
     */
    public void setDojoControllerModule(String dojoControllerModule) {
        this.dojoControllerModule = dojoControllerModule;
    }
    // </editor-fold>
}
