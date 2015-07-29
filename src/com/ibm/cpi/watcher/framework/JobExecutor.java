package com.ibm.cpi.watcher.framework;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ibm.cpi.watcher.framework.job.Job;

public class JobExecutor 
{
	public void executeOnce(List<Job> jobs)
	{
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new Runnable() 
		{
			@Override
			public void run() {
				for(Job job : jobs)
				{
					doJob(job);
				}
			}
		});
		es.shutdown();
	}
	
	/**
	 * @param jobs
	 * @param delay, in millisecond
	 */
	public void executeRepeat(List<Job> jobs, int delay)
	{
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.execute(new Runnable() 
		{
			@Override
			public void run() 
			{
				for(Job job : jobs)
				{
					doJob(job);
				}
				ses.schedule(this, delay, TimeUnit.MILLISECONDS);
			}
		});
	}
	
	private void doJob(Job job)
	{
		JobStatistic.getInstance().addJob(job.getId());
		try
		{
			job.work();
			JobStatistic.getInstance().success(job.getId());
		}
		catch(Exception e)
		{
			JobStatistic.getInstance().fail(job.getId());
		}
	}
}
