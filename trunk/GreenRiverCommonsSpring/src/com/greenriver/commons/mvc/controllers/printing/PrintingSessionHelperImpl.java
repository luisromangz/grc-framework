package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.templating.PrintableDocument;

/**
 *
 * @author luisro
 */
public class PrintingSessionHelperImpl implements PrintingSessionHelper {
    private PrintableDocument document;

    /**
     * @return the document
     */
    @Override
    public PrintableDocument getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    @Override
    public void setDocument(PrintableDocument document) {
        this.document = document;
    }

    @Override
    public void clearDocument(){
        document = null;
    }

    
}
