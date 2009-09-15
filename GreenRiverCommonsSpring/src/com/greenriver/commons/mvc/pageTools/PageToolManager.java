
package com.greenriver.commons.mvc.pageTools;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author luis
 */
public class PageToolManager {

    // The tools to be managed, which will be ordered by the criteria defined
    // for the PageTaks instances.
    private List<PageTool> tools;

    public PageToolManager () {
        tools = new ArrayList<PageTool>();
    }

    /**
     * @return the tools
     */
    public List<PageTool> getTools() {
        return tools;
    }

    /**
     * @param tools the tools to set
     */
    public void setTools(List<PageTool> tools) {
        this.tools = new ArrayList<PageTool>();
        this.tools.addAll(tools);
    }

    public void addTool(PageTool pageTool) {
        tools.add(pageTool);
    }

}
