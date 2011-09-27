package com.greenriver.commons.web.services.mailServerConfig;

import com.greenriver.commons.data.dao.MailServerConfigDao;
import com.greenriver.commons.data.fieldProperties.WidgetFieldsValidator;
import com.greenriver.commons.data.mailing.MailServerConfig;
import com.greenriver.commons.data.validation.ValidationResult;
import com.greenriver.commons.web.services.Result;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

/**
 *
 * @author luisro
 */
public class MailServerConfigServiceImpl implements MailServerConfigService {

    private MailServerConfigDao mailServerConfigDao;
    private WidgetFieldsValidator fieldPropertiesValidator;

    //<editor-fold defaultstate="collapsed" desc="Service methods">
    @Override
    public Result<MailServerConfig> getConfig() {
        Result<MailServerConfig> result = new Result<MailServerConfig>();

        MailServerConfig config = null;
        try {
            config = mailServerConfigDao.get();
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }

        // If we don't have a config stored, we return a new entity.
        if (config == null) {
            config = new MailServerConfig();
        }

        result.setResult(config);

        return result;
    }

    @Override
    public Result saveConfig(MailServerConfig newConfig) {
        Result result = new Result();

        ValidationResult validation = fieldPropertiesValidator.validate(newConfig);
        if (!validation.isValid()) {
            result.addErrorMessages(validation.getErrorMessages());
            return result;
        }

        if (!validateConnection(newConfig, result)) {
            return result;
        }

        try {
            mailServerConfigDao.save(newConfig);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }

        return result;
    }

    boolean validateConnection(MailServerConfig config, Result result) {
        Properties props = createPropertiesFromConfig(config);
        Session session = Session.getInstance(props);
        Transport transport = null;
        try {
            transport = session.getTransport(config.getProtocol().configValue());
        } catch (NoSuchProviderException ex) {
            result.addErrorMessage(
                    "No se encontró el protocolo de correo especificado en la configuración.");
            return false;
        }
        try {
            transport.connect(config.getHostName(), config.getPortNumber(), config.getUserName(), config.getPassword());
            transport.close();

        }catch (AuthenticationFailedException afe) {
            result.addErrorMessage(
                    "El usuario o la contraseña son incorrectos.");
            return false;
        } catch (MessagingException ex) {            
            result.addErrorMessage(
                    "El servidor de correo especificado no existe o no es accesible en el puerto especificado.");
            return false;
        }

        return true;
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="comment">
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

    /**
     * @return the fieldPropertiesValidator
     */
    public WidgetFieldsValidator getFieldPropertiesValidator() {
        return fieldPropertiesValidator;
    }

    /**
     * @param fieldPropertiesValidator the fieldPropertiesValidator to set
     */
    public void setFieldPropertiesValidator(WidgetFieldsValidator fieldPropertiesValidator) {
        this.fieldPropertiesValidator = fieldPropertiesValidator;
    }
    //</editor-fold>
}
