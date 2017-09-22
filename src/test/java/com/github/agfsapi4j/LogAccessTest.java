package com.github.agfsapi4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class LogAccessTest
{
	@Mock
	private GlusterFsSession session;

	@InjectMocks
	private LogAccess logAccess;

	@Test
	public void emptyWhenNoLogFile()
	{
		List<String> logMessages = this.logAccess.getLogMessages();

		assertThat(logMessages, is(Collections.emptyList()));
	}
}