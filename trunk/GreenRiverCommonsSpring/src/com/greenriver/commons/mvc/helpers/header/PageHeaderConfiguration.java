
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

    public void configure(HeaderConfigurer headerConfigurer, ModelAndView mav) {
        headerConfigurer.setCssFiles(this.getCssFiles());
        headerConfigurer.setDojoBundles(this.getDojoBundles());
        headerConfigurer.setDojoModules(this.getDojoModules());
        headerConfigurer.setDwrServices(this.getDwrServices());
        headerConfigurer.setJavaScriptFiles(this.getJavaScriptFiles());
        headerConfigurer.setOnLoadScripts(this.getOnLoadScripts());
        headerConfigurer.setScripts(this.getScripts());
        headerConfigurer.setTitle(this.getTitle());

        headerConfigurer.configure(mav);
    }


    public void addCssFile(String cssFilename) {
        cssFiles.add(cssFilename);
    }

    public void addDwrService(String name) {
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
        this.cssFiles = new ArrayList<String>(cssFiles);
    }

    public void setDwrServices(List<String> dwrServices) {
        this.dwrServices = new ArrayList<String>(dwrServices);
    }

    public void setDojoBundles(List<String> dojoBundles) {
        this.dojoBundles = new ArrayList<String>(dojoBundles);
    }

    public void setDojoModules(List<String> dojoModules) {
        this.dojoModules = new ArrayList<String>(dojoModules);
    }

    public void setJavaScriptFiles(List<String> javascriptFiles) {
        this.javascriptFiles = new ArrayList<String>(javascriptFiles);
    }

    public void setOnLoadScripts(List<String> onLoadScripts) {
        this.onLoadScripts = new ArrayList<String>(onLoadScripts);
    }

    public void setScripts(List<String> scripts) {
        this.scripts = new ArrayList<String>(scripts);
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

}