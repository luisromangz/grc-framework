/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.mailing;

/**
 *
 * @author luis
 */
public enum MailSendingProtocol {
    SMTP("smtp"),
    SMTPS("smtps");

    String name;
    MailSendingProtocol(String name) {
        this.name=name;
    }

    public String configValue() {
        return name;
    }
}
