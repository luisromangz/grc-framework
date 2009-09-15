/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date handling utilities.
 */
public class Dates {

    public static final int MAX_NANOS = 999999999;

    /**
     * Applies an increment in the nanoseconds of a timestamp. The increment 
     * is limited to up to (+/-)999999999 nanos.
     * @param stamp
     * @param nanos Number of nanoseconds to deviate the stamp.
     * @return the timestamp with the amount of nanos added.
     */
    public static final Timestamp addNanos(
            Timestamp stamp, int nanos) {
        if (nanos > MAX_NANOS || nanos < -MAX_NANOS) {
            throw new IllegalArgumentException(
                    "Deviation is limited to up to (+/-)999999999 " +
                    "nanoseconds.");
        }
        
        GregorianCalendar cal = null;
        Timestamp result = null;
        nanos += stamp.getNanos();
        
        cal = (GregorianCalendar) GregorianCalendar.getInstance();
        cal.setTime(stamp);

        if (nanos > MAX_NANOS) {
            //Adding up nanos we can add up to one second that at the end will
            //make the hour change if the current time is 23:59:59
            nanos -= 1000000000;
            cal.add(GregorianCalendar.SECOND, 1);
        } else if (nanos < 0) {
            //Substracting nanos we can remove up to almost a second but we
            //can end up with a negative number of nanos so we must substract
            //a second.

            nanos += 1000000000;
            cal.add(GregorianCalendar.SECOND, -1);
        }

        result = new Timestamp(cal.getTimeInMillis());
        result.setNanos(nanos);

        return result;
    }

    /**
     * Converts an amount of milliseconds and an amount of nanoseconds to a
     * timestamp. Once the millis are converted into a date the milliseconds
     * withing the second are replaced by the nanoseconds within the second.
     * @param millis Date as milliseconds.
     * @param nanos Nanoseconds within the second.
     * @return a timestamp
     * @throws IllegalArgumentException if the amount of nanos
     */
    public static Timestamp toTimestamp(long millis, int nanos) {
        if (nanos > MAX_NANOS || nanos < 0) {
            throw new IllegalArgumentException(
                    "Nanos must be within the second and the range is " +
                    "0-999999999 nanoseconds.");
        }

        Timestamp result = new Timestamp(millis);
        result.setNanos(nanos);

        return result;
    }

    /**
     * Creates a new timestamp from a date and a nanosecond value.
     * @param d
     * @param nanos
     * @return
     */
    public static Timestamp toTimestamp(Date d, int nanos) {
        return toTimestamp(d.getTime(), nanos);
    }

    /**
     * Converts a string that represents a date into a date.
     * <br/>
     * The string format is limited to:
     * <ul>
     *   <li> yyyy/MM/dd</li>
     *   <li> yyyy/MM/dd-hh:mm</li>
     *   <li> yyyy/MM/dd-hh:mm:ss</li>
     *   <li> yyyy/MM/dd-hh:mm:ss.nanos</li>
     * </ul>
     * In all the cases the date returned will be a subtipe of date,
     * java.util.sql.Timestamp, that supports nanosecond precision.
     * @param date
     * @return
     */
    public static Date parseString(String date) {
        Date result = null;
        String[] parts = null;
        int pos = date.indexOf("-");
        int hour = 0, minutes = 0, seconds = 0, nanos = 0;
        int day = 0, month = 0, year = 0;

        if (pos > 0) {
            String time = date.substring(pos + 1);
            date = date.substring(0, pos);
            parts = time.split(":");

            switch (parts.length) {
                case 2:
                    hour = Integer.parseInt(parts[0]);
                    minutes = Integer.parseInt(parts[1]);
                    break;
                case 3:
                    hour = Integer.parseInt(parts[0]);
                    minutes = Integer.parseInt(parts[1]);
                    pos = parts[2].indexOf(".");
                    if (pos > 0) {
                        seconds = Integer.parseInt(parts[2].substring(0, pos));
                        nanos = Integer.parseInt(parts[2].substring(pos + 1));
                    } else {
                        seconds = Integer.parseInt(parts[2]);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid format");
            }
        }

        parts = date.split("/");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid format");
        }

        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2]);

        //month is 0-based
        GregorianCalendar cal = new GregorianCalendar(
                year, month - 1, day, hour, minutes, seconds);

        result = toTimestamp(cal.getTimeInMillis(), nanos);

        return result;
    }

    /**
     * Gets if two date ranges intersects.
     * @param startA
     * @param endA
     * @param startB
     * @param endB
     * @return
     */
    public static boolean intersects(Date startA, Date endA, Date startB,
            Date endB) {
        boolean result = false;

        if (startA == null || startB == null || endA == null || endB == null) {
            throw new NullPointerException("Null is an invalid parameter");
        }
        if (!startA.before(startB) || !startB.before(endB)) {
            throw new IllegalArgumentException("Invalid date ranges");
        }

        if (startA.before(startB)) {
            result = !endA.before(startB);
        } else if (startB.before(startA)) {
            result = !endB.before(startA);
        } else {
            return true;
        }

        return result;
    }

    /**
     * Gets if a date is in a range, including the start and end dates.
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static boolean inRange(Date date, Date start, Date end) {
	if (date == null || start == null || end == null) {
	    throw new IllegalArgumentException("Null date not allowed");
	}
	
	return (date.before(end) && date.after(start)) ||
		date.equals(start) || date.equals(end);
    }

    public static boolean inRange(Date startA, Date endA, Date startB, Date endB) {
	return inRange(startA, startB, endB) &&
		inRange(endA, startB, endB);
    }

    /**
     * Formats the date
     * @param date
     * @return
     */
    public static String formatAsMysqlDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
