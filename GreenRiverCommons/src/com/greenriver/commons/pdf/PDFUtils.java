package com.greenriver.commons.pdf;


import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.util.Streams;

/**
 * This class contains utilities for handling PDF files.
 * @author luisro
 */
public class PDFUtils {

    public static PDFFile getPdfFile(InputStream pdfFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Streams.copy(pdfFile, out, true);
        ByteBuffer buf = ByteBuffer.wrap(out.toByteArray());

        PDFFile file = new PDFFile(buf);

        return file;
    }

    public static void pdfPageToImage(
            PDFPage page, String imageFormat, OutputStream outputStream) throws IOException {

        BufferedImage img = (BufferedImage) page.getImage(
                (int) page.getWidth(),
                (int) page.getHeight(),
                page.getPageBox(), null, true, true);

        ImageIO.write(img, "png", outputStream);
    }
}
