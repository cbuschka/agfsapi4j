package com.github.agfsapi4j;

import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlusterFsSession implements Closeable
{
	private static final int DEFAULT_LOG_LEVEL = 10;

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

	public String cwd()
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

	public void chown(String path, int userId, int groupId)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			int result = lib.glfs_chown(this.glFsPtr, path, userId, groupId);
			checkError(result, "glfs_chown failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsFile create(String path, int flags, int mode)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			long glFsFdPtr = lib.glfs_creat(this.glFsPtr, path, (short) flags, Mode.valueOf(mode));
			checkPtr(glFsFdPtr, "glfs_create failed.");

			return new GlusterFsFile(this, this.lib, this.logAccess, this.resourceTracker, glFsFdPtr);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsFile open(String path, int flags)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			long glFsFdPtr = lib.glfs_open(this.glFsPtr, path, (short) flags);
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

	void checkPtr(Pointer ptr, String message)
	{
		if (ptr == Pointer.NULL)
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

	public GlusterFsFileStats stat(String path)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			byte[] bbuf = new byte[512];
			int result = this.lib.glfs_stat(this.glFsPtr, path, bbuf);
			checkError(result, "glfs_stat failed.");

			ByteBuffer buf = ByteBuffer.wrap(bbuf);
			buf.order(ByteOrder.LITTLE_ENDIAN);

			return new FileStatsImpl(buf);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void symlink(String targetPath, String sourcePath)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_symlink(this.glFsPtr, targetPath, sourcePath);
			checkError(result, "glfs_symlink failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void rename(String oldPath, String newPath)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			int result = this.lib.glfs_rename(this.glFsPtr, oldPath, newPath);
			checkError(result, "glfs_rename failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void mkdir(String path, int mode)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_mkdir(this.glFsPtr, path, Mode.valueOf(mode));
			checkError(result, "glfs_mkdir failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void rmdir(String path)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_rmdir(this.glFsPtr, path);
			checkError(result, "glfs_rmdir failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void unlink(String path)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();
			int result = this.lib.glfs_unlink(this.glFsPtr, path);
			checkError(result, "glfs_unlink failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void truncate(String path)
	{
		truncate(path, 0);
	}

	public void truncate(String path, int offset)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_truncate(this.glFsPtr, path, offset);
			checkError(result, "glfs_truncate failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public void chdir(String path)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_chdir(this.glFsPtr, path);
			checkError(result, "glfs_chdir failed.");
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsDirectoryIndex opendir(String path)
	{
		checkConnected();

		try
		{
			this.logAccess.beforeOp();

			Pointer result = this.lib.glfs_opendir(this.glFsPtr, path);
			checkPtr(result, "glfs_opendir failed.");

			return new DirectoryIndexImpl(this, this.lib,
					this.logAccess, this.resourceTracker, result);
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}
}
