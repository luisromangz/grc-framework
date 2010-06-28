package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.templating.PrintableDocument;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luisro
 */
public class PrintingSessionHelperImpl implements PrintingSessionHelper {

    private List<PrintableDocument> documents;

    /**
     * @return the document
     */
    @Override
    public List<PrintableDocument> getDocuments() {
        return documents;
    }

    /**
     * @param document the document to set
     */
    @Override
    public void setDocuments(List<PrintableDocument> documents) {
        this.documents = documents;
    }

    @Override
    public void clearDocuments() {
        documents = new ArrayList<PrintableDocument>();
    }

    @Override
    public void addDocument(PrintableDocument document) {
        documents.add(document);
    }

    @Override
    public void setDocument(PrintableDocument document) {
        this.clearDocuments();
        this.addDocument(document);
    }
}
