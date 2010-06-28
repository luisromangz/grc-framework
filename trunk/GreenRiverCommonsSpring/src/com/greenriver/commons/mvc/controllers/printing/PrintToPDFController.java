package com.greenriver.commons.mvc.controllers.printing;

import com.greenriver.commons.Strings;

import com.greenriver.commons.pdf.HTMLToPDFConverter;
import com.greenriver.commons.templating.PageConfiguration;
import com.greenriver.commons.templating.PrintableDocument;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.w3c.tidy.Tidy;

/**
 * This controller access the available PrintableDocument for the user,
 * converts it to PDF and serve it.
 *
 * @author luisro
 */
public class PrintToPDFController extends AbstractController {

    private HTMLToPDFConverter converter;
    private PrintingSessionHelper printingSessionHelper;
    private String template;
    private String footerAndHeaderTemplate;
    private final String PAGE_COUNTER = "<span class=\"pageCounter\"/>";
    private final String PAGE_TOTAL = "<span class=\"totalPages\"/>";
    private final String PAGE_SEPARATOR="<div style=\"page-break-after:always\"></div>";

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<PrintableDocument> documents = printingSessionHelper.getDocuments();


        if (documents.isEmpty()) {
            // Nothing to print.
            return new ModelAndView("printError");
        }

        template = Strings.fromInputStream(
                this.getClass().getResourceAsStream("printingTemplate.xhtml"));

        footerAndHeaderTemplate = Strings.fromInputStream(
                this.getClass().getResourceAsStream("footerAndHeaderTemplate.xml"));

        String html = fillTemplate(documents);


        ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
        try {
            converter.convertToPDF(
                    new ByteArrayInputStream(html.getBytes()),
                    pdfOutput);
        } catch (RuntimeException e) {
            return new ModelAndView("printError");
        }

        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"",
                Strings.isNullOrEmpty(documents.get(0).getTitle()) ? "print.pdf"
                : Strings.asciify(documents.get(0).getTitle()) + ".pdf"));

        response.getOutputStream().write(pdfOutput.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();


        printingSessionHelper.clearDocuments();

        return null;
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
        html = html.replace("%MARGIN_RIGTH%", String.valueOf(pageConfig.getRightMargin()));

        html = html.replace("%MARGIN_RIGTH%", String.valueOf(pageConfig.getRightMargin()));

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

        html = html.replace("%DOCUMENT_BODY%", Strings.join(tidiedBodies,this.PAGE_SEPARATOR));

        return html;
    }

    public String replaceFooter(String footer) {
        return footer.replace("%TOTAL_PAGINAS%", this.PAGE_TOTAL).replace("%PAGINA%", this.PAGE_COUNTER);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
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

    /**
     * @return the printingSessionHelper
     */
    public PrintingSessionHelper getPrintingSessionHelper() {
        return printingSessionHelper;
    }

    /**
     * @param printingSessionHelper the printingSessionHelper to set
     */
    public void setPrintingSessionHelper(PrintingSessionHelper printingSessionHelper) {
        this.printingSessionHelper = printingSessionHelper;
    }
    // </editor-fold>
}
