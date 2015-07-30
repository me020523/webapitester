package com.ibm.cpi.watcher.openstack;

import java.io.InputStream;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.Image.Status;


public class ImageTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();
	
	protected Image getImageByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Image> imgs = (List<Image>) OSClientManager.getInstance().images().list();
		for(Image img : imgs)
		{
			if(img.getName().equals(name))
				return img;
		}
		return null;
	}
	public boolean createImage()
	{
		String name = "Test OS Image";
		String containerFormat = "bare";
		boolean isPublic = true;
		
		InputStream in = this.getClass().getResourceAsStream("resource/root.img");
		Payload<InputStream> pl = Payloads.create(in);
		Image img = getImageByName(name);
		if(img != null)
		{
			jobInfo.setImageId(img.getId());
			return true;
		}
		
		img = OSClientManager.getInstance().images()
						.create(Builders.image()
								.name(name)
								.diskFormat(DiskFormat.QCOW2)
								.containerFormat(ContainerFormat.BARE)
								.isPublic(true)
								.build(), pl);
		int i = 0;
		while(i < 2 * 60 * 10)
		{
			Image tmp = OSClientManager.getInstance().images().get(img.getId());
			Status s = tmp.getStatus();
			if(s.equals(Status.UNRECOGNIZED))
			{
				return false;
			}
			else if(s.equals(Status.ACTIVE))
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
		jobInfo.setImageId(img.getId());
		return true;
	}
	public boolean deleteImage()
	{
		String id = jobInfo.getImageId();
		ActionResponse ar = OSClientManager.getInstance().images().delete(id);
		if(!ar.isSuccess())
			return false;
		
		int i = 0;
		while(i < 2 * 60 * 5)
		{
			Image tmp = OSClientManager.getInstance().images().get(id);
			if(tmp == null)   //the volume is already deleted 
				break;
			Status s = tmp.getStatus();
			if(s.equals(Status.UNRECOGNIZED))
			{
				return false;
			}
			else  if(s.equals(Status.PENDING_DELETE))
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(s.equals(Status.DELETED))
			{
				break;
			}
			i++;
		}
		jobInfo.setImageId(null);
		System.out.println("deleting a volume done");
		return true;
	}
}
