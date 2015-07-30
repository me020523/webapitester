package com.ibm.cpi.watcher.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;

public class InstanceTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();
	public boolean createInstance()
	{
		String name = "Test OS VM";
		ServerCreate sc = Builders.server()
									.name(name)
									.image(jobInfo.getImageId())
									.flavor(jobInfo.getFlavorId())
									.keypairName(jobInfo.getKeypairName())
									.addSecurityGroup(jobInfo.getSecurityGroupName())
									.build();
		Server server = OSClientManager.getInstance().compute().servers().bootAndWaitActive(sc, 1000 * 60 * 10);
		if(server.getStatus().equals(Status.ERROR))
			return false;
		jobInfo.setServerId(server.getId());
		return true;
	}
	public boolean deleteInstance()
	{
		String id = jobInfo.getServerId();
		ActionResponse ar = OSClientManager.getInstance().compute().servers().delete(id);
		if(!ar.isSuccess())
			return false;
		int i = 0;
		while(i < 2 * 60 * 5)
		{
			Server tmp = OSClientManager.getInstance().compute().servers().get(id);
			if(tmp == null)   //the volume is already deleted 
				break;
			Status s = tmp.getStatus();
			if(s.equals(Status.ERROR) || s.equals(Status.UNKNOWN) || s.equals(Status.UNKNOWN))
			{
				return false;
			}
			else if(s.equals(Status.DELETED))
			{
				break;
			}
			else
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			i++;
		}
		if(i == 2 * 60 * 5) return false;
		
		jobInfo.setServerId(null);
		return true;
	}
}
