package com.github.agfsapi4j;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

public class GlusterFsFileSystemProvider extends FileSystemProvider
{
	public static final String SYS_PROP_USE_ALT_SCHEME = GlusterFsFileSystemProvider.class.getName() + ".useAltScheme";

	private static boolean useAltScheme()
	{
		return System.getProperty(SYS_PROP_USE_ALT_SCHEME, "false").equalsIgnoreCase("true");
	}

	private boolean useAltScheme = useAltScheme();

	public GlusterFsFileSystemProvider()
	{
	}

	@Override
	public String getScheme()
	{
		return useAltScheme ? "agfsapi4j" : "gluster";
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public FileSystem getFileSystem(URI uri)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getPath(URI uri)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributes) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void createDirectory(Path path, FileAttribute<?>... fileAttributes) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Path path) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void copy(Path path, Path path1, CopyOption... copyOptions) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void move(Path path, Path path1, CopyOption... copyOptions) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameFile(Path path, Path path1) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHidden(Path path) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkAccess(Path path, AccessMode... accessModes) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> aClass, LinkOption... linkOptions)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> aClass, LinkOption... linkOptions) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String s, LinkOption... linkOptions) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(Path path, String s, Object o, LinkOption... linkOptions) throws IOException
	{
		throw new UnsupportedOperationException();
	}
}
