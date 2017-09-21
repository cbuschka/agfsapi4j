package com.github.agfsapi4j;

public interface GlusterFsDirectoryIndex
{
	boolean next();

	String getName();

	void close();
}
