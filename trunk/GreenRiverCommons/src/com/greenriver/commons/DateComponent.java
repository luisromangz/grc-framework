package com.greenriver.commons;

import java.util.GregorianCalendar;

/**
 *
 * @author Miguel Angel
 */
public enum DateComponent {

    DAY,
    MONTH,
    YEAR,
    HOUR,
    MINUTE,
    SECOND,
    MILLISECOND,
    NANOSECOND,
    NONE;

    public int getCalendarConstant() {
        int calFieldSpec;
        switch (this) {
            case DAY:
                calFieldSpec = GregorianCalendar.DAY_OF_MONTH;
                break;
            case MONTH:
                calFieldSpec = GregorianCalendar.MONTH;
                break;
            case YEAR:
                calFieldSpec = GregorianCalendar.YEAR;
                break;
            case HOUR:
                calFieldSpec = GregorianCalendar.HOUR_OF_DAY;
                break;
            case MINUTE:
                calFieldSpec = GregorianCalendar.MINUTE;
                break;
            case SECOND:
                calFieldSpec = GregorianCalendar.SECOND;
                break;
            case MILLISECOND:
                calFieldSpec = GregorianCalendar.MILLISECOND;
                break;
            default:
                // For NANOSECOND there is no constant in the calendar mess
                throw new IllegalArgumentException();
        }

        return calFieldSpec;
    }
}
