package com.greenriver.commons.web.controllers;

import com.greenriver.commons.Strings;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.pageTools.PageTool;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public class PageToolsController extends ConfigurablePageController {

    @Override
    public void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {

        List<String> dialogJspFiles = new ArrayList<String>();
        List<String> setupJspFiles = new ArrayList<String>();
        for (PageTool pageTool : this.getPageTools()) {


            // We only load the jsp files if we are loading the
            // tool defferredly, as otherwise the tool has already been loaded.
            dialogJspFiles.addAll(Strings.addPrefix(
                    "tools/" + pageTool.getName()+"/",
                    pageTool.getDialogJspFiles()));

            dialogJspFiles.addAll(Strings.addPrefix(
                    "tools/" + pageTool.getName()+"/",
                    pageTool.getSetupPaneJspFiles()));

            //Forms ids are prefixed with the task name
            configureForms(
                    pageTool.getForms(), mav, configuration,pageTool.getName() + "_");

            configurePropertiesView(
                    pageTool.getPropertiesView(), mav,pageTool.getName() + "_");

        }


        mav.addObject("toolsDialogJspFiles", dialogJspFiles);
        mav.addObject("toolsSetupJspFiles", setupJspFiles);
    }
}
