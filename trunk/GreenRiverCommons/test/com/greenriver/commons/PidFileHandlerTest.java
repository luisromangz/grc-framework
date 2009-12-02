/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons;

import com.greenriver.commons.os.PidFileHandler;
import java.io.File;
import java.io.IOException;
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
public class PidFileHandlerTest {

    public PidFileHandlerTest() {
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
    public void createPidFileTest() throws IOException {
        System.out.println("createPidFileTest");
        PidFileHandler handler = new PidFileHandler(new File("/tmp/pidtest.pid"), 12345);
        handler.create();
        assertEquals(true, handler.isValid());

        PidFileHandler anotherHandler = new PidFileHandler(new File("/tmp/pidtest.pid"), 54321);
        anotherHandler.create();
        assertEquals(false, anotherHandler.isValid());

        anotherHandler.release();

        assertEquals(true, handler.getPidFile().exists());

        handler.release();

        anotherHandler = new PidFileHandler(new File("/tmp/pidtest.pid"), 54321);
        anotherHandler.create();
        assertEquals(true, anotherHandler.isValid());
    }
}
