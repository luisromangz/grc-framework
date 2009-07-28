/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import com.greenriver.commons.io.AccountingInputStreamDecorator;
import com.greenriver.commons.io.AccountingOutputStreamDecorator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
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
public class AccountedStreamTest {

    public AccountedStreamTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void inputStreamTest() throws IOException {
	InputStream is = new FakeInputStream();
	is = new AccountingInputStreamDecorator(is);
	Random r = new Random();
	int expByteCount = r.nextInt(256);

	for (int i = 0; i < expByteCount; i++) {
	    is.read();
	}

	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount());
	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount(true));
	assertEquals(0, ((AccountingInputStreamDecorator)is).getByteCount());

	expByteCount = r.nextInt(64) + 12;

	byte[] data = new byte[expByteCount];
	Arrays.fill(data, (byte)3);

	is.read(data);

	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount());
	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount(true));
	assertEquals(0, ((AccountingInputStreamDecorator)is).getByteCount());

	expByteCount = 6;
	is.read(data, 4, expByteCount);

	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount());
	assertEquals(expByteCount, ((AccountingInputStreamDecorator)is).getByteCount(true));
	assertEquals(0, ((AccountingInputStreamDecorator)is).getByteCount());
    }

    @Test
    public void outputStreamTest() throws IOException {
	OutputStream os = new FakeOutputStream();
	os = new AccountingOutputStreamDecorator(os);
	Random r = new Random();
	int expByteCount = r.nextInt(256);

	for (int i = 0; i < expByteCount; i++) {
	    os.write(0);
	}

	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount());
	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount(true));
	assertEquals(0, ((AccountingOutputStreamDecorator)os).getByteCount());

	expByteCount = r.nextInt(64) + 12;

	byte[] data = new byte[expByteCount];
	Arrays.fill(data, (byte)3);

	os.write(data);

	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount());
	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount(true));
	assertEquals(0, ((AccountingOutputStreamDecorator)os).getByteCount());

	expByteCount = 6;
	os.write(data, 4, expByteCount);

	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount());
	assertEquals(expByteCount, ((AccountingOutputStreamDecorator)os).getByteCount(true));
	assertEquals(0, ((AccountingOutputStreamDecorator)os).getByteCount());
    }
}