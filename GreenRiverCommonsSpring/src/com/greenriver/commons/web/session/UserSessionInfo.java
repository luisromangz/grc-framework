package com.greenriver.commons.web.session;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import java.io.Serializable;

/**
 * User session information operations
 * @author luis
 */
public interface UserSessionInfo  extends Serializable {

    /**
     * Retrieves the current User object for the session.
     * @return The session's User.
     */
    User getCurrentUser();

    /**
     * Sets the user's dao
     * @param userDao the userDao to set
     */
    void setUserDao(UserDao userDao);
}
