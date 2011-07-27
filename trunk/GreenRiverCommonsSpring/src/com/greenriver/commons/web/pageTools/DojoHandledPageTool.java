package com.greenriver.commons.web.pageTools;

import com.greenriver.commons.web.configuration.DojoHandled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.directwebremoting.json.JsonUtil;

/**
 *
 * @author luisro
 */
public class DojoHandledPageTool extends PageTool implements DojoHandled{
    String dojoControllerModule;
    String controllerInitArgsJson;

    @Override
    protected void internalInitialize() {
        
        this.getDojoModules().add(dojoControllerModule);
        
        try {
            this.controllerInitArgsJson = JsonUtil.toJson(this.getControllerInitArgs()).replace("\"","'");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    
    
    

    @Override
    public List<String> getOnLoadScripts() {
        List<String> scripts = new ArrayList<String>(super.getOnLoadScripts());
        scripts.add(String.format(
                "window['%s'] = new %s(%s);",
                this.getName(),
                this.dojoControllerModule,
                this.controllerInitArgsJson));
        
        return scripts;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    
    
    
    @Override
    public String getDojoControllerModule() {
        return dojoControllerModule;
    }
    
    @Override
    public void setDojoControllerModule(String dojoModuleName) {
        this.dojoControllerModule = dojoModuleName;
    }
    
    @Override
    public Properties getControllerInitArgs() {
        Properties p = new Properties();
        p.put("dojoControllerModule", dojoControllerModule);
        p.put("toolName",this.getName());
        
        return p;
    }
    
    @Override
    public String getControllerInitArgsJson() {
        return controllerInitArgsJson;
    }
    
    //</editor-fold>
    
}
