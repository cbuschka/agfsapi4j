package com.github.agfsapi4j;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class GlusterFsFileSystem extends FileSystem
{
	private GlusterFsFileSystemProvider provider;

	@Override
	public FileSystemProvider provider()
	{
		return provider;
	}

	@Override
	public void close() throws IOException
	{

	}

	@Override
	public boolean isOpen()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSeparator()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Path> getRootDirectories()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<FileStore> getFileStores()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> supportedFileAttributeViews()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getPath(String s, String... strings)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public PathMatcher getPathMatcher(String s)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchService newWatchService() throws IOException
	{
		throw new UnsupportedOperationException();
	}
}
