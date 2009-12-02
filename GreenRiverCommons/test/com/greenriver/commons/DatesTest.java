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
    public void testTime() {
        System.out.println("testTime");
        java.sql.Time time = new Time(14, 0, 0);
        java.sql.Time time2 = Dates.getSqlTime(14, 0, 0);
        long timePart = Dates.getDateTimePart(time, DatePart.TIME);
        long timePart2 = Dates.getDateTimePart(time2, DatePart.TIME);

        assertEquals(time, time2);
        assertEquals(time.getTime(), timePart);
        assertEquals(timePart, timePart2);
    }

    @Test
    public void testEquals() {
        System.out.println("testEquals");
        GregorianCalendar cal =
                (GregorianCalendar)GregorianCalendar.getInstance();
        
        cal.set(2001, 10, 10, 10, 10, 10);
        Date dateA = cal.getTime();
        cal.set(2001, 10, 11, 10, 10, 10);
        Date dateB = cal.getTime();
        cal.set(2001, 10, 10, 11, 10, 10);
        Date dateC = cal.getTime();
        
        Boolean result = false;
        Boolean expectedResult = false;

        // DATE(A) != DATE(B)
        expectedResult = false;
        result = Dates.equals(dateA, dateB, DatePart.DATE);
        assertEquals("DATE(A) != DATE(B)", expectedResult, result);

        // DATE(A) == DATE(C)
        expectedResult = true;
        result = Dates.equals(dateA, dateC, DatePart.DATE);
        assertEquals("DATE(A) == DATE(C)", expectedResult, result);

        // TIME(A) == TIME(B)
        expectedResult = true;
        result = Dates.equals(dateA, dateB, DatePart.TIME);
        assertEquals("TIME(A) == TIME(B)", expectedResult, result);

        // TIME(A) != TIME(c)
        expectedResult = false;
        result = Dates.equals(dateA, dateC, DatePart.TIME);
        assertEquals("TIME(A) != TIME(B)", expectedResult, result);
    }
}