/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.configuration;

import com.greenriver.commons.Strings;
import java.util.Arrays;
import java.util.List;

/**
 * Simple settings provider implementation that provides some useful operations
 * over a storage. This settings provider is the one used by the Application
 * global object and then is rooted into "app" key. <br/><br/>To properly use
 * a configuration storage throught a provider the key hierarchy introduced by
 * the provider must be follow or the client that is reading this configuration
 * won't find the keys. This means to use all the methods of this provider to
 * get or set values. You can set them in the storage but prefixing the keys
 * with the proper 'superkey.sperkeysubkey' hierarchy. The client reading the
 * keys must use the prefix or will be unable to read the configuration values.
 */
public class ApplicationSettingsProvider implements SettingsProvider {

    private SettingsStorage storage;
    private String root = "app";
    private SettingsProvider parent;

    /**
     * Parent settings provider under whose root is the root of this provider.
     * @return the parent provider.
     */
    public SettingsProvider getParent() {
        return parent;
    }

    /**
     * Returns the key that this provider is using as the root. All the keys
     * have this string appended at the start so they are accessed within this
     * hirearchy.
     * @return provider root.
     */
    public String getRoot() {
        return root;
    }

    /**
     * Sets the storage in use.
     * @param storage
     */
    public void setStorage(SettingsStorage storage) {
        this.storage = storage;
    }

    /**
     * Gets the storage in use.
     * @return
     */
    public SettingsStorage getStorage() {
        return storage;
    }

    /**
     * Initiallizes this provider from another one and setting the root of this
     * provider under the root of the other.
     * @param provider Provider root from.
     * @param subKeyRoot subkey under the root of the other provider.
     */
    public ApplicationSettingsProvider(
            SettingsProvider provider, String subKeyRoot) {
        this(provider.getStorage());
        if (Strings.isNullOrEmpty(subKeyRoot)) {
            throw new IllegalArgumentException(
                    "SubKeyRoot cannot be null nor empty.");
        }

        if (!Strings.isNullOrEmpty(provider.getRoot())) {
            root = provider.getRoot() + "." + subKeyRoot;
        }

        this.parent = provider;
    }

    /**
     * Initializes the provider with a new empty Dictionary storage.
     */
    public ApplicationSettingsProvider() {
        this(new DictionarySettingsStorage());
    }

    /**
     * Initializes the provider with a new empty Dictionary storage and sets it
     * rooted into a configuration key prefix.
     * @param root
     * @throws IllegalArgumentException if the root is empty, null or ends with
     * a dot.
     */
    public ApplicationSettingsProvider(String root) {
        this(new DictionarySettingsStorage(), root);
    }

    /**
     * Initializes the provider with a storage.
     * @param storage 
     */
    public ApplicationSettingsProvider(SettingsStorage storage) {
        this.storage = storage;
    }

    /**
     * Initializes the provider with a storage and sets it rooted into a
     * configuration key prefix.
     * @param storage
     * @param root
     * @throws IllegalArgumentException if the root is empty, null or ends with
     * a dot.
     */
    public ApplicationSettingsProvider(SettingsStorage storage, String root) {
        this(storage);

        if (Strings.isNullOrEmpty(root)) {
            throw new IllegalArgumentException("Root cannot be null or empty");
        }

        if (root.endsWith(".")) {
            throw new IllegalArgumentException("Root cannot end with a dot.");
        }

        this.root = root;
    }

    /**
     * If the storage is null returns an exception and if not ensures that
     * is loaded.
     */
    protected void assertStorage() {
        if (storage == null) {
            throw new IllegalStateException("No storage in use. " +
                    "You must use one.");
        }

        if (!storage.isLoaded()) {
            storage.load();
        }
    }

    /**
     * Gets a value as an string. If the key is not set it returns the
     * default value.
     * @param fullKey Key for the value.
     * @param defaultValue Default value.
     * @return the value if the key is set or the default value if not.
     */
    public String get(String fullKey, String defaultValue) {
        assertStorage();
        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }
        return storage.get(fullKey, defaultValue);
    }

    /**
     * Gets a value as an string. If the key is not set it returns a null.
     * @param fullKey Key for the value.
     * @return the value if the key is set or null if not.
     */
    public String get(String fullKey) {
        assertStorage();
        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }
        return storage.get(fullKey, null);
    }

    /**
     * Gets a value converted to bool
     * @param fullKey
     * @param defaultValue
     * @return
     */
    public boolean getBool(String fullKey, boolean defaultValue) {
        assertStorage();
        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defaultValue;
        }

        try {
            return Boolean.valueOf(storage.get(fullKey, ""));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Gets a value converted to int
     * @param fullKey
     * @param defValue
     * @return
     */
    public int getInt(String fullKey, int defValue) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        try {
            return Integer.parseInt(storage.get(fullKey, ""));
        } catch (NumberFormatException ex) {
            return defValue;
        }
    }

    /**
     * Gets a value converted to long
     * @param fullKey
     * @param defValue
     * @return
     */
    public long getLong(String fullKey, long defValue) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        try {
            return Long.parseLong(storage.get(fullKey, ""));
        } catch (NumberFormatException ex) {
            return defValue;
        }
    }

    /**
     * Gets a value converted to byte
     * @param fullKey
     * @param defValue
     * @return
     */
    public byte getByte(String fullKey, byte defValue) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        try {
            return Byte.parseByte(storage.get(fullKey, ""));
        } catch (NumberFormatException ex) {
            return defValue;
        }
    }

    /**
     * Gets a value converted to short
     * @param fullKey
     * @param defValue
     * @return
     */
    public short getShort(String fullKey, short defValue) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        try {
            return Short.parseShort(storage.get(fullKey, ""));
        } catch (NumberFormatException ex) {
            return defValue;
        }
    }

    /**
     * Gets a value converted into a list of strings. The strings are separated
     * using split() method without no further checks.
     * @param fullKey
     * @param defValue
     * @param separator String item separator
     * @return a list of strings or the default value.
     */
    public List<String> getList(String fullKey, List<String> defValue,
            String separator) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        String value = storage.get(fullKey, null);

        if (value == null) {
            return defValue;
        } else {
            return Arrays.asList(value.split(separator));
        }
    }

    /**
     * Gets a key converting the value to an enumeration constant. The value
     * of the key (as an string) is converted to upercase before using the
     * valueOf method of the Enum type to try to convert it.
     * <br/>
     * If the conversion fails the default value (that can't be null) is
     * returned.
     * @param fullKey Key to get under the root of this provider.
     * @param defValue Default value of the enumeration. Can't be null.
     * @return the key value converted to the enumeration or the default value.
     */
    public Enum getEnum(String fullKey, Enum defValue) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            fullKey = root + "." + fullKey;
        }

        if (!storage.hasKey(fullKey)) {
            return defValue;
        }

        if (defValue == null) {
            throw new IllegalArgumentException("Can't work with a null " +
                    "default value. Must provide a valid one.");
        }

        //This won't return null as we have already checked that we have a
        //value for it.
        String value = storage.get(fullKey, null);
        Enum result = null;
        Class enumClass = defValue.getClass();

        //If the value set is null we can't parse it.
        if (value == null) {
            return defValue;
        } else {
            value = value.toUpperCase();
        }

        try {
            result = Enum.valueOf(enumClass, value);
        } catch (RuntimeException ex) {
            return defValue;
        }

        return result;
    }

    /**
     * Sets a key and the value. This implementation is simply a wrapper to
     * the same method in the storage implementation.
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        assertStorage();

        if (!Strings.isNullOrEmpty(root)) {
            key = root + "." + key;
        }

        storage.set(key, value);
    }
}
