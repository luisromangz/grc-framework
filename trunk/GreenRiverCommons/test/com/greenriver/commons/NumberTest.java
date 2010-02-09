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
        assertEquals(1, Numbers.ceilInQuarterSteps(0.8f,0.01f),0.0000001f);
        assertEquals(0, Numbers.ceilInQuarterSteps(0.009f,0.01f),0.0001f);
        assertEquals(0, Numbers.ceilInQuarterSteps(0,0.01f),0.00001f);
        assertEquals(3.25f, Numbers.ceilInQuarterSteps(3.1f,0.01f),0.0000001f);
        assertEquals(0.25f, Numbers.ceilInQuarterSteps(0.25f,0.01f),0.000001f);
        assertEquals(0.25f, Numbers.ceilInQuarterSteps(0.2501f,0.001f),0.0001f);
        assertEquals(5.5f, Numbers.ceilInQuarterSteps(5.3f,0.01f),0.0000001f);
        assertEquals(0.5f, Numbers.ceilInQuarterSteps(0.5f,0.01f),0.000001f);
        assertEquals(0.75f, Numbers.ceilInQuarterSteps(0.501f,0.0f),0.0000001f);
        assertEquals(7.75f, Numbers.ceilInQuarterSteps(7.65f,0.01f),0.00001f);
        assertEquals(10.75f, Numbers.ceilInQuarterSteps(10.75f,0.01f),0.00001f);
        assertEquals(11f, Numbers.ceilInQuarterSteps(10.7501f,0.000000001f),0.00001f);

        assertEquals(10.75f, Numbers.ceilInQuarterSteps(10.8f,0.1f),0.1f);

        assertEquals(-3f, Numbers.ceilInQuarterSteps(-3.1f, 0),0);
         assertEquals(-3.25f, Numbers.ceilInQuarterSteps(-3.3f, 0),0);
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
        System.out.println("testRound_float");
        assertEquals(1.4500F, Numbers.trunc(1.454545F, 2), 0.00001);
        assertEquals(1.6500F, Numbers.trunc(1.655545F, 2), 0.00001);
        assertEquals(1.7500F, Numbers.trunc(1.756545F, 2), 0.00001);
    }
}