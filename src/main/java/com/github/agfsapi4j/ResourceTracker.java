package com.github.agfsapi4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
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
		for (Iterator<Resource> iter = this.allocatedObjects.iterator(); iter.hasNext(); )
		{
			Resource object = iter.next();
			try
			{
				object.close();
			}
			catch (Exception ex)
			{
				log.warn("Closing resource failed.", ex);
			}
			finally
			{
				iter.remove();
			}
		}
	}
}
