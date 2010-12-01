package com.greenriver.commons.web.configuration;

import com.greenriver.commons.web.pageTools.PageTool;
import java.util.List;

/**
 * This interface defines the contract that must be implemented by
 * page controllers that want include a page tools system in the page they
 * serve.
 * 
 * @author luis
 */
public interface PageToolsConfig {
    List<PageTool> getPageTools();
    void setPageTools(List<PageTool> pageTools);
}
