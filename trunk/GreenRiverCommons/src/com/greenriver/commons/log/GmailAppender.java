package com.greenriver.commons.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This class extends Log4j's SMTPAppender so we are able to send log events
 * through GMail's servers.
 *
 * @author luis
 */
public class GmailAppender extends SMTPAppender {

    private String hostname;
    private ExecutorService threadPool;
    

    public GmailAppender() {
        super();

        threadPool = Executors.newSingleThreadExecutor();

        // We try to find the hostname
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
        }

    }

    @Override
    public void append(LoggingEvent event) {
        // We need a thread so we don't have to wait for the email to be sent,
        // which can be lenghty.
        threadPool.execute(new LogMailSender(event,
                    this.getSMTPUsername(),
                    this.getSMTPPassword()));
    }


    class LogMailSender implements Runnable {

        private LoggingEvent event;
        private String username;
        private String password;

        private LogMailSender(LoggingEvent event, String username, String password) {
            this.event =event;
            this.username = username;
            this.password = password;
        }

        public void run() {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            Session session = Session.getDefaultInstance(props);

            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(username));
                message.setRecipient(RecipientType.TO, new InternetAddress(
                        getTo()));
                message.setText(event.getRenderedMessage());
                message.setSubject(event.getLevel() + " on " + hostname);
            } catch (MessagingException ex) {
                Logger.getLogger(GmailAppender.class.getName()).log(Level.SEVERE,
                        null, ex);
            }

            Transport t = null;
            try {
                t = session.getTransport("smtps");
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(GmailAppender.class.getName()).log(Level.SEVERE,
                        null, ex);

                return;
            }


            String host = "smtp.gmail.com";

            try {
                t.connect(host, username, password);
                t.sendMessage(message, message.getAllRecipients());
            } catch (Exception ex) {
                Logger.getLogger(GmailAppender.class.getName()).log(Level.SEVERE,
                        null, ex);
            } finally {
                try {
                    t.close();
                } catch (MessagingException ex) {
                    Logger.getLogger(GmailAppender.class.getName()).log(
                            Level.SEVERE,
                            null, ex);
                }
            }
        }
    }
}
