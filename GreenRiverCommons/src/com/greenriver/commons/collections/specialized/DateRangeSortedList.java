package com.greenriver.commons.collections.specialized;

import com.greenriver.commons.DatePart;
import com.greenriver.commons.DateRange;
import com.greenriver.commons.Dates;
import com.greenriver.commons.collections.SortedArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Miguel Angel
 */
public class DateRangeSortedList extends SortedArrayList<DateRange> {

    private class DateRangeComparator implements Comparator<DateRange> {

        public int compare(DateRange o1, DateRange o2) {
            return o1.compareTo(o2, datePart);
        }
    }
    private DatePart datePart;

    public DatePart getDatePart() {
        return datePart;
    }

    public DateRangeSortedList() {
        this(DatePart.DateTime);
    }

    public DateRangeSortedList(DatePart part) {
        this(10, part);
    }

    public DateRangeSortedList(int initialCapacity) {
        this(initialCapacity, DatePart.DateTime);
    }

    public DateRangeSortedList(int initialCapacity, DatePart part) {
        super(initialCapacity);
        this.datePart = part;
        this.setComparator(new DateRangeComparator());
    }

    private DateRange clone(DateRange dateRange) {
        try {
            return (DateRange) dateRange.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * Adds a new range to the list merging it with any existing range if they
     * intersects or if they are consecutive.
     * @param candidate
     * @return true if added or false if not (only if it already exists)
     * @throws IllegalStateException when the element can't be added and it
     * didn't existed in the collection.
     */
    @Override
    public boolean add(DateRange candidate) throws IllegalStateException {
        DateRange dateRange = null;
        DateRange increasedRange = null;
        int pos = 0;
        increasedRange = this.increaseRangeForComparison(candidate);

        while (pos <= this.size()) {
            
            if (pos == this.size()) {
                return super.addAt(pos, clone(candidate), true);
            }

            dateRange = this.get(pos);

            if (dateRange.equals(candidate, this.datePart)) {
                //range already in collection, bye bye!
                return false;
            }

            if (increasedRange.intersects(dateRange, datePart)) {
                //The candidate can be merged with the existing range
                dateRange.merge(candidate, datePart);
                //We need to check if any other range must be merged with this
                this.mergeAdjacentRanges(pos);
                return true;
            } else if (candidate.after(dateRange, datePart)) {
                //The range is after the current one so we must increase this
                pos++;
            } else if (candidate.before(dateRange, datePart)) {
                //The range goes before the current one
                return super.addAt(pos, clone(candidate), true);
            }
        }
        //We must append the range to the end
        return super.addAt(this.size(), clone(candidate), true);
    }

    /**
     * When a range is merged the next ranges must be checked to see if they
     * intersect the merged one or if they are consecutive to merge them too
     * @param pos Position of the recently merged range.
     */
    private void mergeAdjacentRanges(int pos) {
        DateRange currentRange = this.get(pos);
        DateRange increasedRange = this.increaseRangeForComparison(currentRange);
        pos++;
        while (pos < this.size() && increasedRange.intersects(this.get(pos))) {
            //Merge the range, update the range used for test and remove the
            //merged one from the list to don't check it again.
            currentRange.merge(this.get(pos), datePart);
            increasedRange = this.increaseRangeForComparison(currentRange);
            this.remove(pos);
        }

        return;
    }

    /**
     * Increases the range bounds using the current date part to make easier
     * to detect consecutive ranges.
     * @param range
     * @return
     */
    private DateRange increaseRangeForComparison(DateRange range) {
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        Date min = null;
        Date max = null;

        switch (this.datePart) {
            case Date:
                min = Dates.getDatePart(range.getMin());
                cal.setTime(min);
                cal.add(GregorianCalendar.DAY_OF_MONTH, -1);
                min = cal.getTime();

                max = Dates.getDatePart(range.getMax());
                cal.setTime(max);
                cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
                max = cal.getTime();
                break;
            case DateTime:
                cal.setTime(range.getMin());
                cal.add(GregorianCalendar.MILLISECOND, -1);
                min = cal.getTime();

                cal.setTime(range.getMax());
                cal.add(GregorianCalendar.MILLISECOND, 1);
                max = cal.getTime();
                break;
            case Time:
                throw new UnsupportedOperationException("not implemented for time");
        }

        return new DateRange(min, max);
    }

    public Date getMin() {
        if (this.size() == 0) {
            return null;
        }

        return this.get(0).getMin();
    }

    public Date getMax() {
        if (this.size() == 0) {
            return null;
        }

        return this.get(this.size() - 1).getMax();
    }
}
