package com.ibm.cpi.watcher.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.storage.block.Volume;

public class VolumeTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	//需要轮询检测
	public boolean createVolume(){
		String name = "Test Vol 20G";
		String desc = name;
		int size = 20;
		System.out.println("create a volume");
		Volume v = OSClientManager.getInstance().blockStorage().volumes().create(
					Builders.volume()
					.name(name)
					.description(desc)
					.size(size)
					.build()
				);
		if(v == null)
			return false;
		jobInfo.setVolumeId(v.getId());
		System.out.println("creating a volume done");
		return true;
	}
	
	//需要轮询检测
	public boolean deleteVolume()
	{
		String id = jobInfo.getVolumeId();
		System.out.println("delete a volume");
		ActionResponse ar = OSClientManager.getInstance().blockStorage().volumes().delete(id);
		//jobInfo.setVolumeId(null);
		System.out.println("deleting a volume done");
		return ar.isSuccess();
	}
}
