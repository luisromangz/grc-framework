package com.greenriver.commons.mvc.helpers.header;

import java.util.ArrayList;
import java.util.List;

/**
 * Instances of this class hold the configuration of a page's header.
 * @author luis
 */
public class PageHeaderConfig implements HeaderConfig {

    private List<String> cssFiles;
    private List<String> dwrServices;
    private List<String> dojoBundles;
    private List<String> dojoModules;
    private List<String> javascriptFiles;
    private List<String> onLoadScripts;
    private List<String> scripts;
    private String title;

    public PageHeaderConfig() {
        cssFiles = new ArrayList<String>();
        dwrServices = new ArrayList<String>();
        dojoBundles = new ArrayList<String>();
        dojoModules = new ArrayList<String>();
        javascriptFiles = new ArrayList<String>();
        onLoadScripts = new ArrayList<String>();
        scripts = new ArrayList<String>();
    }

    @Override
    public void addCssFile(String cssFilename) {
        if (!cssFiles.contains(cssFilename)) {
            cssFiles.add(cssFilename);
        }
    }

    @Override
    public void addDwrService(String name) {
        if (!dwrServices.contains(name)) {
            dwrServices.add(name);
        }
    }

    @Override
    public void addDojoBundle(String bundleName) {
        if (!dojoBundles.contains(bundleName)) {
            dojoBundles.add(bundleName);
        }
    }

    @Override
    public void addDojoModule(String dojoModule) {
        if (!dojoModules.contains(dojoModule)) {
            dojoModules.add(dojoModule);
        }
    }

    @Override
    public void addJavaScriptFile(String jsFilename) {
        if (!javascriptFiles.contains(jsFilename)) {
            javascriptFiles.add(jsFilename);
        }
    }

    @Override
    public void addOnLoadScript(String code) {
        if (!onLoadScripts.contains(code)) {
            onLoadScripts.add(code);
        }
    }

    @Override
    public void addScript(String script) {
        if (!scripts.contains(script)) {
            scripts.add(script);
        }
    }

    @Override
    public List<String> getCssFiles() {
        return cssFiles;
    }

    @Override
    public List<String> getDojoModules() {
        return dojoModules;
    }

    @Override
    public List<String> getDwrServices() {
        return dwrServices;
    }

    @Override
    public List<String> getJavaScriptFiles() {
        return javascriptFiles;
    }

    @Override
    public List<String> getOnLoadScripts() {
        return onLoadScripts;
    }

    @Override
    public List<String> getScripts() {
        return scripts;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setCssFiles(List<String> cssFiles) {
        this.cssFiles = new ArrayList<String>();
        for(String cssFile : cssFiles) {
            this.addCssFile(cssFile);
        }
    }

    @Override
    public void setDwrServices(List<String> dwrServices) {
        this.dwrServices = new ArrayList<String>();
        for(String dwrService : dwrServices) {
            this.addDwrService(dwrService);
        }
    }

    @Override
    public void setDojoBundles(List<String> dojoBundles) {
        this.dojoBundles = new ArrayList<String>();
        for(String dojoBundle : dojoBundles) {
            this.addDojoBundle(dojoBundle);
        }
    }

    @Override
    public void setDojoModules(List<String> dojoModules) {
        this.dojoModules = new ArrayList<String>();
        for(String dojoModule : dojoModules) {
            this.addDojoModule(dojoModule);
        }
    }

    @Override
    public void setJavaScriptFiles(List<String> javascriptFiles) {
        this.javascriptFiles = new ArrayList<String>();
        for(String javaScriptFile : javascriptFiles) {
            this.addJavaScriptFile(javaScriptFile);
        }
    }

    @Override
    public void setOnLoadScripts(List<String> onLoadScripts) {
        this.onLoadScripts = new ArrayList<String>();
        for(String onLoadScript : onLoadScripts) {
            this.addOnLoadScript(onLoadScript);
        }
    }

    @Override
    public void setScripts(List<String> scripts) {
        this.scripts = new ArrayList<String>();
        for(String script : scripts) {
            this.addScript(script);
        }
    }

    @Override
    public List<String> getDojoBundles() {
        return this.dojoBundles;
    }

    @Override
    public void addDojoBundles(List<String> dojoBundles) {
        for(String bundle : dojoBundles){
            this.addDojoBundle(bundle);
        }
        
    }

    @Override
    public void addDojoModules(List<String> dojoModules) {
        for(String dojoModule : dojoModules) {
            this.addDojoModule(dojoModule);
        }
    }
}
