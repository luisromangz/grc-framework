package com.greenriver.commons.web.controllers.plugins;

import com.greenriver.commons.Strings;
import com.greenriver.commons.web.configuration.PageConfig;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luis
 */
public class DojoProfileCreatorPlugin implements ControllerPlugin {

    public void doWork(HttpServletRequest request, PageConfig configuration) {
        OutputStreamWriter out = null;
        String path = request.getSession().getServletContext().getRealPath("");
        path += "/" + request.getServletPath() + "-dojo-bundle.profile.js";

        String profileContent =
                "dependencies={\nstripConsole:'normal',\n"
                + "layers:[{\nname:'%s',\n dependencies: [\n'%s'\n]\n}],\n "
                + "prefixes:[\n['dijit','../dijit'],\n['dojox','../dojox'],\n['grc','../grc']\n]\n}";

        // We remove the initial slash and replace the dots with hyphens.
        String fileName = request.getServletPath().substring(1);
        fileName = fileName.replace('.', '-');
        profileContent = String.format(
                profileContent,
                fileName + "-dojo-bundle.js",
                Strings.join(configuration.getDojoModules(), "',\n'"));
        
        try {
            out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            out.write(profileContent);
            out.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioex) {
                }
            }
        }
    }
}
