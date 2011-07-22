package com.greenriver.commons.web.configuration;

import java.util.Properties;

/**
 *
 * @author luisro
 */
public interface DojoHandled {
    String getDojoControllerModule();

    void setDojoControllerModule(String dojoModuleName);
    
    Properties getControllerInitArgs();
    String getControllerInitArgsJson();
}
