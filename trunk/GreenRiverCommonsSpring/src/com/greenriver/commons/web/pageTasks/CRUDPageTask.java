
package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.Strings;
import java.util.Properties;

/**
 * Defines de config for a page task able to list add edit and remove elements.
 * @author luisro
 */
public class CRUDPageTask extends GridAndPropsPageTask {
    private String formClass;
    private String getForEditMethod="getForEdit";
    private String removeMethod="remove";
    private String getNewMethod="getNew";
    private String saveMethod = "save";
    private String formDialogControllerModule="grc.web.dialog.FormDialogController";

    public CRUDPageTask() {
        this.setDojoControllerModule("grc.web.tasks.CRUDPageTaskController");
        this.setMainJspFileName("../../crudPageTask.jsp");
    }
    
    

    @Override
    public Properties getControllerInitArgs() {
        Properties p = super.getControllerInitArgs();
        
        if (Strings.isNullOrEmpty(getForEditMethod)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its getForEditMethod property");
        }
        p.put("getForEditMethod",getForEditMethod);
        if (Strings.isNullOrEmpty(removeMethod)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its removeMethod property");
        }
        p.put("removeMethod",removeMethod);
        if (Strings.isNullOrEmpty(getNewMethod)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its getNewMethod property");
        }
        p.put("getNewMethod",getNewMethod);
        if (Strings.isNullOrEmpty(saveMethod)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its saveMethod property");
        }
        p.put("saveMethod",saveMethod);
        
        p.put("formDialogController", this.formDialogControllerModule);
        
        return p;
    }

    @Override
    protected void initializeInternal() {
        super.initializeInternal();
        
        if(Strings.isNullOrEmpty(formClass)) {
            throw new IllegalStateException("Task "+this.getTaskName()+" doesn't speficy its formClass field");
        }
        
        this.addForm("form", formClass);
        
        this.addDojoModule(formDialogControllerModule);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
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
     * @return the getForEditMethod
     */
    public String getGetForEditMethod() {
        return getForEditMethod;
    }
    
    /**
     * @param getForEditMethod the getForEditMethod to set
     */
    public void setGetForEditMethod(String getForEditMethod) {
        this.getForEditMethod = getForEditMethod;
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
    
    /**
     * @return the formDialogControllerModule
     */
    public String getFormDialogControllerModule() {
        return formDialogControllerModule;
    }

    /**
     * @param formDialogControllerModule the formDialogControllerModule to set
     */
    public void setFormDialogControllerModule(String formDialogControllerModule) {
        this.formDialogControllerModule = formDialogControllerModule;
    }

    //</editor-fold>

    
    
}
