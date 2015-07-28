package com.ibm.cpi.watcher.job;

public class JobCase implements Comparable<JobCase>
{
	private String id = null;
	private String methodName = null;
	private int order;
	private JobCaseClass jobClass = null;
	
	
	public JobCase(String id, String methodName, String order, JobCaseClass jobClass)
	{
		this.id = id;
		this.methodName = methodName;
		this.order = Integer.valueOf(order);
		this.jobClass = jobClass;
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

	public void setOrder(int order) {
		this.order = order;
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
