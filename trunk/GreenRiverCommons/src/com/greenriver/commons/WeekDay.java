package com.greenriver.commons;

import java.text.DateFormatSymbols;

/**
 * This enum contains the days of the week constants, and provide methods to
 * retrieve the localized labels for them.
 *
 * @author luis
 */
public enum WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    /**
     * Gets the localized label for the day of the week.
     * @return
     */
    public String getLabel() {
        return DateFormatSymbols.getInstance().getWeekdays()[this.ordinal()];
    }

    /**
     * Gets a shortened localized label for the day of the week.
     * @return
     */
    public String getShortLabel() {
        return DateFormatSymbols.getInstance().getShortWeekdays()[this.ordinal()];
    }
}
