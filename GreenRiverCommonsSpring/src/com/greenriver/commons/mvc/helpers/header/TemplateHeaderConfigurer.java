/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a header configurer to be used with
 * the tiles template defined for the project.
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
    }

    // <editor-fold defaultstate="collapsed" desc="Adding methods">
    public void addCssFile(String cssFilename) {
        cssFiles.add(cssFilename);
    }

    public void addJavaScriptFile(String jsFilename) {
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
    // </editor-fold>

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
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    public List<String> getCssFiles() {
        return cssFiles;
    }

    public List<String> getJavaScriptFiles() {
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

    public void addDwrService(String name) {
        dwrFiles.add(name);
    }

    public List<String> getDwrServices() {
        return dwrFiles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addDojoBundle(String bundleName) {
        this.dojoBundles.add(bundleName);
    }

    public void setCssFiles(List<String> cssFiles) {
        this.cssFiles = new ArrayList(cssFiles);
    }

    public void setDwrServices(List<String> dwrServices) {
        this.dwrFiles = new ArrayList(dwrServices);
    }

    public void setDojoBundles(List<String> dojoBundles) {
        this.dojoBundles = new ArrayList(dojoBundles);
    }

    public void setDojoModules(List<String> dojoModules) {
        this.dojoModules = new ArrayList(dojoModules);
    }

    @SuppressWarnings("unchecked")
    public void setJavaScriptFiles(List<String> javascriptFiles) {
        this.jsFiles = new ArrayList(javascriptFiles);
    }

    @SuppressWarnings("unchecked")
    public void setOnLoadScripts(List<String> onLoadScripts) {
        this.onLoadScripts = new ArrayList(onLoadScripts);
    }

    @SuppressWarnings("unchecked")
    public void setScripts(List<String> scripts) {
        this.scripts = new ArrayList(scripts);
    }

    public List<String> getDojoBundles() {
        return this.dojoBundles;
    }

    public void addDojoBundles(List<String> dojoBundles) {
        this.dojoBundles.addAll(dojoBundles);
    }

    public void addDojoModules(List<String> dojoModules) {
        this.dojoModules.addAll(dojoModules);
    }

    
    // </editor-fold>
}
