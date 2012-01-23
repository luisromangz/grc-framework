
package com.greenriver.commons.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * User role entity.
 * @author luis
 */
@Entity
public class UserAuthority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne    
    private User user;

    private String authority;

  

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAuthority)) {
            return false;
        }
        UserAuthority other = (UserAuthority) object;
        if (!user.equals(other.user) || !authority.equals(other.authority)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 67 * hash + (this.authority != null ? this.authority.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.greenriver.commons.data.model.UserAuthority[id=" + id + "]";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
   
    /**
     * @return the authority
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * @param authority the authority to set
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
