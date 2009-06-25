/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/**
 * Implements bridge (adapter) to send data over a serial port using a tcp/ip
 * communication stack in the client end.
 *
 * All the property values that this implementation doesn't use are ignored.
 * This implementation considers that the bindpoint and the endpoint are the same
 * as there is no network, being the local and remote address the same thing,
 * the hw communication port.
 *
 * This way using bind() and then connect requires you to not use an address
 * in the second method or not to use bind at all.
 *
 * The addresses for this socket are of type SerialSocketAddress defined in
 * package com.greenriver.commons.dev
 */
public class SerialSocket extends Socket {

    /**
     * @return the options
     */
    public SerialSocketOptions getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(SerialSocketOptions options) {
        this.options = options;
    }
    
    /**
     * Default time to wait when connecting with a serial device
     */
    public static final int openTimeout = 10000;
    /**
     * Default time to wait response when communicating with serial device
     */
    public static final int commTimeout = 5000;
    
    private String ownerName;
    private SerialPort serialPort;
    private SerialSocketAddress bindAddr;
    private SerialSocketOptions options;

    /**
     * @return the communication port adapter
     */
    public CommPort getCommPort() {
        return serialPort;
    }

    /**
     * Initiallizes the owner name.
     * @param ownerName
     */
    public SerialSocket(String ownerName) {
        
        if (ownerName == null || ownerName.length() == 0) {
            throw new IllegalArgumentException("The owner must be a valid " +
                    "non-zero length string");
        }

        this.ownerName = ownerName;
    }

    @Override
    public boolean isConnected() {
        return serialPort != null;
    }

    @Override
    public boolean isBound() {
        return bindAddr != null;
    }

    @Override
    public boolean isInputShutdown() {
        return serialPort == null;
    }

    @Override
    public boolean isOutputShutdown() {
        return serialPort == null;
    }

    @Override
    public boolean isClosed() {
        return serialPort == null;
    }

    public SerialSocketAddress getBindAddr() {
        return bindAddr;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (isInputShutdown()) {
            throw new IOException("There is no stream");
        }

        return serialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (isOutputShutdown()) {
            throw new IOException("There is no stream");
        }

        return serialPort.getOutputStream();
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, openTimeout);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        //If there is no endpoint nor bind we throw exception
        //If there also are both we also throw exception
        if ((endpoint == null && bindAddr == null) ||
                !(endpoint instanceof SerialSocketAddress)) {
            throw new IllegalArgumentException("SerialSocketAddress required " +
                    "as address");
        } else if (bindAddr != null && endpoint != null) {
            throw new IllegalArgumentException("This socket is already bound" +
                    " use a null instead of an endpoint");
        }

        //In case the port is here we throw an exception as connect should be
        //called only once
        if (serialPort != null) {
            throw new IOException("Already connected");
        }

        //If it is not bound we bound to the endpoint as they are the same thing
        if (bindAddr == null) {
            bindAddr = (SerialSocketAddress) endpoint;
        }

        //We gonna try get the identifier to the serial device by name and
        //then try to open a connection to it.
        CommPortIdentifier ident = null;

        try {
            ident = CommPortIdentifier.getPortIdentifier(
                    bindAddr.getDeviceName());
        } catch (NoSuchPortException ex) {
            throw new IllegalArgumentException("Unknown serial device " +
                    bindAddr.getDeviceName(), ex);
        }

        if (ident.getPortType() != CommPortIdentifier.PORT_SERIAL) {
            throw new IOException("Unsupported serial type");
        }

        if (options == null) {
            //Defaults
            options = new SerialSocketOptions();
            options.setBaudRate(9600);
            options.setDataBits(SerialSocketOptions.DataBits.EIGHT);
            options.setFlowControl(SerialSocketOptions.FlowControl.NONE);
            options.setParity(SerialSocketOptions.Parity.NONE);
            options.setStopBits(SerialSocketOptions.StopBits.ONE);
        }

        //Try to get a hold in the comm port. We could do first a check if it
        //is owned, but we must do this anyway and we can loose it before we
        //call this.
        try {
            serialPort = (SerialPort) ident.open(ownerName, timeout);
        } catch (PortInUseException ex) {
            throw new IOException("Comm port already in use");
        }

        configure();
    }

    @Override
    public synchronized void close() throws IOException {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
            bindAddr = null;
        }
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        if (isBound()) {
            throw new IOException("Already bound");
        }

        if (!(bindpoint instanceof SerialSocketAddress)) {
            throw new IllegalArgumentException("Address must be " +
                    "SerialSocketAddress instance");
        }

        bindAddr = (SerialSocketAddress) bindpoint;
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        assertConnected();

        return serialPort.getInputBufferSize();
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        assertConnected();

        return serialPort.getOutputBufferSize();
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        return false;
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return false;
    }

    @Override
    public int getSoLinger() throws SocketException {
        return 0;
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return false;
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
        assertConnected();

        return serialPort.getReceiveTimeout();
    }

    @Override
    public synchronized void setSoTimeout(int timeout) throws SocketException {
        throw new SocketException("Not supported");
    }

    @Override
    public synchronized void setReceiveBufferSize(int size) throws SocketException {
        assertConnected();

        serialPort.setInputBufferSize(size);
    }

    @Override
    public synchronized void setSendBufferSize(int size) throws SocketException {
        assertConnected();

        serialPort.setOutputBufferSize(size);
    }

    @Override
    public void shutdownInput() throws IOException {
        throw new IOException("Not implemented");
    }

    @Override
    public void shutdownOutput() throws IOException {
        throw new IOException("Not implemented");
    }

    /**
     * 
     * @throws com.greenriver.commons.net.SerialSocketException
     */
    protected void configure() throws SerialSocketException {
        try {
            serialPort.setSerialPortParams(
                        options.getBaudRate(),
                        options.getDataBitsAsInt(),
                        options.getStopBitsAsInt(),
                        options.getParityAsInt()
                    );
            serialPort.setFlowControlMode(options.getFlowControlAsInt());
            serialPort.disableReceiveFraming();
            serialPort.disableReceiveThreshold();
            serialPort.disableReceiveTimeout();
        } catch (UnsupportedCommOperationException ex) {
            //throw new SerialSocketException("Unable to configure serial", ex);
        } catch (Exception ex) {
            //throw new SerialSocketException("Socket error", ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        //We should try to ensure that close method have been called.
        try {
            close();
        } catch (Exception ex) {
            
        }
    }

    protected void assertConnected() throws SocketException {
        if (!isConnected()) {
            throw new SocketException("Not connected");
        }
    }
}
