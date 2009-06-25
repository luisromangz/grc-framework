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
import java.io.InputStream;

/**
 * Decorator for an input stream that accounts the number of bytes read.
 */
public class AccountingInputStreamDecorator extends InputStream
        implements AccountedStream {

    private InputStream decorated;
    private long readCount;

    public InputStream getDecorated() {
        return decorated;
    }

    public long getByteCount() {
        return readCount;
    }

    public AccountingInputStreamDecorator(InputStream decorated) {
        this.decorated = decorated;
    }

    @Override
    public int read() throws IOException {
        int result = decorated.read();
        readCount++;
        return result;
    }

    @Override
    public int available() throws IOException {
        try {
            return decorated.available();
        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public void close() throws IOException {
        decorated.close();
    }

    @Override
    public boolean equals(Object obj) {
        return decorated.equals(obj);
    }

    @Override
    public int hashCode() {
        return decorated.hashCode();
    }

    @Override
    public synchronized void mark(int readlimit) {
        decorated.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return decorated.markSupported();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return decorated.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
        decorated.reset();
    }

    public void setDecorated(InputStream decorated) {
        this.decorated = decorated;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    @Override
    public long skip(long n) throws IOException {
        return decorated.skip(n);
    }

    @Override
    public String toString() {
        return decorated.toString();
    }
}
