package com.greenriver.commons.data.mailing;

import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.fieldProperties.WidgetAction;
import com.greenriver.commons.data.fieldProperties.WidgetActions;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class contains the config info for conecction to a mail server.
 * @author luis
 */
@Entity
public class MailServerConfig implements DataEntity{

    // <editor-fold defaultstate="collapsed" desc="Fields">
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @WidgetProps(label = "Servidor de correo")
    private String hostName;
    @WidgetProps(label = "Protocolo de envío", type = FieldType.SELECTION,externalValues=false,enumLabelMethod="getName")
    @Enumerated(EnumType.STRING)
    private MailSendingProtocol protocol= MailSendingProtocol.SMTP;
    @WidgetProps(label = "Puerto", type = FieldType.NUMBER)
    @WidgetActions({
        @WidgetAction(triggerField = "protocol", triggerValue = "'SMTP'", newValue = "25"),
        @WidgetAction(triggerField = "protocol", triggerValue = "'SMTPS'", newValue = "465")})
    private int portNumber=25;
    @WidgetProps(label = "Requiere autenticación", type = FieldType.CHECKBOX, getterPrefix="get")
    private boolean requiresAuthentication;
    @WidgetProps(label = "Nombre de usuario", required = false, getterPrefix="get")
    @WidgetAction(triggerField = "requiresAuthentication", triggerValue = "false", newValue = "''", deactivate = true)
    private String userName;
    @WidgetProps(label = "Contraseña", required = false, type = FieldType.PASSWORDEDITOR)
    @WidgetAction(triggerField = "requiresAuthentication", triggerValue = "false", newValue = "''", deactivate = true)
    private String password;
    @WidgetProps(label = "Usar StartTTLS (requerido por GMail)", type = FieldType.CHECKBOX,getterPrefix="get")
    private boolean useStartTtls;
    // </editor-fold>

    public String getProtocolName() {
        if(this.protocol==null){
            return "";
        }
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
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // </editor-fold>

    @Override
    public String getLabel() {
        return "";
    }

}
