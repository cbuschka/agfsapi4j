package com.github.agfsapi4j;

import java.nio.ByteBuffer;

class DirEntry
{
	private GlusterFsFileStats stats;
	private GlusterFsSession session;
	private String name;

	DirEntry(GlusterFsSession session, ByteBuffer direntBuf, ByteBuffer statsBuf)
	{
		this.session = session;
		this.name = getStringFrom(19, direntBuf);
		this.stats = new FileStatsImpl(statsBuf);
	}

	public GlusterFsFileStats getStats()
	{
		return stats;
	}

	private String getStringFrom(int pos, ByteBuffer buf)
	{
		return new CStringReader(this.session.getMaxFileNameLength(), this.session.getCharSet()).readFrom(buf, pos);
	}

	public String getName()
	{
		return name;
	}
}
