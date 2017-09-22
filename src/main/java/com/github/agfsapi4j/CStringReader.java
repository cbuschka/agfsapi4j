package com.github.agfsapi4j;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;

public class CStringReader
{
	private int limit;
	private final String charSet;

	public CStringReader(int limit, String charSet)
	{
		this.limit = limit;
		this.charSet = charSet;
	}

	public String readFrom(ByteBuffer buf, int offset)
	{
		try
		{
			byte[] bbuf = new byte[512];
			int i;
			for (i = 0; true; ++i)
			{
				int c = buf.get(offset + i);
				if (c == 0)
				{
					break;
				}

				if (i >= limit)
				{
					throw new IllegalStateException("Limit exceeded.");
				}

				if (i >= bbuf.length)
				{
					byte[] newBbuf = new byte[bbuf.length + 1024];
					System.arraycopy(newBbuf, 0, bbuf, 0, bbuf.length);
					bbuf = newBbuf;
				}

				bbuf[i] = (byte) c;
			}

			return new String(bbuf, 0, i, charSet);
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}
}
