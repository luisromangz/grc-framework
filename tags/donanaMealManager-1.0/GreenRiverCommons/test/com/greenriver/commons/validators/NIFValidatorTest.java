package com.greenriver.commons.validators;

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
public class NIFValidatorTest {

    public NIFValidatorTest() {
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
        System.out.println("testNIFValidate");
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

        nif = "1234567L";
        expectedResult = true;
        nifNum = 1234567L;
        letter = nif.charAt(nif.length() - 1);

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

        nif="2910832H";
        expectedResult = false;
        nifNum = 2910832L;
        letter = nif.charAt(nif.length() - 1);

        result = validator.validate(nif);
        assertEquals(expectedResult, result);

        result = validator.validate(nifNum, letter);
        assertEquals(expectedResult, result);

        nif="28910832H";
        expectedResult = false;
        nifNum = 2910832L;
        letter = nif.charAt(nif.length() - 1);

        result = validator.validate(nif);
        assertEquals(expectedResult, result);

        result = validator.validate(nifNum, letter);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCifControlCharacter() {
        System.out.println("testGetNifControlCharacter");
        String[] nifs = new String[]{"12345678Z", "12345678", "2342345"};
        Character[] chars = new Character[]{'Z', 'Z', 'J'};
        NIFValidator validator = new NIFValidator();
        Character result = null;

        for (int i=0; i<nifs.length; i++) {
            result = validator.getNifControlCharacter(nifs[i]);
            if (i <= 1) {
                assertEquals(chars[i], result);
            } else {
                assertFalse(chars[i].equals(result));
            }
        }
    }
}
