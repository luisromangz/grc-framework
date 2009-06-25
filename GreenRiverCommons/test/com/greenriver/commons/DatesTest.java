/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

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
 * @author mangelp
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
}