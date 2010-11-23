package com.greenriver.commons.web.helpers.header;

import org.springframework.web.servlet.ModelAndView;

/**
 * This interface defines a model for beans used to configure a page's header.
 * @author luis
 */
public interface HeaderConfigurer {

     /**
     * Makes the actual configuration of the header.
      * @param mav The ModelAndView object that will carry the configuration.
      */
    public void configure(ModelAndView mav, HeaderConfig configuration);

    
}
