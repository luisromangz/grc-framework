package com.greenriver.commons.web.session;

import java.io.Serializable;

/**
 * Helper operations to store information needed for authenticating an
 * installation through a file that contains a special key.
 * @author luis
 */
public interface InstallHelper extends Serializable {

    /**
     * Gets the key used to authenticate access
     * @return the key
     */
    String getKey();

    /**
     * Gets the absolute path to the key file
     * @return path to the key file
     */
    String getKeyFilePath();

    /**
     * Gets if the key file have been created
     * @return if the key file have been created
     */
    boolean isKeyFileCreated();

    /**
     * Sets the key used to authenticate access
     * @param key the key to set
     */
    void setKey(String key);

    /**
     * Sets if the key file have been created or not
     * @param keyFileCreated value of the flag
     */
    void setKeyFileCreated(boolean keyFileCreated);

    /**
     * Sets the path to the key file
     * @param path Absolute path to the key file
     */
    void setKeyFilePath(String path);   
}
