package com.greenriver.commons.services;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.session.InstallHelper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import org.springframework.security.providers.encoding.PasswordEncoder;

/**
 *
 * @author luis
 */
public class InstallServiceImpl implements InstallService, Serializable {

    private UserDao userDao;
    private InstallHelper installHelper;
    private FieldsValidator fieldsValidator;
    private PasswordEncoder passwordEncoder;

    public ServiceResult checkKeyFile() {
        ServiceResult serviceResult = new ServiceResult();

        String key = installHelper.getKey();

        String path = installHelper.getKeyFilePath();

        BufferedReader keyFile = null;
        try {
            keyFile = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            serviceResult.setSuccess(false);
            serviceResult.addErrorMessage(
                    "No se encontró el fichero «key.txt» en la ubicación indicada.");
            return serviceResult;
        }

        if (keyFile != null) {
            String readKey = "";
            try {
                readKey = keyFile.readLine();
                keyFile.close();
            } catch (IOException ex) {
                serviceResult.setSuccess(false);
                serviceResult.addErrorMessage(
                        "Ocurrió un error al leer el fichero «key.txt».");
                return serviceResult;
            }

            if (!key.equals(readKey)) {
                serviceResult.setSuccess(false);
                serviceResult.addErrorMessage(
                        "El contenido del fichero «key.txt» difiere del código indicado.");
            }
        }

        // The key test has been passed, so we set the flag consecuently.
        installHelper.setKeyFileCreated(true);

        return serviceResult;
    }

    @SuppressWarnings("unchecked")
    public ServiceResult addAdminUser(User adminUser) {
        ServiceResult serviceResult = new ServiceResult();

        if (!installHelper.isKeyFileCreated()) {
            serviceResult.setSuccess(false);
            serviceResult.addErrorMessage(
                    "No se ha completado el proceso de comprobación de acceso al servidor.");
        } else {

            // Needed to bypass validation.
            adminUser.setRoles(new String[]{"ROLE_USER"});
            FieldsValidationResult validationResult =
                    fieldsValidator.validate(adminUser);

            // The user is an admin, and we ensure it is active;
            adminUser.setRoles(new String[]{"ROLE_ADMIN"});

            
            if (validationResult.isValid()) {
                try {
                    userDao.save(adminUser,
                            passwordEncoder.encodePassword(
                            adminUser.getPassword(),
                            null));


                } catch (RuntimeException re) {
                    serviceResult.setSuccess(false);
                    serviceResult.addErrorMessage(
                            "Ocurrió un error de base de datos al guardar el administrador.");
                }

            } else {
                serviceResult.setSuccess(false);
                serviceResult.addErrorMessages(
                        validationResult.getErrorMessages());
            }
        }


        return serviceResult;
    }

    /**
     * @param userDao the userDao to set
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @param installHelper the installHelper to set
     */
    public void setInstallHelper(InstallHelper installHelper) {
        this.installHelper = installHelper;
    }

    /**
     * @param fieldsValidator the fieldsValidator to set
     */
    public void setFieldsValidator(FieldsValidator fieldsValidator) {
        this.fieldsValidator = fieldsValidator;
    }

    /**
     * @param passwordEncoder the passwordEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
