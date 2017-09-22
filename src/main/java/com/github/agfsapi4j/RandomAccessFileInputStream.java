package com.github.agfsapi4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

class RandomAccessFileInputStream extends InputStream
{
	private final RandomAccessFile file;
	private final boolean closeFileOnClose;

	public RandomAccessFileInputStream(RandomAccessFile file, boolean closeFileOnClose)
	{
		this.file = file;
		this.closeFileOnClose = closeFileOnClose;
	}

	@Override
	public int read() throws IOException
	{
		return this.file.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return this.file.read(b, off, len);
	}

	@Override
	public void close() throws IOException
	{
		if (this.closeFileOnClose)
		{
			this.file.close();
		}
	}
}
