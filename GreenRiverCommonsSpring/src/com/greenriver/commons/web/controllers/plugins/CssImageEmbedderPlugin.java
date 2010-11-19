package com.greenriver.commons.web.controllers.plugins;

import com.greenriver.commons.Base64;
import com.greenriver.commons.web.configuration.PageConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luis
 */
public class CssImageEmbedderPlugin implements ControllerPlugin {

    private boolean createAlways;
    private String ebeddedSuffix = "-embedded";
    private String pathPrefix = "css/";
    private String basePath = null;
    private static final String URL_REGEX = "url\\(([\"'](.*)[\"'])\\)";
    private Pattern urlPattern;

    public CssImageEmbedderPlugin() {
        urlPattern = Pattern.compile(URL_REGEX);
    }

    @Override
    public void doWork(HttpServletRequest request, PageConfig configuration) {
        if (basePath == null) {
            basePath = String.format(
                    "%s/%s",
                    request.getSession().getServletContext().getRealPath(""), getPathPrefix());
        }



        List<String> cssFileNames = configuration.getCssFiles();
        List<String> embeddedCssFileNames = new ArrayList<String>();

        for (String cssFileName : cssFileNames) {

            String embeddedFileName = cssFileName + this.ebeddedSuffix;
            embeddedCssFileNames.add(embeddedFileName);

            processCssFile(cssFileName, embeddedFileName);

        }

        configuration.setCssFiles(embeddedCssFileNames);

    }

    private void processCssFile(String cssFileName, String embeddedFileName) {
        String embeddedPath = String.format("%s/%s.css",
                basePath,
                embeddedFileName);

        File embeddedFile = new File(embeddedPath);
        if (embeddedFile.exists() && !createAlways) {
            // We do nothing
            return;
        }

        // We create the new file.

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(embeddedFile));
            processCssFile(cssFileName, writer);
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

    private void processCssFile(String cssFileName, BufferedWriter fileWriter) throws IOException {
        File cssInputFile = new File(basePath + "/" + cssFileName + ".css");
        Scanner scanner = null;

        try {
            scanner = new Scanner(cssInputFile);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        // We start reading the module file
        while (scanner.hasNextLine()) {

            String nextLine = scanner.nextLine();

            Matcher matchResult = urlPattern.matcher(nextLine);

            if (matchResult.find()) {
                for (int groupIndex = 1; groupIndex <= matchResult.groupCount(); groupIndex += 3) {

                    String urlInside = matchResult.group(groupIndex);
                    String imageUrl = matchResult.group(groupIndex + 1);// Its the second group

                    // We have an url, so we load the file. If paths are relative,
                    // they will be relative to the path of the css file.

                    String imagePath = cssInputFile.getParent() + "/" + imageUrl;
                    File urlFile = new File(imagePath);
                    if (!urlFile.exists()) {
                        throw new RuntimeException(String.format(""
                                + "Image file '%s' used in '%s.css' doesn't exists.",
                                imagePath,
                                cssFileName));
                    }

                    String mimeTipe = "image/png";

                    // TODO: Maybe we could introduce IE hacks to support IE.
                    String base64FileContents = Base64.encodeFromFile(imagePath);

                    nextLine = nextLine.replace(urlInside, String.format(
                            "data:%s;base64,%s",
                            mimeTipe,
                            base64FileContents));
                }
            }


            fileWriter.write(nextLine);
            fileWriter.newLine();

        }
        scanner.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the createAlways
     */
    public boolean getCreateAlways() {
        return createAlways;
    }

    /**
     * @param createAlways the createAlways to set
     */
    public void setCreateAlways(boolean createAlways) {
        this.createAlways = createAlways;
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

    /**
     * @return the ebeddedSuffix
     */
    public String getEbeddedSuffix() {
        return ebeddedSuffix;
    }

    /**
     * @param ebeddedSuffix the ebeddedSuffix to set
     */
    public void setEbeddedSuffix(String ebeddedSuffix) {
        this.ebeddedSuffix = ebeddedSuffix;
    }
    // </editor-fold>
}
