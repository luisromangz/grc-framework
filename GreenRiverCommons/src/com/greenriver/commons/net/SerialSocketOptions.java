/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.net;

import javax.comm.SerialPort;

/**
 * Option modeling for serial port
 */
public class SerialSocketOptions {


    public enum Parity {
        EVEN,
        ODD,
        NONE
    }

    public enum StopBits {
        ONE,
        ONE_AND_HALF,
        TWO
    }

    public enum FlowControl {
        HARDWARE,
        SOFTWARE,
        NONE
    }

    public enum DataBits {
        EIGHT,
        SEVEN,
        SIX,
        FIVE
    }

    private Parity parity;
    private StopBits stopBits;
    private FlowControl flowControl;
    private DataBits dataBits;
    private int baudRate;

    /**
     * @return the parity
     */
    public Parity getParity() {
        return parity;
    }

    /**
     * @param parity the parity to set
     */
    public void setParity(Parity parity) {
        this.parity = parity;
    }

    /**
     * @return the stopBits
     */
    public StopBits getStopBits() {
        return stopBits;
    }

    /**
     * @param stopBits the stopBits to set
     */
    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    /**
     * @return the flowControl
     */
    public FlowControl getFlowControl() {
        return flowControl;
    }

    /**
     * @param flowControl the flowControl to set
     */
    public void setFlowControl(FlowControl flowControl) {
        this.flowControl = flowControl;
    }

    /**
     * @return the dataBits
     */
    public DataBits getDataBits() {
        return dataBits;
    }

    /**
     * @param dataBits the dataBits to set
     */
    public void setDataBits(DataBits dataBits) {
        this.dataBits = dataBits;
    }

    /**
     * @return the baudRate
     */
    public int getBaudRate() {
        return baudRate;
    }

    /**
     * @param baudRate the baudRate to set
     */
    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public SerialSocketOptions() {
    }

    public int getParityAsInt() {
        switch(parity) {
            case EVEN:
                return SerialPort.PARITY_EVEN;
            case ODD:
                return SerialPort.PARITY_ODD;
            case NONE:
                return SerialPort.PARITY_NONE;
            default:
                throw new IllegalStateException("Can't convert to int");
        }
    }

    public int getStopBitsAsInt() {
        switch(stopBits) {
            case ONE:
                return SerialPort.STOPBITS_1;
            case TWO:
                return SerialPort.STOPBITS_2;
            case ONE_AND_HALF:
                return SerialPort.STOPBITS_1_5;
            default:
                throw new IllegalStateException("Can't convert to int");
        }
    }

    public int getFlowControlAsInt() {
        switch(flowControl) {
            case HARDWARE:
                return SerialPort.FLOWCONTROL_RTSCTS_OUT;
            case SOFTWARE:
                return SerialPort.FLOWCONTROL_XONXOFF_OUT;
            case NONE:
                return SerialPort.FLOWCONTROL_NONE;
            default:
                throw new IllegalStateException("Can't convert to int");
        }
    }

    public int getDataBitsAsInt() {
        switch(dataBits) {
            case EIGHT:
                return SerialPort.DATABITS_8;
            case SEVEN:
                return SerialPort.DATABITS_7;
            case SIX:
                return SerialPort.DATABITS_6;
            case FIVE:
                return SerialPort.DATABITS_5;
            default:
                throw new IllegalStateException("Can't convert to int");
        }
    }
    
}
