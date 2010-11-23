package com.greenriver.commons.web.services;

import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;

/**
 * Encapsulates the data needed to change a user's password.
 * @author luis
 */
public class PasswordChangeData {
    @FieldProperties(label="Contraseña actual", type=FieldType.PASSWORD)
    private String currentPassword;

    @FieldProperties(label="Nueva contraseña", type=FieldType.PASSWORDEDITOR)
    private String newPassword;

    /**
     * @return the currentPassword
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * @param currentPassword the currentPassword to set
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
