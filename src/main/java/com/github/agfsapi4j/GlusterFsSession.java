package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

public class GlusterFsSession implements Closeable
{
	private static final int DEFAULT_LOG_LEVEL = 6;

	private Logger log = LoggerFactory.getLogger(GlusterFsSession.class);

	private LibGfapi lib = LibGfapiProvider.get();
	private LogAccess logAccess = new LogAccess();
	private ResourceTracker resourceTracker = new ResourceTracker();

	private Throwable connectStackTrace;
	long glFsPtr;

	GlusterFsSession()
	{
	}

	void connect(String hostName, int port, String volName)
	{
		this.connectStackTrace = new Throwable("Stack trace of allocation");
		this.connectStackTrace.fillInStackTrace();

		this.glFsPtr = lib.glfs_new(volName);
		checkPtr(this.glFsPtr, "glfs_new failed (0).");

		int error = lib.glfs_set_logging(glFsPtr, logAccess.getLogFilePath(), DEFAULT_LOG_LEVEL);
		checkError(error, "glfs_set_logging failed (%d).");

		try
		{
			this.logAccess.beforeOp();
			error = lib.glfs_set_volfile(glFsPtr, volName);
			checkError(error, "glfs_set_volfile failed (%d).");
		}
		finally
		{
			this.logAccess.afterOp();
		}

		try
		{
			this.logAccess.beforeOp();
			error = lib.glfs_set_volfile_server(glFsPtr, "tcp", hostName, port);
			checkError(error, "glfs_set_volfile_server failed (%d).");
		}
		finally
		{
			this.logAccess.afterOp();
		}

		try
		{
			this.logAccess.beforeOp();
			error = lib.glfs_init(glFsPtr);
			checkError(error, "glfs_init failed (%d).");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public String getCwd()
	{
		try
		{
			this.logAccess.beforeOp();
			byte[] cbuf = new byte[4096];
			long ptr = lib.glfs_getcwd(glFsPtr, cbuf, cbuf.length);
			checkPtr(ptr, "glfs_getcwd failed.");
			int zeroPos;
			for (zeroPos = 0; zeroPos < cbuf.length && cbuf[zeroPos] != 0; ++zeroPos) ;
			return new String(cbuf, 0, zeroPos);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsFile createFile(String path, int flags, int mode)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			long glFsFdPtr = lib.glfs_creat(this.glFsPtr, path, flags, mode);
			checkPtr(glFsFdPtr, "glfs_create failed.");

			return new GlusterFsFile(this, this.lib, this.logAccess, this.resourceTracker, glFsFdPtr);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsFile openFile(String path, int flags)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			long glFsFdPtr = lib.glfs_open(this.glFsPtr, path, flags);
			checkPtr(glFsFdPtr, "glfs_open failed.");

			return new GlusterFsFile(this, this.lib, this.logAccess, this.resourceTracker, glFsFdPtr);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	private boolean isClosed()
	{
		return this.glFsPtr == 0;
	}

	@Override
	protected void finalize() throws Throwable
	{
		if (!isClosed())
		{
			closeOnFinalize();
		}

		super.finalize();
	}

	private void closeOnFinalize()
	{
		try
		{
			log.warn("Session has not been closed.", this.connectStackTrace);

			close();
		}
		catch (Exception ex)
		{
			log.warn("Error while closing session in finalize.", ex);
		}
	}

	public void close()
	{
		this.resourceTracker.closeResources();

		if (glFsPtr != 0)
		{
			lib.glfs_fini(glFsPtr);
			glFsPtr = 0;
		}

		this.logAccess.close(true);
	}

	private void checkConnected()
	{
		checkPtr(this.glFsPtr, "Session closed.");
	}

	void checkPtr(long ptr, String message)
	{
		if (ptr == 0)
		{
			raiseError(message);
		}
	}

	void checkError(int error, String msg)
	{
		if (error != 0)
		{
			raiseError(String.format(msg, error));
		}
	}

	void raiseError(String message)
	{
		String errors = this.logAccess.getLogMessages();
		throw new IllegalStateException(message + "\n" + errors);
	}
}
