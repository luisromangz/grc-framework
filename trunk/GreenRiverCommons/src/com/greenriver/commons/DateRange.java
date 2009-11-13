package com.greenriver.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Date range.
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
        this(min.getTime(), max.getTime());
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
        return intersects(range, DatePart.DateTime);
    }

    public boolean intersects(DateRange range, DatePart part) {
        if (range == null || range.isEmpty() || this.isEmpty()) {
            return false;
        }

        return Dates.intersects(this.min, this.max, range.min, range.max, part);
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals((DateRange) obj, DatePart.DateTime);
    }

    public boolean equals(DateRange obj, DatePart part) {
        if (obj == null || !this.getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        DateRange dateRange = (DateRange) obj;

        return Dates.equals(this.min, dateRange.min, part) &&
                Dates.equals(this.max, dateRange.max, part);
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
     * range. That means this range goes before the another one
     * @param dateRange
     * @return
     */
    public boolean before(DateRange dateRange) {
        return !Dates.greaterOrEqual(this.max, dateRange.min, DatePart.Date);
    }

    /**
     * Checks if the minimum of this range is after the maximum of another
     * range. That means this range goes after the another one.
     * @param dateRange
     * @param part 
     * @return
     */
    public boolean before(DateRange dateRange, DatePart part) {
        return !Dates.greaterOrEqual(this.max, dateRange.min, part);
    }

    public boolean after(DateRange dateRange) {
        return !Dates.lessOrEqual(this.min, dateRange.max, DatePart.Date);
    }

    public boolean after(DateRange dateRange, DatePart part) {
        return !Dates.lessOrEqual(this.min, dateRange.max, part);
    }

    public int compareTo(DateRange o, DatePart part) {
        if (this.before(o, part)) {
            return -1;
        } else if (this.after(o, part)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int compareTo(DateRange o) {
        return this.compareTo(o, DatePart.DateTime);
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
            return "(" + (min == null ? "Infinite" : Dates.formatAsMysqlDate(min)) + ", " +
                    (max == null ? "Infinite" : Dates.formatAsMysqlDate(max)) + ")";
        }
    }

    public boolean contains(Date time) {
        return contains(time, DatePart.DateTime);
    }

    public boolean contains(Date time, DatePart part) {
        if (this.isEmpty()) {
            return false;
        }

        return (this.max == null || Dates.lessOrEqual(time, this.max, part)) &&
                (this.min == null || Dates.greaterOrEqual(time, this.min, part));
    }

    public DateRange getIntersection(DateRange range) {
        return getIntersection(range, DatePart.Date);
    }

    public DateRange getIntersection(DateRange range, DatePart part) {
        if (this.isEmpty() || this.isInfinite() || range.isEmpty() || range.isInfinite()) {
            throw new IllegalArgumentException("One of the ranges is empty or " +
                    "infinite, can't get an intersection");
        }

        if (this.before(range, part) || this.after(range, part)) {
            return new DateRange();
        }

        DateRange result = new DateRange();

        if (Dates.lessOrEqual(min, range.min, part)) {
            result.setMin(new Date(range.min.getTime()));
        } else {
            result.setMin(new Date(min.getTime()));
        }

        if (Dates.greaterOrEqual(max, range.max, part)) {
            result.setMax(new Date(range.max.getTime()));
        } else {
            result.setMax(new Date(max.getTime()));
        }

        return result;
    }

    /**
     * Gets if a date range is entirely contained within another range
     * @param target
     * @return
     */
    public boolean isContained(DateRange target) {
        return isContained(target, DatePart.Date);
    }

    /**
     * Gets if a date range is entirely contained within another range
     * @param target
     * @param part 
     * @return
     */
    public boolean isContained(DateRange target, DatePart part) {
        return Dates.lessOrEqual(target.min, min, part) &&
                Dates.greaterOrEqual(target.max, max, part);
    }

    /**
     * Gets the difference within another range. The resulting range(s) will be
     * made up of those days that are not in the other range. If no days matches
     * the condition an empty list of ranges will be returned.
     * @param target
     * @return
     */
    public List<DateRange> getDifference(DateRange target) {
        return getDifference(target, DatePart.Date);
    }

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
        } else if (!Dates.greaterOrEqual(min, target.min, part) ||
                Dates.equals(max, target.max, part)) {

            cal.setTime(target.min);
            cal.add(GregorianCalendar.DAY_OF_MONTH, -1);
            result.add(new DateRange(min, cal.getTime()));
        } else if (!Dates.lessOrEqual(max, target.max, part) ||
                Dates.equals(min, target.min, part)) {

            cal.setTime(target.max);
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
            result.add(new DateRange(cal.getTime(), max));
        }

        return result;
    }
}
