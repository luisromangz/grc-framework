/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a header configurer to be used with
 * the tiles template defined for the Sensis project.
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
 
    public void configure(ModelAndView mav) {
       mav.addObject("jsFiles", jsFiles);
       mav.addObject("jsScripts", scripts);
       mav.addObject("onLoadScripts", onLoadScripts);
       mav.addObject("dojoModules", dojoModules);
       mav.addObject("cssFiles", cssFiles);
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
