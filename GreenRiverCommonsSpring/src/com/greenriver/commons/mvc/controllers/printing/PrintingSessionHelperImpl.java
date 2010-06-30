package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.Strings;
import com.greenriver.commons.pdf.HTMLToPDFConverter;
import com.greenriver.commons.services.ServiceResult;
import com.greenriver.commons.templating.PageConfiguration;
import com.greenriver.commons.templating.PrintableDocument;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.w3c.tidy.Tidy;

/**
 *
 * @author luisro
 */
public class PrintingSessionHelperImpl implements PrintingSessionHelper {

    private List<PrintableDocument> documents;
    private byte[] pdfContent;
    private HTMLToPDFConverter converter;
    private String template;
    private String footerAndHeaderTemplate;
    private final String PAGE_COUNTER = "<span class=\"pageCounter\"/>";
    private final String PAGE_TOTAL = "<span class=\"totalPages\"/>";
    private final String PAGE_SEPARATOR = "<div style=\"page-break-after:always\"><!--Non empty--></div>";

    // <editor-fold defaultstate="collapsed" desc="PDF-creation methods">
    private boolean convertDocuments(ServiceResult sr) {
        Logger logger = Logger.getRootLogger();
        try {           
            template = Strings.fromInputStream(
                    this.getClass().getResourceAsStream("printingTemplate.xhtml"));
            footerAndHeaderTemplate = Strings.fromInputStream(
                    this.getClass().getResourceAsStream("footerAndHeaderTemplate.xml"));

        } catch (IOException ex) {
            sr.setSuccess(false);
            sr.addErrorMessage("No se pudo acceder a las plantillas de impresión.");
            logger.error(ex,ex.getCause());
            return false;
        }

        String html = fillTemplate(documents);
        try {
             pdfContent = null;
            ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
            converter.convertToPDF(new ByteArrayInputStream(html.getBytes("UTF-8")), pdfOutput);
            this.pdfContent = pdfOutput.toByteArray();
        } catch (Exception e) {
            sr.setSuccess(false);
            sr.addErrorMessage("Error al generar el archivo PDF. Puede que la estructura de la plantilla sea errónea.");
            
            logger.error(html, e.getCause());
            return false;
        }
        return true;

    }

    private String fillTemplate(List<PrintableDocument> documents) {
        // All documents share template so share all info.
        PrintableDocument firstDocument = documents.get(0);
        String html = template.replace("%TITLE%", firstDocument.getTitle());
        html = html.replace("%DOCUMENT_STYLES%", firstDocument.getCssStyles());

        PageConfiguration pageConfig = firstDocument.getPageConfiguration();
        if (pageConfig.isLandscape()) {
            html = html.replace("%PAGE_HEIGHT%", String.valueOf(pageConfig.getPageWidth()));
            html = html.replace("%PAGE_WIDTH%", String.valueOf(pageConfig.getPageHeight()));
        } else {
            html = html.replace("%PAGE_WIDTH%", String.valueOf(pageConfig.getPageWidth()));
            html = html.replace("%PAGE_HEIGHT%", String.valueOf(pageConfig.getPageHeight()));
        }

        html = html.replace("%MARGIN_TOP%", String.valueOf(pageConfig.getTopMargin()));
        html = html.replace("%MARGIN_BOTTOM%", String.valueOf(pageConfig.getBottomMargin()));
        html = html.replace("%MARGIN_LEFT%", String.valueOf(pageConfig.getLeftMargin()));
        html = html.replace("%MARGIN_RIGHT%", String.valueOf(pageConfig.getRightMargin()));

        if (pageConfig.hasFooter()) {
            String footer = footerAndHeaderTemplate.replace("%REGION_CLASS%", "footer");
            footer = footer.replace("%REGION_LEFT%", replaceFooter(pageConfig.getFooterLeft()));
            footer = footer.replace("%REGION_CENTER%", replaceFooter(pageConfig.getFooterCenter()));
            footer = footer.replace("%REGION_RIGHT%", replaceFooter(pageConfig.getFooterRight()));

            html = html.replace("%FOOTER%", footer);
        } else {
            html = html.replace("%FOOTER%", "");
        }

        Tidy tidier = new Tidy();
        tidier.setXHTML(true);
        tidier.setPrintBodyOnly(true);
        tidier.setAsciiChars(false);
        tidier.setNumEntities(true);

        List<String> tidiedBodies = new ArrayList<String>();
        // We process each document's body.
        for (PrintableDocument document : documents) {
            StringWriter tidiedWriter = new StringWriter();
            tidier.parse(new StringReader(document.getBody()), tidiedWriter);
            tidiedBodies.add(tidiedWriter.toString());
        }

        html = html.replace("%DOCUMENT_BODY%", Strings.join(tidiedBodies, this.PAGE_SEPARATOR));

        return html;
    }

    public String replaceFooter(String footer) {
        return footer.replace("%TOTAL_PAGINAS%", this.PAGE_TOTAL).replace("%PAGINA%", this.PAGE_COUNTER);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
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
    public boolean setDocuments(List<PrintableDocument> documents, ServiceResult sr) {
        this.documents = documents;
        return this.convertDocuments(sr);
    }

    @Override
    public void clearDocuments() {
        documents = new ArrayList<PrintableDocument>();
        pdfContent = null;
    }

    @Override
    public boolean setDocument(PrintableDocument document, ServiceResult sr) {
        ArrayList<PrintableDocument> list = new ArrayList<PrintableDocument>();
        list.add(document);
        return this.setDocuments(list, sr);

    }

    @Override
    public byte[] getPDFContent() {
        return pdfContent;
    }

    /**
     * @return the converter
     */
    public HTMLToPDFConverter getConverter() {
        return converter;
    }

    /**
     * @param converter the converter to set
     */
    public void setConverter(HTMLToPDFConverter converter) {
        this.converter = converter;
    }
    // </editor-fold>
}
