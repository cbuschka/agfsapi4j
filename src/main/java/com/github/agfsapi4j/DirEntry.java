package com.github.agfsapi4j;

import java.nio.ByteBuffer;

class DirEntry
{
	private static final int MAX_FILE_NAME_LENGTH = 512;
	
	private GlusterFsFileStats stats;
	private String name;

	DirEntry(ByteBuffer direntBuf, ByteBuffer statsBuf)
	{
		this.name = getStringFrom(19, direntBuf);
		this.stats = new FileStatsImpl(statsBuf);
	}

	public GlusterFsFileStats getStats()
	{
		return stats;
	}

	private String getStringFrom(int pos, ByteBuffer buf)
	{
		return new CStringReader(MAX_FILE_NAME_LENGTH, "UTF-8").readFrom(buf, pos);
	}

	public String getName()
	{
		return name;
	}
}
