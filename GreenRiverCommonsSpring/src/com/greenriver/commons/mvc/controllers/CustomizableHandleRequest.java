/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public interface CustomizableHandleRequest {

    void customHandleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception;

}
