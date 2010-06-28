/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.services.ServiceResult;
import com.greenriver.commons.templating.PrintableDocument;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author luisro
 */
public interface PrintingSessionHelper extends Serializable {

    void clearDocuments();

    /**
     * @return the document
     */
    List<PrintableDocument> getDocuments();

    /**
     * @param document the document to set
     */
    boolean setDocument(PrintableDocument document, ServiceResult sr);

    boolean setDocuments(List<PrintableDocument> documents, ServiceResult sr);

    byte[] getPDFContent();
   
}
