package com.github.agfsapi4j;

import java.nio.ByteBuffer;

class FileStatsImpl implements GlusterFsFileStats
{
	private static final int MASK_FOLDER = 0x00004000;
	private static final int MASK_MODE_ONLY = 0x00007fff;
	private static final int MASK_SYMBOLIC_LINK = 0x0000a000;
	private static final int MASK_REGULAR_FILE = 0x00008000;

	private final int uid;
	private final int gid;
	private final int size;
	private final int mode;

	FileStatsImpl(ByteBuffer buf)
	{
		this.mode = (0x7fffffff & buf.getInt(24));
		this.uid = buf.getShort(28);
		this.gid = buf.getShort(32);
		this.size = buf.getInt(48);
	}

	@Override
	public boolean isRegularFile()
	{
		int mask = MASK_REGULAR_FILE;
		return (mask & mode) == mask;
	}

	@Override
	public boolean isSymbolicLink()
	{
		int mask = MASK_SYMBOLIC_LINK;
		return (mask & mode) == mask;
	}

	@Override
	public boolean isDirectory()
	{
		int mask = MASK_FOLDER;
		return (mask & mode) == mask;
	}

	public int getMode()
	{
		return MASK_MODE_ONLY & mode;
	}

	public int getGid()
	{
		return gid;
	}

	public int getUid()
	{
		return uid;
	}

	public int getSize()
	{
		return size;
	}
}
