package com.github.agfsapi4j;

public class GlusterFsFile implements Resource
{
	private GlusterFsSession session;
	private LibGfapi lib;
	private LogAccess logAccess;
	private ResourceTracker resourceTracker;
	private long glFsFdPtr;

	GlusterFsFile(GlusterFsSession session, LibGfapi lib, LogAccess logAccess, ResourceTracker resourceTracker, long glFsFdPtr)
	{
		this.session = session;
		this.lib = lib;
		this.logAccess = logAccess;
		this.resourceTracker = resourceTracker;
		this.glFsFdPtr = glFsFdPtr;

		this.resourceTracker.allocated(this);
	}

	public void close()
	{
		if (this.glFsFdPtr != 0)
		{
			this.lib.glfs_close(this.glFsFdPtr);
			this.resourceTracker.unallocated(this);
			this.glFsFdPtr = 0;
			this.session = null;
		}
	}

	public void write(byte[] buf)
	{
		write(buf, 0, buf.length);
	}

	public void write(byte[] buf, int offset, int count)
	{
		checkNotClosed();

		try
		{
			this.logAccess.beforeOp();

			int result;
			if (offset != 0)
			{
				byte[] newBuf = new byte[count];
				System.arraycopy(newBuf, 0, buf, offset, count);
				result = this.lib.glfs_write(this.glFsFdPtr, newBuf, count, 0);
			}
			else
			{
				result = this.lib.glfs_write(this.glFsFdPtr, buf, count, 0);
			}

			if (result < 0 || result != count)
			{
				session.raiseError(String.format("glfs_write failed (result=%d, count=%d).", result, count));
			}
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	private void checkNotClosed()
	{
		if (this.glFsFdPtr == 0)
		{
			throw new IllegalStateException("Closed.");
		}
	}

	public int read(byte[] buf)
	{
		checkNotClosed();

		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_read(this.glFsFdPtr, buf, buf.length, 0);
			if (result < 0)
			{
				session.raiseError(String.format("glfs_read failed (result=%d).", result));
			}

			return result;
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}
}
