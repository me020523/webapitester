package com.ibm.cpi.watcher.job;

public class JobCaseClass
{
	private String className = null;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	private String onFailMethod = null;
	public String getOnFailMethod() {
		return onFailMethod;
	}
	public void setOnFailMethod(String onFailMethod) {
		this.onFailMethod = onFailMethod;
	}
	
	public JobCaseClass(String className, String onFailMethod)
	{
		this.className = className;
		this.onFailMethod = onFailMethod;
	}
	
	public boolean equals(JobCaseClass jc)
	{
		if(jc.className.equals(this.className))
			return true;
		else 
			return false;
	}
}
