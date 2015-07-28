package com.ibm.cpi.watcher;

import java.util.List;
import com.ibm.cpi.watcher.job.Job;

public class JobExecutor 
{
	public void executeOnce(List<Job> jobs)
	{
		for(Job job : jobs)
		{
			job.work();
		}
	}
	
	/**
	 * @param jobs
	 * @param delay, in millisecond
	 */
	public void executeCycle(List<Job> jobs, int delay)
	{
		
	}
}
