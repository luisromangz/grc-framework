/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.templating.PrintableDocument;
import java.io.Serializable;

/**
 *
 * @author luisro
 */
public interface PrintingSessionHelper extends Serializable {

    void clearDocument();

    /**
     * @return the document
     */
    PrintableDocument getDocument();

    /**
     * @param document the document to set
     */
    void setDocument(PrintableDocument document);

}
