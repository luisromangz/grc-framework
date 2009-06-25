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
 * Operations for a loader of settings from a source.
 */
public interface SettingsSource {

    /**
     * Gets if this source allows to save settings to the underlying media
     * @return if it can save settings or not.
     */
    public boolean canSave();

    /**
     * Gets if the source allows loading again from the media and filling into
     * the storage with the latest version available.
     * @return if it can reload settings or not.
     */
    public boolean canReload();

    /**
     * Loads configuration settings from the source and sets them into the
     * storage.
     * @param storage
     */
    public void fill(SettingsStorage storage);

    /**
     * Stores configuration settings from the storage into the underlying media
     * @param storage
     */
    public void save(SettingsStorage storage);
}
