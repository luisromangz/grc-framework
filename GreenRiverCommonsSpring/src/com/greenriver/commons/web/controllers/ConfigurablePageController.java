package com.greenriver.commons.web.controllers;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.configuration.FormsContainer;
import com.greenriver.commons.web.configuration.GridsContainer;
import com.greenriver.commons.web.configuration.PageToolsContainer;
import com.greenriver.commons.web.configuration.PropsViewsContainer;
import com.greenriver.commons.web.controllers.plugins.ControllerPlugin;
import com.greenriver.commons.web.helpers.form.Form;
import com.greenriver.commons.web.helpers.header.HeaderConfig;
import com.greenriver.commons.web.pageTools.PageTool;
import com.greenriver.commons.web.helpers.form.FormBuilder;
import com.greenriver.commons.web.helpers.grid.GridBuilder;
import com.greenriver.commons.web.helpers.grid.GridInfo;
import com.greenriver.commons.web.helpers.header.HeaderConfigurer;
import com.greenriver.commons.web.helpers.propertiesView.PropertiesView;
import com.greenriver.commons.web.helpers.propertiesView.PropertiesViewBuilder;
import com.greenriver.commons.web.helpers.session.UserSessionInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * This controller is intended to be used by configuring it through the Spring
 * configuration file. This allow to define the CSS files, JavaScript files,
 * etc., that are needed by the page whitout code.
 *
 * Classes specializing this one can override the <c>handleRequestInternal</c>
 * method, grabbing the <c>ModelAndView</c> object it returns (which is fully
 * configured) and add to it whatever extra objects are needed by the view.
 * @author luis
 */
public class ConfigurablePageController
        extends AbstractController
        implements PropsViewsContainer, GridsContainer, FormsContainer, HeaderConfig, PageToolsContainer {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private HeaderConfigurer headerConfigurer;
    private FormBuilder formBuilder;
    private GridBuilder gridBuilder;
    private PropertiesViewBuilder propertiesViewBuilder;
    private PageConfig pageConfig;
    private String viewName;
    private UserSessionInfo userSessionInfo;
    private List<ControllerPlugin> plugins;
    private boolean toolsLoadDelayed = true;
    private boolean configuredAlways = false;
    private Map<Integer, ModelAndView> mavsByHash;

    // </editor-fold>
    public ConfigurablePageController() {
        // We intialize the pageConfiguration object;
        pageConfig = new PageConfig();
        plugins = new ArrayList<ControllerPlugin>();
        mavsByHash = new HashMap<Integer, ModelAndView>();
    }

    @Override
    public ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        ModelAndView mav = new ModelAndView(viewName);
        if (this.userSessionInfo != null) {
            mav.addObject("userSessionInfo", this.userSessionInfo);
        }

        int requestHash = createRequestHash(request.getParameterMap());

        if (configuredAlways || !mavsByHash.containsKey(requestHash)) {
            // We need to configure all.            
            Logger.getLogger(getClass().getName()).info("Configuring!");

            // We clone it so changes made by configuration process doesnt
            // get stuck into the bean definition.
            PageConfig configuration = (PageConfig) this.pageConfig.clone();

            configureForms(this.getForms(), mav, configuration, null);
            configurePropertiesView(this.getPropsViews(), mav, null);
            configureGrids(this.getGrids(), mav, configuration, null);
            configurePageTools(mav, configuration);

            customConfiguration(request, response, configuration, mav);

            for (ControllerPlugin plugin : this.getPlugins()) {
                plugin.doWork(request, configuration);
            }

            headerConfigurer.configure(mav, configuration);

            mavsByHash.put(requestHash, mav);

        } else {
            Logger.getLogger(getClass().getName()).info("Configuration reused!");
            mav = mavsByHash.get(requestHash);
        }
        
        customHandleRequest(request, response, pageConfig, mav);

        return mav;
    }

    /**
     * This method, when overriden in an extending class, gets called
     * only when we need to configure.
     * 
     * @param request
     * @param response
     * @param configuration
     * @param mav
     * @throws Exception 
     */
    protected void customConfiguration(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {
    }
    
    /**
     * This method, when overriden, gets called on each request.
     * @param request
     * @param response
     * @param configuration
     * @param mav
     * @throws Exception 
     */
    protected void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {
    }

    // <editor-fold defaultstate="collapsed" desc="Page properties configuration methods">
    /**
     * Configures forms to edit entities from a map     
     * @param forms
     * @param mav
     * @param prefix Prefix to append to the name of the form
     * @throws ClassNotFoundException
     */
    protected List<Form> configureForms(
            Map<String, String> forms,
            ModelAndView mav,
            PageConfig pageConfig,
            String prefix)
            throws ClassNotFoundException {

        if (formBuilder == null && forms.size() > 0) {
            throw new IllegalStateException(
                    "Must configure formBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        List<Form> createdForms = new ArrayList<Form>();
        for (String formId : forms.keySet()) {
            String className = forms.get(formId);
            Class entityClass = Class.forName(className);
            createdForms.add(formBuilder.addForm(prefix + formId, pageConfig, mav));
            formBuilder.addFieldsFromClass(entityClass);
        }

        return createdForms;
    }

    /**
     * Configures properties view from a map     
     * @param propertiesViews
     * @param mav
     * @param prefix Name prefix for the generated elements
     */
    protected List<PropertiesView> configurePropertiesView(
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

        List<PropertiesView> createdPropViews =
                new ArrayList<PropertiesView>();
        for (String propsViewId : propertiesViews.keySet()) {
            createdPropViews.add(propertiesViewBuilder.addPropertiesView(prefix + propsViewId, mav));
            propertiesViewBuilder.addPropertiesViewFromClass(propertiesViews.get(propsViewId));
        }

        return createdPropViews;
    }

    /**
     * Configures grids from a map     
     * @param propertiesViews
     * @param mav
     * @param prefix Name prefix for the generated elements
     */
    protected List<GridInfo> configureGrids(
            Map<String, String> grids,
            ModelAndView mav,
            PageConfig pageConfig,
            String prefix) {
        if (getGridBuilder() == null && grids.size() > 0) {
            throw new IllegalStateException(
                    "Must configure gridsBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        List<GridInfo> createdGrids = new ArrayList<GridInfo>();
        for (String gridId : grids.keySet()) {
            createdGrids.add(getGridBuilder().addGridInfo(prefix + gridId, pageConfig, mav));
            getGridBuilder().addGridInfoFromClass(grids.get(gridId));
        }

        return createdGrids;
    }

    private void configurePageTools(
            ModelAndView mav,
            PageConfig configuration)
            throws ClassNotFoundException {

        List<String> dialogJspFiles = new ArrayList<String>();
        List<String> setupJspFiles = new ArrayList<String>();

        // This makes the initilization code needed by forms to be inside this function.
        configuration.addOnLoadScript("window['onToolsLoaded']=function(){");



        for (PageTool pageTool : this.getPageTools()) {
            
            if(!pageTool.isAllowedForUser(this.userSessionInfo.getCurrentUser())) {
                continue;
            }
            
            if (!pageTool.isInitialized() ) {
                pageTool.initialize();
            }

            if (!this.toolsLoadDelayed) {
                // We only load the jsp files if we are loading the
                // tool with the page.
                dialogJspFiles.addAll(Strings.addPrefix(
                        "tools/" + pageTool.getName() + "/",
                        pageTool.getDialogJspFiles()));

                dialogJspFiles.addAll(Strings.addPrefix(
                        "tools/" + pageTool.getName() + "/",
                        pageTool.getSetupPaneJspFiles()));



                configurePropertiesView(pageTool.getPropsViews(), mav,
                        pageTool.getName() + "_");


            }

            // We always process forms, even if loading on demand, so
            // we can ensure that we are getting all the dojo.requires
            // actually needed.

            //Forms ids are prefixed with the task name
            configureForms(
                    pageTool.getForms(), mav, configuration, pageTool.getName() + "_");

            // We always include the rest of things, so we are sure
            // we bundle (if bundling is activated) all the required modules
            // and js files.

            configuration.getJavaScriptFiles().addAll(Strings.addPrefix(
                    "tools/" + pageTool.getName() + "/",
                    pageTool.getJavaScriptFiles()));


            configuration.getCssFiles().addAll(Strings.addPrefix(
                    pageTool.getName() + "/",
                    pageTool.getCssFiles()));

            configuration.getDojoBundles().addAll(
                    pageTool.getDojoBundles());

            configuration.getDojoModules().addAll(
                    pageTool.getDojoModules());
            configuration.getDwrServices().addAll(
                    pageTool.getDwrServices());

            configuration.getScripts().addAll(pageTool.getScripts());

            configuration.getOnLoadScripts().addAll(
                    pageTool.getOnLoadScripts());

        }

        // Close of the init function.
        configuration.addOnLoadScript("} // End of onToolsLoaded");

        if (!this.isToolsLoadDelayed()) {
            configuration.addOnLoadScript("window.onToolsLoaded()");
        }

        mav.addObject("pageTools", this.getPageTools());
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
        this.pageConfig = pageConfiguration;
    }

    /**
     * Adds an entity's name so a form capable of editing the object is created
     * automagically.
     * @param entityName The name of the entity the form will be created for.
     */
    @Override
    public void addForm(String id, String entityName) {
        getPageConfig().addForm(id, entityName);
    }

    /**
     * Gets the names of the entities that will have editing forms created for.
     * @return A list containing the entity names.
     */
    @Override
    public Map<String, String> getForms() {
        return getPageConfig().getForms();
    }

    /**
     * Sets the list of entity names for which forms are going to be created.
     * @param formEntities A list of entity names.
     */
    @Override
    public void setForms(Map<String, String> formEntities) {
        getPageConfig().setForms(formEntities);
    }

    @Override
    public Map<String, String> getGrids() {
        return getPageConfig().getGrids();
    }

    @Override
    public void setGrids(Map<String, String> grids) {
        getPageConfig().setGrids(grids);
    }

    @Override
    public void addGrid(String id, String className) {
        getPageConfig().addGrid(id, className);
    }

    /**
     * Adds a CSS file name so the header is configured to include it.
     * @param cssFilename The name (without the extension) of the CSS file.
     */
    @Override
    public void addCssFile(String cssFilename) {
        getPageConfig().addCssFile(cssFilename);
    }

    /**
     * Adds the name of a DWRService so it will be configured for its use in
     * the page.
     * @param name The name of the service.
     */
    @Override
    public void addDwrService(String name) {
        getPageConfig().addDwrService(name);
    }

    /**
     * Adds a Dojo JavaScript bundle file so its loaded by the page.
     * @param bundleName The bundle file name (without its extension).
     */
    @Override
    public void addDojoBundle(String bundleName) {
        getPageConfig().addDojoBundle(bundleName);
    }

    /**
     * Adds a Dojo module so its loaded by the page using a <c>dojo.require(..)</c>
     * JavaScript statment.
     * @param dojoModule The full name (including path) of the Dojo module
     * being loaded (e.g. dijit.form.Button).
     */
    @Override
    public void addDojoModule(String dojoModule) {
        getPageConfig().addDojoModule(dojoModule);
    }

    /**
     * Adds a JavaScript file name so the file is loaded by the page.
     * @param jsFilename The name of the Javascript file, without extension.
     */
    @Override
    public void addJavaScriptFile(String jsFilename) {
        getPageConfig().addJavaScriptFile(jsFilename);
    }

    /**
     * Adds a piece of Javascript code so its executed after the page is loaded.
     * @param code The piece of code to be executed.
     */
    @Override
    public void addOnLoadScript(String code) {
        getPageConfig().addOnLoadScript(code);
    }

    /**
     * Adds a piece of JavaScript code so its inclued. in the page.
     * @param script The piece of code to be included.
     */
    @Override
    public void addScript(String script) {
        getPageConfig().addScript(script);
    }

    /**
     * Gets the names of the CSS files considered for use in the page.
     * @return A list of the names of the CSS files, without extensions.
     */
    @Override
    public List<String> getCssFiles() {
        return getPageConfig().getCssFiles();
    }

    /**
     * Gets the Dojo modules that will be loaded.
     * @return A list with the names of the Dojo modules to be 'required'.
     */
    @Override
    public List<String> getDojoModules() {
        return getPageConfig().getDojoModules();
    }

    /**
     * Gets the names of the Dojo Javascript bundle files used in the page.
     * @return A list with the names of the Javascript files, without the extension.
     */
    @Override
    public List<String> getDojoBundles() {
        return getPageConfig().getDojoBundles();
    }

    /**
     * Gets the names of the DWR services that will be used by the page.
     * @return A list with the services' names.
     */
    @Override
    public List<String> getDwrServices() {
        return getPageConfig().getDwrServices();
    }

    /**
     * Gets the names of the JavaScript files included in the page.
     * @return A list with the names of the files, without the extension.
     */
    @Override
    public List<String> getJavaScriptFiles() {
        return getPageConfig().getJavaScriptFiles();
    }

    /**
     * Gets the pieces of Javascript code that will be loaded after the page
     * finishes loading.
     * @return A list with the pieces of code.
     */
    @Override
    public List<String> getOnLoadScripts() {
        return getPageConfig().getOnLoadScripts();
    }

    /**
     * Gets the pieces of Javascript code included in the page.
     * @return A list with the pieces of code.
     */
    @Override
    public List<String> getScripts() {
        return getPageConfig().getScripts();
    }

    /**
     * Gets the title of the page.
     * @return The page's title.
     */
    @Override
    public String getTitle() {
        return getPageConfig().getTitle();
    }

    /**
     * Sets the title of the page.
     * @param title The page's title.
     */
    @Override
    public void setTitle(String title) {
        getPageConfig().setTitle(title);
    }

    /**
     * Sets the names of the CSS files to be loaded by the page.
     * @param cssFiles A list with the CSS filenames, without extensions.
     */
    @Override
    public void setCssFiles(List<String> cssFiles) {
        getPageConfig().setCssFiles(cssFiles);
    }

    /**
     * Sets the names of the DWR services to be used by the page.
     * @param dwrServices A list of the DWR service names.
     */
    @Override
    public void setDwrServices(List<String> dwrServices) {
        getPageConfig().setDwrServices(dwrServices);
    }

    /**
     * Sets the names of the Dojo JavaScript bundle files to be loaded
     * by the page.
     * @param dojoBundles The name of the javascript bundle files, without
     * extensions.
     */
    @Override
    public void setDojoBundles(List<String> dojoBundles) {
        getPageConfig().setDojoBundles(dojoBundles);
    }

    /**
     * Sets the Dojo modules required by the page.
     * @param dojoModules A list containing the Dojo module names
     * (e.g. dijit.form.Button) required by the page.
     */
    @Override
    public void setDojoModules(List<String> dojoModules) {
        getPageConfig().setDojoModules(dojoModules);
    }

    /**
     * Sets the JavaScript files names to be loaded by the page.
     * @param javascriptFiles A list with the names of the files (without extensions).
     */
    @Override
    public void setJavaScriptFiles(List<String> javascriptFiles) {
        getPageConfig().setJavaScriptFiles(javascriptFiles);
    }

    /**
     * Sets the pieces of JavaScript code that will be run after the page is
     * loaded.
     * @param onLoadScripts The pieces of code to be run after the page finishes
     * loading.
     */
    @Override
    public void setOnLoadScripts(List<String> onLoadScripts) {
        getPageConfig().setOnLoadScripts(onLoadScripts);
    }

    /**
     * Sets the pieces of JavaScript code that will be include in the page.
     * @param scripts A list of pieces of JavaScript code.
     */
    @Override
    public void setScripts(List<String> scripts) {
        getPageConfig().setScripts(scripts);
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
    public void addPropsView(String id, String configuration) {
        this.getPageConfig().addPropsView(id, configuration);
    }

    @Override
    public void setPropsViews(Map<String, String> configuration) {
        this.getPageConfig().setPropsViews(configuration);
    }

    @Override
    public Map<String, String> getPropsViews() {
        return getPageConfig().getPropsViews();
    }

    @Override
    public void addDojoBundles(List<String> dojoBundles) {
        getPageConfig().addDojoBundles(dojoBundles);
    }

    @Override
    public void addDojoModules(List<String> dojoModules) {
        getPageConfig().addDojoModules(dojoModules);
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

    /**
     * @return the pageConfiguration
     */
    protected PageConfig getPageConfig() {
        return pageConfig;
    }

    @Override
    public List<PageTool> getPageTools() {
        return this.getPageConfig().getPageTools();
    }

    @Override
    public void setPageTools(List<PageTool> pageTools) {
        this.getPageConfig().setPageTools(pageTools);
    }

    /**
     * @return the gridBuilder
     */
    public GridBuilder getGridBuilder() {
        return gridBuilder;
    }

    /**
     * @param gridBuilder the gridBuilder to set
     */
    public void setGridBuilder(GridBuilder gridBuilder) {
        this.gridBuilder = gridBuilder;
    }

    /**
     * @return the configuredAlways
     */
    public boolean isConfiguredAlways() {
        return configuredAlways;
    }

    /**
     * @param configuredAlways the configuredAlways to set
     */
    public void setConfiguredAlways(boolean configuredAlways) {
        this.configuredAlways = configuredAlways;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Auxiliary methods">
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

    private int createRequestHash(Map parameterMap) {
        int hash = 7;

        for (Object key : parameterMap.keySet()) {
            hash = 83 * hash + key.hashCode();

            String[] parameters = (String[]) parameterMap.get(key);
            for (int i = 0; i < parameters.length; i++) {
                hash = 83 * hash + (parameters[i].hashCode());
            }
        }

        if (this.userSessionInfo != null
                && this.userSessionInfo.getCurrentUser() != null) {
            for (String role : this.userSessionInfo.getCurrentUser().getRoles()) {
                hash = 83 * hash + role.hashCode();
            }
        }



        return hash;
    }
}
