package com.github.agfsapi4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GlusterFsFileSystemProviderTest
{
	@InjectMocks
	private GlusterFsFileSystemProvider glusterFsFileSystemProvider;

	@Test
	public void scheme() throws URISyntaxException
	{
		assertThat(glusterFsFileSystemProvider.getScheme(), is("gluster"));
	}
}