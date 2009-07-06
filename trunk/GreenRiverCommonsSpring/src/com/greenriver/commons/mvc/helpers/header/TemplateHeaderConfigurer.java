/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a header configurer to be used with
 * the tiles template defined for the Sensis project.
 *
 * If used as a bean, it must be used within a "request" scope, as if not,
 * it would carry state between requests.
 * @author luis
 */
public class TemplateHeaderConfigurer implements HeaderConfigurer {

    private List<String> cssFiles;
    private List<String> jsFiles;
    private List<String> dwrFiles;
    private List<String> scripts;
    private List<String> dojoModules;
    private List<String> onLoadScripts;
    private Map<String,Object> objects;
    private String title;
    private List<String> dojoBundles;

    /**
     * Creates an instance of TemplateHeaderConfigurer.
     */
    public TemplateHeaderConfigurer() {
        cssFiles = new ArrayList<String>();
        jsFiles = new ArrayList<String>();
        scripts = new ArrayList<String>();
        dojoModules = new ArrayList<String>();
        onLoadScripts = new ArrayList<String>();
        dwrFiles = new ArrayList<String>();
        dojoBundles = new ArrayList<String>();

        objects = new HashMap<String, Object>();
    }

    public void useCssFile(String cssFilename) {
        cssFiles.add(cssFilename);
    }

    public void useJsFile(String jsFilename) {
        jsFiles.add(jsFilename);
    }

    public void addScript(String script) {       
        scripts.add(script);
    }

    public void addDojoModule(String dojoModule) {
        dojoModules.add(dojoModule);
    }

    public void addOnLoadScript(String code) {
        onLoadScripts.add(code);
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
     */
    public void configure(ModelAndView mav) {
       mav.addObject("jsFiles", jsFiles);
       mav.addObject("jsScripts", scripts);
       mav.addObject("onLoadScripts", onLoadScripts);
       mav.addObject("dojoModules", dojoModules);
       mav.addObject("cssFiles", cssFiles);
       mav.addObject("dwrServices", dwrFiles);
       // The following remains here for compatibility's sake.
       mav.addObject("dwrScripts", dwrFiles);
       mav.addObject("title", title);
       mav.addObject("dojoBundles", dojoBundles);

       mav.addAllObjects(objects);
    }

    public List<String> getCssFiles() {
        return cssFiles;
    }

    public List<String> getJsFiles() {
        return jsFiles;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public List<String> getDojoModules() {
        return dojoModules;
    }

    public List<String> getOnLoadScripts() {
        return onLoadScripts;
    }

    public void useDWRService(String name) {
        dwrFiles.add(name);
    }

    public List<String> getDwrScripts() {
        return dwrFiles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addObject(String identifier, Object object) {
        objects.put(identifier, object);
    }

    public void addDojoBundle(String bundleName) {
        dojoBundles.add(bundleName);
    }

}
