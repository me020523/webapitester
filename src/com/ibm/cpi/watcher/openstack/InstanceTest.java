package com.ibm.cpi.watcher.openstack;

import java.util.ArrayList;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VolumeAttachment;
import org.openstack4j.model.network.Network;

public class InstanceTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();
	public boolean createInstance()
	{
		String name = "Test OS VM";
		List<String> nets = new ArrayList<String>();
		nets.add(jobInfo.getNetworkId());
		ServerCreate sc = Builders.server()
									.name(name)
									.image(jobInfo.getImageId())
									.flavor(jobInfo.getFlavorId())
									.keypairName(jobInfo.getKeypairName())
									.networks(nets)
									.addSecurityGroup(jobInfo.getSecurityGroupId())
									.build();
		Server server = null;
		try
		{
			server = OSClientManager.getInstance().compute().servers().bootAndWaitActive(sc, 1000 * 60 * 10);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		if(server.getStatus().equals(Status.ERROR))
			return false;
		jobInfo.setServerId(server.getId());
		return true;
	}
	public boolean deleteInstance()
	{
		try
		{
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			if(s.equals(Status.ERROR) || s.equals(Status.UNKNOWN) || s.equals(Status.UNRECOGNIZED))
			{
				return false;
			}
			else if(s.equals(Status.DELETED))
			{
				break;
			}
			else
			{
				try 
				{
					Thread.sleep(500);
				} 
				catch (InterruptedException e)
				{
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
	public boolean attachVolume()
	{
		System.out.println("Attach the volume " + jobInfo.getVolumeId() + "to " + jobInfo.getServerId());
		String device = "/dev/loop0";
		try
		{
			VolumeAttachment va = OSClientManager.getInstance()
										.compute()
										.servers()
										.attachVolume(jobInfo.getServerId(), jobInfo.getVolumeId(), device);
		}
		catch(Exception e)
		{
			return false;
		}
		System.out.println("Done Attaching the volume " + jobInfo.getVolumeId() + "to " + jobInfo.getServerId());
		return true;
	}
	public boolean detachVolume()
	{
		try
		{
			ActionResponse va = OSClientManager.getInstance()
											.compute()
											.servers()
											.detachVolume(jobInfo.getServerId(), jobInfo.getVolumeId());
			return va.isSuccess();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public boolean assignFloatingIP()
	{
		try
		{	
			@SuppressWarnings("unchecked")
			List<FloatingIP> ips = (List<FloatingIP>) OSClientManager.getInstance().compute().floatingIps().list();
			FloatingIP ip = null;
			if(ips.size() <= 0)
			{
				List<String> pools = OSClientManager.getInstance().compute().floatingIps().getPoolNames();
				if(pools.size() <= 0)
					return false;
				ip = OSClientManager.getInstance().compute().floatingIps().allocateIP(pools.get(0));
			}
			else
			{
				ip = ips.get(0);
			}
			
			Server server = OSClientManager.getInstance().compute().servers().get(jobInfo.getServerId());
			ActionResponse ar = OSClientManager.getInstance().compute().floatingIps().addFloatingIP(server, ip.getFloatingIpAddress());
			if(!ar.isSuccess())
				return false;
			jobInfo.setFloatingIPId(ip.getId());
			jobInfo.setFloatingIPAddress(ip.getFloatingIpAddress());
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public boolean unassignFloatingIP()
	{
		try
		{
			Server server = OSClientManager.getInstance().compute().servers().get(jobInfo.getServerId());
			OSClientManager.getInstance().compute().floatingIps().removeFloatingIP(server, jobInfo.getFloatingIPAddress());
			OSClientManager.getInstance().compute().floatingIps().deallocateIP(jobInfo.getFloatingIPId());
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
