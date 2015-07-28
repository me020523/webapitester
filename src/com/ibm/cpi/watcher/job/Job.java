package com.ibm.cpi.watcher.job;

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
	/**
	 * the method needed to override
	 */
	public void work()
	{
		
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
}