/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Hierarchical storage of settings as pairs key-value. This implementation is
 * synchronized.
 */
public class DictionarySettingsStorage implements SettingsStorage {

    private Map<String, String> data;
    private SettingsSource source;
    private boolean loaded;
    private boolean reloaded;
    private SettingsLoadedObservable settingsLoadedObservable;

    /**
     * @return the source
     */
    public SettingsSource getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public synchronized void setSource(SettingsSource source) {
        if (loaded) {
            throw new IllegalStateException("The source have been already " +
                    "loaded. You can't set a new one.");
        }

        this.source = source;
    }

    /**
     * Gets if the source have been loaded.
     * @return if the source have been loaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    public DictionarySettingsStorage() {
        //This implementation of map is not synchronized
        data = new HashMap<String, String>();
        settingsLoadedObservable = new SettingsLoadedObservable();
    }

    public DictionarySettingsStorage(SettingsSource cfg) {
        this();
        source = cfg;
    }

    /**
     * If a valid configuration source have been provided this method ensures
     * that when called it will call the load of the source only if there is
     * no data loaded and the load method haven't been called before.
     */
    private boolean ensureLoad() {
        if (source != null && !loaded && data.size() == 0) {
            load();
        }

        return source != null && loaded;
    }

    public synchronized void set(String key, String value) {
        //If the same as add for this implementation
        data.put(key, value);
    }

    public synchronized String get(String key, String defaultValue) {
        ensureLoad();
        if (data.containsKey(key)) {
            return data.get(key);
        } else {
            return defaultValue;
        }
    }

    public synchronized String[] getSubKeys(String key) {
        ensureLoad();
        if (data.containsKey(key)) {
            return new String[]{data.get(key)};
        } else {
            return new String[]{};
        }
    }

    public synchronized String[] getValues(String key) {
        ensureLoad();
        if (data.containsKey(key)) {
            return new String[]{data.get(key)};
        } else {
            return new String[]{};
        }
    }

    public synchronized int size(String key) {
        ensureLoad();
        if (data.containsKey(key)) {
            return 1;
        } else {
            return 0;
        }
    }

    public synchronized void clear() {
        data.clear();
    }

    public synchronized void clear(String key) {
        ensureLoad();
        if (data.containsKey(key)) {
            data.put(key, null);
        } else {
            throw new IllegalArgumentException("Key not exists");
        }
    }

    public synchronized void save() {
        if (source == null) {
            throw new IllegalStateException("Can't save because there is " +
                    "no ConfigSource implementation");
        }

        if (!source.canSave()) {
            throw new UnsupportedOperationException("Source doesn't allows " +
                    "to save");
        }

        source.save(this);
    }

    /**
     * Each load does first a clear and then a fill over the source
     */
    public synchronized void load() {
        if (source == null) {
            return;
        }
        
        if (loaded) {
            throw new IllegalStateException(
                    "Source already loaded. Use reload().");
        }

        source.fill(this);
        loaded = true;
        settingsLoadedObservable.notifyObservers(this,
                new SettingsLoadedObserverArgs(reloaded));
    }

    public synchronized void reload() {
        clear();
        loaded = false;
        reloaded = true;
        try {
            load();
        } catch (RuntimeException ex) {
            reloaded = false;
            throw ex;
        }
    }

    public synchronized boolean hasKey(String key) {
        return data.containsKey(key);
    }
}
