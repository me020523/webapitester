package com.ibm.cpi.watcher.job;

public class JobCase implements Comparable<JobCase>
{
	private String id = null;
	private String methodName = null;
	private int order;
	private JobCaseClass jobClass = null;
	
	public JobCaseClass getJobClass() {
		return jobClass;
	}
	public void setJobClass(JobCaseClass jobClass) {
		this.jobClass = jobClass;
	}

	private String onFailureMethod = null;
	
	public String getOnFailureMethod() {
		return onFailureMethod;
	}
	public void setOnFailureMethod(String onFailMethod) {
		this.onFailureMethod = onFailMethod;
	}
	public JobCase()
	{
		super();
	}
	public JobCase(String id, String methodName, String order, JobCaseClass jobClass, String onFailMethod)
	{
		this.id = id;
		this.methodName = methodName;
		this.order = Integer.valueOf(order);
		this.jobClass = jobClass;
		this.onFailureMethod = onFailMethod;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = Integer.valueOf(order);
	}

	public boolean equals(JobCase jc)
	{
		if(jc.id.equals(this.id))
			return true;
		else
			return false;
	}

	@Override
	public int compareTo(JobCase o) 
	{
		if(this.order < o.order)
			return -1;
		else if(this.order > o.order)
			return 1;
		else
			return 0;
	}
}
