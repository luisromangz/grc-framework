/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author luisro
 */
public class HTMLToPDFConverterTest {

    public HTMLToPDFConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    public void testConvertXMLToFop() throws Exception {
        System.out.println("./convertXMLToFop");


        InputStream input = this.getClass().getResourceAsStream("test-xml-to-fop.xml");

        File fopFile = new File("/tmp/fop.pdf");

        OutputStream pdfOutput = new FileOutputStream(fopFile);
        HTMLToPDFConverter instance = new HTMLToPDFConverter();
        instance.convertXMLToFop(input, pdfOutput);
    }

    @Test
    public void testConvertHTMLToPDF() throws Exception {
        System.out.println("./convertHTMLToPDF");
         InputStream input = this.getClass().getResourceAsStream("test.html");


        HTMLToPDFConverter instance = new HTMLToPDFConverter();
        File outputFile = new File("/tmp/html.pdf");
        outputFile.createNewFile();
        
        instance.convertToPDF(input, new FileOutputStream(outputFile));

    }
}
