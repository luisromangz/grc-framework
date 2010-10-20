
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
public class CIFOrNIFValidatorTest {

    public CIFOrNIFValidatorTest() {
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
     * Test of validate method, of class CIFOrNIFValidator.
     */
    @Test
    public void testValidate() {
        System.out.println("validate");
        String[] validOnes = new String[]{"Q2818002D", "B91789214", "12345678Z"};
        String[] invalidOnes = new String[]{"B91789215", "Q3818002D", "2342345J"};
        CIFOrNIFValidator instance = new CIFOrNIFValidator();
        boolean expResult = true;
        boolean result = false;

        for (int i=0; i < validOnes.length ; i++) {
            result = instance.validate(validOnes[i]);
            assertEquals(expResult, result);
        }
        
        expResult = false;

        for (int i=0; i < invalidOnes.length ; i++) {
            result = instance.validate(invalidOnes[i]);
            assertEquals(expResult, result);
        }
    }

}