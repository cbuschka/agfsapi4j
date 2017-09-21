package com.github.agfsapi4j;

import com.sun.jna.IntegerType;

public class Mode extends IntegerType
{
	public Mode()
	{
		super(4, true);
	}

	public Mode(long value)
	{
		super(4, value, true);
	}

	public Mode(int value)
	{
		super(4, value, true);
	}

	public static Mode valueOf(int value)
	{
		return new Mode(value);
	}
}
