/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luis
 */
public class NumberTest {

    public NumberTest() {
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
    public void testCeilInQuarterSteps() {
        System.out.println("testCeilInQuarterSteps");
        assertEquals("1",1, Numbers.ceilInQuarterSteps(0.8f), 0);
        assertEquals("2",0.250f, Numbers.ceilInQuarterSteps(0.009f), 0);
        assertEquals("3",0, Numbers.ceilInQuarterSteps(0), 0);
        assertEquals("4",3.25f, Numbers.ceilInQuarterSteps(3.1f), 0);
        assertEquals("5",0.25f, Numbers.ceilInQuarterSteps(0.25f), 0);
        assertEquals("6",0.5f, Numbers.ceilInQuarterSteps(0.2501f), 0);
        assertEquals("7",5.5f, Numbers.ceilInQuarterSteps(5.3f), 0);
        assertEquals("8",0.5f, Numbers.ceilInQuarterSteps(0.5f), 0);
        assertEquals("9",0.75f, Numbers.ceilInQuarterSteps(0.501f), 0);
        assertEquals("10",7.75f, Numbers.ceilInQuarterSteps(7.65f), 0);
        assertEquals("11",10.75f, Numbers.ceilInQuarterSteps(10.75f), 0);
        assertEquals("12",11f, Numbers.ceilInQuarterSteps(10.7501f), 0);

        assertEquals("13",11f, Numbers.ceilInQuarterSteps(10.8f),0);

        assertEquals("14",-3f, Numbers.ceilInQuarterSteps(-3.1f),0);
        assertEquals("15",-3.25f, Numbers.ceilInQuarterSteps(-3.3f),0);
    }

    @Test
    public void testCeilToSteps() {
         assertEquals("1",0.8f, Numbers.ceilingToSteps(0.8f,5), 0);
         assertEquals("2", 0.2f, Numbers.ceilingToSteps(0.15f, 5),0);
    }

    @Test
    public void testRound_float() {
        System.out.println("testRound_float");
        assertEquals(1.4500F, Numbers.round(1.454545F, 2), 0.00001);
        assertEquals(1.4600F, Numbers.round(1.455545F, 2), 0.00001);
        assertEquals(1.4600F, Numbers.round(1.456545F, 2), 0.00001);
        assertEquals(2F, Numbers.round(1.99765434F, 2), 0.00001);
    }

    @Test
    public void testTrunc_float() {
        System.out.println("testTrunc_float");
        assertEquals(1.4500F, Numbers.trunc(1.454545F, 2), 0.00001);
        assertEquals(1.6500F, Numbers.trunc(1.655545F, 2), 0.00001);
        assertEquals(1.7500F, Numbers.trunc(1.756545F, 2), 0.00001);
    }

    @Test
    public void testEquals() {
        System.out.println("TestEquals");
        assertTrue(Numbers.equals(0.0199999f, 0.019010201f, 0.01));
        assertTrue(Numbers.equals(0.0199999f, 0.01999999f, 0.01));
        assertTrue(Numbers.equals(0.0200000f, 0.01999999f, 0.01));
        assertFalse(Numbers.equals(0.0212345f, 0.0312345f, 0.01));
    }
}
