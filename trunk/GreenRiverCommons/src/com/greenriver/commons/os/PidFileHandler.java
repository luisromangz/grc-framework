/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Handles pid file creation/destruction and locking onto it.
 */
public class PidFileHandler {

	private File pidFile;
	private int pid;
	private FileLock lock;
	private FileChannel channel;

	/**
	 * Gets the abstract file path for the pid file
	 * @return abstract file path for the pid file.
	 */
	public File getPidFile() {
		return pidFile;
	}

	/**
	 * 
	 * @param pidFile
	 * @param pid
	 */
	public PidFileHandler(File pidFile, int pid) {
		if (pidFile == null) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		if (pid < 1) {
			throw new IllegalArgumentException("Invalid pid number.");
		}

		this.pid = pid;
		this.pidFile = pidFile;
	}

	/**
	 * Performs the creation of the pid file. A lock is used to check for
	 * exclusive use of the pid file ensuring that after a forced shutdown a
	 * stale pid file will not prevent the application to run.
	 *
	 * @throws IOException If the file can't be opened or if another app
	 * already got the lock over the file.
	 */
	public void create() throws IOException {
		//- If the pid file exists we need to check the lock, if the lock
		//exists there is an existing app running. If the lock doesn't exists
		//we can lock it and use the existing file.
		//- If the pid file doesn't exists we need to create it and get the
		//lock.

		if (lock != null) {
			throw new IllegalStateException("Handler create have been called twice!");
		}

		RandomAccessFile rafile = null;

		if (!pidFile.exists() &&
				!OsEnvironment.get().createFile(pidFile, false)) {
			throw new IOException("Can't create pid file " + pidFile.getPath());
		}

		try {
			//get the lock over the file
			rafile = new RandomAccessFile(pidFile, "rw");
			channel = rafile.getChannel();
			lock = channel.tryLock();
		} catch (FileNotFoundException ex) {

		} catch (IOException ex) {
			throw ex;
		} catch (OverlappingFileLockException ex) {
			//this means that the file is locked. We do nothing with this
			//exception.
		}

		if (!isValid()) {
			if (channel != null) {
				channel.close();
			}
		} else {
			rafile.writeBytes("" + pid + "");
			channel.force(false);
		}
	}

	/**
	 * Gets if this handler has adquired a valid lock over the pid file and thus
	 * the service can run. If this returns false the service must stop as
	 * another one is running.
	 *
	 * @return true if the pid file is valid or false if not.
	 */
	public boolean isValid() {
		return lock != null && channel != null && lock.isValid();
	}

	/**
	 * Releases and removes the pid file
	 */
	public void release() {
		if (lock != null && lock.isValid()) {
			try {
				lock.release();
			} catch (IOException ex) {

			}

			//Only if the lock is still valid we can remove this file.
			if (pidFile != null && pidFile.exists()) {
				pidFile.delete();
			}
		}

		lock = null;

		pidFile = null;
	}

	@Override
	protected void finalize() throws Throwable {
		release();
	}
}
