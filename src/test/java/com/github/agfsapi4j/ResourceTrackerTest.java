package com.github.agfsapi4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResourceTrackerTest
{
	@InjectMocks
	private ResourceTracker resourceTracker;

	@Mock
	private Resource resource;

	@Mock
	private Resource failingResource;

	@Test
	public void closesResources()
	{
		this.resourceTracker.allocated(resource);

		this.resourceTracker.closeResources();

		verify(resource).close();
	}

	@Test
	public void closesResourcesWhenExceptionOccurs()
	{
		doThrow(new RuntimeException()).when(failingResource).close();
		this.resourceTracker.allocated(failingResource);
		this.resourceTracker.allocated(resource);

		this.resourceTracker.closeResources();

		verify(failingResource).close();
		verify(resource).close();
	}
}