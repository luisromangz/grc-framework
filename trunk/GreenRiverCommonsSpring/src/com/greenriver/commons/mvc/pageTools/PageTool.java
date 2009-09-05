package com.greenriver.commons.mvc.pageTools;

import java.util.ArrayList;
import java.util.List;

/**
 * This class' instances contain information about tools meant to be used
 * system wide.
 * @author luis
 */
public class PageTool {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The tool's name.
     */
    private String name;
    /**
     * The JSP files used as dialogs in the application.
     */
    private List<String> dialogJspFiles;
    /**
     * The JSP files used as panes in the config area of the app.
     */
    private List<String> setupPaneJspFiles;
    /**
     * The JavaScript files provided by the tool.
     */
    private List<String> javaScriptFiles;
    // </editor-fold>

    public PageTool() {
        name="Unnamed tool";
        dialogJspFiles = new ArrayList<String>();
        setupPaneJspFiles = new ArrayList<String>();
        javaScriptFiles = new ArrayList<String>();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dialogJspFiles
     */
    public List<String> getDialogJspFiles() {
        return dialogJspFiles;
    }

    /**
     * @param dialogJspFiles the dialogJspFiles to set
     */
    public void setDialogJspFiles(List<String> dialogJspFiles) {
        this.dialogJspFiles = dialogJspFiles;
    }

    /**
     * @return the setupPaneJspFiles
     */
    public List<String> getSetupPaneJspFiles() {
        return setupPaneJspFiles;
    }

    /**
     * @param setupPaneJspFiles the setupPaneJspFiles to set
     */
    public void setSetupPaneJspFiles(List<String> setupPaneJspFiles) {
        this.setupPaneJspFiles = setupPaneJspFiles;
    }

    public List<String> getJavaScriptFiles() {
        return this.javaScriptFiles;
    }

    /**
     * @param javaScriptFiles the javaScriptFiles to set
     */
    public void setJavaScriptFiles(List<String> javaScriptFiles) {
        this.javaScriptFiles = javaScriptFiles;
    }
    // </editor-fold>
}


