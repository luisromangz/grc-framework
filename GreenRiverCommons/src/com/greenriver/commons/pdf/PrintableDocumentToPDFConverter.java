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

    private static String template;
    private static String footerAndHeaderTemplate;
    private static final String PAGE_COUNTER = "<span class=\"pageCounter\"/>";
    private static final String PAGE_TOTAL = "<span class=\"totalPages\"/>";
    private static final String PAGE_SEPARATOR = "<div style=\"page-break-after:always\"><!--Non empty--></div>";

    public static void convert (PrintableDocument document, OutputStream pdfOutput) {
        ArrayList<PrintableDocument> list = new ArrayList<PrintableDocument>();
        list.add(document);
        convert(list, pdfOutput);
    }

    public static void convert(List<PrintableDocument> documents, OutputStream pdfOutput) {
        Logger logger = Logger.getLogger("grc-commons");
        try {
            template = Strings.fromInputStream(
                    PrintableDocumentToPDFConverter.class.getResourceAsStream("printingTemplate.xhtml"));
            footerAndHeaderTemplate = Strings.fromInputStream(
                    PrintableDocumentToPDFConverter.class.getResourceAsStream("footerAndHeaderTemplate.xml"));

        } catch (IOException ex) {
            logger.error(ex, ex.getCause());
            throw new RuntimeException("A template wasn't found.", ex);
        }

        String html = fillTemplate(documents);
        try {
            HTMLToPDFConverter.convertToPDF(new ByteArrayInputStream(html.getBytes("UTF-8")), pdfOutput);
        } catch (Exception e) {
            logger.error(html, e.getCause());
            throw new RuntimeException("There was a failure filling the template", e);
        }
    }

    private static String fillTemplate(List<PrintableDocument> documents) {
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

        String footer="";
        if (pageConfig.hasFooter()) {
            footer = footerAndHeaderTemplate.replace("%REGION_CLASS%", "footer");
            footer = footer.replace("%REGION_LEFT%", replaceFooter(pageConfig.getFooterLeft()));
            footer = footer.replace("%REGION_CENTER%", replaceFooter(pageConfig.getFooterCenter()));
            footer = footer.replace("%REGION_RIGHT%", replaceFooter(pageConfig.getFooterRight()));

            
        } 
        
        html = html.replace("%FOOTER%", footer);
        
        String header = "";
        if(pageConfig.hasHeader()) {
            header = footerAndHeaderTemplate.replace("%REGION_CLASS%", "header");
            header = header.replace("%REGION_LEFT%", replaceFooter(pageConfig.getHeaderLeft()));
            header = header.replace("%REGION_CENTER%", replaceFooter(pageConfig.getHeaderCenter()));
            header = header.replace("%REGION_RIGHT%", replaceFooter(pageConfig.getHeaderRight()));
        }
        
        html = html.replace("%HEADER", header);

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

        html = html.replace("%DOCUMENT_BODY%", Strings.join(tidiedBodies, PAGE_SEPARATOR));

        return html;
    }

    private static String replaceFooter(String footer) {
        return footer.replace("%TOTAL_PAGINAS%", PAGE_TOTAL).replace("%PAGINA%", PAGE_COUNTER);
    }
}
