package com.greenriver.commons.mailing;

import com.greenriver.commons.ErrorMessagesException;
import com.greenriver.commons.data.dao.MailServerConfigDao;
import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.mailing.MailServerConfig;
import com.greenriver.commons.data.transactions.TransactionRollback;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Implemetantion of a mail server helper, using JavaMail.
 * @author luis
 */
public class MailSendingHelperImpl implements MailSendingHelper {

    private MailServerConfigDao mailServerConfigDao;

    @Override
    @TransactionRollback
    public boolean sendHtmlMail(Mail mail) throws ErrorMessagesException {

        buildAndSendMail(mail);

        if (mail.getSendCopyToSender()) {
            mail.setTo(mail.getFrom());
            mail.setSubject(mail.getSubject() + " (Copia al remitente)");
            buildAndSendMail(mail);
        }


        return true;
    }

    private void buildAndSendMail(Mail mail) throws ErrorMessagesException {
        // Retrieve config from database
        MailServerConfig config = null;
        try {
            config = getMailServerConfigDao().get();
        } catch (RuntimeException e) {
            throw new ErrorMessagesException("Ocurrió un error de base de datos.");
        }

        Properties props = createPropertiesFromConfig(config);
        Session session = Session.getInstance(props);
        Message message = new MimeMessage(session);


        try {
            buildHtmlMail(mail, message);
        } catch (MessagingException ex) {
            throw new ErrorMessagesException("Ocurrió un error componer el correo.");

        } catch (IOException ex) {
            throw new ErrorMessagesException("Ocurrió un error componer el correo.");
        }

        Transport transport = null;
        try {
            transport = session.getTransport(config.getProtocol().configValue());
        } catch (NoSuchProviderException ex) {
            throw new ErrorMessagesException(
                    "No se encontró el protocolo de correo especificado en la configuración.");
        }
        try {
            transport.connect(config.getHostName(), config.getPortNumber(), config.getUserName(), config.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException ex) {
            throw new ErrorMessagesException(
                    "Ocurrió un error al conectarse al servidor.");
        }

    }

    private void buildHtmlMail(Mail mail, Message message)
            throws MessagingException, IOException {

        message.setSubject(mail.getSubject());
        message.setFrom(new InternetAddress(mail.getFrom()));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(mail.getTo(), false));

        StringBuilder sb = new StringBuilder();
        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<TITLE>\n");
        sb.append(mail.getSubject()).append("\n");
        sb.append("</TITLE>\n");
        sb.append("</HEAD>\n");

        sb.append("<BODY>\n");
        sb.append(mail.getBody());
        sb.append("</BODY>\n");
        sb.append("</HTML>\n");


        ByteArrayDataSource dataSource =
                new ByteArrayDataSource(sb.toString(), "text/html");
        DataHandler dataHandler = new DataHandler(dataSource);

        message.setDataHandler(dataHandler);

        message.setHeader("X-Mailer", "Comidas de Donnana");
        message.setSentDate(new Date());

    }

    private Properties createPropertiesFromConfig(MailServerConfig config) {
        Properties properties = new Properties();

        if (config.getRequiresAuthentication()) {
            properties.put("mail.smtp.auth", "true");
        }
        if (config.getUseStartTtls()) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }

        return properties;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the mailServerConfigDao
     */
    public MailServerConfigDao getMailServerConfigDao() {
        return mailServerConfigDao;
    }
    
    /**
     * @param mailServerConfigDao the mailServerConfigDao to set
     */
    public void setMailServerConfigDao(MailServerConfigDao mailServerConfigDao) {
        this.mailServerConfigDao = mailServerConfigDao;
    }
    
    //</editor-fold>
}
