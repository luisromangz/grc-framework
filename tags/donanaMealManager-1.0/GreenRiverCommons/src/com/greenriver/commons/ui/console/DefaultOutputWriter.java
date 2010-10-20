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
 * Implementation that writes to standard out
 */
public class DefaultOutputWriter implements OutputWriter {
    private String padding;

    public void increasePad() {
        padding = padding + "\t";
    }

    public void decreasePad() {
        if (padding.length() > 1) {
            padding = padding.substring(0, padding.length() - 1);
        } else {
            padding = "";
        }
    }

    public String getPad() {
        return this.padding;
    }

    public void write(String msg) {
        System.out.print(msg);
    }

    public void writeln(String msg) {
        System.out.println(msg);
    }

    public void writeln(String[] msgs) {
        for (String msg : msgs) {
            System.out.println(msg);
        }
    }

    public void writeln(Throwable t) {
        System.out.println("Exception stack trace: ");
        t.printStackTrace(System.out);
    }

}
