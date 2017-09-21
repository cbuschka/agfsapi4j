package com.github.agfsapi4j;

import com.sun.jna.Library;

// see https://github.com/gluster/glusterfs/blob/master/api/src/glfs.h
public interface LibGfapi extends Library
{
	long glfs_new(String volName);

	int glfs_set_logging(long glfsPtr, String logfilePath, int logLevel);

	int glfs_init(long glfsPtr);

	int glfs_set_volfile(long glfsPtr, String volFile);

	int glfs_set_volfile_server(long glfsPtr, String transport, String server, int port);

	long glfs_creat(long glfsPtr, String path, int openFlags, Mode mode);

	long glfs_open(long glfsPtr, String path, int openFlags);

	int glfs_fini(long glfsPtr);

	int glfs_close(long glFsFdPtr);

	long glfs_getcwd(long glFsPtr, byte[] buf, int length);

	int glfs_chdir(long glFsPtr, String path);

	int glfs_write(long glFsFdPtr, byte[] buf, int length, int flags);

	int glfs_read(long glFsFdPtr, byte[] buf, int length, int flags);

	int glfs_chown(long glFsPtr, String path, int uid, int gid);

	int glfs_truncate(long glFsFdPtr, String path, int offset);

	int glfs_mkdir(long glFsPtr, String path, Mode mode);

	int glfs_rmdir(long glFsPtr, String path);

	int glfs_rename(long glFsPtr, String oldPath, String newPath);

	int glfs_unlink(long glFsPtr, String path);

	int glfs_symlink(long glFsPtr, String targetPath, String sourcePath);

	int glfs_stat(long glFsPtr, String path, byte[] statBuf);
}
