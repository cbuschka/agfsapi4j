package com.github.agfsapi4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;

class DirEntry
{
	private String name;

	DirEntry(ByteBuffer direntBuf, ByteBuffer statsBuf)
	{
		this.name = getStringFrom(19, direntBuf);
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


	public String getName()
	{
		return name;
	}
}
