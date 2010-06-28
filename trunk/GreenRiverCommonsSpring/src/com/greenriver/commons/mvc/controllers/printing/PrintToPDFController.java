package com.greenriver.commons.mvc.controllers.printing;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * This controller access the available PrintableDocument for the user,
 * converts it to PDF and serve it.
 *
 * @author luisro
 */
public class PrintToPDFController extends AbstractController {

    private PrintingSessionHelper printingSessionHelper;
   
    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (printingSessionHelper.getPDFContent()==null) {
            // Nothing to print.
            return new ModelAndView("printError");
        }
      
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Content-Disposition","attachment; filename=\"print.pdf\"");

        response.getOutputStream().write(printingSessionHelper.getPDFContent());
        response.getOutputStream().flush();
        response.getOutputStream().close();


        printingSessionHelper.clearDocuments();

        return null;
    }

    

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    

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
