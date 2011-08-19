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
    
     class BackgroundMailer implements Runnable {
        Mail mail;
        MailServerConfig config;
        MailSendingHelper helper;
        
        public BackgroundMailer(
                Mail mail, 
                MailServerConfig config,
                MailSendingHelper helper) {
            this.mail = mail;
            this.config = config;
            this.helper = helper;
        }

        @Override
        public void run() {
            try {
                helper.sendHtmlMail(this.mail,this.config);
            } catch (ErrorMessagesException ex) {
                // We dont log as we have logged the errors in the helper.
            }
        }
        
    }
}
