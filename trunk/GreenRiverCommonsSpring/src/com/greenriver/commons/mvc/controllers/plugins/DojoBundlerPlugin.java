
package com.greenriver.commons.mvc.controllers.plugins;

import com.greenriver.commons.mvc.configuration.PageConfiguration;
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
 *
 * @author luis
 */
public class DojoBundlerPlugin extends BaseBundlerPlugin {

    private List<String> excludedNamespaces;

    public DojoBundlerPlugin () {
         this.excludedNamespaces = new ArrayList<String>();
         this.setBundlePrefix("dojo");
    }

    @Override
    protected List<String> getFileNames(PageConfiguration configuration) {
        return configuration.getDojoModules();
    }

   

    @Override
    protected void addBundle(String bundleName,PageConfiguration configuration) {
        // When using the auto bundler, we discard other bundles.
        configuration.getDojoBundles().clear();
        configuration.addDojoBundle(bundleName);
    }

    @Override
    protected void bundleFiles(PageConfiguration configuration, File bundleFile) {
        addDojoModules(configuration.getDojoModules(), bundleFile);
    }

     public void addDojoModules(List<String> newModules, File bundleFile) {
        List<String> loadedModules = new ArrayList<String>();

        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(
                    bundleFile, false));

            for (String newModule : newModules) {
                if (loadedModules.contains(newModule) || isModuleExcluded(
                        newModule)) {
                    continue;
                }

                processModuleFile(loadedModules, newModule, fileWriter);
            }

            fileWriter.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
        return this.getJavascriptBasePath()+"/" + moduleName.replace('.', '/') + ".js";
    }

    public void processModuleFile(
            List<String> loadedModules,
            String moduleName,
            BufferedWriter fileWriter) throws IOException {

        if (loadedModules.contains(moduleName)) {
            return;
        }

        loadedModules.add(moduleName);


        File moduleFile = new File(getPathFromModuleName(moduleName));
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
            String regex =
                    "dojo\\.require\\([\"']((\\w+\\.)+(\\w)+)[\"']\\)(;)?";
            String declarationFound = scanner.findInLine(regex);

            if (declarationFound != null) {
                MatchResult matchResult = scanner.match();
                for (int groupIndex = 1; groupIndex < matchResult.groupCount(); groupIndex++) {
                    String requiredModule = matchResult.group(groupIndex);

                    if (!requiredModule.matches("^(\\w+\\.)+(\\w)+$")) {
                        continue;
                    }

                    // We have found a declare statement, so we remove process it
                    // before we write anything to the file.
                    if (!isModuleExcluded(requiredModule)) {
                        processModuleFile(loadedModules, requiredModule,
                                fileWriter);
                        nextLine = nextLine.replaceAll(regex, "");
                    }
                }
            }

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

    
    // </editor-fold>
}
