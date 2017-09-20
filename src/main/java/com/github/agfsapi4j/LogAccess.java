package com.github.agfsapi4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

class LogAccess
{
	private static final Random random = new Random();

	private File logFile = new File("/tmp/gfapi4j-" + Long.toHexString(System.currentTimeMillis()) + "-" + Long.toHexString(random.nextLong()) + ".log");
	private long prevLogLength = 0L;

	public String getLogMessages()
	{
		if (this.logFile != null && this.logFile.isFile())
		{
			return readLog();
		}
		else
		{
			return "(No log information available, sorry.)";
		}
	}

	private String readLog()
	{
		try (FileInputStream in = new FileInputStream(this.logFile);)
		{
			StringBuilder buf = new StringBuilder();
			in.skip(this.prevLogLength);
			char[] cbuf = new char[1024];
			Reader rd = new InputStreamReader(in);
			int count;
			while ((count = rd.read(cbuf)) != -1)
			{
				buf.append(cbuf, 0, count);
			}

			return buf.toString();
		}
		catch (IOException ex)
		{
			return ex.getMessage();
		}
	}

	public void beforeOp()
	{
		updateLogFilePos();
	}

	public void afterOp()
	{
		updateLogFilePos();
	}

	public String getLogFilePath()
	{
		return logFile.getAbsolutePath();
	}

	private void updateLogFilePos()
	{
		if (this.logFile != null && this.logFile.isFile())
		{
			this.prevLogLength = this.logFile.length();
		}
	}

	public void close(boolean delete)
	{
		if (delete && this.logFile != null && this.logFile.isFile())
		{
			this.logFile.delete();
			this.logFile = null;
		}
	}
}
