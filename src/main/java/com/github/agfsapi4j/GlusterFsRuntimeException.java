package com.github.agfsapi4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlusterFsRuntimeException extends RuntimeException
{
	private final List<String> logMessages;

	public GlusterFsRuntimeException(String message, List<String> logMessages)
	{
		super(logMessages.isEmpty() ? message + "; " + "No log information available, sorry.)" :
				message + "; " + logMessages.get(logMessages.size() - 1));

		this.logMessages = Collections.unmodifiableList(new ArrayList<>(logMessages));
	}

	public List<String> getLogMessages()
	{
		return logMessages;
	}
}
