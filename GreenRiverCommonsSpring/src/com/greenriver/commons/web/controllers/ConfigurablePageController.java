package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.configuration.FormsConfig;
import com.greenriver.commons.web.configuration.PageToolsConfiguration;
import com.greenriver.commons.web.configuration.PropertiesViewConfig;
import com.greenriver.commons.web.controllers.plugins.ControllerPlugin;
import com.greenriver.commons.mvc.helpers.form.FormBuilder;
import com.greenriver.commons.mvc.helpers.header.HeaderConfig;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.properties.PropertiesViewBuilder;
import com.greenriver.commons.mvc.pageTools.PageTool;
import com.greenriver.commons.mvc.pageTools.PageToolManager;
import com.greenriver.commons.session.UserSessionInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * This controller is intended to be used by configuring it through the Spring
 * configuration file. This allow to define the CSS files, JavaScript files,
 * etc., that are needed by the page whitout code.
 *
 * Classes specializing this one can override the <c>handleRequestInternal<c>
 * method, grabbing the <c>ModelAndView<c> object it returns (which is fully
 * configured) and add to it whatever extra objects are needed by the view.
 * @author luis
 */
public class ConfigurablePageController
        extends AbstractController
        implements PropertiesViewConfig,
        FormsConfig,
        HeaderConfig,
        PageToolsConfiguration,
        CustomizableHandleRequest {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private HeaderConfigurer headerConfigurer;
    private FormBuilder formBuilder;
    private PropertiesViewBuilder propertiesViewBuilder;
    private PageConfig pageConfiguration;
    private String viewName;
    private UserSessionInfo userSessionInfo;
    private PageToolManager pageToolManager;
    private List<ControllerPlugin> plugins;
    private boolean toolsLoadDelayed=true;
    // </editor-fold>

    public ConfigurablePageController() {
        // We intialize the pageConfiguration object;
        pageConfiguration = new PageConfig();
        plugins = new ArrayList<ControllerPlugin>();
    }

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        ModelAndView mav = new ModelAndView(viewName);

        configureForms(this.getForms(), mav, null);
        configurePropertiesView(this.getPropertiesView(), mav, null);
        configurePageTools(mav);

        if (this.userSessionInfo != null) {
            mav.addObject("userSessionInfo", this.userSessionInfo);
        }

        customHandleRequest(request, response, mav);

        PageConfig configuration = (PageConfig) this.pageConfiguration.clone();
       
        for (ControllerPlugin plugin : this.getPlugins()) {
            plugin.doWork(request, configuration);
        }

        headerConfigurer.configure(mav, configuration);

        return mav;
    }

    @Override
    public void customHandleRequest(HttpServletRequest request,
            HttpServletResponse response, ModelAndView mav) throws Exception {
    }

    // <editor-fold defaultstate="collapsed" desc="Page properties configuration methods">
    /**
     * Configures forms to edit entities from a map     
     * @param forms
     * @param mav
     * @param prefix Prefix to append to the name of the form
     * @throws ClassNotFoundException
     */
    protected void configureForms(
            Map<String, String> forms,
            ModelAndView mav,
            String prefix)
            throws ClassNotFoundException {

        if (formBuilder == null && forms.size() > 0) {
            throw new IllegalStateException(
                    "Must configure formBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        for (String formId : forms.keySet()) {
            String className = forms.get(formId);
            Class entityClass = Class.forName(className);
            formBuilder.addForm(prefix + formId,this.getPageConfiguration(),mav);
            formBuilder.addFieldsFromClass(entityClass);
        }
    }

    /**
     * Configures properties view from a map     
     * @param propertiesViews
     * @param mav
     * @param prefix Name prefix for the generated elements
     */
    protected void configurePropertiesView(
            Map<String, String> propertiesViews,
            ModelAndView mav,
            String prefix) {
        if (propertiesViewBuilder == null && propertiesViews.size() > 0) {
            throw new IllegalStateException(
                    "Must configure propertiesViewBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        for (String propsViewId : propertiesViews.keySet()) {
            propertiesViewBuilder.addPropertiesView(prefix + propsViewId, mav);            
            propertiesViewBuilder.addPropertyViewsFromClass(propertiesViews.get(propsViewId));
        }
    }

    private void configurePageTools(ModelAndView mav)
            throws ClassNotFoundException {

        List<String> dialogJspFiles = new ArrayList<String>();
        List<String> setupJspFiles = new ArrayList<String>();

        // This makes the initilization code needed by forms to be inside this function.
        pageConfiguration.addOnLoadScript("window['onToolsLoaded']=function(){");

        if (pageToolManager != null) {

            for (PageTool pageTool : this.pageToolManager.getTools()) {

                if (!this.toolsLoadDelayed) {
                    // We only load the jsp files if we are loading the
                    // tool with the page.
                    dialogJspFiles.addAll(addPathPrefixToFileNames(
                            "tools/" + pageTool.getName(),
                            pageTool.getDialogJspFiles()));

                    dialogJspFiles.addAll(addPathPrefixToFileNames(
                            "tools/" + pageTool.getName(),
                            pageTool.getSetupPaneJspFiles()));



                    configurePropertiesView(pageTool.getPropertiesView(), mav,
                            pageTool.getName() + "_");

                    pageConfiguration.getOnLoadScripts().addAll(
                            pageTool.getOnLoadScripts());
                }

                // We always process forms, even if loading on demand, so
                // we can ensure that we are getting all the dojo.requires
                // actually needed.

                //Forms ids are prefixed with the task name
                configureForms(pageTool.getForms(), mav,
                        pageTool.getName() + "_");


                // We always include the rest of things, so we are sure
                // we bundle (if bundling is activated) all the required modules
                // and js files.

                pageConfiguration.getJavaScriptFiles().addAll(addPathPrefixToFileNames(
                        "tools/" + pageTool.getName(),
                        pageTool.getJavaScriptFiles()));

                pageConfiguration.getCssFiles().addAll(addPathPrefixToFileNames(
                        pageTool.getName(),
                        pageTool.getCssFiles()));

                pageConfiguration.getDojoBundles().addAll(
                        pageTool.getDojoBundles());

                pageConfiguration.getDojoModules().addAll(
                        pageTool.getDojoModules());
                pageConfiguration.getDwrServices().addAll(
                        pageTool.getDwrServices());

                pageConfiguration.getScripts().addAll(pageTool.getScripts());

            }

        }

        // Close of the init function.
        pageConfiguration.addOnLoadScript("}");

        if(!this.isToolsLoadDelayed()) {
            pageConfiguration.addOnLoadScript("window.onToolsLoaded()");
        }

        mav.addObject("toolsDialogJspFiles", dialogJspFiles);
        mav.addObject("toolsSetupJspFiles", setupJspFiles);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters, setters and adders">
    /**
     * @param headerConfigurer the headerConfigurer to set
     */
    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer) {
        this.headerConfigurer = headerConfigurer;
    }

    public HeaderConfigurer getHeaderConfigurer() {
        return headerConfigurer;
    }

    /**
     * @param formBuilder the formBuilder to set
     */
    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }

    public FormBuilder getFormBuilder() {
        return formBuilder;
    }

    public void setPropertiesViewBuilder(
            PropertiesViewBuilder propertiesViewBuilder) {

        this.propertiesViewBuilder = propertiesViewBuilder;
    }

    public PropertiesViewBuilder getPropertiesViewBuilder() {
        return propertiesViewBuilder;
    }

    /**
     * The name of the view that is used to render the page that is managed
     * by the controller.
     * @param viewName the view to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Sets a page configuration object containing the configuration of
     * the page regarding JavaScript files, CSS files, etc.
     * @param pageConfiguration the pageConfiguration to set
     */
    public void setPageConfiguration(PageConfig pageConfiguration) {
        this.pageConfiguration = pageConfiguration;
    }

    /**
     * Adds an entity's name so a form capable of editing the object is created
     * automagically.
     * @param entityName The name of the entity the form will be created for.
     */
    @Override
    public void addForm(String id, String entityName) {
        getPageConfiguration().addForm(id, entityName);
    }

    /**
     * Gets the names of the entities that will have editing forms created for.
     * @return A list containing the entity names.
     */
    @Override
    public Map<String, String> getForms() {
        return getPageConfiguration().getForms();
    }

    /**
     * Sets the list of entity names for which forms are going to be created.
     * @param formEntities A list of entity names.
     */
    @Override
    public void setForms(Map<String, String> formEntities) {
        getPageConfiguration().setForms(formEntities);
    }

    /**
     * Adds a CSS file name so the header is configured to include it.
     * @param cssFilename The name (without the extension) of the CSS file.
     */
    @Override
    public void addCssFile(String cssFilename) {
        getPageConfiguration().addCssFile(cssFilename);
    }

    /**
     * Adds the name of a DWRService so it will be configured for its use in
     * the page.
     * @param name The name of the service.
     */
    @Override
    public void addDwrService(String name) {
        getPageConfiguration().addDwrService(name);
    }

    /**
     * Adds a Dojo JavaScript bundle file so its loaded by the page.
     * @param bundleName The bundle file name (without its extension).
     */
    @Override
    public void addDojoBundle(String bundleName) {
        getPageConfiguration().addDojoBundle(bundleName);
    }

    /**
     * Adds a Dojo module so its loaded by the page using a <c>dojo.require(..)</c>
     * JavaScript statment.
     * @param dojoModule The full name (including path) of the Dojo module
     * being loaded (e.g. dijit.form.Button).
     */
    @Override
    public void addDojoModule(String dojoModule) {
        getPageConfiguration().addDojoModule(dojoModule);
    }

    /**
     * Adds a JavaScript file name so the file is loaded by the page.
     * @param jsFilename The name of the Javascript file, without extension.
     */
    @Override
    public void addJavaScriptFile(String jsFilename) {
        getPageConfiguration().addJavaScriptFile(jsFilename);
    }

    /**
     * Adds a piece of Javascript code so its executed after the page is loaded.
     * @param code The piece of code to be executed.
     */
    @Override
    public void addOnLoadScript(String code) {
        getPageConfiguration().addOnLoadScript(code);
    }

    /**
     * Adds a piece of JavaScript code so its inclued. in the page.
     * @param script The piece of code to be included.
     */
    @Override
    public void addScript(String script) {
        getPageConfiguration().addScript(script);
    }

    /**
     * Gets the names of the CSS files considered for use in the page.
     * @return A list of the names of the CSS files, without extensions.
     */
    @Override
    public List<String> getCssFiles() {
        return getPageConfiguration().getCssFiles();
    }

    /**
     * Gets the Dojo modules that will be loaded.
     * @return A list with the names of the Dojo modules to be 'required'.
     */
    @Override
    public List<String> getDojoModules() {
        return getPageConfiguration().getDojoModules();
    }

    /**
     * Gets the names of the Dojo Javascript bundle files used in the page.
     * @return A list with the names of the Javascript files, without the extension.
     */
    @Override
    public List<String> getDojoBundles() {
        return getPageConfiguration().getDojoBundles();
    }

    /**
     * Gets the names of the DWR services that will be used by the page.
     * @return A list with the services' names.
     */
    @Override
    public List<String> getDwrServices() {
        return getPageConfiguration().getDwrServices();
    }

    /**
     * Gets the names of the JavaScript files included in the page.
     * @return A list with the names of the files, without the extension.
     */
    @Override
    public List<String> getJavaScriptFiles() {
        return getPageConfiguration().getJavaScriptFiles();
    }

    /**
     * Gets the pieces of Javascript code that will be loaded after the page
     * finishes loading.
     * @return A list with the pieces of code.
     */
    @Override
    public List<String> getOnLoadScripts() {
        return getPageConfiguration().getOnLoadScripts();
    }

    /**
     * Gets the pieces of Javascript code included in the page.
     * @return A list with the pieces of code.
     */
    @Override
    public List<String> getScripts() {
        return getPageConfiguration().getScripts();
    }

    /**
     * Gets the title of the page.
     * @return The page's title.
     */
    @Override
    public String getTitle() {
        return getPageConfiguration().getTitle();
    }

    /**
     * Sets the title of the page.
     * @param title The page's title.
     */
    @Override
    public void setTitle(String title) {
        getPageConfiguration().setTitle(title);
    }

    /**
     * Sets the names of the CSS files to be loaded by the page.
     * @param cssFiles A list with the CSS filenames, without extensions.
     */
    @Override
    public void setCssFiles(List<String> cssFiles) {
        getPageConfiguration().setCssFiles(cssFiles);
    }

    /**
     * Sets the names of the DWR services to be used by the page.
     * @param dwrServices A list of the DWR service names.
     */
    @Override
    public void setDwrServices(List<String> dwrServices) {
        getPageConfiguration().setDwrServices(dwrServices);
    }

    /**
     * Sets the names of the Dojo JavaScript bundle files to be loaded
     * by the page.
     * @param dojoBundles The name of the javascript bundle files, without
     * extensions.
     */
    @Override
    public void setDojoBundles(List<String> dojoBundles) {
        getPageConfiguration().setDojoBundles(dojoBundles);
    }

    /**
     * Sets the Dojo modules required by the page.
     * @param dojoModules A list containing the Dojo module names
     * (e.g. dijit.form.Button) required by the page.
     */
    @Override
    public void setDojoModules(List<String> dojoModules) {
        getPageConfiguration().setDojoModules(dojoModules);
    }

    /**
     * Sets the JavaScript files names to be loaded by the page.
     * @param javascriptFiles A list with the names of the files (without extensions).
     */
    @Override
    public void setJavaScriptFiles(List<String> javascriptFiles) {
        getPageConfiguration().setJavaScriptFiles(javascriptFiles);
    }

    /**
     * Sets the pieces of JavaScript code that will be run after the page is
     * loaded.
     * @param onLoadScripts The pieces of code to be run after the page finishes
     * loading.
     */
    @Override
    public void setOnLoadScripts(List<String> onLoadScripts) {
        getPageConfiguration().setOnLoadScripts(onLoadScripts);
    }

    /**
     * Sets the pieces of JavaScript code that will be include in the page.
     * @param scripts A list of pieces of JavaScript code.
     */
    @Override
    public void setScripts(List<String> scripts) {
        getPageConfiguration().setScripts(scripts);
    }

    /**
     * @return the userSessionInfo
     */
    public UserSessionInfo getUserSessionInfo() {
        return userSessionInfo;
    }

    /**
     * @param userSessionInfo the userSessionInfo to set
     */
    public void setUserSessionInfo(UserSessionInfo userSessionInfo) {
        this.userSessionInfo = userSessionInfo;
    }

    @Override
    public void addPropertiesView(String id, String configuration) {
        this.getPageConfiguration().addPropertiesView(id, configuration);
    }

    @Override
    public void setPropertiesView(Map<String, String> configuration) {
        this.getPageConfiguration().setPropertiesView(configuration);
    }

    @Override
    public Map<String, String> getPropertiesView() {
        return getPageConfiguration().getPropertiesView();
    }

    /**
     * @return the pageToolManager
     */
    @Override
    public PageToolManager getPageToolManager() {
        return pageToolManager;
    }

    /**
     * @param pageToolManager the pageToolManager to set
     */
    @Override
    public void setPageToolManager(PageToolManager pageToolManager) {
        this.pageToolManager = pageToolManager;
    }

    @Override
    public void addDojoBundles(List<String> dojoBundles) {
        getPageConfiguration().addDojoBundles(dojoBundles);
    }

    @Override
    public void addDojoModules(List<String> dojoModules) {
        getPageConfiguration().addDojoModules(dojoModules);
    }

    /**
     * @return the plugin
     */
    public List<ControllerPlugin> getPlugins() {
        return plugins;
    }

    /**
     * @param plugin the plugin to set
     */
    public void setPlugins(List<ControllerPlugin> plugin) {
        this.plugins = plugin;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Auxiliary methods">
    protected List<String> addPathPrefixToFileNames(
            String path,
            List<String> filenames) {

        ArrayList<String> prefixedFileNames = new ArrayList<String>();

        for (String fileName : filenames) {
            prefixedFileNames.add(String.format("%s/%s", path, fileName));
        }

        return prefixedFileNames;
    }

    /**
     * @return the pageConfiguration
     */
    protected PageConfig getPageConfiguration() {
        return pageConfiguration;
    }

    /**
     * @return the toolsLoadDelayed
     */
    public boolean isToolsLoadDelayed() {
        return toolsLoadDelayed;
    }

    /**
     * @param toolsLoadDelayed the toolsLoadDelayed to set
     */
    public void setToolsLoadDelayed(boolean toolsLoadDelayed) {
        this.toolsLoadDelayed = toolsLoadDelayed;
    }
    // </editor-fold>
}
