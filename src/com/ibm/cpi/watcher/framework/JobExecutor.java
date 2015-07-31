package com.ibm.cpi.watcher.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ibm.cpi.watcher.framework.job.Job;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class JobExecutor 
{
	private final static JobExecutor instance = new JobExecutor();
	public static JobExecutor getInstance()
	{
		return instance;
	}
	
	private ScheduledExecutorService executorService = null;
	protected JobExecutor()
	{
		int cpus = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newScheduledThreadPool(cpus);
	}
	
	private List<Job> jobs = java.util.Collections.synchronizedList(new ArrayList<Job>());
	public List<Job> getJobs()
	{
		return this.jobs;
	}
	public void addJob(Job job)
	{
		jobs.add(job);
	}
	public void addJob(List<Job> jobs)
	{
		for(Job job : jobs)
		{
			this.jobs.add(job);
		}
	}
	public void removeJob(Job job)
	{
		jobs.remove(job);
	}
	public void removeJob(List<Job> jobs)
	{
		for(Job job : jobs)
		{
			this.jobs.remove(job);
		}
	}
	
	public void execute()
	{
		for(final Job job : jobs)
		{
			executorService.schedule(new Runnable() 
			{
				@Override
				public void run() 
				{
					System.out.println("start the job: " + job.getId());
					doJob(job);
					executorService.schedule(this, job.getJobDelay(), TimeUnit.MILLISECONDS); //repeat to execute this job
				}
			}, job.getJobDelay(), TimeUnit.MILLISECONDS);
		}
	}
	
	private void doJob(Job job)
	{
		try
		{
			job.work();
			JobStatistic.getInstance().success(job.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JobStatistic.getInstance().fail(job.getId());
		}
	}
}
