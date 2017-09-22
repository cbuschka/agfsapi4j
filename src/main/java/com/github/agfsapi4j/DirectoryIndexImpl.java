package com.github.agfsapi4j;

import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class DirectoryIndexImpl implements GlusterFsDirectoryIndex, Resource
{
	private GlusterFsSession session;
	private LibGfapi lib;
	private LogAccess logAccess;
	private ResourceTracker resourceTracker;
	private Pointer glFsFilePtr;
	private DirEntry entry;
	private byte[] statsBuf = new byte[512];
	private byte[] direntBuf = new byte[512];
	private byte[] resultBuf = new byte[512];

	public DirectoryIndexImpl(GlusterFsSession session, LibGfapi lib, LogAccess logAccess, ResourceTracker resourceTracker, Pointer glFsFilePtr)
	{
		this.session = session;
		this.lib = lib;
		this.logAccess = logAccess;
		this.resourceTracker = resourceTracker;
		this.glFsFilePtr = glFsFilePtr;

		resourceTracker.allocated(this);
	}

	public boolean next()
	{
		if (this.glFsFilePtr != null)
		{
			readNextEntry();
		}

		return entry != null;
	}

	private void readNextEntry()
	{
		try
		{
			this.logAccess.beforeOp();

			int result = this.lib.glfs_readdirplus_r(this.glFsFilePtr, statsBuf, direntBuf, resultBuf);
			session.checkError(result, "glfs_readdirplus_r failed.");
			if (!isResultBufEmpty(resultBuf))
			{
				ByteBuffer direntByteBuffer = ByteBuffer.wrap(direntBuf);
				direntByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
				ByteBuffer statsByteBuffer = ByteBuffer.wrap(statsBuf);
				statsByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
				this.entry = new DirEntry(direntByteBuffer, statsByteBuffer);
			}
			else
			{
				this.entry = null;
			}
		}
		finally
		{
			this.logAccess.afterOp();
		}
	}

	public GlusterFsFileStats getStats()
	{
		checkEntry();

		return this.entry.getStats();
	}

	private boolean isResultBufEmpty(byte[] resultBuf)
	{
		return resultBuf[0] == 0 && resultBuf[1] == 0 && resultBuf[2] == 0 && resultBuf[3] == 0;
	}

	@Override
	public String getName()
	{
		checkEntry();

		return this.entry.getName();
	}

	private void checkEntry()
	{
		if (this.entry == null)
		{
			throw new IllegalStateException("No current entry.");
		}
	}

	@Override
	public boolean isRegularFile()
	{
		checkEntry();

		return this.entry.getStats().isRegularFile();
	}

	@Override
	public boolean isDirectory()
	{
		checkEntry();

		return this.entry.getStats().isDirectory();
	}

	@Override
	public boolean isSymbolicLink()
	{
		checkEntry();

		return this.entry.getStats().isSymbolicLink();
	}

	public void close()
	{
		if (this.glFsFilePtr != null)
		{
			this.entry = null;
			this.resultBuf = null;
			this.statsBuf = null;
			this.direntBuf = null;

			try
			{
				this.logAccess.beforeOp();

				int result = this.lib.glfs_closedir(this.glFsFilePtr);
				session.checkError(result, "glfs_closedir failed.");
			}
			finally
			{
				this.logAccess.afterOp();
			}

			resourceTracker.unallocated(this);
			this.glFsFilePtr = null;
		}
	}
}
