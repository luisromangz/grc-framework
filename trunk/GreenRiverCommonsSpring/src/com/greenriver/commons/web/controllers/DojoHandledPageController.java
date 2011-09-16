package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.DojoHandled;
import com.greenriver.commons.web.configuration.PageConfig;
import java.util.Properties;
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
    
    private String dojoControllerModule = "grc.web.PageController";
    private String controllerInitArgsJson="";

    @Override
    protected void customConfiguration(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {
        super.customConfiguration(request, response, configuration, mav);

        configuration.addDojoModule(dojoControllerModule);

        mav.addObject("dojoControllerModule", this.getDojoControllerModule());
    }

    @Override
    public Properties getControllerInitArgs() {
        return new Properties();
    }

    @Override
    public final String getControllerInitArgsJson() {
        return controllerInitArgsJson;
    }

    
    
    
   // <editor-fold defaultstate="collapsed" desc="Getter and setters">
    /**
     * @return the dojoControllerModule
     */
    @Override
    public String getDojoControllerModule() {
        return dojoControllerModule;
    }

    /**
     * @param dojoControllerModule the dojoControllerModule to set
     */
    @Override
    public void setDojoControllerModule(String dojoControllerModule) {
        this.dojoControllerModule = dojoControllerModule;
    }
    // </editor-fold>
}
