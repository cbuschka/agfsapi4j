package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

class ResourceTracker
{
	private static Logger log = LoggerFactory.getLogger(ResourceTracker.class);

	private Set<Resource> allocatedObjects = new HashSet<>();

	public void allocated(Resource resource)
	{
		this.allocatedObjects.add(resource);
	}

	public void unallocated(Resource resource)
	{
		this.allocatedObjects.remove(resource);
	}

	public void closeResources()
	{
		Set<Resource> resources = new HashSet<>(this.allocatedObjects);
		for (Resource resource : resources)
		{
			try
			{
				resource.close();
			}
			catch (Exception ex)
			{
				log.warn("Closing resource failed.", ex);
			}
		}

		this.allocatedObjects.removeAll(resources);
	}
}
