
package com.greenriver.commons.mvc.controllers;
import com.greenriver.commons.mvc.helpers.form.FormBuilder;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author luis
 */
public class ConfigurablePageController extends AbstractController {

    private HeaderConfigurer headerConfigurer;
    private FormBuilder formBuilder;
    private PageConfiguration pageConfiguration;
    private String viewName;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
            HttpServletResponse arg1)
            throws Exception {
        
        ModelAndView mav = new ModelAndView(viewName);

        headerConfigurer.setCssFiles(pageConfiguration.getCssFiles());
        headerConfigurer.setDojoBundles(pageConfiguration.getDojoBundles());
        headerConfigurer.setDojoModules(pageConfiguration.getDojoModules());
        headerConfigurer.setDWRServices(pageConfiguration.getDWRServices());
        headerConfigurer.setJavaScriptFiles(pageConfiguration.getJavaScriptFiles());
        headerConfigurer.setOnLoadScripts(pageConfiguration.getOnLoadScripts());
        headerConfigurer.setScripts(pageConfiguration.getScripts());
        headerConfigurer.setTitle(pageConfiguration.getTitle());

        headerConfigurer.configure(mav);


        for(String entityName : pageConfiguration.getFormEntities()) {
            formBuilder.addForm(entityName + "Form", mav);
            formBuilder.addFieldsFromModel(Class.forName(entityName));
        }

        return mav;
    }

    /**
     * @param headerConfigurer the headerConfigurer to set
     */
    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer) {
        this.headerConfigurer = headerConfigurer;
    }

    /**
     * @param formBuilder the formBuilder to set
     */
    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }

   

    /**
     * @param view the view to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * @param pageConfiguration the pageConfiguration to set
     */
    public void setPageConfiguration(PageConfiguration pageConfiguration) {
        this.pageConfiguration = pageConfiguration;
    }
}
