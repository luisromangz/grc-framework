/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mangelp
 */
public class DateRangeTests {

    public DateRangeTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEquals() {
        System.out.println("testEquals");
        Date ref = new Date();
        long monthLen = 20 * 24 * 60 * 60 * 1000;
        long hourLen = 60 * 60 * 1000;
        boolean result = false;
        GregorianCalendar cal =
                (GregorianCalendar)GregorianCalendar.getInstance();

        cal.set(2001, 10, 10, 10, 10, 10);
        Date dateA = cal.getTime();
        cal.set(2001, 10, 10, 11, 10, 10);
        Date dateB = cal.getTime();

        //Range from month and a hour in the past to a month in the past
        DateRange rangeA = new DateRange(
                ref.getTime() - monthLen - hourLen,
                ref.getTime() - monthLen);
        //Range from an hour in the past to the current time
        DateRange rangeB = new DateRange(
                ref.getTime() - hourLen,
                ref.getTime());

        result = rangeA.equals(rangeB);
        assertFalse(result);

        result = rangeA.equals(rangeA);
        assertTrue(result);

        result = rangeB.equals(rangeB);
        assertTrue(result);

        // 1 hour range
        rangeA = new DateRange(dateA, dateB);
        // 1 hour range
        rangeB = new DateRange(dateA);

        result = rangeA.equals(rangeB, DatePart.DATE);
        assertTrue(result);

        result = rangeA.equals(rangeB, DatePart.TIME);
        assertFalse(result);
    }

    @Test
    public void testIntersects () {
        System.out.println("testIntersects");
        Date ref = new Date();
        long monthLen = 20 * 24 * 60 * 60 * 1000;
        long hourLen = 60 * 60 * 1000;
        boolean result = false;

        //Range from month and a hour in the past to a month in the past
        DateRange rangeA = new DateRange(
                ref.getTime() - monthLen - hourLen,
                ref.getTime() - monthLen);
        //Range from an hour in the past to the current time
        DateRange rangeB = new DateRange(
                ref.getTime() - hourLen,
                ref.getTime());

        result = rangeA.intersects(rangeB);
        assertFalse(result);

        rangeB.setMin(new Date(ref.getTime() - monthLen - 1000));

        result = rangeA.intersects(rangeB);
        assertTrue(result);

        result = rangeA.intersects(rangeA);
        assertTrue(result);

        result = rangeA.intersects(rangeA, DatePart.DATE);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.DATE);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.TIME);
        assertTrue(result);

        rangeB.setMin(new Date(ref.getTime() - monthLen + 1000));

        result = rangeA.intersects(rangeB, DatePart.DATE);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.TIME);
        assertTrue(result);

        rangeB.setMin(null);
        result = rangeA.intersects(rangeB);
        assertTrue(result);

        rangeB = new DateRange();
        result = rangeA.intersects(rangeB);
        assertFalse(result);
    }

    @Test
    public void testInRange () {
        System.out.println("testInRange");
        Date ref = new Date();
        long monthLen = 20 * 24 * 60 * 60 * 1000;
        long weekLen = 7 * 24 * 60 * 60 * 1000;
        long hourLen = 60 * 60 * 1000;
        boolean result = false;
        Date test = new Date(ref.getTime() - monthLen - 1000);

        //Range from month and a hour in the past to a month in the past
        DateRange rangeA = new DateRange(
                ref.getTime() - monthLen - hourLen,
                ref.getTime() - monthLen);
        //Range from an hour in the past to the current time
        DateRange rangeB = new DateRange(
                ref.getTime() - hourLen,
                ref.getTime());

        result = Dates.inRange(test, rangeA.getMin(), rangeA.getMax());
        assertTrue(result);

        result = Dates.inRange(test, rangeB.getMin(), rangeB.getMax());
        assertFalse(result);
    }

    @Test
    public void testIterator() {
        System.out.println("testIterator");
        Date[] dates = new Date[]{
            Dates.getDateTime(2009, 11, 24, 0, 0, 0),
            Dates.getDateTime(2009, 11, 25, 0, 0, 0),
            Dates.getDateTime(2009, 11, 26, 0, 0, 0),
            Dates.getDateTime(2009, 11, 27, 0, 0, 0)
        };
        int count = 0;
        DateRange dateRange = new DateRange(dates[0], dates[3]);
        Date next = null;
        Iterator<Date> dateIterator = dateRange.getDateIterator(DatePart.DATE);

        while(dateIterator.hasNext()) {
            next = dateIterator.next();
            assertTrue(Dates.equals(dates[count], next, DatePart.DATE));
            count ++;
        }

        assertEquals(dates.length, count);
    }
}
