/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.helpers;

import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.services.ServiceResult;

/**
 * This interface defines a helper method to send messages.
 * @author luis
 */
public interface MailSendingHelper {
    boolean sendHtmlMail(Mail mail, ServiceResult serviceResult);
}
