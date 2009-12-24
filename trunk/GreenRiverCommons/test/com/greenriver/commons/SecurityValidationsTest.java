/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import com.greenriver.commons.security.NIFValidator;
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
public class SecurityValidationsTest {

    public SecurityValidationsTest() {
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
    public void validateNIFTest() {
        String nif = "12345678Z";
        long nifNum = 12345678L;
        Character letter = nif.charAt(nif.length() - 1);
        boolean expectedResult = true;
        boolean result = false;
        NIFValidator validator = new NIFValidator();

        result = validator.validate(nif);
        assertEquals(expectedResult, result);

        result = validator.validate(nifNum, letter);
        assertEquals(expectedResult, result);

        nif = "2342345J";
        expectedResult = false;
        nifNum = 2342345L;
        letter = nif.charAt(nif.length() - 1);
        
        result = validator.validate(nif);
        assertEquals(expectedResult, result);

        result = validator.validate(nifNum, letter);
        assertEquals(expectedResult, result);
    }
}