package com.github.agfsapi4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;

class DirEntry
{
	private static final int DT_DIR = 4;
	private static final int DT_LNK = 10;
	private static final int DT_REG = 8;

	private GlusterFsFileStats stats;
	private String name;
	private int type;


	DirEntry(ByteBuffer direntBuf, ByteBuffer statsBuf)
	{
		this.name = getStringFrom(19, direntBuf);
		this.type = direntBuf.get(18);

		this.stats = new GlusterFsFileStats(statsBuf);
	}

	public GlusterFsFileStats getStats()
	{
		return stats;
	}

	private String getStringFrom(int pos, ByteBuffer buf)
	{
		try
		{
			Reader rd = new InputStreamReader(new InputStream()
			{
				private int off = 0;

				@Override
				public int read() throws IOException
				{
					int b = buf.get(pos + off);
					if (b == 0)
					{
						return -1;
					}

					off++;
					return b;
				}
			}, "UTF-8");
			char[] cbuf = new char[512];
			int len = rd.read(cbuf);
			return new String(cbuf, 0, len);
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	public boolean isRegularFile()
	{
		return this.type == DT_REG;
	}

	public boolean isDirectory()
	{
		return this.type == DT_DIR;
	}


	public boolean isSymbolicLink()
	{
		return this.type == DT_LNK;
	}


	public String getName()
	{
		return name;
	}
}
