package com.ibm.cpi.watcher.openstack;

import org.openstack4j.api.Builders;
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
		flavor = OSClientManager.getInstance().compute().flavors().create(flavor);
		
		if(flavor == null)
			return false;
		
		jobInfo.setFlavorId(flavor.getId());
		return true;
	}
	public boolean deleteFlavor()
	{
		String flavorId = jobInfo.getFlavorId();
		ActionResponse ar = OSClientManager.getInstance().compute().flavors().delete(flavorId);
		Flavor flavor = null;
		do
		{
			flavor = OSClientManager.getInstance().compute().flavors().get(flavorId);
		} 
		while (flavor != null);
		jobInfo.setFlavorId(null);
		return ar.isSuccess();
	}
}
