/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.ui.console;

/**
 * Default implementation of the history command.
 *
 * It is not complete but holds the space.
 */
class DefaultCommandHistory implements CommandHistory {

    private String[] history;
    private int size;
    private int pos;
    private int first;

    /**
     * Initializes the maximum number of items to hold
     * @param max maximum number of items
     */
    public DefaultCommandHistory(int max) {
        
        if (max < 1) {
            throw new IllegalArgumentException();
        }
        
        history = new String[max];
        size = 0;
        pos = -1;
        first = -1;
    }

    public int getSize() {
        return size;
    }

    public int getMax() {
        return history.length;
    }

    public int currentPosition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goPrevious() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goFirst() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goLast() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPrevious() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getFirst() {
        if (first < 0 || first > size) {
            return null;
        }
        return history[first];
    }

    public String getLast() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void add(String cmd) {
        pos++;

        if (pos >= history.length) {
            pos = 0;
        } else {
            size ++;
        }

        if (first == -1) {
            first = pos;
        } else if (pos == first) {
            first ++;
            if (first >= size) {
                first = 0;
            }
        }

        history[pos] = cmd;
    }

}
