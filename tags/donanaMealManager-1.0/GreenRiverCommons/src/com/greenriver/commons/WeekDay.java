package com.greenriver.commons;

import java.text.DateFormatSymbols;

/**
 * This enum contains the days of the week constants, and provide methods to
 * retrieve the localized labels for them.
 * The order of this enumerations is important so don't change it.
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
        return Strings.toUpperCase(
                DateFormatSymbols.getInstance().getWeekdays()[this.ordinal() + 1],
                0, 1);
    }

    /**
     * Gets a shortened localized label for the day of the week.
     * @return
     */
    public String getShortLabel() {
        return Strings.toUpperCase(
                DateFormatSymbols.getInstance().getShortWeekdays()[this.ordinal() + 1],
                0,1);
    }
}
