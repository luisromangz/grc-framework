package com.greenriver.commons.helpers;

import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.mailing.MailSendingConfig;
import com.greenriver.commons.services.ServiceResult;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author luis
 */
public class MailSendingHelperImpl implements MailSendingHelper {

    private MailSendingConfig defaultConfig;

    @Override
    public boolean sendHtmlMail(Mail mail, ServiceResult serviceResult) {

        if (!buildAndSendMail(mail, serviceResult)) {
            return false;
        }

        if (mail.getSendCopyToSender()) {
            mail.setTo(mail.getFrom());
            mail.setSubject(mail.getSubject() + " (Copia al remitente)");
            if (!buildAndSendMail(mail, serviceResult)) {
                return false;
            }
        }


        return true;
    }

    private boolean buildAndSendMail(Mail mail, ServiceResult serviceResult) {
        // Retrieve config from database
        MailSendingConfig config = defaultConfig;

        Properties props = createPropertiesFromConfig(config);
        Session session = Session.getInstance(props);
        Message message = new MimeMessage(session);


        try {
            buildHtmlMail(mail, message, config);
        } catch (MessagingException ex) {
            Logger.getLogger(this.getClass()).error(ex);
            serviceResult.formatErrorMessage("Ocurrió un error componer el correo.");
            serviceResult.setSuccess(false);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass()).error(ex);
            serviceResult.formatErrorMessage("Ocurrió un error componer el correo.");
            serviceResult.setSuccess(false);
            return false;
        } catch (Throwable e) {
            Logger.getLogger(this.getClass()).error(e);
            serviceResult.formatErrorMessage("Ocurrió un error enviando el correo.");
            return false;
        }

        Transport transport = null;
        try {
            transport = session.getTransport(config.getProtocol().configValue());
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(this.getClass()).error(ex);
            serviceResult.formatErrorMessage(
                    "No se encontró el protocolo de correo especificado en la configuración.");
            serviceResult.setSuccess(false);
            return false;
        } catch (Throwable e) {
            Logger.getLogger(this.getClass()).error(e);
            serviceResult.formatErrorMessage("Ocurrió un error enviando el correo.");
            return false;
        }
        try {
            transport.connect(config.getHostName(), config.getPortNumber(), config.getUserName(), config.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException ex) {
            Logger.getLogger(this.getClass()).error(ex);
            serviceResult.formatErrorMessage(
                    "Ocurrió un error al conectarse al servidor.");
            serviceResult.setSuccess(false);
            return false;
        } catch (Throwable e) {
            Logger.getLogger(this.getClass()).error(e);
            serviceResult.formatErrorMessage("Ocurrió un error enviando el correo.");
            return false;
        }

        return true;

    }

    private void buildHtmlMail(
            Mail mail, Message message, MailSendingConfig config)
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

    private Properties createPropertiesFromConfig(MailSendingConfig config) {
        Properties properties = new Properties();

        if (config.getRequiresAuthentication()) {
            properties.put("mail.smtp.auth", "true");
        }
        if (config.getUseStartTtls()) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }

        return properties;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the defaultConfig
     */
    public MailSendingConfig getDefaultConfig() {
        return defaultConfig;
    }

    /**
     * @param defaultConfig the defaultConfig to set
     */
    public void setDefaultConfig(MailSendingConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
    // </editor-fold>
}
