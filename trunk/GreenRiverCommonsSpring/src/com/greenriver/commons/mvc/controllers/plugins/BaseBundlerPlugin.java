package com.greenriver.commons.mvc.controllers.plugins;

// <editor-fold defaultstate="collapsed" desc="Imports">
import com.greenriver.commons.Strings;
import com.greenriver.commons.mvc.configuration.PageConfiguration;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.springframework.security.authentication.encoding.PasswordEncoder;
// </editor-fold>

/**
 * This is the base class for all JavaScript bundler pluggins.
 * Concrete inplementations just have to define some abstract methods, but most
 * of the boilerplate is handled here:
 * 
 * <ul>
 * <li>Creating the bundle's file</li>
 * <li>Compressing the file</li>
 * <li>Checking if the bundle needs to be regenerated</li>
 * </ul>
 *
 * @author luis
 */
public abstract class BaseBundlerPlugin implements ControllerPlugin {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private String javascriptBasePath;
    private boolean alwaysCreate;
    private PasswordEncoder passwordEncoder;
    private boolean applyCompression;
    private String pathPrefix = "js";
    private String bundlePrefix;
    // </editor-fold>

    public BaseBundlerPlugin() {
    }

    @Override
    public void doWork(HttpServletRequest request, PageConfiguration configuration) {

        if (javascriptBasePath == null) {
            javascriptBasePath = String.format(
                    "%s/%s",
                    request.getSession().getServletContext().getRealPath(""), getPathPrefix());
        }

        List<String> fileNames = getFileNames(configuration);

        String bundleName = Strings.join(fileNames, ",");

        // We encode the bundle's name, so if modules change, the name
        // of the bundled file will change too, and thus will force a new
        // bundle generation.
        bundleName = passwordEncoder.encodePassword(bundleName, null);
        bundleName = this.getBundlePrefix() + "-" + bundleName;

        String bundlePath = String.format(
                "%s/%s.js",
                javascriptBasePath,
                bundleName);

       

        File bundleFile = new File(bundlePath);
        if (bundleFile.exists() && !alwaysCreate) {
             addBundle(bundleName, configuration);
            return;
        }

        File outFile = bundleFile;
        if(applyCompression) {
            // If we compress, the uncompressed bundle will be created to a temp file.
            try {
                outFile = File.createTempFile("bundle", ".tmp");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        bundleFiles(configuration, outFile);
         addBundle(bundleName, configuration);

        if (applyCompression) {
            FileReader reader;
            try {
                reader = new FileReader(outFile);
                JavaScriptCompressor compressor = new JavaScriptCompressor(
                        reader,
                        new ToolErrorReporter(false));
                FileWriter compressedWriter = new FileWriter(bundleFile);
                compressor.compress(compressedWriter, 1000, false, false, false,false);

                compressedWriter.flush();
                compressedWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (EvaluatorException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Abstract methods">
    protected abstract List<String> getFileNames(PageConfiguration configuration);

    protected abstract void addBundle(String bundleName, PageConfiguration configuration);

    protected abstract void bundleFiles(PageConfiguration configuration, File bundleFile);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the alwaysCreate
     */
    public boolean getAlwaysCreate() {
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
     * @return the getApplyCompression
     */
    public boolean getApplyCompression() {
        return applyCompression;
    }

    /**
     * @param applyCompression the getApplyCompression to set
     */
    public void setApplyCompression(boolean applyCompression) {
        this.applyCompression = applyCompression;
    }

    /**
     * @return the pathPrefix
     */
    public String getPathPrefix() {
        return pathPrefix;
    }

    /**
     * @param pathPrefix the pathPrefix to set
     */
    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    protected String getJavascriptBasePath() {
        return this.javascriptBasePath;
    }

    /**
     * @return the bundlePrefix
     */
    public String getBundlePrefix() {
        return bundlePrefix;
    }

    /**
     * @param bundlePrefix the bundlePrefix to set
     */
    public void setBundlePrefix(String bundlePrefix) {
        this.bundlePrefix = bundlePrefix;
    }
    // </editor-fold>
}
