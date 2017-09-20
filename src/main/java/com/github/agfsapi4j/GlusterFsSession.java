package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GlusterFsSession implements Closeable
{
	private static final Random random = new Random();

	private static final int DEFAULT_LOG_LEVEL = 6;

	private Logger log = LoggerFactory.getLogger(GlusterFsSession.class);

	LibGfapi lib = LibGfapi.INSTANCE;

	private String hostName;
	private int port;
	private String volName;

	private Throwable connectStackTrace;
	private Set<GlusterFsSessionObject> allocatedObjects = new HashSet<>();
	private File logFile;
	private long prevLogLength = 0L;
	private long glFsPtr;

	GlusterFsSession(String hostName, int port, String volName)
	{
		this.hostName = hostName;
		this.port = port;
		this.volName = volName;

		connect();
	}

	private void connect()
	{
		this.connectStackTrace = new Throwable("Stack trace of allocation");
		this.connectStackTrace.fillInStackTrace();

		this.glFsPtr = lib.glfs_new(volName);
		checkPtr(this.glFsPtr, "glfs_new failed (0).", false);

		this.logFile = new File("/tmp/gfapi4j-" + Long.toHexString(System.currentTimeMillis()) + "-" + Long.toHexString(random.nextLong()) + ".log");
		int error = lib.glfs_set_logging(glFsPtr, (logFile.getAbsolutePath()), DEFAULT_LOG_LEVEL);
		checkError(error, "glfs_set_logging failed (%d).", false);

		try
		{
			beforeOp();
			error = lib.glfs_set_volfile(glFsPtr, volName);
			checkError(error, "glfs_set_volfile failed (%d).", true);
		}
		finally
		{
			afterOp();
		}

		try
		{
			beforeOp();
			error = lib.glfs_set_volfile_server(glFsPtr, "tcp", hostName, port);
			checkError(error, "glfs_set_volfile_server failed (%d).", true);
		}
		finally
		{
			afterOp();
		}

		try
		{
			beforeOp();
			error = lib.glfs_init(glFsPtr);
			checkError(error, "glfs_init failed (%d).", true);
		}
		finally
		{
			afterOp();
		}
	}

	public String getCwd()
	{
		try
		{
			beforeOp();
			byte[] cbuf = new byte[4096];
			long ptr = lib.glfs_getcwd(glFsPtr, cbuf, cbuf.length);
			checkPtr(ptr, "glfs_getcwd failed.", true);
			int zeroPos;
			for (zeroPos = 0; zeroPos < cbuf.length && cbuf[zeroPos] != 0; ++zeroPos) ;
			return new String(cbuf, 0, zeroPos);
		}
		finally
		{
			afterOp();
		}
	}

	public GlusterFsFile createFile(String path, int flags, int mode)
	{
		checkConnected();

		try
		{
			beforeOp();
			long glFsFdPtr = lib.glfs_creat(this.glFsPtr, path, flags, mode);
			checkPtr(glFsFdPtr, "glfs_create failed.", true);

			return new GlusterFsFile(this, glFsFdPtr);
		}
		finally
		{
			afterOp();
		}
	}

	public GlusterFsFile openFile(String path, int flags)
	{
		checkConnected();

		try
		{
			beforeOp();
			long glFsFdPtr = lib.glfs_open(this.glFsPtr, path, flags);
			checkPtr(glFsFdPtr, "glfs_open failed.", true);

			return new GlusterFsFile(this, glFsFdPtr);
		}
		finally
		{
			afterOp();
		}
	}

	void afterOp()
	{
		updateLogFilePos();
	}

	void beforeOp()
	{
		updateLogFilePos();
	}

	private void updateLogFilePos()
	{
		if (this.logFile != null)
		{
			this.prevLogLength = this.logFile.length();
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

		super.finalize();
	}

	public void close()
	{
		for (GlusterFsSessionObject object : this.allocatedObjects)
		{
			object.close();
		}

		if (glFsPtr != 0)
		{
			lib.glfs_fini(glFsPtr);
			glFsPtr = 0;
		}

		if (this.logFile != null && this.logFile.isFile())
		{
			this.logFile.delete();
			this.logFile = null;
		}
	}

	private void checkConnected()
	{
		checkPtr(this.glFsPtr, "Session closed.", false);
	}

	void checkPtr(long ptr, String message, boolean readLog)
	{
		if (ptr == 0)
		{
			raiseError(message, readLog);
		}
	}

	void checkError(int error, String msg, boolean readLog)
	{
		if (error != 0)
		{
			raiseError(String.format(msg, error), readLog);
		}
	}

	void raiseError(String message, boolean readLog)
	{
		String errors = "(No log information available, sorry.)";
		if (readLog && this.logFile != null && this.logFile.isFile())
		{
			errors = readLog();
		}

		throw new IllegalStateException(message + "\n" + errors);
	}

	private String readLog()
	{
		try (FileInputStream in = new FileInputStream(this.logFile);)
		{
			StringBuilder buf = new StringBuilder();
			in.skip(this.prevLogLength);
			char[] cbuf = new char[1024];
			Reader rd = new InputStreamReader(in);
			int count;
			while ((count = rd.read(cbuf)) != -1)
			{
				buf.append(cbuf, 0, count);
			}

			return buf.toString();
		}
		catch (IOException ex)
		{
			return ex.getMessage();
		}
	}

	void allocated(GlusterFsSessionObject object)
	{
		this.allocatedObjects.add(object);
	}

	void freed(GlusterFsSessionObject object)
	{
		this.allocatedObjects.remove(object);
	}
}
