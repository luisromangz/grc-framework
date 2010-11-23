package com.greenriver.commons.web.helpers.session;

/**
 * Default implementation of InstallHelper
 * @author luis
 */
public class InstallHelperImpl implements InstallHelper {
    private String key;
    private boolean keyFileCreated;
    private String keyFilePath;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.keyFileCreated = false;
    }

    public boolean isKeyFileCreated() {
        return keyFileCreated;
    }

    public void setKeyFileCreated(boolean keyFileCreated) {
        this.keyFileCreated = keyFileCreated;
    }

    public String getKeyFilePath() {
        return this.keyFilePath;
    }

    public void setKeyFilePath(String path) {
        this.keyFilePath = path;
    }
}
