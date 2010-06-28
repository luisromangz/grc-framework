package com.greenriver.commons.pdf;

import be.re.css.CSSToFOPNew;
import be.re.css.CSSToXSLFOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author luisro
 */
public class HTMLToPDFConverter {

    public void convertToPDF(InputStream htmlStream, OutputStream pdfOutput) throws IOException {        

        convertXMLToFop(
                htmlStream,
                pdfOutput);
        
    }

    void convertXMLToFop(InputStream xml, OutputStream fopOutput) throws IOException {
        try {
            CSSToFOPNew.convert(xml, fopOutput,"application/pdf");
        } catch (CSSToXSLFOException ex) {
             throw new RuntimeException(ex);
        }finally {
            fopOutput.close();
        }
    }

  
}
