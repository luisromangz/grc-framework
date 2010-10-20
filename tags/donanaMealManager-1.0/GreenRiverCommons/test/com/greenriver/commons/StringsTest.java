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
 * @author mangelp
 */
public class StringsTest {

    public StringsTest() {
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
    public void testStartsWith() {
        System.out.println("testStartsWith");
        String prefix = "prefix";
        boolean result = true;
        String test = "prefixString";

        result = Strings.startsWith(prefix, test);
        assertTrue(result);

        test = "prefIxString";
        result = Strings.startsWith(prefix, test);
        assertFalse(result);

        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        test = "";
        result = Strings.startsWith(prefix, test, true);
        assertFalse(result);

        test = null;
        result = Strings.startsWith(prefix, test, true);
        assertFalse(result);

        prefix = null;
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        prefix = "";
        test = "blabla";
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        prefix = null;
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        prefix = "bla";
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        prefix = "blabla";
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);

        prefix = "";
        test = "";
        result = Strings.startsWith(prefix, test, true);
        assertTrue(result);
    }

    @Test
    public void testEndsWith() {
        System.out.println("testEndsWith");
        String suffix = "suffix";
        boolean result = true;
        String test = "Stringsuffix";

        result = Strings.endsWith(suffix, test);
        assertTrue(result);

        test = "StringSuffix";
        result = Strings.endsWith(suffix, test);
        assertFalse(result);
        
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        test = "";
        result = Strings.endsWith(suffix, test, true);
        assertFalse(result);

        test = null;
        result = Strings.endsWith(suffix, test, true);
        assertFalse(result);

        suffix = null;
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        suffix = "";
        test = "blabla";
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        suffix = null;
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        suffix = "bla";
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        suffix = "blabla";
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);

        suffix = "";
        test = "";
        result = Strings.endsWith(suffix, test, true);
        assertTrue(result);
    }
}