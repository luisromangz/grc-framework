/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.mailing;

import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;

/**
 *
 * @author luis
 */
public class Mail {
    @FieldProperties(label="De", type=FieldType.EMAIL)
    private String from;
    @FieldProperties(label="Para", type=FieldType.EMAIL)
    private String to;
    @FieldProperties(label="Asunto", widgetWidth="30em")
    private String subject;
    @FieldProperties(label="Cuerpo del correo", type=FieldType.RICHTEXT)
    private String body;

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }
    // </editor-fold>
}
