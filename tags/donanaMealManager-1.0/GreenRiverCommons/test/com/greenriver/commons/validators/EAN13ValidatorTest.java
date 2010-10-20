
package com.greenriver.commons.validators;

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
public class EAN13ValidatorTest {

    public EAN13ValidatorTest() {
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

    /**
     * Test of validate method, of class EAN13Validator.
     */
    @Test
    public void testValidate() {
        System.out.println("validate");
        String data = "0012345678905";
        EAN13Validator instance = new EAN13Validator();
        boolean expResult = true;
        boolean result = instance.validate(data);
        assertEquals(expResult, result);

        expResult = false;
        data = "0012345678915";
        result = instance.validate(data);
        assertEquals(expResult, result);
    }

}