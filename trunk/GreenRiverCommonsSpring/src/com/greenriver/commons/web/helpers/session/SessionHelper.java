
package com.greenriver.commons.web.helpers.session;

import java.io.Serializable;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This class allows to manage session objects from anywhere in the app where
 * normally it would be difficult because the HttpContext is not directly avalaible.
 * 
 * @author luisro
 */
public class SessionHelper {
    /**
     * Gets the HttpSession object.
     * @return 
     */
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true);   
        
        return session;
    }
    
    /**
     * Gets the value of a session attribute.
     * @param attrName
     * @return 
     */
    public static Object get(String attrName) {
        HttpSession session = getSession();
        return session.getAttribute(attrName);
    }

    /**
     * Sets the value of a session attribute.
     * @param attr
     * @param object 
     */
    public static void set(String attr, Serializable object) {
        HttpSession session = getSession();
        session.setAttribute(attr, object);
    }
    
    /**
     * Removes a session attribute.
     * @param attr 
     */
    public static void remove(String attr) {
        getSession().removeAttribute(attr);
    }
}