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
 * won't find the keys. <br/> If the methods of this provider are used the
 * root key prefix is appended to each key before trying to get it. <br/>
 * If the storage is accessed directly the client must supply the right key
 * prefixing it with the root key prefix of the provider.
 */
public class ApplicationSettingsProvider implements SettingsProvider {

    private static String keySeparator = ".";
    private static String defaultRoot = "app";

    /**
     * Wraps a provider inside an ApplicationSettingsProvider using a key prefix
     * (a root) appended to each accessed key.<br/>
     * If the provider is already of type ApplicationSettingsProvider the new
     * provider wraps the storage of the first one using the root key resulting
     * of adding to the root key of the first provider the new subkey root.
     * @param settingsProvider
     * @param subKeyRoot
     * @return a new ApplicationSettingsProvider instance
     */
    public static ApplicationSettingsProvider wrapProvider(
	    SettingsProvider settingsProvider,
	    String subKeyRoot) {
	ApplicationSettingsProvider other = null;
	SettingsStorage storage = null;
	boolean end = false;

	//If the another instance is the same type as this we can avoid
	//wrap overhead.
	while (!end && settingsProvider instanceof ApplicationSettingsProvider) {
	    other = (ApplicationSettingsProvider) settingsProvider;
	    
	    if (other.getParent() != null) {
		subKeyRoot = other.getRoot() + keySeparator + subKeyRoot;
		settingsProvider = other.getParent();
	    } else {
		storage = other.getStorage();
		end = true;
	    }
	}

	//If there is an storage we can create the new provider to use it, if
	//not we must create our provider over an existing one.
	if (storage == null) {
	    return new ApplicationSettingsProvider(settingsProvider, subKeyRoot);
	} else {
	    return new ApplicationSettingsProvider(storage, subKeyRoot);
	}
    }
    
    private SettingsStorage storage;
    private String root = defaultRoot;
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
	    root = provider.getRoot() + keySeparator + subKeyRoot;
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
    protected void assertSettingsSource() {
	if (storage == null && parent == null) {
	    throw new IllegalStateException("No storage or parent provider" +
		    " in use. You must use one.");
	}

	if (storage != null && !storage.isLoaded()) {
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
	assertSettingsSource();
	
	if (!Strings.isNullOrEmpty(root)) {
	    fullKey = root + keySeparator + fullKey;
	}

	if (storage != null && storage.hasKey(fullKey)) {
	    return storage.get(fullKey, defaultValue);
	} else if (storage != null && parent != null) {
	    return parent.get(fullKey, defaultValue);
	} else {
	    return defaultValue;
	}
    }

    /**
     * Gets a value as an string. If the key is not set it returns a null.
     * This method is equivalent to get(String, null).
     * @param fullKey Key for the value.
     * @return the value if the key is set or null if not.
     */
    public String get(String fullKey) {
	return get(fullKey, null);
    }

    /**
     * Gets a value converted to bool
     * @param fullKey
     * @param defValue
     * @return
     */
    public boolean getBool(String fullKey, boolean defValue) {
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}
	
	try {
	    return Boolean.valueOf(value);
	} catch (RuntimeException ex) {
	    return defValue;
	}
    }

    /**
     * Gets a value converted to int
     * @param fullKey
     * @param defValue
     * @return
     */
    public int getInt(String fullKey, int defValue) {
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	try {
	    return Integer.parseInt(value);
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
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	try {
	    return Long.parseLong(value);
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
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	try {
	    return Byte.parseByte(value);
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
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	try {
	    return Short.parseShort(value);
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
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	return Arrays.asList(value.split(separator));
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
     * @param enumType Type of the enumeration
     * @return the key value converted to the enumeration or the default value.
     */
    public Object getEnum(String fullKey, Object defValue, Class enumType) {
	String value = get(fullKey);

	if (value == null) {
	    return defValue;
	}

	try {
	    return Enum.valueOf(enumType, value);
	} catch (RuntimeException ex) {
	    return defValue;
	}
    }

    /**
     * Sets a key and the value. This implementation is simply a wrapper to
     * the same method in the storage implementation.
     * @param key
     * @param value
     */
    public void set(String key, String value) {
	assertSettingsSource();

	if (!Strings.isNullOrEmpty(root)) {
	    key = root + keySeparator + key;
	}

	storage.set(key, value);
    }

    public void setBool(String key, boolean value) {
	set(key, value ? "true" : "false");
    }

    public void setByte(String key, byte value) {
	set(key, value + "");
    }

    public void setShort(String key, short value) {
	set(key, value + "");
    }

    public void setInt(String key, int value) {
	set(key, value + "");
    }

    public void setLong(String key, long value) {
	set(key, value + "");
    }

    public void setList(String key, List<String> value, String separator) {
	set(key, Strings.join(value, separator));
    }

    public void setEnum(String key, Enum value) {
	set(key, value.name());
    }
}
