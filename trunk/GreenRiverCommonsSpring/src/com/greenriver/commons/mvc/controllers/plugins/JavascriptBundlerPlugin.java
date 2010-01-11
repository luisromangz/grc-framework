package com.greenriver.commons.mvc.controllers.plugins;

import com.greenriver.commons.mvc.configuration.PageConfiguration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author luis
 */
public class JavascriptBundlerPlugin extends BaseBundlerPlugin {

    public JavascriptBundlerPlugin () {
        this.setBundlePrefix("js");
    }

    @Override
    protected List<String> getFileNames(PageConfiguration configuration) {
        return configuration.getJavaScriptFiles();
    }

    @Override
    protected void addBundle(String bundleName, PageConfiguration configuration) {
        configuration.getJavaScriptFiles().clear();
        configuration.addJavaScriptFile(bundleName);
    }

    @Override
    protected void bundleFiles(PageConfiguration configuration, File bundleFile) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(bundleFile));
            for (String fileName : configuration.getJavaScriptFiles()) {
                addFileToBundle(fileName, writer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void addFileToBundle(String fileName, BufferedWriter writer)
            throws IOException {
        BufferedReader reader =
                new BufferedReader(
                new FileReader(
                String.format("%s/%s.js", this.getJavascriptBasePath(), fileName)));

        writer.newLine();
        writer.write("// File: "+fileName);
        writer.newLine();

        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }

            writer.write(line);
            writer.newLine();
        }
    }
}
