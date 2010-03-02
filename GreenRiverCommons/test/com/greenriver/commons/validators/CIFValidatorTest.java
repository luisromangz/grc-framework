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
public class CIFValidatorTest {

    public CIFValidatorTest() {
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
     * Test of validate method, of class CIFValidator.
     */
    @Test
    public void testCIFValidate() {
        System.out.println("testCIFValidate");
        // CSIC CIF
        String cif = "Q2818002D";
        CIFValidator instance = new CIFValidator();
        boolean expResult = true;
        boolean result = instance.validate(cif);
        assertEquals(expResult, result);

        // GRC SL CIF
        cif = "B91789214";
        expResult = true;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        // Asociación Nacional de Criadores de Ganado Marismeño
        cif = "G21163944";
        expResult = true;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        cif = "B91789215";
        expResult = false;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        cif = "C91789214";
        expResult = false;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        cif = null;
        expResult = false;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        cif = "";
        expResult = false;
        result = instance.validate(cif);
        assertEquals(expResult, result);

        cif = "2222222222222222";
        expResult = false;
        result = instance.validate(cif);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCifControlCharacter() {
        System.out.println("testGetCifControlCharacter");
        String[] cifs = new String[]{"Q2818002", "Q2818002D", "B9178921", "B9178222"};
        Character[] chars = new Character[]{'D', 'D', '4', '5'};
        CIFValidator validator = new CIFValidator();
        Character result = null;

        for (int i=0; i<cifs.length; i++) {
            result = validator.getCifControlCharacter(cifs[i]);
            if (i <= 2) {
                assertEquals(chars[i], result);
            } else {
                assertFalse(chars[i].equals(result));
            }
        }
    }
}
