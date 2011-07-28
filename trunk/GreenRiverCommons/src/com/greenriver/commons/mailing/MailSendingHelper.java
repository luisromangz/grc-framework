package com.greenriver.commons.mailing;

import com.greenriver.commons.ErrorMessagesException;
import com.greenriver.commons.data.mailing.Mail;

/**
 * Interface for a helper that allows sending mails.
 * @author luis
 */
public interface MailSendingHelper {
    
    boolean sendHtmlMail(Mail mail) throws ErrorMessagesException;
}
