
package com.greenriver.commons.web.pageTools;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author luisro
 */
public class FormDialogPageTool extends DojoHandledPageTool{
    private String formClass;
    private String saveMethod="save";

    @Override
    protected void internalInitialize() {
        super.internalInitialize();
        
        this.getDialogJspFiles().add("../../formDialogPageTool.jsp");
    }
    
    @Override
    public Properties getControllerInitArgs() {
        Properties p =  super.getControllerInitArgs();
        p.put("form",this.getName()+"_form");
        p.put("dialog",this.getName()+"_dialog");
        return p;                
    }
    
    @Override
    public Map<String, String> getForms() {
        // We add the form the forms.
        Map<String,String> forms = new HashMap<String, String>(super.getForms());
        forms.put("form", formClass);
        return forms;
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    /**
     * @return the formClass
     */
    public String getFormClass() {
        return formClass;
    }
    
    /**
     * @param formClass the formClass to set
     */
    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }
    
    
    /**
     * @return the saveMethod
     */
    public String getSaveMethod() {
        return saveMethod;
    }

    /**
     * @param saveMethod the saveMethod to set
     */
    public void setSaveMethod(String saveMethod) {
        this.saveMethod = saveMethod;
    }
    //</editor-fold>


}
