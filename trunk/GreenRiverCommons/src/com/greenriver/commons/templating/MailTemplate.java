
package com.greenriver.commons.templating;

import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author luis
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class MailTemplate<T extends TemplateReplacement> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @FieldProperties(label="Asunto del correo", widgetWidth="30em")
    private String subject;
    @FieldProperties(label="Cuerpo del correo",type=FieldType.RICHTEXT)
    @Column(length=2048)
    private String body;

    public Mail createMailFromTemplate(
            Map<T,String> replacements){

        String mailBody = this.getBody();
        String mailSubject = this.getSubject();

        for(T replacementType : replacements.keySet()){
            mailBody = mailBody.replace(replacementType.getPlaceholder(),
                    replacements.get(replacementType));

            mailSubject = mailSubject.replace(replacementType.getPlaceholder(),
                    replacements.get(replacementType));
        }

        Mail result = new Mail();
        result.setBody(mailBody);
        result.setSubject(mailSubject);

        return result;
    };

    // <editor-fold defaultstate="collapsed" desc="Setters getters & auto-generated stuff">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MailTemplate)) {
            return false;
        }
        MailTemplate other = (MailTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.greenriver.doñanaMealManager.model.guests.GuestManagementOptions[id=" + id + "]";
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

  
    // </editor-fold>

}