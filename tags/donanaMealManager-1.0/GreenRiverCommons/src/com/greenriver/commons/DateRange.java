package com.greenriver.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * DATE range.
 * @author Miguel Angel
 */
public class DateRange implements Comparable<DateRange>, Cloneable, Serializable {

    private Date min;
    private Date max;

    public Date getMax() {
        return max;
    }

    /**
     * Sets the maximum value of the date range (included)
     * @param max
     */
    public void setMax(Date max) {
        if (this.min == null && max == null) {
            throw new IllegalArgumentException("Both dates of the range can't be null");
        }
        if (max != null) {
            this.max = new Date(max.getTime());
        } else {
            this.max = null;
        }
    }

    public Date getMin() {
        return min;
    }

    /**
     * Sets the minumum value of the date range (included)
     * @param min
     */
    public void setMin(Date min) {
        if (this.max == null && min == null) {
            throw new IllegalArgumentException("Both dates of the range can't be null");
        }
        if (min != null) {
            this.min = new Date(min.getTime());
        } else {
            this.min = null;
        }
    }

    public void set(Date min, Date max) {
        if (min == null && max == null) {
            throw new IllegalArgumentException("Both dates of the range can't be null");
        }

        if (min != null) {
            this.min = new Date(min.getTime());
        } else {
            this.min = null;
        }

        if (max != null) {
            this.max = new Date(max.getTime());
        } else {
            this.max = null;
        }

        if (this.min != null && this.max != null && this.max.before(this.min)) {
            Date tmp = this.min;
            this.min = this.max;
            this.max = tmp;
        }
    }

    public boolean isEmpty() {
        return min == null && max == null;
    }

    /**
     * Gets if the range is infinite (has no lower or upper limit).
     * @return
     */
    public boolean isInfinite() {
        return (min == null && max != null) || (min != null && max == null);
    }

    public DateRange() {
    }

    public DateRange(Date singleRangeDate) {
        this(singleRangeDate.getTime(), singleRangeDate.getTime());
    }

    public DateRange(Date min, Date max) {
        this(
                (Long) (min != null ? min.getTime() : null),
                (Long) (max != null ? max.getTime() : null));
    }

    public DateRange(Long min, Long max) {
        if (min == null && max == null) {
            throw new IllegalArgumentException("Both parameters can't be null");
        }

        if (min != null) {
            this.min = new Date(min);
        }

        if (max != null) {
            this.max = new Date(max);
        }

        if (this.min != null && this.max != null && this.min.after(this.max)) {
            Date tmp = this.min;
            this.min = this.max;
            this.max = tmp;
        }
    }

    public boolean intersects(DateRange range) {
        return intersects(range, DatePart.DATE_TIME);
    }

    public boolean intersects(DateRange range, DatePart part) {
        if (range == null || range.isEmpty() || this.isEmpty()) {
            return false;
        }

        return Dates.intersects(this.min, this.max, range.min, range.max, part);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return this.equals((DateRange) obj, DatePart.DATE_TIME);
    }

    public boolean equals(DateRange obj, DatePart part) {
        if (obj == null || !this.getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        DateRange dateRange = (DateRange) obj;

        return Dates.equals(this.min, dateRange.min, part)
                && Dates.equals(this.max, dateRange.max, part);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.min != null ? this.min.hashCode() : 0);
        hash = 53 * hash + (this.max != null ? this.max.hashCode() : 0);
        return hash;
    }

    private Date min(Date a, Date b, Date c) {
        if (a.before(b)) {
            if (a.before(c)) {
                return a;
            } else {
                return c;
            }
        } else if (b.before(a)) {
            if (b.before(c)) {
                return b;
            } else {
                return c;
            }
        } else if (c.before(a)) {
            if (c.before(b)) {
                return c;
            } else {
                return b;
            }
        }

        return a;
    }

    private Date max(Date a, Date b, Date c) {
        if (a.after(b)) {
            if (a.after(c)) {
                return a;
            } else {
                return c;
            }
        } else if (b.after(a)) {
            if (b.after(c)) {
                return b;
            } else {
                return c;
            }
        } else if (c.after(a)) {
            if (c.after(b)) {
                return c;
            } else {
                return b;
            }
        }

        return a;
    }

    /**
     * Merges into this range another range. The other range must be consecutive
     * or intersect with this one.
     * @param dateRange
     * @param part
     */
    public void merge(DateRange dateRange, DatePart part) {
        if (this.isEmpty() || dateRange.isEmpty()) {
            throw new IllegalArgumentException("Can't merge empty ranges");
        }

        //We must do a lot of cases before merging
        if (this.isInfinite() && dateRange.isInfinite()) {
            //Both ranges are partial (one of the sides is not set and thus infinite)
            //The result will be a concrete range.
            if (this.min == null && dateRange.min == null) {
                this.set(this.max, dateRange.max);
            } else if (this.max == null && dateRange.max == null) {
                this.set(this.min, dateRange.min);
            } else if (this.min == null && dateRange.max == null) {
                this.set(dateRange.min, this.max);
            } else {
                this.set(this.min, dateRange.max);
            }
        } else if (this.isInfinite() && !dateRange.isInfinite()) {
            //One range is partial and the other not. We need to get the min
            //and max of the three dates for the merge.
            if (this.min == null) {
                this.set(min(this.max, dateRange.min, dateRange.max),
                        max(this.max, dateRange.min, dateRange.max));
            } else {
                this.set(min(this.min, dateRange.min, dateRange.max),
                        max(this.min, dateRange.min, dateRange.max));
            }
        } else if (!this.isInfinite() && dateRange.isInfinite()) {
            //Same as above
            if (dateRange.min == null) {
                this.set(min(this.max, this.min, dateRange.max),
                        max(this.max, this.min, dateRange.max));
            } else {
                this.set(min(this.max, this.min, dateRange.min),
                        max(this.max, this.min, dateRange.min));
            }
        } else {
            //No range is partial. We update the min and max of the current
            //instance if the merged ones are better (lower or higher).
            if (this.min.after(dateRange.min)) {
                this.min = new Date(dateRange.min.getTime());
            }

            if (this.max.before(dateRange.max)) {
                this.max = new Date(dateRange.max.getTime());
            }
        }
    }

    /**
     * Checks if the maximum of this range is before the minimum of another
     * range. That means this range goes before the another one.
     * The comparison is done only with the date part.
     * @param dateRange
     * @return
     */
    public boolean before(DateRange dateRange) {
        return Dates.before(this.max, dateRange.min, DatePart.DATE);
    }

    /**
     * Checks if the maximum of this range is before the minimum of another
     * range. That means this range goes before the another one.
     * @param dateRange
     * @param part 
     * @return
     */
    public boolean before(DateRange dateRange, DatePart part) {
        return Dates.before(this.max, dateRange.min, part);
    }

    /**
     * Checks if the minimum of this range is after the maximum of another
     * range. That means this range goes after the another one.
     * The comparison is done only with the date part.
     * @param dateRange
     * @return
     */
    public boolean after(DateRange dateRange) {
        return Dates.after(this.min, dateRange.max, DatePart.DATE);
    }

    /**
     * Checks if the minimum of this range is after the maximum of another
     * range. That means this range goes after the another one.
     * @param dateRange
     * @param part Part to use in the comparison
     * @return
     */
    public boolean after(DateRange dateRange, DatePart part) {
        return Dates.after(this.min, dateRange.max, part);
    }

    /**
     * This comparison returns 0 if the ranges intersects. If not it will return
     * 1 or -1
     * @param o
     * @param part
     * @return
     */
    public int compareTo(DateRange o, DatePart part) {
        if (this.before(o, part)) {
            return -1;
        } else if (this.after(o, part)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * The comparison is done with both the time and date parts.
     * @param o
     * @return
     */
    @Override
    public int compareTo(DateRange o) {
        return this.compareTo(o, DatePart.DATE_TIME);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new DateRange(this.min, this.max);
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "(EMPTY)";
        } else {
            return "(" + (min == null ? "Infinite" : Dates.formatAsMysqlDate(min)) + ", "
                    + (max == null ? "Infinite" : Dates.formatAsMysqlDate(max)) + ")";
        }
    }

    /**
     * The comparison to check the existance is done with only the date part.
     * @param date
     * @return
     */
    public boolean contains(Date date) {
        return contains(date, DatePart.DATE);
    }

    /**
     * Gets if a given date is contained within the range
     * @param date
     * @param part
     * @return
     */
    public boolean contains(Date date, DatePart part) {
        if (this.isEmpty()) {
            return false;
        }

        return (this.max == null || Dates.beforeOrEquals(date, this.max, part))
                && (this.min == null || Dates.afterOrEquals(date, this.min, part));
    }

    /**
     * Gets a date range with the intersection of this date range with another
     * one. If the two ranges don't intersects an empty date range is returned.
     * If one range is empty or infinite an exception is thrown.
     * This overload compares using only the date part.
     * @param range 
     * @return
     */
    public DateRange getIntersection(DateRange range) {
        return getIntersection(range, DatePart.DATE);
    }

    /**
     * Gets a date range with the intersection of this date range with another
     * one. If the two ranges don't intersects an empty date range is returned.
     * If one range is empty or infinite an exception is thrown.
     * @param range
     * @param part
     * @return
     */
    public DateRange getIntersection(DateRange range, DatePart part) {
        if (this.isEmpty() || this.isInfinite() || range.isEmpty() || range.isInfinite()) {
            throw new IllegalArgumentException("One of the ranges is empty or "
                    + "infinite, can't get an intersection");
        }

        if (this.before(range, part) || this.after(range, part)) {
            return new DateRange();
        }

        DateRange result = new DateRange();

        if (Dates.beforeOrEquals(min, range.min, part)) {
            result.setMin(new Date(range.min.getTime()));
        } else {
            result.setMin(new Date(min.getTime()));
        }

        if (Dates.afterOrEquals(max, range.max, part)) {
            result.setMax(new Date(range.max.getTime()));
        } else {
            result.setMax(new Date(max.getTime()));
        }

        return result;
    }

    /**
     * Gets if a date range is entirely contained within another range.
     * This overload compares using only the date part.
     * @param target
     * @return
     */
    public boolean isContained(DateRange target) {
        return isContained(target, DatePart.DATE);
    }

    /**
     * Gets if a date range is entirely contained within another range.
     * @param target
     * @param part 
     * @return
     */
    public boolean isContained(DateRange target, DatePart part) {
        return Dates.beforeOrEquals(target.min, min, part)
                && Dates.afterOrEquals(target.max, max, part);
    }

    /**
     * Gets the difference within another range. The resulting range(s) will be
     * made up of those days that are not in the other range. If no days matches
     * the condition an empty list of ranges will be returned.
     * This overload compares using only the date part.
     * @param target
     * @return
     */
    public List<DateRange> getDifference(DateRange target) {
        return getDifference(target, DatePart.DATE);
    }

    /**
     * Gets the difference within another range. The resulting range(s) will be
     * made up of those days that are not in the other range. If no days matches
     * the condition an empty list of ranges will be returned.
     * @param target
     * @param part 
     * @return
     */
    public List<DateRange> getDifference(DateRange target, DatePart part) {
        List<DateRange> result = new ArrayList<DateRange>(2);

        if (!this.intersects(target, part)) {
            result.add(new DateRange(min, max));
            return result;
        }

        if (this.equals(target, part)) {
            return result;
        }

        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();

        if (target.isContained(this)) {
            // When we contain the target range we end up with two ranges
            // as result
            cal.setTime(target.min);
            cal.add(GregorianCalendar.DAY_OF_MONTH, -1);
            result.add(new DateRange(min, cal.getTime()));
            cal.setTime(target.max);
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
            result.add(new DateRange(cal.getTime(), max));
        } else if (!Dates.afterOrEquals(min, target.min, part)
                || Dates.equals(max, target.max, part)) {

            cal.setTime(target.min);
            cal.add(GregorianCalendar.DAY_OF_MONTH, -1);
            result.add(new DateRange(min, cal.getTime()));
        } else if (!Dates.beforeOrEquals(max, target.max, part)
                || Dates.equals(min, target.min, part)) {

            cal.setTime(target.max);
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
            result.add(new DateRange(cal.getTime(), max));
        }

        return result;
    }

    /**
     * Gets an iterator over days since the minimun date of the range to the
     * maximum date.
     * @param part Part to use in the comparisons.
     * @return
     * @throws IllegalStateException If one or both range dates are not specified.
     */
    public Iterator<Date> getDateIterator(DatePart part) {
        if (this.isInfinite()) {
            throw new IllegalStateException(
                    "The range is infinite. Can only iterate finite ranges");
        }

        return new DateRangeIterator(
                this.min,
                this.max,
                part);
    }

    /**
     * Gets the number of days in the range
     * @return number of days in the range
     */
    public int getDayCount() {
        if (this.isInfinite()) {
            throw new IllegalStateException("The range is infinite");
        }

        return Dates.daysDifference(min, max);
    }

    /**
     * Increases the range width with the specified amount in both limits so
     * the real width increment is the double of the specified amount.
     * @param amount Milliseconds to addDateToRange to the max and to substract from the
     * min
     * @param component Date component affected by the change. Take into account
     * that NANOSECONDS are not supported and that NONE causes this method to
     * return the current values without changes.
     * @return
     */
    public DateRange increase(int amount, DateComponent component) {
        DateRange result = new DateRange();
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        int calFieldSpec = component.getCalendarConstant();

        cal.setTime(this.min);
        cal.add(calFieldSpec, -amount);
        result.setMin(cal.getTime());

        cal.setTime(this.max);
        cal.add(calFieldSpec, amount);
        result.setMax(cal.getTime());

        return result;
    }

    /**
     * Adds a date to the range increasing it if needed.<br/>
     * First tests the date to add within the limits of the current range, if
     * is between them this method then does nothing, this date range includes
     * the date. If not the interval is incresed using the date as one of the
     * limits.
     * @param dateTime
     * @param datePart
     */
    public void addDateToRange(Date dateTime, DatePart datePart) {
        if (this.contains(dateTime, datePart)) {
            return;
        }

        if (this.min != null && Dates.before(dateTime, this.min, datePart)) {
            this.setMin(dateTime);
        }

        if (this.max != null && Dates.after(dateTime, this.max, datePart)) {
            this.setMax(dateTime);
        }
    }

    /**
     * Iterator that iterates dates from min to max
     */
    private class DateRangeIterator implements Iterator<Date> {

        private Date end;
        private Date next;
        private DatePart part;
        private GregorianCalendar cal;

        public DateRangeIterator(Date start, Date end, DatePart part) {
            this.end = new Date(Dates.getDateTimePart(end, part));
            this.next = new Date(Dates.getDateTimePart(start, part));
            this.part = part;
            this.cal = (GregorianCalendar) GregorianCalendar.getInstance();
        }

        @Override
        public boolean hasNext() {
            return Dates.beforeOrEquals(next, end, part);
        }

        @Override
        public Date next() {
            Date result = this.next;
            cal.setTime(this.next);

            switch (part) {
                case DATE:
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    break;
                case DATE_TIME:
                case TIME:
                    cal.add(GregorianCalendar.SECOND, 1);
                    break;
            }

            this.next = cal.getTime();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}
