/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.session;

import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author luisro
 */
public class SessionHelper {
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true);   
        
        return session;
    }
    
    public static Object getSessionAttr(String attrName) {
        HttpSession session = getSession();
        return session.getAttribute(attrName);
    }

    public static void setSessionAttr(String attr, Object object) {
        HttpSession session = getSession();
        session.setAttribute(attr, object);
    }
}
