package com.greenriver.commons.web.helpers.header;

import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a header configurer to be used with the
 * template defined for the project.
 *
 * If used as a bean, it must be used within a "request" scope, as if not,
 * it would carry state between requests.
 * @author luis
 */
public class TemplateHeaderConfigurer
        extends PageHeaderConfig
        implements HeaderConfigurer {

    public TemplateHeaderConfigurer() {
        super();
    }

    
    /**
     * Sets properties into a given ModelAndView object so
     * the page this object is going to be passed to gets all the
     * configuration done.
     * The added parameters are:
     *  - jsFiles, a collection of JavaScript file names.
     *  - jsScripts, a collection of pieces of JavaScript code.
     *  - onLoadScripts, a collection of pieces of JavaScript code
     * that must be run after the page finishes loading.
     *  - dojoModules, a list of dojo module full names (e.g. dijit.form.Button)
     * that are needed by the page.
     * - cssFiles, a list of CSS file names that should be loaded by the page.
     * - dwrServices, a list of DWR serviceNames to be used by the page.
     * - title, the page's title.
     * - dojoBundles, the name of the JavaScript file that bundles the Dojo
     * JavaScript files that the page needs to load.
     *
     * In order to this configuration to be effective, a view that receives
     * the configured ModelAndView object must use these properties in a
     * sensible way, which is not enforced here in any way.
     *
     * @param mav
     * @param configuration
     */
    @Override
    public void configure(ModelAndView mav, HeaderConfig configuration) {
       mav.addObject("jsFiles", configuration.getJavaScriptFiles());
       mav.addObject("jsScripts", configuration.getScripts());
       mav.addObject("onLoadScripts", configuration.getOnLoadScripts());
       mav.addObject("dojoModules", configuration.getDojoModules());
       mav.addObject("cssFiles", configuration.getCssFiles());
       mav.addObject("dwrServices", configuration.getDwrServices());
       // The following remains here for compatibility's sake.
       mav.addObject("dwrScripts", configuration.getDwrServices());
       mav.addObject("title", configuration.getTitle());
       mav.addObject("dojoBundles", configuration.getDojoBundles());
    }
}
