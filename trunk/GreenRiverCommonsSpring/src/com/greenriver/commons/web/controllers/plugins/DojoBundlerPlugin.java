package com.greenriver.commons.web.controllers.plugins;

import com.greenriver.commons.web.configuration.PageConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Creates a bundle of dojo javascript files on the fly reducing the number of
 * request for dojo components to almost 1. Also supports minimizing the size of
 * the bundle by using a javascript compressor like yui's javascript compressor.
 * 
 * @author luis
 */
public class DojoBundlerPlugin extends BaseBundlerPlugin {

    private List<String> excludedNamespaces;
    private List<String> localizations;
    private static final String DOJO_REQUIRE_REGEX =
            "dojo\\.require\\([\"'](.*)[\"']\\)(;)?";
    private static final String MODULE_NAME_REGEX = "^(\\w+\\.)*\\w+$";
    private static final String DOJO_REQUIRE_LOCALIZATION_REGEX =
            "dojo\\.requireLocalization\\([\"']((?:\\w+\\.)*\\w+)['\"],[\"'](\\w+)['\"],.*\\)(?:;)";
    private static final String TEMPLATE_PATH_REGEX =
           "templatePath\\s*:\\s*dojo.moduleUrl\\(['\"]((?:\\w+\\.)*\\w+)['\"],['\"]((?:\\w+/)*\\w+\\.html)['\"]\\)";

    public DojoBundlerPlugin() {
        this.excludedNamespaces = new ArrayList<String>();
        this.setBundlePrefix("dojo");
    }

    @Override
    protected List<String> getFileNames(PageConfig configuration) {
        return configuration.getDojoModules();
    }

    @Override
    protected void addBundle(String bundleName, PageConfig configuration) {
        // When using the auto bundler, we discard other bundles.
        configuration.getDojoModules().clear();
        configuration.getDojoBundles().clear();
        configuration.addDojoBundle(bundleName);
    }

    @Override
    protected void bundleFiles(PageConfig configuration, File bundleFile) {

        if (configuration.getDojoModules().isEmpty()) {
            throw new RuntimeException("We should have dojo modules.");
        }
        addDojoModules(configuration.getDojoModules(), bundleFile);
    }

    public void addDojoModules(List<String> newModules, File bundleFile) {

        BufferedWriter fileWriter = null;

        List<String> loadedModules = new ArrayList<String>();
        List<String> loadedLocalizations = new ArrayList<String>();

        try {
            fileWriter = new BufferedWriter(new FileWriter(
                    bundleFile, false));

            for (String newModule : newModules) {
                if (loadedModules.contains(newModule)
                        || isModuleExcluded(newModule)) {
                    continue;
                }

                processModuleFile(loadedModules,
                        loadedLocalizations,
                        newModule,
                        fileWriter);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public boolean isModuleExcluded(String moduleName) {
        for (String excludedNamespace : this.excludedNamespaces) {
            if (moduleName.startsWith(excludedNamespace)) {
                return true;
            }
        }

        return false;
    }

    public String getPathFromModuleName(String moduleName) {
        return this.getJavascriptBasePath() + "/" + moduleName.replace('.', '/') + ".js";
    }

    public void processModuleFile(
            List<String> loadedModules,
            List<String> loadedLocalizations,
            String moduleName,
            BufferedWriter fileWriter) throws IOException {

        if (loadedModules.contains(moduleName)) {
            return;
        }

        // We wont further process this module.
        loadedModules.add(moduleName);

        File moduleFile = new File(getPathFromModuleName(moduleName.trim()));
        Scanner scanner = null;

        try {
            scanner = new Scanner(moduleFile);

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        List<String> lines = new ArrayList<String>();

        // We start reading the module file
        while (scanner.hasNextLine()) {

            String nextLine = scanner.nextLine();

            // We search for a declare statement in the line.           
            searchDojoRequire(
                    nextLine,
                    loadedModules, loadedLocalizations,
                    fileWriter);

            searchDojoRequireLocalization(
                    nextLine,
                    loadedLocalizations,
                    fileWriter);

            nextLine = searchTemplatePathAttribute(nextLine);

            if (!nextLine.isEmpty()) {
                lines.add(nextLine);
            }
        }

        fileWriter.write("// Module: " + moduleName);
        fileWriter.newLine();

        for (String line : lines) {
            fileWriter.write(line);
            fileWriter.newLine();
        }

        scanner.close();
    }

    private String searchTemplatePathAttribute(
            String currentLine) throws FileNotFoundException {

        Scanner scanner= new Scanner(currentLine);
        String templatePathFound = scanner.findInLine(TEMPLATE_PATH_REGEX);
        if (templatePathFound == null) {
            return currentLine;
        }

        MatchResult matchResult = scanner.match();
        for(int groupIndex = 0; groupIndex <matchResult.groupCount();groupIndex+=3) {
            String templatePathStatement = matchResult.group(groupIndex);
            String modulePath = matchResult.group(groupIndex+1);
            String filePath = matchResult.group(groupIndex+2);

            // We create the imported file's path.
            filePath=getPathFromModuleName(modulePath).replace(".js", "/")+filePath;

            Scanner resourceScanner = new Scanner(new File(filePath));

            StringBuilder templateStringBuilder = new StringBuilder("templateString: '");
            while(resourceScanner.hasNextLine()) {
                String resourceLine = resourceScanner.nextLine();

                templateStringBuilder.append(resourceLine.trim());
            }
            templateStringBuilder.append("'");

            resourceScanner.close();

            currentLine=
                    currentLine.replace(templatePathStatement, templateStringBuilder.toString());
        }

        scanner.close();

        return currentLine.trim();
    }

    private void searchDojoRequire(
            String currentLine,
            List<String> loadedModules,
            List<String> loadedLocalizations,            
            BufferedWriter fileWriter) throws IOException {

        Scanner scanner = new Scanner(currentLine);
        String requireFound = scanner.findInLine(DOJO_REQUIRE_REGEX);

        if (requireFound == null) {
            return;
        }

        MatchResult matchResult = scanner.match();
        for (int groupIndex = 1; groupIndex < matchResult.groupCount(); groupIndex++) {
            String requiredModule = matchResult.group(groupIndex);

            if (!requiredModule.matches(MODULE_NAME_REGEX)) {
                continue;
            }

            // We have found a declare statement, so we remove process it
            // before we write anything to the file.
            if (!isModuleExcluded(requiredModule)) {
                processModuleFile(
                        loadedModules,
                        loadedLocalizations,
                        requiredModule,
                        fileWriter);
            }
        }

        scanner.close();
    }

    private void searchDojoRequireLocalization(
            String currentLine,
            List<String> loadedLocalizations,            
            BufferedWriter fileWriter) throws IOException {
        Scanner scanner=new Scanner(currentLine);
        String requireFound = scanner.findInLine(DOJO_REQUIRE_LOCALIZATION_REGEX);

        if (requireFound == null) {
            return;
            
        }
        MatchResult matchResult = scanner.match();
        for (int groupIndex = 1; groupIndex < matchResult.groupCount(); groupIndex++) {
            String modulePath = matchResult.group(groupIndex);

            if (!modulePath.matches(MODULE_NAME_REGEX)) {
                continue;
            }

            // The next parameter is the module name.
            groupIndex++;
            String moduleName = matchResult.group(groupIndex);

            String localizationName = modulePath + "." + moduleName;
            if (!loadedLocalizations.contains(localizationName)) {
                loadedLocalizations.add(localizationName);
                addLocalizationFiles(modulePath, moduleName, fileWriter);
            }
        }

        scanner.close();
    }

    private void addLocalizationFiles(
            String localizedModulePath,
            String localizedModuleName,
            BufferedWriter fileWriter) throws IOException {

        fileWriter.write("// Localization files:" + localizedModulePath + ".nls." + localizedModuleName);
        fileWriter.newLine();
        fileWriter.write(String.format("dojo.provide(\"%s.nls.%s\");",
                localizedModulePath,
                localizedModuleName));
        fileWriter.newLine();

        fileWriter.write(String.format("%s.nls.%s._built=true",
                localizedModulePath,
                localizedModuleName));
        fileWriter.newLine();

        for (String localization : localizations) {
            addLocalizationFile(localizedModulePath, localization, localizedModuleName, fileWriter);
        }
    }

    private void addLocalizationFile(
            String localizedModulePath,
            String localization,
            String localizedModuleName,
            BufferedWriter fileWriter) throws IOException {
        String filePath = String.format("%s.nls.%s.%s",
                localizedModulePath,
                localization,
                localizedModuleName);
        File localizationFile = new File(getPathFromModuleName(
                filePath));

        Scanner scanner = null;
        try {
            scanner = new Scanner(localizationFile);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        fileWriter.write(String.format("dojo.provide(\"%s.nls.%s.%s\");",
                localizedModulePath,
                localizedModuleName,
                localization.replace("-", "_")));
        fileWriter.newLine();

        fileWriter.write(String.format("%s.nls.%s.%s=",
                localizedModulePath,
                localizedModuleName,
                localization.replace("-", "_")));

        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            fileWriter.write(nextLine);
        }

        fileWriter.newLine();

        scanner.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the excludedNamespaces
     */
    public List<String> getExcludedNamespaces() {
        return excludedNamespaces;
    }

    /**
     * @param excludedNamespaces the excludedNamespaces to set
     */
    public void setExcludedNamespaces(List<String> excludedNamespaces) {
        this.excludedNamespaces = excludedNamespaces;
    }

    /**
     * @return the localizations
     */
    public List<String> getLocalizations() {
        return localizations;
    }

    /**
     * @param localizations the localizations to set
     */
    public void setLocalizations(List<String> localizations) {
        this.localizations = localizations;
    }
    // </editor-fold>
}
