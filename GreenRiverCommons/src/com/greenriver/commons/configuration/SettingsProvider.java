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
 * Source of configuration for hierarchical settings.
 *
 * Al the settings key and values must be of type String.
 *
 * @author mangelp
 */
public interface SettingsProvider {
    /**
     * Finds the key and returns a value if it exists or the default value if
     * it doesn't exists.
     *
     * The key is an string with the names of the parent hierarchy items separated
     * by dots. The last part must be a key as this method tries to find a leaf
     * and return his value.
     *
     * @param fullKey dot-spaced path in the hierarchy to the key
     * @param defaultValue default value to return when the key is not found
     * @return
     */
    public String get(String fullKey, String defaultValue);

    /**
     * Finds the key and returns his value if it exists, if not returns null.
     *
     * The key is an string with the names of the parent hierarchy items separated
     * by dots. The last part must be a key as this method tries to find a leaf
     * and return his value.
     *
     * The key is an string with the full path to th
     * @param fullKey dot-spaced path in the hierarchy to the key
     * @return
     */
    public String get(String fullKey);

    /**
     * Gets value as a boolean. If it cannot be converted returns the default
     * value.
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBool(String key, boolean defaultValue);

    /**
     * Converts the value to an integer and returns it. If it can't do the
     * conversion it returns the default value.
     * @param key
     * @param defValue
     * @return
     */
    public int  getInt(String key, int defValue);

    /**
     * Converts the value to a long and returns it. If it can't do the
     * conversion it returns the default value.
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue);

    /**
     * Converts the value to a byte and returns it. If it can't do the
     * conversion it returns the default value.
     * @param key
     * @param defValue
     * @return
     */
    public byte getByte(String key, byte defValue);

    /**
     * Converts the value to a short and returns it. If it can't do the
     * conversion it returns the default value.
     * @param key
     * @param defValue
     * @return
     */
    public short getShort(String key, short defValue);

    /**
     * Gets the storage where the configuration is
     * @return the configuration storage
     */
    public SettingsStorage getStorage();

    /**
     * Returns the root key that this provider used to provide access to
     * subkeys.<br/>This root is appended to every key before looking for it
     * using a dot as a separator and thus providing access to all subkeys
     * under it.
     * @return the root key.
     */
    String getRoot();
}
