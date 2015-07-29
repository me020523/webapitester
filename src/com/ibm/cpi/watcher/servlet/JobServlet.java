package com.ibm.cpi.watcher.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.ibm.cpi.watcher.framework.CPIWebWatcher;
import com.ibm.cpi.watcher.framework.JobExecutor;
import com.ibm.cpi.watcher.framework.JobStatistic;
import com.ibm.cpi.watcher.framework.JobStatistic.JobData;
import com.ibm.cpi.watcher.framework.job.Job;

public class JobServlet extends HttpServlet
{
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try
		{
			CPIWebWatcher.getInstance().start();
		} 
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		List<JSONObject> jobJson = new ArrayList<JSONObject>();
		List<Job> jobs = JobExecutor.getInstance().getJobs();
		for(Job j : jobs)
		{
			JobData jd = JobStatistic.getInstance().getJobDataById(j.getId());
			try 
			{
				JSONObject jdJson = jd.toJson();
				jobJson.add(jdJson);
			} 
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JSONObject ret = new JSONObject();
		try 
		{
			ret.put("jobs", jobJson);
			Writer w = resp.getWriter();
			w.append(ret.toString());
			w.flush();
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
