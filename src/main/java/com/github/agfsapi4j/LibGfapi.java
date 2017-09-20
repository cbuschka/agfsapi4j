package com.github.agfsapi4j;

import com.sun.jna.Library;
import com.sun.jna.Native;

// see https://github.com/gluster/glusterfs/blob/master/api/src/glfs.h
public interface LibGfapi extends Library
{
	LibGfapi INSTANCE = (LibGfapi) Native.loadLibrary("gfapi", LibGfapi.class);

	long glfs_new(String volName);

	int glfs_set_logging(long glfsPtr, String logfilePath, int logLevel);

	int glfs_init(long glfsPtr);

	int glfs_set_volfile(long glfsPtr, String volFile);

	int glfs_set_volfile_server(long glfsPtr, String transport, String server, int port);

	long glfs_creat(long glfsPtr, String path, int openFlags, int mode);

	long glfs_open(long glfsPtr, String path, int openFlags);

	int glfs_fini(long glfsPtr);

	int glfs_close(long glFsFdPtr);

	long glfs_getcwd(long glFsPtr, byte[] buf, int length);

	int glfs_write(long glFsFdPtr, byte[] buf, int length, int flags);

	int glfs_read(long glFsFdPtr, byte[] buf, int length, int flags);
}
