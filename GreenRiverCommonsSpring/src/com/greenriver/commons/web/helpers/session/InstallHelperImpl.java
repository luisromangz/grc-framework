package com.greenriver.commons.web.helpers.session;

/**
 * Default implementation of InstallHelper
 * @author luis
 */
public class InstallHelperImpl implements InstallHelper {
    private String key;
    private boolean keyFileCreated;
    private String keyFilePath;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
        this.keyFileCreated = false;
    }

    @Override
    public boolean isKeyFileCreated() {
        return keyFileCreated;
    }

    @Override
    public void setKeyFileCreated(boolean keyFileCreated) {
        this.keyFileCreated = keyFileCreated;
    }

    @Override
    public String getKeyFilePath() {
        return this.keyFilePath;
    }

    @Override
    public void setKeyFilePath(String path) {
        this.keyFilePath = path;
    }
}
