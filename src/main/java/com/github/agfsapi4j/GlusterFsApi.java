package com.github.agfsapi4j;

public class GlusterFsApi
{
	public static final int DEFAULT_PORT = 24007;

	static final int DEFAULT_MAX_PATH_LENGTH = 2048;
	static final int DEFAULT_MAX_FILE_NAME_LENGTH = 512;
	static final String DEFAULT_CHAR_SET = "UTF-8";

	public static final int O_RDONLY = 0x0000;
	public static final int O_WRONLY = 0x0001;
	public static final int O_RDWR = 0x0002;
	public static final int O_CREAT = 0x0100;
	public static final int O_EXCL = 0x0200;
	public static final int O_TRUNC = 0x0800;
	public static final int O_APPEND = 0x1000;

	public static GlusterFsApi newInstance()
	{
		return new GlusterFsApi();
	}

	GlusterFsApi()
	{
	}

	public GlusterFsSession connect(String hostName, int port, String volName)
	{
		GlusterFsSession session = new GlusterFsSession();
		session.connect(hostName, port, volName);
		return session;
	}
}
