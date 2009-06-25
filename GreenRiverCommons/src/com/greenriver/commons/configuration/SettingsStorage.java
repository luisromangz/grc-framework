/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.configuration;

/**
 * Operations for the in-memory storage of settings loaded from a source.
 * Implementors are responsible of the concrete storage implementation.
 * @author mangelp
 */
public interface SettingsStorage {

    /**
     * Stores configuration in the source.
     * @throws UnsupportedOperationException if the source can't store data
     */
    public void save();

    /**
     * Gets if this storage have been loaded from any source
     * @return if the storage have been loaded.
     */
    public boolean isLoaded();

    /**
     * Explicitly loads the storage with the data got from a source.
     * @throws IllegalStateException If this method is called twice. For
     * reloading must use reload() method.
     */
    public void load();

    /**
     * Reloads the configuration from the source.
     * @throws UnsupportedOperationException if the source doesn't support
     * this operation.
     */
    public void reload();

    /**
     * Sets the source to use. This has no effect until load or reload are used.
     * @param src
     */
    public void setSource(SettingsSource src);

    /**
     * Gets the source in use
     * @return the configuration source
     */
    public SettingsSource getSource();

    /**
     * Checks for a key to exists.
     * @param key
     * @return
     */
    public boolean hasKey(String key);

    /**
     * Adds a new key-value
     * @param key
     * @param value
     */
    public void add(String key, String value);

    /**
     * Gets a key value
     * @param key
     * @param defaultValue
     * @return the key value
     */
    public String get(String key, String defaultValue);

    /**
     * Sets the value of a key in the storage
     * @param key
     * @param value
     */
    public void set(String key, String value);

    /**
     * Adds a new key and some subkeys
     * @param key
     * @param subKeys
     */
    public void add(String key, String[] subKeys);

    /**
     * Gets all subkey names if key matches a node. If not returns an empty array.
     * @param key
     * @return
     */
    public String[] getSubKeys(String key);

    /**
     * Gets all values if the key matches a leaf, if not returns empty array
     * @param key
     * @return
     */
    public String[] getValues(String key);

    /**
     * Gets the number of sub-elements of a key
     * @param key
     * @return the number of sub-elements of a key
     */
    public int size(String key);

    /**
     * Removes everything
     */
    public void clear();

    /**
     * Removes everything under a key but doesn't removes that key.
     * @param key
     * @throws IllegalArgumentException if the key doesn't exists
     */
    public void clear(String key);
}
