package com.github.agfsapi4j;

import java.nio.ByteBuffer;

class FileStatsImpl implements GlusterFsFileStats
{
	private final int uid;
	private final int gid;
	private final int size;
	private final int mode;

	FileStatsImpl(ByteBuffer buf)
	{
		this.mode = (0x00007fff & buf.getInt(24));
		this.uid = buf.getShort(28);
		this.gid = buf.getShort(32);
		this.size = buf.getInt(48);
	}

	public int getMode()
	{
		return mode;
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
