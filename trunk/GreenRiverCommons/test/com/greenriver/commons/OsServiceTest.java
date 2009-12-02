/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import com.greenriver.commons.os.OsService;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OsServiceTest {

	private static FakeOsService osService;

    public OsServiceTest() {
    }

	@BeforeClass
	public static void setUpClass() throws Exception {
		osService = new FakeOsService();
		osService.setInterval(1500);
		osService.setLogger(Logger.getLogger("Testing Stuff"));
		OsService.setLogLevel(Level.ALL);
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

	@Test
	public void shutdownTest() throws InterruptedException {
            System.out.println("shutdownTest");
            Thread osSrvThread = new Thread(osService);
            osSrvThread.start();
            //Add a little delay to wait for the service to start
            //before shutting down
            Thread.sleep(1000);
            osService.shutdown();
            //Add a little delay to wait for the service to end
            Thread.sleep(1000);
            assertEquals(true, osService.isFinished());
            assertEquals(false, osService.isDaemon());
            assertEquals(false, osService.isAborted());
            assertEquals(false, osService.isRunning());
            assertEquals(true, osService.isStopping());
	}
}