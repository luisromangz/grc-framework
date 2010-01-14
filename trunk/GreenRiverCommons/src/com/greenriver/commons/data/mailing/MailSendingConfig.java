/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.mailing;

import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;

/**
 *
 * @author luis
 */
public class MailSendingConfig {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    @FieldProperties(label="Servidor de correo")
    private String hostName;
    @FieldProperties(label="Puerto",type=FieldType.NUMBER)
    private int portNumber;

    @FieldProperties(label="Requiere autenticación", type=FieldType.BOOLEAN)
    private boolean requiresAuthentication;

    @FieldProperties(label="Nombre de usuario",required=false,deactivationConditions={
        @FieldDeactivationCondition(triggerField="requiresAuthentication", newValue="",equals="false")
    })
    private String userName;
    @FieldProperties(label="Contraseña", required=false, type=FieldType.PASSWORDEDITOR,deactivationConditions={
        @FieldDeactivationCondition(triggerField="requiresAuthentication", newValue="",equals="false")
    })
    private String password;

    @FieldProperties(label="Protocolo de envío", type=FieldType.SELECTION)
    private MailSendingProtocol protocol;
    @FieldProperties(label="Usar StartTTLS (requerido por GMail)", type=FieldType.BOOLEAN)
    private boolean useStartTtls;
    // </editor-fold>

    public String getProtocolName() {
        return this.protocol.configValue();
    }
    public void setProtocolName(String protocolName) {
        this.protocol = MailSendingProtocol.valueOf(protocolName);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the useStartTtls
     */
    public boolean getUseStartTtls() {
        return useStartTtls;
    }

    /**
     * @param useStartTtls the useStartTtls to set
     */
    public void setUseStartTtls(boolean useStartTtls) {
        this.useStartTtls = useStartTtls;
    }

    /**
     * @return the protocol
     */
    public MailSendingProtocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(MailSendingProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the requiresAuthentication
     */
    public boolean getRequiresAuthentication() {
        return requiresAuthentication;
    }

    /**
     * @param requiresAuthentication the requiresAuthentication to set
     */
    public void setRequiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }
    // </editor-fold>
}