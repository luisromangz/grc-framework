package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.web.configuration.DojoHandled;
import com.greenriver.commons.web.configuration.PageConfig;
import org.springframework.web.servlet.ModelAndView;

/**
 * A page task that will use a client side dojo module to contain its client-side
 * functionality.
 * 
 * @author luisro
 */
public class DojoHandledPageTask
    extends PageTask
    implements DojoHandled {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    private String dojoControllerModule = "grc.controller.PageTaskController";
    // </editor-fold>
    
    @Override
    public void configurePage(PageConfig configuration, ModelAndView mav) {
        configuration.getDojoModules().add(dojoControllerModule);

        super.configurePage(configuration,mav);
    }


    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    @Override
    public String getDojoControllerModule() {
        return dojoControllerModule;
    }

    @Override
    public void setDojoControllerModule(String dojoModuleName) {
        this.dojoControllerModule = dojoModuleName;
    }
    // </editor-fold>
}
