
package com.greenriver.commons;

import java.util.Arrays;
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
public class ListsTest {

    public ListsTest() {
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
    public void testJoin() {
        System.out.println("testJoin");
	String[] elems = new String[] {"Test1 ", "Test2 ", "Test3 "};
	String expResult = "Test1 +Test2 +Test3 ";
	String testResult = Strings.join(Arrays.asList(elems), "+");

	assertEquals(expResult, testResult);
    }
}