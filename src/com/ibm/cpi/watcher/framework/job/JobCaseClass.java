package com.ibm.cpi.watcher.framework.job;

public class JobCaseClass
{
	private String name = null;
	public String getName() {
		return name;
	}
	public void setName(String className) {
		this.name = className;
	}
	
	public boolean equals(JobCaseClass jc)
	{
		if(jc.name.equals(this.name))
			return true;
		else 
			return false;
	}
}
