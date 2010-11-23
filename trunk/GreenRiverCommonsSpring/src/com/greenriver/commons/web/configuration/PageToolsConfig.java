package com.greenriver.commons.web.configuration;

import com.greenriver.commons.web.pageTools.PageToolManager;

/**
 * This interface defines the contract that must be implemented by
 * page controllers that want include a page tools system in the page they
 * serve.
 * 
 * @author luis
 */
public interface PageToolsConfig {
    PageToolManager getPageToolManager();
    void setPageToolManager(PageToolManager pageToolManager);
}
