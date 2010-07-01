package com.greenriver.commons.pdf;

import com.greenriver.commons.Strings;
import com.greenriver.commons.templating.PageConfiguration;
import com.greenriver.commons.templating.PrintableDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.w3c.tidy.Tidy;

/**
 *
 * @author luisro
 */
public class PrintableDocumentToPDFConverter {
    private HTMLToPDFConverter htmlToPDFConverter;

    private String template;
    private String footerAndHeaderTemplate;
    private final String PAGE_COUNTER = "<span class=\"pageCounter\"/>";
    private final String PAGE_TOTAL = "<span class=\"totalPages\"/>";
    private final String PAGE_SEPARATOR = "<div style=\"page-break-after:always\"><!--Non empty--></div>";

    public void convert(PrintableDocument document, OutputStream pdfOutput) {
        ArrayList<PrintableDocument> list = new ArrayList<PrintableDocument>();
        list.add(document);
        this.convert(list, pdfOutput);
    }

    public void convert(List<PrintableDocument> documents, OutputStream pdfOutput) {
        Logger logger = Logger.getLogger("grc-commons");
        try {
            template = Strings.fromInputStream(
                    this.getClass().getResourceAsStream("printingTemplate.xhtml"));
            footerAndHeaderTemplate = Strings.fromInputStream(
                    this.getClass().getResourceAsStream("footerAndHeaderTemplate.xml"));

        } catch (IOException ex) {           
            logger.error(ex,ex.getCause());
            throw new RuntimeException("A template wasn't found.", ex);
        }

        String html = fillTemplate(documents);
        try {           
            htmlToPDFConverter.convertToPDF(new ByteArrayInputStream(html.getBytes("UTF-8")), pdfOutput);           
        } catch (Exception e) {
            logger.error(html, e.getCause());
            throw new RuntimeException("There was a failure filling the template", e);
        }
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

    private String replaceFooter(String footer) {
        return footer.replace("%TOTAL_PAGINAS%", this.PAGE_TOTAL).replace("%PAGINA%", this.PAGE_COUNTER);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the htmlToPDFConverter
     */
    public HTMLToPDFConverter getHtmlToPDFConverter() {
        return htmlToPDFConverter;
    }

    /**
     * @param htmlToPDFConverter the htmlToPDFConverter to set
     */
    public void setHtmlToPDFConverter(HTMLToPDFConverter htmlToPDFConverter) {
        this.htmlToPDFConverter = htmlToPDFConverter;
    }
    // </editor-fold>
}
