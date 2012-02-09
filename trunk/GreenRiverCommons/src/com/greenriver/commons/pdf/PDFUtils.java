package com.greenriver.commons.pdf;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * This class contains utilities for handling PDF files.
 * @author luisro
 */
public class PDFUtils {

    /**
     * Converts a PDF pages into images.
     * @param filePath the input pdf path.
     * @param resultPath the output images path.
     * @param appendImages If true, the multiple pdf images will be merged into just one big vertical image.
     */
    public static void pdfToImages(String filePath, String resultPath, boolean appendImages) throws Exception{
         ConvertCmd cmd = new ConvertCmd();
         
         IMOperation op = new IMOperation();
         
         op.density(200);
         op.addImage(filePath);
         if(appendImages) {
             op.append();
         }
         
         op.addImage(resultPath);
         
         cmd.run(op);
    }
}
