package com.github.agfsapi4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlusterFsFileStats
{
	private final int uid;
	private final int gid;
	private final int size;
	private final int mode;

	/*
	struct stat {
        unsigned long   st_dev;         /* Device.
        unsigned long   st_ino;         /* File serial number.
        24 unsigned int    st_mode;        /* File mode.
        unsigned int    st_nlink;       /* Link count.
        28 unsigned int    st_uid;         /* User ID of the file's owner.
        32 unsigned int    st_gid;         /* Group ID of the file's group.
        unsigned long   st_rdev;        /* Device number, if device.
        unsigned long   __pad1;
        long            st_size;        /* Size of file, in bytes.
        int             st_blksize;     /* Optimal block size for I/O.
        int             __pad2;
        long            st_blocks;      /* Number 512-byte blocks allocated.
        long            st_atime;       /* Time of last access.
        unsigned long   st_atime_nsec;
        long            st_mtime;       /* Time of last modification.
        unsigned long   st_mtime_nsec;
        long            st_ctime;       /* Time of last status change.
        unsigned long   st_ctime_nsec;
        unsigned int    __unused4;
        unsigned int    __unused5;
    };
*/

	GlusterFsFileStats(ByteBuffer buf) throws IOException
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
