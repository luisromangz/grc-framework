/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.log;

import com.greenriver.commons.Exceptions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Simple formater that does the same as SimpleFormatter (his parent) but
 * doesn't repeat message headers (date, source, level) if they haven't changed.
 * So that's why this is a simpler message formatter than the simple formatter.
 */
public class SimplifiedFormatter extends SimpleFormatter {

    private String lastClass = "";
    private Level lastLevel = Level.OFF;
    private String lastMethod = "";
    private boolean maxSimplify = false;
    private String lastSourceName = "";
    private static final String startStr = "<T%1$s@%2$7s>  %3$s";
//    private long nextTime = -1;
//    private long timeMarkTime = 5000;

    public SimplifiedFormatter() {
        super();
    }

    /**
     * Initiallizes a new simpler formatter
     * @param maxSimplify True to enable maximum simplify or false to disable it.
     */
    public SimplifiedFormatter(boolean maxSimplify) {
        super();
        this.maxSimplify = maxSimplify;
    }

    /**
     * Gets class name or null from record
     * @param record
     * @return
     */
    public String getClassName(LogRecord record) {
        if (record.getSourceClassName() == null) {
            return null;
        }

        int pos = record.getSourceClassName().lastIndexOf(".");

        String className = record.getSourceClassName().substring(pos + 1);
        className += ":" + record.getSourceMethodName();
        return className;
    }

    /**
     * Get class namespace or null from record
     * @param record
     * @return
     */
    public String getNamespace(LogRecord record) {
        if (record.getSourceClassName() == null) {
            return null;
        }

        int pos = record.getSourceClassName().lastIndexOf(".");

        return record.getSourceClassName().substring(0, pos);
    }

    /**
     * Formats an exception
     * @param t
     * @return
     */
    private String formatException(Throwable t, GregorianCalendar cal) {
        return Exceptions.formatException(t, cal.getTime());
    }

    @Override
    public synchronized String format(LogRecord record) {
        if (maxSimplify) {
            String sourceName = getClassName(record);
            String result = "";
            String date = "";
            GregorianCalendar cal =
                    (GregorianCalendar) GregorianCalendar.getInstance();


//            if (now.getTime() > nextTime) {
            Format formatter =
                    new SimpleDateFormat("(yyyy/MM/dd HH:mm:ss) -- ");
            date = formatter.format(cal.getTime());
//                nextTime = now.getTime() + timeMarkTime;
//            }

            if (sourceName != null && !sourceName.equals(lastSourceName)) {
                lastSourceName = sourceName;
                result += String.format(
                        startStr,
                        record.getThreadID(),
                        record.getLevel(),
                        "** " + date + record.getLoggerName() + ":" + sourceName +
                        " **\n");
            }

            result += String.format(
                    startStr,
                    record.getThreadID(),
                    record.getLevel(),
                    "   " + formatMessage(record) + "\n");

            if (record.getThrown() != null) {
                result += formatException(record.getThrown(), cal);
            }

            return result;
        } else {
            if (!lastClass.equals(record.getSourceClassName()) ||
                    lastLevel != record.getLevel() ||
                    !lastMethod.equals(record.getSourceMethodName())) {

                lastClass = record.getSourceClassName();
                lastLevel = record.getLevel();
                lastMethod = record.getSourceMethodName();

                if (lastClass == null) {
                    lastClass = "";
                }

                if (lastMethod == null) {
                    lastMethod = "";
                }

                return super.format(record);
            } else {
                return "    * " + formatMessage(record) + "\n";
            }
        }
    }
}
