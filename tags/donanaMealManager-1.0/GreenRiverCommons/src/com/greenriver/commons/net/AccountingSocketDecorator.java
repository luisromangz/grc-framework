/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.net;

import com.greenriver.commons.io.AccountingInputStreamDecorator;
import com.greenriver.commons.io.AccountingOutputStreamDecorator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

/**
 * Socket implementation that accounts for the amout of read and written bytes
 * to the input and output streams.
 */
public class AccountingSocketDecorator extends Socket {

    private Socket decorated;
    private AccountingInputStreamDecorator inputDecorator;
    private AccountingOutputStreamDecorator outputDecorator;

    public AccountingSocketDecorator(Socket decorated) {
        this.decorated = decorated;
        if (decorated == null) {
            throw new IllegalArgumentException("The socket was null.");
        }
    }

    public long getInputByteCount(boolean reset) {
        if (inputDecorator != null) {
            return inputDecorator.getByteCount(reset);
        }

        return 0L;
    }

    public long getOutputByteCount(boolean reset) {
        if (outputDecorator != null) {
            return outputDecorator.getByteCount(reset);
        }

        return 0L;
    }

    public long getInputByteCount() {
        return getInputByteCount(false);
    }

    public long getOutputByteCount() {
        return getOutputByteCount(false);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        //Decorate and return.
        if (inputDecorator == null && decorated != null) {
            inputDecorator =
                    new AccountingInputStreamDecorator(
                        decorated.getInputStream());
        } else if (decorated == null) {
            throw new NullPointerException(
                    "This instance have been already finalized.");
        }

        return inputDecorator;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        //Decorate and return.
        if (outputDecorator == null && decorated != null) {
            outputDecorator =
                    new AccountingOutputStreamDecorator(
                        decorated.getOutputStream());
        } else if (decorated == null) {
            throw new NullPointerException(
                    "This instance have been already finalized.");
        }

        return outputDecorator;
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        decorated.bind(bindpoint);
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        decorated.connect(endpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        decorated.connect(endpoint, timeout);
    }

    @Override
    public SocketChannel getChannel() {
        return decorated.getChannel();
    }

    @Override
    public InetAddress getInetAddress() {
        return decorated.getInetAddress();
    }

    public AccountingInputStreamDecorator getInputDecorator() {
        return inputDecorator;
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
        return decorated.getKeepAlive();
    }

    @Override
    public InetAddress getLocalAddress() {
        return decorated.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        return decorated.getLocalPort();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return decorated.getLocalSocketAddress();
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return decorated.getOOBInline();
    }

    @Override
    public int getPort() {
        return decorated.getPort();
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        return decorated.getReceiveBufferSize();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return decorated.getRemoteSocketAddress();
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        return decorated.getReuseAddress();
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        return decorated.getSendBufferSize();
    }

    @Override
    public int getSoLinger() throws SocketException {
        return decorated.getSoLinger();
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
        return decorated.getSoTimeout();
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return decorated.getTcpNoDelay();
    }

    @Override
    public int getTrafficClass() throws SocketException {
        return decorated.getTrafficClass();
    }

    @Override
    public int hashCode() {
        return decorated.hashCode();
    }

    @Override
    public boolean isBound() {
        return decorated.isBound();
    }

    @Override
    public boolean isClosed() {
        return decorated.isClosed();
    }

    @Override
    public boolean isConnected() {
        return decorated.isConnected();
    }

    @Override
    public boolean isInputShutdown() {
        return decorated.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
        return decorated.isOutputShutdown();
    }

    @Override
    public void sendUrgentData(int data) throws IOException {
        decorated.sendUrgentData(data);
    }

    @Override
    public void setKeepAlive(boolean on) throws SocketException {
        decorated.setKeepAlive(on);
    }

    @Override
    public void setOOBInline(boolean on) throws SocketException {
        decorated.setOOBInline(on);
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        decorated.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    @Override
    public synchronized void setReceiveBufferSize(int size) throws SocketException {
        decorated.setReceiveBufferSize(size);
    }

    @Override
    public void setReuseAddress(boolean on) throws SocketException {
        decorated.setReuseAddress(on);
    }

    @Override
    public synchronized void setSendBufferSize(int size) throws SocketException {
        decorated.setSendBufferSize(size);
    }

    @Override
    public void setSoLinger(boolean on, int linger) throws SocketException {
        decorated.setSoLinger(on, linger);
    }

    @Override
    public synchronized void setSoTimeout(int timeout) throws SocketException {
        decorated.setSoTimeout(timeout);
    }

    @Override
    public void setTcpNoDelay(boolean on) throws SocketException {
        decorated.setTcpNoDelay(on);
    }

    @Override
    public void setTrafficClass(int tc) throws SocketException {
        decorated.setTrafficClass(tc);
    }

    @Override
    public void shutdownInput() throws IOException {
        decorated.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
        decorated.shutdownOutput();
    }

    @Override
    public synchronized void close() throws IOException {
        decorated.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (decorated != null) {
            try {
                decorated.close();
            } catch (Exception ex) {
            }
            decorated = null;
        }
    }
}
