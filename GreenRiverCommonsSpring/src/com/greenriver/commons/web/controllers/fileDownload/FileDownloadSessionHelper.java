package com.greenriver.commons.web.controllers.fileDownload;

import java.io.Serializable;

/**
 *
 * @author luisro
 */
public interface FileDownloadSessionHelper extends Serializable {

    void clearFileInfo();

    void setFileInfo(String mymeType, String filename, byte[] content);

    byte[] getFileContent();
    String getFileMIMEType();
    String getFilename();
   
}
