package com.github.agfsapi4j;

public interface GlusterFsDirectoryIndex
{
	boolean next();

	String getName();

	boolean isRegularFile();

	boolean isDirectory();

	boolean isSymbolicLink();

	void close();
}
