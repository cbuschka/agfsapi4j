package com.github.agfsapi4j;

public class GlusterFsFile implements GlusterFsSessionObject
{
	private GlusterFsSession session;
	private long glFsFdPtr;

	GlusterFsFile(GlusterFsSession session, long glFsFdPtr)
	{
		this.session = session;
		this.glFsFdPtr = glFsFdPtr;

		this.session.allocated(this);
	}

	public void close()
	{
		if (this.glFsFdPtr != 0)
		{
			this.session.lib.glfs_close(this.glFsFdPtr);
			this.session.freed(this);
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
			session.beforeOp();

			int result;
			if (offset != 0)
			{
				byte[] newBuf = new byte[count];
				System.arraycopy(newBuf, 0, buf, offset, count);
				result = this.session.lib.glfs_write(this.glFsFdPtr, newBuf, count, 0);
			}
			else
			{
				result = this.session.lib.glfs_write(this.glFsFdPtr, buf, count, 0);
			}

			if (result < 0 || result != count)
			{
				session.raiseError(String.format("glfs_write failed (result=%d, count=%d).", result, count));
			}
		}
		finally
		{
			session.logAccess.afterOp();
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
			session.beforeOp();

			int result = this.session.lib.glfs_read(this.glFsFdPtr, buf, buf.length, 0);
			if (result < 0)
			{
				session.raiseError(String.format("glfs_read failed (result=%d).", result));
			}

			return result;
		}
		finally
		{
			session.logAccess.afterOp();
		}
	}
}
