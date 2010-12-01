package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.PageConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public interface CustomizableHandleRequest {

    void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig pageConfiguration,
            ModelAndView modelAndView) throws Exception;

}
