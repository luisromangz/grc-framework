package com.greenriver.commons.mvc.configuration;

import com.greenriver.commons.mvc.pageTools.PageTool;
import java.util.List;

/**
 * This interface defines the contract that must be implemented by
 * page controllers that want include a page tools system in the page they
 * serve.
 * 
 * @author luis
 */
public interface PageToolsConfiguration {
    /**
     * Adds a page tool to the page.
     * @param pageTool The <c>PageTool</c> instance being added.
     */
    void addPageTool(PageTool pageTool);

    /**
     * Retrieves the configured page tools for the page.
     * @return A list of <c>PageTool</c> instances.
     */
    List<PageTool> getPageTools();

    /**
     * Sets the page tools used by the controller, replacing the already
     * configured, if any.
     * 
     * @param pageTools
     */
    void setPageTools(List<PageTool> pageTools);
}
