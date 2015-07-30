package com.ibm.cpi.watcher.openstack;

import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Flavor;

public class FlavorTest
{
	private JobInfo jobInfo = JobInfo.getInstance();
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	public boolean createFlavor()
	{
		String name = "Test OS Flavor 2C4G50B";
		int ram = 2048;
		int vcpus = 4;
		int disk = 50;
		Flavor flavor = Builders.flavor()
                .name(name)
                .ram(ram)
                .vcpus(vcpus)
                .disk(disk)
                .build();
		
		int i = 0;
		while(i < 10)
		{
			try
			{
				flavor = OSClientManager.getInstance().compute().flavors().create(flavor);
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this flavor already exsits, so must wait until it has been deleted
					try 
					{
						Flavor f = getFlavorByName(name);
						if(f != null)
							OSClientManager.getInstance().compute().flavors().delete(f.getId());
						System.out.println("the flavor already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					return false;
				}
			}
			i++;
		}
		if(i == 10) return false;
		
		jobInfo.setFlavorId(flavor.getId());
		return true;
	}
	protected Flavor getFlavorByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Flavor> flavors = (List<Flavor>)OSClientManager.getInstance().compute().flavors().list();
		for(Flavor f : flavors)
		{
			if(f.getName().equals(name))
				return f;
		}
		return null;
	}
	public boolean deleteFlavor()
	{
		String flavorId = jobInfo.getFlavorId();
		ActionResponse ar = OSClientManager.getInstance().compute().flavors().delete(flavorId);
		jobInfo.setFlavorId(null);
		return ar.isSuccess();
	}
}
