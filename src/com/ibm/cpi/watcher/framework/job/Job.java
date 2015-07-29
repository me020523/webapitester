package com.ibm.cpi.watcher.framework.job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ibm.cpi.watcher.framework.JobStatistic;


public class Job
{
	private String id = null;
	
	public List<JobCaseClass> getJobClasses() {
		return jobClasses;
	}
	public void setJobClasses(List<JobCaseClass> jobClasses) {
		this.jobClasses = jobClasses;
	}
	public List<JobCase> getJobCases() {
		return jobCases;
	}
	public void setJobCases(List<JobCase> jobCases) {
		this.jobCases = jobCases;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
	
	private int delay = 1000; //milliseconds before the job is executed, default is 1000
	
	public int getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = Integer.valueOf(delay);
	}
	public boolean equals(Job job)
	{
		if(this.id.equals(job.id))
			return true;
		else
			return false;
	}
	
	private List<JobCaseClass> jobClasses = new ArrayList<JobCaseClass>();
	public void addCaseClass(JobCaseClass jc)
	{
		jobClasses.add(jc);
	}
	
	private List<JobCase> jobCases = new ArrayList<JobCase>();
	public void addCase(JobCase jc)
	{
		jobCases.add(jc);
	}
	public void sortCase()
	{
		jobCases.sort(null);
	}
	public JobCase getCaseById(String id)
	{
		for(JobCase c : jobCases)
		{
			if(c.getId().equals(id))
				return c;
		}
		return null;
	}
	
	/**
	 * the method needed to override
	 */
	private List<JobCase> finishedCases = Collections.synchronizedList(new ArrayList<JobCase>());
	public List<JobCase> getFinishedCases() {
		return finishedCases;
	}

	private JobCase currentCase = null; //current executing case
	public JobCase getCurrentCase()
	{
		return currentCase;
	}
	public void work() throws Exception
	{
		try 
		{
			for(JobCase jc : jobCases)
			{
				System.out.println("start case: " + jc.getId());
				try
				{
					boolean ret = doCase(jc);
				    if(!ret)
				    {
				    	JobStatistic.getInstance().fail(this.id, jc.getId());
				    	throw new Exception();
				    }
				    else
				    {
				    	finishedCases.add(0, jc);
				    	JobStatistic.getInstance().success(this.id, jc.getId());
					}
				}
				catch(Exception e)
				{
					JobStatistic.getInstance().fail(this.id, jc.getId());
					throw new Exception();
				}
			}
		} 
		catch (Exception e) 
		{
			fail();
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean doCase(JobCase c) throws ClassNotFoundException, InstantiationException, 
												IllegalAccessException, NoSuchMethodException, 
												SecurityException, IllegalArgumentException, 
												InvocationTargetException
	{
		currentCase = c;   //now current executing case is c
		String methodName = c.getMethodName();
		String className = c.getJobClass().getName();
		Class cls = Class.forName(className);
		Object obj = cls.newInstance();
		Method m = cls.getMethod(methodName);
	    Boolean ret = (Boolean)m.invoke(obj);
	    return ret;
	}
	public void fail()
	{
		for(JobCase jc : finishedCases)
		{
			try 
			{
				boolean ret = doCase(jc);
				if(!ret)
					throw new Exception();
			} 
			catch (Exception e)
			{
				
			}
		}
	}
}
