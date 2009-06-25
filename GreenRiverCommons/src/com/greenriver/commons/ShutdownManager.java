/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons;

/**
 * Interface for those objects that needs an explicit call to a shutdown
 * methown when the application is about to be shutdown too.
 * The objects that implement this interface must be thread-safe and mut not
 * throw any exception or cause errors if they are called more than one time.
 * @author mangelp
 */
public interface ShutdownManager extends Runnable {

}
