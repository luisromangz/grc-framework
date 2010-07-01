

package com.greenriver.commons.mvc.controllers.fileDownload;

/**
 *
 * @author luisro
 */
public class FileDownloadSessionHelperImpl
    implements FileDownloadSessionHelper {

    private String fileMIMEType;
    private String filename;
    private byte[] fileContent;

    @Override
    public void clearFileInfo() {
        fileMIMEType=null;
        fileContent=null;
        filename = null;
    }

    @Override
    public void setFileInfo(String mimeType, String filename, byte[] content) {
        this.fileMIMEType=mimeType;
        this.filename = filename;
        this.fileContent = content;
    }

    @Override
    public byte[] getFileContent() {
        return fileContent;
    }

    @Override
    public String getFileMIMEType() {
        return fileMIMEType;
    }

    @Override
    public String getFilename() {
        return filename;
    }

}
