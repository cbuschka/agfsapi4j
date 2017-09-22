package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class LogAccess
{
	private static Logger log = LoggerFactory.getLogger(LogAccess.class);

	private static final Random random = new Random();

	private GlusterFsSession session;

	private File logFile = new File("/tmp/gfapi4j-" + Long.toHexString(System.currentTimeMillis()) + "-" + Long.toHexString(random.nextLong()) + ".log");
	private long prevLogLength = 0L;

	public LogAccess(GlusterFsSession session)
	{
		this.session = session;
	}

	public List<String> getLogMessages()
	{
		if (isLogFileAvailable())
		{
			return readLog();
		}
		else
		{
			return Collections.emptyList();
		}
	}

	private List<String> readLog()
	{
		try (RandomAccessFile f = new RandomAccessFile(this.logFile, "r");)
		{
			f.seek(this.prevLogLength);

			return readLines(f);
		}
		catch (IOException ex)
		{
			log.error("Error reading log file.", ex);

			return Collections.emptyList();
		}
	}

	private List<String> readLines(RandomAccessFile f) throws IOException
	{
		BufferedReader bufRd = new BufferedReader(new InputStreamReader(new RandomAccessFileInputStream(f, false), session.getCharSet()));
		List<String> lines = new ArrayList<>();
		String line;
		while ((line = bufRd.readLine()) != null)
		{
			lines.add(line);
		}

		return lines;
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
