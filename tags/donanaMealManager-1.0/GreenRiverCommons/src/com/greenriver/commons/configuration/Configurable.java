package com.greenriver.commons.configuration;

/**
 * Marks the implementor as configurable throught a settings provider
 * implementation.
 * @param <T> Concrete SettingsProvider implementation
 * @author mangelp
 */
public interface Configurable<T extends SettingsProvider> {

    /**
     * Configures using the settings provider
     * @param settingsProvider
     */
    void configure(T settingsProvider);
}
