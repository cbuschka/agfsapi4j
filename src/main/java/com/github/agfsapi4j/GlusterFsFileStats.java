package com.github.agfsapi4j;

public interface GlusterFsFileStats
{
	boolean isRegularFile();

	boolean isSymbolicLink();

	boolean isDirectory();

	int getMode();

	int getGid();

	int getUid();

	int getSize();
}
