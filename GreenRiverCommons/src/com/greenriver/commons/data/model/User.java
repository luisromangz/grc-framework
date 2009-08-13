package com.greenriver.commons.data.model;

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

/**
 * Common user entity.
 * 
 * This entity implements Comparable. Two users are the same if the name is the
 * same (case-sensitive).
 * @author luis
 */
@Entity
public class User implements Serializable, Comparable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @FieldProperties(label = "Nombre de usuario", customRegExp = "^[\\w]{6,}$")
    @Column(unique=true)
    private String username;

    @FieldProperties(label = "Nombre y apellidos")
    private String name;

    @FieldProperties(label="Contraseña", type = FieldType.PASSWORDEDITOR)
    private String password;

    @FieldProperties(label = "Permisos", type = FieldType.ROLESELECTOR, required=false)
    private String[] roles;

    public boolean hasRole(String role) {
        return Arrays.asList(this.roles).contains(role);
    }

    public boolean hasAnyRole(String[] roles) {
	List<String> curRoles = Arrays.asList(this.roles);
	
	for (String role : roles) {
	    if (curRoles.contains(role)) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + getId() + "]";
    }

    public int compareTo(User o) {
        // We compare by the username so we can compare non-persited objects.
        return this.getUsername().compareTo(o.getUsername());
    }

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
    public String[] getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String[] roles) {
        this.roles = roles;
    }   
}
