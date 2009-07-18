/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Decorator for OutputStream that accounts the number of written bytes
 */
public class AccountingOutputStreamDecorator extends OutputStream
	implements AccountedStream {

    private OutputStream decorated;
    private long writeCount;

    public synchronized long getByteCount() {
	return writeCount;
    }

    public synchronized long getByteCount(boolean reset) {
	long result = writeCount;

	if (reset) {
	    writeCount = 0L;
	}

	return result;
    }

    @Override
    public synchronized void write(int b) throws IOException {
	decorated.write(b);
	writeCount++;
    }

    public AccountingOutputStreamDecorator(OutputStream decorated) {
	this.decorated = decorated;
    }

    @Override
    public void close() throws IOException {
	super.close();
    }

    @Override
    public boolean equals(Object obj) {
	return decorated.equals(obj);
    }

    @Override
    public void flush() throws IOException {
	decorated.flush();
    }

    @Override
    public int hashCode() {
	return decorated.hashCode();
    }

    @Override
    public String toString() {
	return decorated.toString();
    }

    @Override
    public synchronized void write(byte[] b) throws IOException {
	decorated.write(b, 0, b.length);
	writeCount += b.length;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
	decorated.write(b, off, len);
	writeCount += len;
    }
}
