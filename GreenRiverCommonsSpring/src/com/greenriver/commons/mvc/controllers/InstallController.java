package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.mvc.helpers.form.FormBuilder;
import com.greenriver.commons.mvc.helpers.form.FormBuilderClient;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurerClient;
import com.greenriver.commons.session.InstallHelper;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author luis
 */
public class InstallController extends AbstractController
        implements HeaderConfigurerClient, FormBuilderClient {

    private FormBuilder formBuilder;
    private HeaderConfigurer headerConfigurer;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private InstallHelper installHelper;
    private String userClass;

    public InstallController() {
    }

    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("install");

        if(userDao.getUserCount()>0) {
            response.sendRedirect("login.htm");
            return mav;
        }

        headerConfigurer.setTitle("Sensis - Instalación");
        headerConfigurer.addJavaScriptFile("install");
        headerConfigurer.addCssFile("install");
        headerConfigurer.addDWRService("installService");        

        Date now = new Date();
        String key = passwordEncoder.encodePassword(now.toString(), null);

        installHelper.setKey(key);

        String path = request.getSession().getServletContext().getRealPath("");
        installHelper.setKeyFilePath(path+"/key.txt");

        mav.addObject("keyPath",  path);
        mav.addObject("key", key);

        formBuilder.addForm("adminForm",mav);
        formBuilder.addFieldsFromModel(Class.forName(userClass));
        formBuilder.removeField("roles");
        formBuilder.removeField("enabled");

        headerConfigurer.configure(mav);

        return mav;
    }

    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer) {
        this.headerConfigurer = headerConfigurer;
    }

    public HeaderConfigurer getHeaderConfigurer() {
        return this.headerConfigurer;
    }

    public FormBuilder getFormBuilder() {
        return formBuilder;
    }

    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }

    /**
     * @param passworEncoder the passworEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passworEncoder) {
        this.passwordEncoder = passworEncoder;
    }

    /**
     * @param userDao the userDao to set
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @param installHelper the installHelper to set
     */
    public void setInstallHelper(InstallHelper installHelper) {
        this.installHelper = installHelper;
    }

    public String getUserClass() {
	return userClass;
    }

    public void setUserClass(String userClass) {
	this.userClass = userClass;
    }
}