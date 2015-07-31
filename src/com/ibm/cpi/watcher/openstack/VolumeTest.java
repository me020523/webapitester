package com.ibm.cpi.watcher.openstack;

import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.Volume.Status;


public class VolumeTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	protected Volume getVolumeByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Volume> vols = (List<Volume>) OSClientManager.getInstance().blockStorage().volumes().list();
		for(Volume vol : vols)
		{
			if(vol.getName().equals(name))
				return vol;
		}
		return null;
	}
	public boolean createVolume(){
		String name = "Test Vol 20G";
		String desc = name; 
		int size = 20;
		System.out.println("create a volume");
		
		Volume v = getVolumeByName(name);
		if(v != null && v.getStatus().equals(Status.AVAILABLE))
		{
			jobInfo.setVolumeId(v.getId());
			System.out.println("creating a volume done");
			return true;
		}
		else if (v != null && v.getStatus().equals(Status.CREATING))
		{
			ActionResponse ar = OSClientManager.getInstance().blockStorage().volumes().delete(v.getId());
		}
		v = OSClientManager.getInstance().blockStorage().volumes().create(
				Builders.volume()
				.name(name)
				.description(desc)
				.size(size)
				.build());
		
		int i = 0;
		while(i < 2 * 60 * 5)
		{
			Volume tmp = OSClientManager.getInstance().blockStorage().volumes().get(v.getId());
			Status s = tmp.getStatus();
			if(s.equals(Status.ERROR))
			{
				return false;
			}
			else  if(s.equals(Status.CREATING))
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(s.equals(Status.AVAILABLE))
			{
				break;
			}
				
			i++;
		}
		if(i == 2 * 60 * 5) return false;
		
		jobInfo.setVolumeId(v.getId());
		System.out.println("creating a volume done");
		return true;
	}

	public boolean deleteVolume()
	{
		String id = jobInfo.getVolumeId();
		System.out.println("delete a volume");
		ActionResponse ar = OSClientManager.getInstance().blockStorage().volumes().delete(id);
		if(!ar.isSuccess())
			return false;
		
		int i = 0;
		while(i < 2 * 60 * 5)
		{
			Volume tmp = OSClientManager.getInstance().blockStorage().volumes().get(id);
			if(tmp == null)   //the volume is already deleted 
				break;
			Status s = tmp.getStatus();
			if(s.equals(Status.ERROR))
			{
				return false;
			}
			else  //if(s.equals(Status.CREATING))
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
		
		jobInfo.setVolumeId(null);
		System.out.println("deleting a volume done");
		return true;
	}
}
