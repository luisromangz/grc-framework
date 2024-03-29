package com.greenriver.commons.web.pageTasks;

import com.google.gson.Gson;
import com.greenriver.commons.Strings;
import com.greenriver.commons.web.configuration.DojoHandled;
import com.greenriver.commons.web.configuration.PageConfig;
import java.util.Properties;
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
    private String dojoControllerModule = "grc.web.tasks.PageTaskController";
    private String controllerInitArgsJson;
    // </editor-fold>
    
    @Override
    public void configurePage(PageConfig configuration, ModelAndView mav) {
        configuration.getDojoModules().add(dojoControllerModule);

        super.configurePage(configuration,mav);
    }

    @Override
    protected void initializeInternal() {
        super.initializeInternal();
        Gson gson = new Gson();
        this.controllerInitArgsJson= gson.toJson(this.getControllerInitArgs());
        this.controllerInitArgsJson = controllerInitArgsJson.replaceAll("\"", "'");
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

    @Override
    public Properties getControllerInitArgs() {
        Properties props = new Properties();
        if(Strings.isNullOrEmpty(this.getTaskName())) {
            throw new IllegalStateException("Task doesn't define its taskName");
        }
        props.put("taskName", this.getTaskName());
        
        if(Strings.isNullOrEmpty(this.getTitle())) {
            throw new IllegalStateException("Task "+this.getTaskName()+" doesn't specify its title property");
        }
        props.put("taskTitle", this.getTitle());
        
        if(Strings.isNullOrEmpty(dojoControllerModule)) {
            throw new IllegalStateException("Task "+this.getTaskName()+" doesn't specify its dojoControllerModule property");
        }
        props.put("dojoControllerModule", dojoControllerModule);
        
        return props;
    }

    @Override
    public final String getControllerInitArgsJson() {
       return controllerInitArgsJson;
    }
  
    
    
}
