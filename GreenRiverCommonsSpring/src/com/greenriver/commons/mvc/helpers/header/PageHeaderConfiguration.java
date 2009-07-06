
package com.greenriver.commons.mvc.helpers.header;

import java.util.ArrayList;
import java.util.List;

/**
 * Instances of this class hold the configuration of a page's header.
 * @author luis
 */
public class PageHeaderConfiguration implements HeaderConfiguration {

    private List<String> cssFiles;
    private List<String> dwrServices;
    private List<String> dojoBundles;
    private List<String> dojoModules;
    private List<String> javascriptFiles;
    private List<String> onLoadScripts;
    private List<String> scripts;
    private String title;

    public PageHeaderConfiguration() {
        cssFiles = new ArrayList<String>();
        dwrServices = new ArrayList<String>();
        dojoBundles = new ArrayList<String>();
        dojoModules = new ArrayList<String>();
        javascriptFiles = new ArrayList<String>();
        onLoadScripts = new ArrayList<String>();
        scripts = new ArrayList<String>();
    }


    public void addCssFile(String cssFilename) {
        cssFiles.add(cssFilename);
    }

    public void addDWRService(String name) {
        dwrServices.add(name);
    }

    public void addDojoBundle(String bundleName) {
        dojoBundles.add(bundleName);
    }

    public void addDojoModule(String dojoModule) {
        dojoModules.add(dojoModule);
    }

    public void addJavaScriptFile(String jsFilename) {
        javascriptFiles.add(jsFilename);
    }

    public void addOnLoadScript(String code) {
       onLoadScripts.add(code);
    }

    public void addScript(String script) {
       scripts.add(script);
    }

    public List<String> getCssFiles() {
        return cssFiles;
    }

    public List<String> getDojoModules() {
        return dojoModules;
    }

    public List<String> getDWRServices() {
        return dwrServices;
    }

    public List<String> getJavaScriptFiles() {
        return javascriptFiles;
    }

    public List<String> getOnLoadScripts() {
        return onLoadScripts;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCssFiles(List<String> cssFiles) {
        this.cssFiles = cssFiles;
    }

    public void setDWRServices(List<String> dwrServices) {
        this.dwrServices = dwrServices;
    }

    public void setDojoBundles(List<String> dojoBundles) {
        this.dojoBundles = dojoBundles;
    }

    public void setDojoModules(List<String> dojoModules) {
        this.dojoModules = dojoModules;
    }

    public void setJavaScriptFiles(List<String> javascriptFiles) {
        this.javascriptFiles = javascriptFiles;
    }

    public void setOnLoadScripts(List<String> onLoadScripts) {
        this.onLoadScripts = onLoadScripts;
    }

    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }

    public List<String> getDojoBundles() {
        return this.dojoBundles;
    }

}
