package com.greenriver.commons;

import java.util.Date;

/**
 * Date range.
 * @author Miguel Angel
 */
public class DateRange {

    private Date min;
    private Date max;

    public Date getMax() {
        return max;
    }

    public void setMax(Date max) {
        if (this.min == null && max == null) {
            throw new IllegalArgumentException("Both dates of the range can't be null");
        }
        this.max = max;
        if (this.max != null && this.min != null && this.min.after(this.max)) {
            Date tmp = this.min;
            this.min = this.max;
            this.max = tmp;
        }
    }

    public Date getMin() {
        return min;
    }

    public void setMin(Date min) {
        if (this.max == null && min == null) {
            throw new IllegalArgumentException("Both dates of the range can't be null");
        }
        this.min = min;
        if (this.min != null && this.max != null && this.max.before(this.min)) {
            Date tmp = this.min;
            this.min = this.max;
            this.max = tmp;
        }
    }

    public boolean isEmpty() {
        return min == null && max == null;
    }

    public DateRange() {
    }

    public DateRange(Date min, Date max) {
        if (min == null && max == null) {
            throw new IllegalArgumentException("Both parameters can't be null");
        }

        if (min != null && max != null && min.after(max)) {
            this.min = max;
            this.max = min;
        } else {
            this.min = min;
            this.max = max;
        }
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
        return this.equals(obj, DatePart.DateTime);
    }

    public boolean equals(Object obj, DatePart part) {
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
}
