/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.doñanaMealManager.services.helpers.interfaces;

import com.greenriver.commons.services.ServiceResult;
import com.greenriver.commons.data.mailing.Mail;

/**
 *
 * @author luis
 */
public interface MailSendingHelper {
    boolean sendHtmlMail(Mail mail, ServiceResult serviceResult);
}
