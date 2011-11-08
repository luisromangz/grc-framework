package com.greenriver.commons.pdf;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * This class contains utilities for handling PDF files.
 * @author luisro
 */
public class PDFUtils {

    public static void pdfToImages(String filePath, String resultPath) throws Exception{
         ConvertCmd cmd = new ConvertCmd();
         
         IMOperation op = new IMOperation();
         
         op.density(120);
         op.addImage(filePath);
         op.addImage(resultPath);
         
         cmd.run(op);
    }
}
