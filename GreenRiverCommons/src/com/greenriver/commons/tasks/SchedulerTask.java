/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

/**
 * Task that is executed one or more times
 * @param <D> Type of the descriptor for the task with aditional task properties
 */
public interface SchedulerTask<D extends TaskDescriptor> {
    D getDescriptor();
}
