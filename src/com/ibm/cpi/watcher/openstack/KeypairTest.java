package com.ibm.cpi.watcher.openstack;


import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.model.network.IP;

public class KeypairTest {
	private JobInfo jobInfo = JobInfo.getInstance();
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	public boolean createKeypair()
	{
		String name = jobInfo.getKeypairName();
		Keypair kp = OSClientManager.getInstance().compute().keypairs().create(name, null);
		if(kp == null)
			return false;
		else
			return true;
	}
	public boolean deleteKeypair()
	{
		String name = jobInfo.getKeypairName();
		ActionResponse ar = OSClientManager.getInstance().compute().keypairs().delete(name);
		Keypair kp = null;
		do {
			kp = OSClientManager.getInstance().compute().keypairs().get(name);
		} while (kp != null);
		return ar.isSuccess();
	}
}
