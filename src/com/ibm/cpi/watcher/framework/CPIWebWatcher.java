package com.ibm.cpi.watcher.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ibm.cpi.watcher.framework.job.Job;

public class CPIWebWatcher
{
	private static CPIWebWatcher instance = new CPIWebWatcher();
	public static CPIWebWatcher getInstance()
	{
		return instance;
	}
	
	private boolean started = false;
	public synchronized void start() throws ParserConfigurationException, SAXException, IOException
	{
		String path = "jobs.xml";
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
		List<Job> jobs = JobFactory.getInstance().loadJobsFromXml(in);
		JobExecutor.getInstance().addJob(jobs);
		JobExecutor.getInstance().execute();
		started = true;
	}
	public synchronized boolean isStarted()
	{
		return started;
	}
}
