package com.ibm.cpi.watcher.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class JobStatistic 
{
	private static JobStatistic instance = new JobStatistic();
	
	public static JobStatistic getInstance()
	{
		return instance;
	}
	
	public static class JobData
	{
		private int success = 0;
		public int getSuccess() {
			return success;
		}
		public int getTotal() {
			return total;
		}
		private int total = 0;
		private Map<String, CaseData> caseData = new HashMap<String, JobStatistic.CaseData>();
		public Map<String, CaseData> getCaseData() {
			return caseData;
		}
		public void setCaseData(Map<String, CaseData> caseData) {
			this.caseData = caseData;
		}
		
		public JSONObject toJson() throws JSONException
		{
			JSONObject obj = new JSONObject();
			obj.put("success", success);
			obj.put("total", total);
			List<JSONObject> caseJsons = new ArrayList<JSONObject>();
			Set<String> keys = caseData.keySet();
			for(String key : keys)
			{
				CaseData cd = caseData.get(key);
				JSONObject caseJson = cd.toJson();
				caseJson.put("id", key);
				caseJsons.add(caseJson);
			}
			obj.put("cases", caseJsons);
			return obj;
		}
	}
	public static class CaseData
	{
		private String jobId = null;
		private int success = 0;
		private int total = 0;
		public String getJobId() {
			return jobId;
		}
		public void setJobId(String jobId) {
			this.jobId = jobId;
		}
		public int getSuccess() {
			return success;
		}
		public int getTotal() {
			return total;
		}
		public JSONObject toJson() throws JSONException
		{
			JSONObject obj = new JSONObject();
			obj.put("success", success);
			obj.put("total", total);
			return obj;
		}
	}
	
	private Map<String, JobData> jobData = new HashMap<String, JobData>();
	public Map<String, JobData> getJobData()
	{
		return jobData;
	}
	public void addJob(String jobId)
	{
		synchronized (jobData) 
		{
			if(!jobData.containsKey(jobId))
				jobData.put(jobId, new JobData());
		}
	}
	public void removeJob(String jobId)
	{
		synchronized (jobData) {
			jobData.remove(jobId);
		}
	}
	public void success(String jobId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.success++;
				jd.total++;
			}
		}
	}
	public void fail(String jobId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.success++;
			}
		}	
	}
	public JobData getJobDataById(String jobId)
	{
		return jobData.get(jobId);
	}
	public void addCase(String jobId,String caseId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.getCaseData().put(caseId, new CaseData());
			}
		}
	}
	public void removeCase(String jobId,String caseId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.getCaseData().remove(caseId);
			}
		}
	}
	public void success(String jobId,String caseId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.getCaseData().get(caseId).success++;
				jd.getCaseData().get(caseId).total++;
			}
		}
	}
	public void fail(String jobId, String caseId)
	{
		synchronized (jobData) {
			if(jobData.containsKey(jobId))
			{
				JobData jd = jobData.get(jobId);
				jd.getCaseData().get(caseId).total++;
			}
		}
	}
	public CaseData getCaseDataById(String jobId,String caseId)
	{
		JobData jd = jobData.get(jobId);
		if(jd != null)
			return jd.getCaseData().get(caseId);
		return null;
	}
}
