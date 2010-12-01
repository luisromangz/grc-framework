package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.web.configuration.DojoHandled;

/**
 *
 * @author luisro
 */
public class DojoHandledPageTask
    extends PageTask
    implements DojoHandled {

    private String dojoControllerModule="grc.controller.PageTaskController";

    @Override
    public String getDojoControllerModule() {
        return dojoControllerModule;
    }

    @Override
    public void setDojoControllerModule(String dojoModuleName) {
        this.dojoControllerModule = dojoModuleName;
    }

}
