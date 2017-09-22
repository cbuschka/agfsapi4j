package com.github.agfsapi4j;

import com.sun.jna.Native;

@SuppressWarnings({"squid:S1191"})
class LibGfapiProvider
{
	private static LibGfapi nativeLib;

	private static ThreadLocal<LibGfapi> threadLocalLib = new ThreadLocal<>();

	public static void override(LibGfapi lib)
	{
		threadLocalLib.set(lib);
	}

	public static LibGfapi get()
	{
		LibGfapi lib = threadLocalLib.get();
		if (lib == null)
		{
			lib = getNativeLib();
		}

		return lib;
	}

	private static synchronized LibGfapi getNativeLib()
	{
		if (nativeLib == null)
		{
			nativeLib = Native.loadLibrary("gfapi", LibGfapi.class);
		}

		return nativeLib;
	}

	public static void clearOverride()
	{
		threadLocalLib.remove();
	}
}
