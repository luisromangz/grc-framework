/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.configuration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Concrete configuration source to save settings from ini-style files with no
 * sections and no subkey support.
 *
 * Due to the hierarchical nature of the settings this implementation uses as
 * root the key named 'app', so any setting can be retrieved using app as the
 * root.
 *
 * Example:
 *
 * SettingsStorage storage = ...
 * SettingsSource src = new IniSettingsSource();
 * src.fill(storage);
 * storage.get("app.debug", "true");
 */
public class IniSettingsSource implements SettingsSource,
        FileSettingsSource {

    private File file;
    private char valueSeparator = '=';
    private char lineComment = '#';

    public File getFile() {
        return file;
    }

    /**
     * @return the valueSeparator
     */
    public char getValueSeparator() {
        return valueSeparator;
    }

    /**
     * @param valueSeparator the valueSeparator to set
     */
    public void setValueSeparator(char valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    /**
     * @return the lineComment
     */
    public char getLineComment() {
        return lineComment;
    }

    /**
     * @param lineComment the lineComment to set
     */
    public void setLineComment(char lineComment) {
        this.lineComment = lineComment;
    }

    public IniSettingsSource(File file) throws FileNotFoundException {
        this.file = file;
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getPath());
        }
    }

    /**
     * @throws SettingsException
     * @param storage
     */
    public void fill(SettingsStorage storage) {
        BufferedReader reader = null;

        try {
            FileInputStream fileInput = new FileInputStream(file);
            DataInputStream dataInput = new DataInputStream(fileInput);
            reader = new BufferedReader(new InputStreamReader(dataInput));
            internalFill(reader, storage);
        } catch (FileNotFoundException ex) {
            throw new SettingsException("Can't access file " +
                    file.getAbsolutePath() + ".", ex);
        } catch (IOException ex) {
            throw new SettingsException("Can't read data from file " +
                    file.getAbsolutePath() + ".", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * @throws FileNotFoundException 
     * @throws IOException 
     * @throws SettingsException
     * @param reader
     * @param storage
     */
    protected void internalFill(BufferedReader reader, SettingsStorage storage)
            throws FileNotFoundException, IOException {


        String line = null;
        int sepPos = 0;
        int sharpPos = 0;
        String key = null;
        String value = null;

        storage.clear();

        //First create a root key and set it, then we can start to parse lines
        //and add pairs key-value
        storage.add("app", "app");


        line = reader.readLine();

        while (line != null) {
            line = line.trim();

            //If the line is empty or if it starts with # ignore it
            if (line.length() > 0) {

                sharpPos = line.indexOf(getLineComment());

                if (sharpPos >= 0) {
                    line = line.substring(0, sharpPos);
                }

                sepPos = line.indexOf(getValueSeparator());

                //Lines with no separator or with it in the first position are
                //ignored
                if (sepPos >= 0) {
                    key = line.substring(0, sepPos).trim();
                    if (!key.startsWith("app.")) {
                        throw new SettingsException(
                                "Keys must start with app. prefix");
                    }
                    value = line.substring(sepPos + 1).trim();
                    storage.add(key, value);
                }
            }
            line = reader.readLine();
        }
    }

    public boolean canSave() {
        return false;
    }

    public boolean canReload() {
        return true;
    }

    public void save(SettingsStorage storage) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
