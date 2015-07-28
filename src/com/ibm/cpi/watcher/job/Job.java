package com.ibm.cpi.watcher.job;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	
	public String getId()
	{
		return id;
	}
	public boolean equals(Job job)
	{
		if(this.id.equals(job.id))
			return true;
		else
			return false;
	}
	
	/**
	 * the method needed to override
	 */
	private List<JobCase> finishedCases = new ArrayList<JobCase>();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void work()
	{
		try 
		{
			for(JobCase jc : jobCases)
			{
				String methodName = jc.getMethodName();
				String className = jc.getJobClass().getName();
				Class cls = Class.forName(className);
				Object obj = cls.newInstance();
				Method m = cls.getMethod(methodName);
			    Boolean ret = (Boolean)m.invoke(obj);
			    if(!ret)
			    {
			    	throw new Exception();
			    }
			}
		} 
		catch (Exception e) 
		{
			fail();
			e.printStackTrace();
		}
	}
	public void fail()
	{
		System.out.println("sorry, something wrong happens");
	}
}
