package com.ibm.cpi.watcher.openstack;

import org.openstack4j.api.OSClient;
import org.openstack4j.openstack.OSFactory;

public class OSClientManager 
{
	private static OSClient instance = null;
	public static OSClient getInstance()
	{
		synchronized (OSClientManager.class)
		{
			instance = OSFactory.builder()
					.endpoint("")
					.credentials("","")
					.tenantName("")
					.authenticate();
		}
		return instance;
	}
}
