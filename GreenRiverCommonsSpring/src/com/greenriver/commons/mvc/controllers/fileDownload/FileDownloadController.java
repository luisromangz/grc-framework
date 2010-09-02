package com.greenriver.commons.mvc.controllers.fileDownload;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * This controller access the available PrintableDocument for the user,
 * converts it to PDF and serve it.
 *
 * @author luisro
 */
public class FileDownloadController extends AbstractController {

    private FileDownloadSessionHelper fileDownloadSessionHelper;

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (fileDownloadSessionHelper.getFileContent() == null) {
            // Nothing to print.
            Logger.getRootLogger().error("No download content available!");
            return new ModelAndView("fileDownloadError");
        }

        response.reset();
        response.setContentType(fileDownloadSessionHelper.getFileMIMEType());
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"",fileDownloadSessionHelper.getFilename()));

        response.getOutputStream().write(fileDownloadSessionHelper.getFileContent());
        response.getOutputStream().flush();
        response.getOutputStream().close();


       // fileDownloadSessionHelper.clearFileInfo();

        return null;
    }

    /**
     * @return the fileDownloadSessionHelper
     */
    public FileDownloadSessionHelper getFileDownloadSessionHelper() {
        return fileDownloadSessionHelper;
    }

    /**
     * @param fileDownloadSessionHelper the fileDownloadSessionHelper to set
     */
    public void setFileDownloadSessionHelper(FileDownloadSessionHelper fileDownloadSessionHelper) {
        this.fileDownloadSessionHelper = fileDownloadSessionHelper;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
   
    // </editor-fold>
}
