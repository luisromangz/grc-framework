package com.greenriver.commons.data.model;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.queryArgs.QueryArgsProps;
import com.greenriver.commons.data.fieldProperties.FieldType;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Common user entity.
 * 
 * This entity implements Comparable. Two users are the same if the name is the
 * same (case-sensitive).
 * @author luis
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@QueryArgsProps(textFilterFields={"name","username","emailAddress"},
  defaultSortFields={"name"},defaultSortOrders={true})
public class User implements Comparable<User>, DataEntity {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @WidgetProps(label = "Nombre y apellidos")
    private String name;
    @WidgetProps(label = "Nombre de usuario",
    customRegExp = "[\\w]{6,}",
    invalidMessage = "El nombre de usuario debe tener al menos 6 carácteres y "
    + "sólo carácteres alfabéticos.")
    @Column()
    private String username;
    @WidgetProps(label = "Correo electrónico", type = FieldType.EMAIL, required = false)
    private String emailAddress;
    @WidgetProps(label = "Contraseña", type = FieldType.PASSWORDEDITOR)
    private String password;
    @WidgetProps(label = "Permisos", type = FieldType.ROLESELECTOR, required = false)        
    @OneToMany(mappedBy="user", cascade= CascadeType.ALL)
    private List<UserAuthority> roles;
    // This flag tells if the user have been deleted or not. A deleted user
    // is kept for history purposses in the system but is not shown nor allowed
    // to log in.
    private Boolean deleted = false;
    // </editor-fold>

    public User() {
        roles = new ArrayList<UserAuthority>();
    }

    public boolean hasRole(String role) {
        UserAuthority auth = new UserAuthority();
        auth.setUser(this);
        auth.setAuthority(role);
        return getRoles().contains(auth);
    }

    public boolean hasAnyRole(String[] roles) {
	
	for (String role : roles) {
	    if (hasRole(role)) {
                return true;
            }
        }

	return false;
    }
    
    
    public void addRole(String role) {
      
        UserAuthority newAuth = new UserAuthority();
        newAuth.setUser(this);
        newAuth.setAuthority(role);
        
        if(!roles.contains(newAuth)) {
            roles.add(newAuth);
        }
        
    }

    public void removeRole(String role) {
        UserAuthority oldAuth=  new UserAuthority();
        oldAuth.setUser(this);
        oldAuth.setAuthority(role);
        
        roles.remove(oldAuth);
    }
    
    public void setRoles(String [] roles) {
        this.roles = new ArrayList<UserAuthority>();
        
        for(String role : roles) {
            this.addRole(role);
        }
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
    @Override
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
    public List<UserAuthority> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<UserAuthority> roles) {
        this.roles = roles;
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

}
