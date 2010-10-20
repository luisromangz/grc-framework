package com.greenriver.commons;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * This class provides utility methods for use with exceptions and throwables
 * in general.
 * 
 * @author luisro
 */
public class Exceptions {

    public static String formatException(Throwable t, Date date) {
        try {
            StringWriter strWriter = new StringWriter();
            PrintWriter print = new PrintWriter(strWriter);
            t.printStackTrace(print);
            print.close();
            return t.getClass() + "(" + date + "): " +
                    t.getMessage() + "\n" + strWriter.toString();
        } catch (Exception ex) {
        }

        return "";
    }

    public static String formatException(Throwable t) {
        return Exceptions.formatException(t,new Date());
    }
}
