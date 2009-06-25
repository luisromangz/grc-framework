/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.os;

import com.greenriver.commons.tasks.TimedWorker;

/**
 * Kills a process after a timeout.
 */
class KillProcessTask extends TimedWorker {

    Process process;
    
    public KillProcessTask(Process process) {
        super();
        this.process = process;
    }

    @Override
    protected void work() {
        if (process != null) {
            try {
                process.destroy();
            } catch (Exception ex) {

            }

            process = null;
            stop();
        }
    }
}
