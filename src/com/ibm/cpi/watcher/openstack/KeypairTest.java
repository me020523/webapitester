package com.ibm.cpi.watcher.openstack;


import java.util.List;

import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Keypair;

public class KeypairTest {
	private JobInfo jobInfo = JobInfo.getInstance();
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	public boolean createKeypair()
	{
		String name = jobInfo.getKeypairName();
		
		int i = 0;
		while(i < 10)
		{
			try
			{
				Keypair kp = OSClientManager.getInstance().compute().keypairs().create(name, null);
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this flavor already exsits, so must wait until it has been deleted
					try 
					{
						Keypair f = getKeypairByName(name);
						if(f != null)
							OSClientManager.getInstance().compute().keypairs().delete(name);
						System.out.println("the keypair already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
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
		
		return true;
	}
	protected Keypair getKeypairByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Keypair> keypairs = (List<Keypair>) OSClientManager.getInstance().compute().keypairs().list();
		for(Keypair kp : keypairs)
		{
			if(kp.getName().equals(name))
				return kp;
		}
		return null;
	}
	public boolean deleteKeypair()
	{
		String name = jobInfo.getKeypairName();
		ActionResponse ar = OSClientManager.getInstance().compute().keypairs().delete(name);
		Keypair kp = null;
		return ar.isSuccess();
	}
}
