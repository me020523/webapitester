package com.ibm.cpi.watcher.openstack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ibm.cpi.watcher.framework.JobFactory;
import com.ibm.cpi.watcher.framework.job.Job;

public class Test 
{
	public static void main(String[] args)
	{
		/*JobInfo jobInfo = new JobInfo();
		VolumeTest test = new VolumeTest();
		test.createVolume();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.deleteVolume();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.deleteVolume();*/
		/*JobFactory jf = JobFactory.getInstance();
		File f = new File("C:\\Users\\IBM_ADMIN\\git\\cpiwebwatcher\\src\\resource\\jobs.xml");
		
		try 
		{
			InputStream in = new FileInputStream(f);
			List<Job> jobs = jf.loadJobsFromXml(in);
			for(Job job : jobs)
				job.work();
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
