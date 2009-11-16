package com.greenriver.commons.mvc.controllers.plugins;

// <editor-fold defaultstate="collapsed" desc="Imports">
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.mvc.configuration.PageConfiguration;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import javax.servlet.http.HttpServletRequest;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.springframework.security.providers.encoding.PasswordEncoder;
// </editor-fold>

/**
 *
 * @author luis
 */
public class DojoBundlerPlugin implements ControllerPlugin {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private String javascriptBasePath;
    private boolean alwaysCreate;
    private PasswordEncoder passwordEncoder;
    private List<String> excludedNamespaces;
    private boolean applyCompression;
    // </editor-fold>

    public DojoBundlerPlugin() {
        this.excludedNamespaces = new ArrayList<String>();
    }

    public void doWork(HttpServletRequest request, PageConfiguration configuration) {
        if (javascriptBasePath == null) {
            javascriptBasePath = String.format(
                    "%s/js/",
                    request.getSession().getServletContext().getRealPath(""));
        }

        List<String> dojoModules = configuration.getDojoModules();

        String bundleName = Lists.join(dojoModules, ",");

        // We encode the bundle's name, so if modules change, the name
        // of the bundled file will change too, and thus will force a new
        // bundle generation.
        bundleName = passwordEncoder.encodePassword(bundleName, null);
        bundleName = "dojo-" + bundleName;

        String bundlePath = String.format(
                "%sdojo/%s.js",
                javascriptBasePath,
                bundleName);

        File bundleFile = new File(bundlePath);
        if (bundleFile.exists() && !alwaysCreate) {
            configuration.getDojoBundles().clear();
            //configuration.addDojoBundle(bundleName);
            configuration.addDojoBundle(
                    bundleName + (applyCompression ? ".js.compressed" : ""));
            return;
        }

        addDojoModules(dojoModules, bundleFile);


        // When using the auto bundler, we discard other bundles.
        configuration.getDojoBundles().clear();

        if (applyCompression) {
            FileReader reader;
            try {
                reader = new FileReader(bundlePath);
                JavaScriptCompressor compressor = new JavaScriptCompressor(
                        reader,
                        new ToolErrorReporter(false));
                FileWriter compressedWriter =
                        new FileWriter(bundlePath + ".compressed.js");
                compressor.compress(compressedWriter, 1000, false, false, false,
                        false);

                compressedWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (EvaluatorException ex) {
                throw new RuntimeException(ex);
            }
        }

        configuration.addDojoBundle(
                bundleName + (applyCompression ? ".js.compressed" : ""));
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


        for (String loadedModule : loadedModules) {
            if (newModules.contains(loadedModule)) {
                newModules.remove(loadedModule);
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
        return this.javascriptBasePath + moduleName.replace('.', '/') + ".js";
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
        Scanner scanner;
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

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the alwaysCreate
     */
    public boolean isAlwaysCreate() {
        return alwaysCreate;
    }

    /**
     * @param alwaysCreate the alwaysCreate to set
     */
    public void setAlwaysCreate(boolean alwaysCreate) {
        this.alwaysCreate = alwaysCreate;
    }

    /**
     * @return the passwordEncoder
     */
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * @param passwordEncoder the passwordEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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
     * @return the getApplyCompression
     */
    public boolean getApplyCompression() {
        return applyCompression;
    }

    /**
     * @param getApplyCompression the getApplyCompression to set
     */
    public void setApplyCompression(boolean applyCompression) {
        this.applyCompression = applyCompression;
    }
    // </editor-fold>
}
