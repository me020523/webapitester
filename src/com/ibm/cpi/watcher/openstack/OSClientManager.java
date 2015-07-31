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
					.endpoint("http://9.111.109.100:5000/v2.0")
					.credentials("cpiwatcher","password")
					.tenantName("cpiwatcher")
					.authenticate();
		}
		return instance;
	}
}
