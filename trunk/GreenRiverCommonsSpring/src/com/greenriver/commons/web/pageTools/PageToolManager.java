
package com.greenriver.commons.web.pageTools;

import com.greenriver.commons.Strings;
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
        for(PageTool tool : tools) {
            addTool(tool);
        }
    }

    public void addTool(PageTool pageTool) {
        if(Strings.isNullOrEmpty(pageTool.getName())){
            throw new IllegalArgumentException("Tool has no name");
        }

        if(tools.contains(pageTool)) {
            throw new IllegalArgumentException(
                    "Duplicate tool name "+pageTool.getName());
        }

        tools.add(pageTool);
    }

}
