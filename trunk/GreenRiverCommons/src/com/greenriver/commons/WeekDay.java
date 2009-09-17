package com.greenriver.commons;

import java.text.DateFormatSymbols;

/**
 * This enum contains the days of the week constants, and provide methods to
 * retrieve the localized labels for them.
 *
 * @author luis
 */
public enum WeekDay {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;
    

    /**
     * Gets the localized label for the day of the week.
     * @return
     */
    public String getLabel() {
        return DateFormatSymbols.getInstance().getWeekdays()[this.ordinal() +1];
    }

    /**
     * Gets a shortened localized label for the day of the week.
     * @return
     */
    public String getShortLabel() {
        return DateFormatSymbols.getInstance().getShortWeekdays()[this.ordinal() +1];
    }
}
