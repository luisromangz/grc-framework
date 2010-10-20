/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.configuration;

import java.util.List;

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

    /**
     * Gets a key converting the value to an enumeration constant.
     * If the conversion fails the default value (that can't be null) is
     * returned.
     * @param fullKey Key to get under the root of this provider.
     * @param defValue Default value of the enumeration. Can't be null.
     * @param enumType 
     * @return the key value converted to the enumeration or the default value.
     */
    Object getEnum(String fullKey, Object defValue, Class enumType);

    /**
     * Gets a value converted into a list of strings. The strings are separated
     * using split() method without no further checks.
     * @param fullKey
     * @param defValue
     * @param separator String item separator
     * @return a list of strings or the default value.
     */
    List<String> getList(String fullKey, List<String> defValue,
	    String separator);

    /**
     * Sets a key and the value. This implementation is simply a wrapper to
     * the same method in the storage implementation.
     * @param key
     * @param value
     */
    void set(String key, String value);

    void setBool(String key, boolean value);

    void setByte(String key, byte value);

    void setEnum(String key, Enum value);

    void setInt(String key, int value);

    void setList(String key, List<String> value, String separator);

    void setLong(String key, long value);

    void setShort(String key, short value);
}
