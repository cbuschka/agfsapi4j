package com.github.agfsapi4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlusterFsFileStats
{
	private final int uid;
	private final int gid;
	private final int size;

	GlusterFsFileStats(byte[] bbuf) throws IOException
	{
		ByteBuffer buf = ByteBuffer.wrap(bbuf);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		this.size = buf.getInt(48);
		this.uid = buf.getShort(28);
		this.gid = buf.getShort(32);
		/*
		struct stat {
		dev_t st_dev;
		ino_t st_ino;
		mode_t st_mode;
		nlink_t st_nlink;
		uid_t st_uid;
		gid_t st_gid;
		dev_t st_rdev;
		off_t st_size;
		time_t st_atime;
		time_t st_mtime;
		time_t st_ctime;
		blksize_t st_blksize;
		blkcnt_t st_blocks;
		mode_t st_attr;
	};
	*/
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
