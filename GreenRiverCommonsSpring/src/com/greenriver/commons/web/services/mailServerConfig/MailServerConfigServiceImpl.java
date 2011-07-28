package com.greenriver.commons.web.services.mailServerConfig;

import com.greenriver.commons.data.dao.MailServerConfigDao;
import com.greenriver.commons.data.fieldProperties.FieldPropertiesValidator;
import com.greenriver.commons.data.mailing.MailServerConfig;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.web.services.Result;

/**
 *
 * @author luisro
 */
public class MailServerConfigServiceImpl implements MailServerConfigService {

    private MailServerConfigDao mailServerConfigDao;
    private FieldPropertiesValidator fieldPropertiesValidator;

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
        if(config==null) {
            config= new MailServerConfig();
        }
        
        result.setResult(config);

        return result;
    }

    @Override
    public Result saveConfig(MailServerConfig newConfig) {
        Result result = new Result();

        FieldsValidationResult validation = fieldPropertiesValidator.validate(newConfig);
        if (!validation.isValid()) {
            result.addErrorMessages(validation.getErrorMessages());
            return result;
        }

        try {
            mailServerConfigDao.save(newConfig);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }

        return result;
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
    public FieldPropertiesValidator getFieldPropertiesValidator() {
        return fieldPropertiesValidator;
    }

    /**
     * @param fieldPropertiesValidator the fieldPropertiesValidator to set
     */
    public void setFieldPropertiesValidator(FieldPropertiesValidator fieldPropertiesValidator) {
        this.fieldPropertiesValidator = fieldPropertiesValidator;
    }
    //</editor-fold>
}
