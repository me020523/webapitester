package com.ibm.cpi.watcher.job.openstack;

import org.openstack4j.api.OSClient;
import org.openstack4j.openstack.OSFactory;

public class OSClientManager 
{
	private static OSClient instance = null;
	public static OSClient getInstance()
	{
		synchronized (OSClientManager.class) {
			if(instance == null)
			{
				instance = OSFactory.builder()
	                       .endpoint("http://9.112.242.116:5000/v2.0")
	                       .credentials("cpiwatcher","password")
	                       .tenantName("cpiwatcher")
	                       .authenticate();
			}
		}
		return instance;
	}
}
