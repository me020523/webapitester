package com.ibm.cpi.watcher.job.openstack;

import java.security.KeyPair;

import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Keypair;

import sun.font.TrueTypeFont;

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
		return ar.isSuccess();
	}
}
