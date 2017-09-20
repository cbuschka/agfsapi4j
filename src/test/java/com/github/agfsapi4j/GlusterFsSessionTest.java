package com.github.agfsapi4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlusterFsSessionTest
{
	private static final long CONNECTED_SESSION_ID = 1;

	private static final long OPENED_FILE_ID = 2;

	@InjectMocks
	private GlusterFsSession session;

	@Mock
	private LogAccess logAccess;

	@Mock
	private ResourceTracker resourceTracker;

	@Mock
	private LibGfapi lib;

	@Test
	public void openedFileIsTracked()
	{
		givenAConnectedSession();
		givenOpenSucceeds("aPath");

		GlusterFsFile file = session.openFile("aPath", 0);

		verify(resourceTracker).allocated(file);
	}

	private void givenOpenSucceeds(String aPath)
	{
		when(lib.glfs_open(CONNECTED_SESSION_ID, aPath, 0)).thenReturn(OPENED_FILE_ID);
	}

	private void givenAConnectedSession()
	{
		session.glFsPtr = CONNECTED_SESSION_ID;
	}
}