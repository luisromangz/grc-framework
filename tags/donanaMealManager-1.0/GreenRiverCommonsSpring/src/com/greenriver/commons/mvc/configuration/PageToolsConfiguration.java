package com.greenriver.commons.mvc.configuration;

import com.greenriver.commons.mvc.pageTools.PageToolManager;

/**
 * This interface defines the contract that must be implemented by
 * page controllers that want include a page tools system in the page they
 * serve.
 * 
 * @author luis
 */
public interface PageToolsConfiguration {
    PageToolManager getPageToolManager();
    void setPageToolManager(PageToolManager pageToolManager);
}
