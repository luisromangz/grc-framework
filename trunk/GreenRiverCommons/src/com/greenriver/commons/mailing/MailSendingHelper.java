package com.greenriver.commons.mailing;

import com.greenriver.commons.ErrorMessagesException;
import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.mailing.MailServerConfig;

/**
 * Interface for a helper that allows sending mails.
 * @author luis
 */
public interface MailSendingHelper {
    
    MailServerConfig getMailServerConfig() throws ErrorMessagesException;
    boolean sendHtmlMail(Mail mail) throws ErrorMessagesException;
    boolean sendHtmlMail(Mail mail, MailServerConfig config) throws ErrorMessagesException;
}
