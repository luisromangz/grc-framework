/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Angel
 */
public class DatesTest {

    private GregorianCalendar testDate;

    public DatesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        testDate = new GregorianCalendar(1900, 4, 14, 14, 14, 14);
        testDate.add(GregorianCalendar.MILLISECOND, 999);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addNanos method, of class Dates.
     */
    @Test
    public void testAddNanos() {
        System.out.println("addNanos");
        Timestamp stamp = new Timestamp(testDate.getTimeInMillis());
        int deviation = 999999;
        Timestamp expResult = new Timestamp(testDate.getTimeInMillis());
        expResult.setNanos(999999999);
        Timestamp result = Dates.addNanos(stamp, deviation);
        assertEquals(expResult, result);

        testDate.add(GregorianCalendar.SECOND, 1);
        deviation = 1;
        expResult = new Timestamp(testDate.getTimeInMillis());
        expResult.setNanos(0);
        result = Dates.addNanos(result, deviation);
        assertEquals(expResult, result);

        testDate.add(GregorianCalendar.SECOND, -1);
        expResult = new Timestamp(testDate.getTimeInMillis());
        expResult.setNanos(000000001);
        deviation = -999999999;
        result.setNanos(0);
        result = Dates.addNanos(result, deviation);
        assertEquals(expResult, result);

        testDate.add(GregorianCalendar.SECOND, -1);
        expResult = new Timestamp(testDate.getTimeInMillis());
        expResult.setNanos(999999999);
        deviation = -000000002;
        result = Dates.addNanos(result, deviation);
        assertEquals(expResult, result);
    }

    /**
     * Test of toTimestamp method, of class Dates.
     */
    @Test
    public void testToTimestamp_long_int() {
        System.out.println("toTimestamp");
        long millis = testDate.getTimeInMillis();
        int nanos = 555555555;
        Timestamp expResult = new Timestamp(millis);
        expResult.setNanos(555555555);
        Timestamp result = Dates.toTimestamp(millis, nanos);
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
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

        result = rangeA.equals(rangeB);
        assertFalse(result);

        result = rangeA.equals(rangeA);
        assertTrue(result);

        result = rangeB.equals(rangeB);
        assertTrue(result);

        result = Dates.equals(rangeA.getMin(), rangeA.getMax());
        assertFalse(result);

        result = Dates.equals(rangeA.getMin(), rangeA.getMax(), DatePart.Date);
        assertTrue(result);

        result = Dates.equals(rangeA.getMin(), rangeA.getMax(), DatePart.Time);
        assertFalse(result);
    }

    @Test
    public void testIntersects () {
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

        result = rangeA.intersects(rangeA, DatePart.Date);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.Date);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.Time);
        assertTrue(result);

        rangeB.setMin(new Date(ref.getTime() - monthLen + 1000));

        result = rangeA.intersects(rangeB, DatePart.Date);
        assertTrue(result);

        result = rangeA.intersects(rangeB, DatePart.Time);
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
    public void testTime() {
        java.sql.Time time = new Time(14, 0, 0);
        java.sql.Time time2 = Dates.getSqlTime(14, 0, 0);
        long timePart = Dates.getDateTimePart(time, DatePart.Time);
        long timePart2 = Dates.getDateTimePart(time2, DatePart.Time);

        assertEquals(time, time2);
        assertEquals(time.getTime(), timePart);
        assertEquals(timePart, timePart2);
    }
}