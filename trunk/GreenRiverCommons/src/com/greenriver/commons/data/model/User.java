package com.greenriver.commons.data.model;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.Labelled;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Common user entity.
 * 
 * This entity implements Comparable. Two users are the same if the name is the
 * same (case-sensitive).
 * @author luis
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class User implements Serializable, Comparable<User>, Labelled {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @FieldProperties(label = "Nombre y apellidos")
    private String name;
    @FieldProperties(label = "Nombre de usuario",
    customRegExp = "[\\w]{6,}",
    invalidMessage = "El nombre de usuario debe tener al menos 6 carácteres y "
    + "sólo carácteres alfabéticos.")
    @Column(unique = true)
    private String username;
    @FieldProperties(label = "Correo electrónico", type = FieldType.EMAIL, required = false)
    private String emailAddress;
    @FieldProperties(label = "Contraseña", type = FieldType.PASSWORDEDITOR)
    private String password;
    @FieldProperties(label = "Permisos", type = FieldType.ROLESELECTOR, required = false)
    private String[] roles;
    // This flag tells if the user have been deleted or not. A deleted user
    // is kept for history purposses in the system but is not shown nor allowed
    // to log in.
    private Boolean deleted = false;
    // </editor-fold>

    public User() {
        roles = new String[]{};
    }

    public boolean hasRole(String role) {
        return Arrays.asList(this.roles).contains(role);
    }

    public boolean hasAnyRole(String[] roles) {
	
	for (String role : roles) {
	    if (this.getRoles().contains(role)) {
                return true;
            }
        }

	return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || !this.getClass().isAssignableFrom(object.getClass())) {
            return false;
        }

        User other = (User) object;

        return Strings.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + getId() + "]";
    }

    @Override
    public String getLabel() {
        return this.getName();
    }

    @Override
    public int compareTo(User o) {
        // We compare by the username so we can compare non-persited objects.
        return this.getUsername().compareTo(o.getUsername());
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user real name
     * @return the full user name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the user in the system. This name must be unique.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return Arrays.asList(roles);
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles.toArray(new String[]{});
    }
    
    public void setRoles(String[] roles) {
        this.roles= roles;
    }

    public Boolean isDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    // </editor-fold>

    public void addRole(String role) {
        List<String> rolesList = getRoles();
        if(!rolesList.contains(role)) {
            rolesList.add(name);
        }
        setRoles(rolesList);
    }

    public void removeRole(String role) {
         List<String> rolesList = getRoles();
        if(rolesList.contains(role)) {
            rolesList.remove(role);
        }
        setRoles(rolesList);
    }
}
