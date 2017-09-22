package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Random;

class LogAccess
{
	private static Logger log = LoggerFactory.getLogger(LogAccess.class);

	private static final Random random = new Random();

	private File logFile = new File("/tmp/gfapi4j-" + Long.toHexString(System.currentTimeMillis()) + "-" + Long.toHexString(random.nextLong()) + ".log");
	private long prevLogLength = 0L;

	public String getLogMessages()
	{
		if (isLogFileAvailable())
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
			seekToPrevPosition(in);
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

	private void seekToPrevPosition(FileInputStream in) throws IOException
	{
		int len = 0;
		while ((len += in.skip(this.prevLogLength - len)) < this.prevLogLength)
		{
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
		if (isLogFileAvailable())
		{
			this.prevLogLength = this.logFile.length();
		}
	}

	public void close(boolean delete)
	{
		if (delete && isLogFileAvailable())
		{
			try
			{
				Files.delete(this.logFile.toPath());
				this.logFile = null;
			}
			catch (IOException ex)
			{
				log.warn("Deleting log file {} failed.", this.logFile, ex);
			}
		}
	}

	private boolean isLogFileAvailable()
	{
		return this.logFile != null && this.logFile.isFile();
	}
}
