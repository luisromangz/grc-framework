/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons;

import com.greenriver.commons.os.OsService;

/**
 *
 */
public class FakeOsService extends OsService {

	@Override
	protected void work() {
	}

	@Override
	public synchronized void stop() {
		super.stop();
	}

	@Override
	protected void cleanup() {
		super.cleanup();
	}

	@Override
	public synchronized void shutdown() {
		super.shutdown();
	}

	@Override
	protected void init() {
		super.init();
	}
}
