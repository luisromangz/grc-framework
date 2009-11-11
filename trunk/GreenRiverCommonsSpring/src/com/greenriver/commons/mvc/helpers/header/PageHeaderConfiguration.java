package com.greenriver.commons.mvc.helpers.header;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

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
        if (!cssFiles.contains(cssFilename)) {
            cssFiles.add(cssFilename);
        }
    }

    public void addDwrService(String name) {
        if (!dwrServices.contains(name)) {
            dwrServices.add(name);
        }
    }

    public void addDojoBundle(String bundleName) {
        if (!dojoBundles.contains(bundleName)) {
            dojoBundles.add(bundleName);
        }
    }

    public void addDojoModule(String dojoModule) {
        if (!dojoModules.contains(dojoModule)) {
            dojoModules.add(dojoModule);
        }
    }

    public void addJavaScriptFile(String jsFilename) {
        if (!javascriptFiles.contains(jsFilename)) {
            javascriptFiles.add(jsFilename);
        }
    }

    public void addOnLoadScript(String code) {
        if (!onLoadScripts.contains(code)) {
            onLoadScripts.add(code);
        }
    }

    public void addScript(String script) {
        if (!scripts.contains(script)) {
            scripts.add(script);
        }
    }

    public List<String> getCssFiles() {
        return cssFiles;
    }

    public List<String> getDojoModules() {
        return dojoModules;
    }

    public List<String> getDwrServices() {
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
        this.cssFiles = new ArrayList<String>();
        for(String cssFile : cssFiles) {
            this.addCssFile(cssFile);
        }
    }

    public void setDwrServices(List<String> dwrServices) {
        this.dwrServices = new ArrayList<String>();
        for(String dwrService : dwrServices) {
            this.addDwrService(dwrService);
        }
    }

    public void setDojoBundles(List<String> dojoBundles) {
        this.dojoBundles = new ArrayList<String>();
        for(String dojoBundle : dojoBundles) {
            this.addDojoBundle(dojoBundle);
        }
    }

    public void setDojoModules(List<String> dojoModules) {
        this.dojoModules = new ArrayList<String>();
        for(String dojoModule : dojoModules) {
            this.addDojoModule(dojoModule);
        }
    }

    public void setJavaScriptFiles(List<String> javascriptFiles) {
        this.javascriptFiles = new ArrayList<String>();
        for(String javaScriptFile : javascriptFiles) {
            this.addJavaScriptFile(javaScriptFile);
        }
    }

    public void setOnLoadScripts(List<String> onLoadScripts) {
        this.onLoadScripts = new ArrayList<String>();
        for(String onLoadScript : onLoadScripts) {
            this.addOnLoadScript(onLoadScript);
        }
    }

    public void setScripts(List<String> scripts) {
        this.scripts = new ArrayList<String>();
        for(String script : scripts) {
            this.addScript(script);
        }
    }

    public List<String> getDojoBundles() {
        return this.dojoBundles;
    }

    public void addDojoBundles(List<String> dojoBundles) {
        for(String bundle : dojoBundles){
            this.addDojoBundle(bundle);
        }
        
    }

    public void addDojoModules(List<String> dojoModules) {
        for(String dojoModule : dojoModules) {
            this.addDojoModule(dojoModule);
        }
    }
}
