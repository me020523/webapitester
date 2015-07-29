package com.ibm.cpi.watcher.framework;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.cpi.utils.Utils;
import com.ibm.cpi.watcher.framework.job.Job;
import com.ibm.cpi.watcher.framework.job.JobCase;
import com.ibm.cpi.watcher.framework.job.JobCaseClass;

/**
 * @author shuaibc
 * create jobs from a config file, such as xml
 */
public class JobFactory 
{
	private static JobFactory instance = new JobFactory();
	public static JobFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * load jobs from a xml file
	 * @param path
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public List<Job> loadJobsFromXml(InputStream in) throws 
											ParserConfigurationException,
											SAXException, 
											IOException
	{
		List<Job> jobs = new ArrayList<Job>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in);
		
		Element root = doc.getDocumentElement();
		
		NodeList jobNodes = root.getChildNodes();
		
		for(int i = 0; i < jobNodes.getLength(); i++)
		{
			Node n = jobNodes.item(i);
			if(!n.getNodeName().equals("job"))
				continue;
			try 
			{
				Job job = parseJob((Element)n);
				if(job != null)
					jobs.add(job);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return jobs;
	}
	
	protected Job parseJob(Element e) throws NoSuchMethodException, 
													SecurityException,IllegalAccessException, 
													IllegalArgumentException,InvocationTargetException
	{
		Job job = new Job();
		NamedNodeMap nnMap = e.getAttributes();
		if(nnMap == null)
			return null;
		//parse job attributes
		parseNodeAttribute(job, nnMap);
		//parse job case classes belongs to this job
		List<JobCaseClass> caseClasses = new ArrayList<JobCaseClass>();
		List<JobCase> cases = new ArrayList<JobCase>();
		NodeList classList = e.getChildNodes();
		for(int i = 0; i < classList.getLength(); i++)
		{
			Node n = classList.item(i);
			if(!n.getNodeName().equals("class"))
				continue;
			
			nnMap = n.getAttributes();
			if(nnMap == null)
				continue;
			JobCaseClass jcc = new JobCaseClass();
			parseNodeAttribute(jcc, nnMap);
			//parse job cases belongs to this classes
			NodeList caseList = n.getChildNodes();
			for(int j = 0; j < caseList.getLength(); j++)
			{
				Node nn = caseList.item(j);
				if(!nn.getNodeName().equals("case"))
					continue;
				JobCase c = new JobCase();
				c.setJobClass(jcc);
				
				nnMap = nn.getAttributes();
				if(nnMap == null)
					continue;
				parseNodeAttribute(c, nnMap);
				cases.add(c);
			}
		}
		job.setJobCases(cases);
		job.setJobClasses(caseClasses);
		job.sortCase();
		return job;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> void parseNodeAttribute(T obj, NamedNodeMap nnMap)throws NoSuchMethodException, 
																		SecurityException,IllegalAccessException, 
																		IllegalArgumentException,InvocationTargetException
	{
		//parse job attribution
		for(int i = 0; i < nnMap.getLength(); i++)
		{
			Node n = nnMap.item(i);
			String name = n.getNodeName();
			String value = n.getNodeValue();
			String setterName = Utils.setterName(name);

			Class c = obj.getClass();
			Method setter = c.getMethod(setterName, String.class);

			setter.invoke(obj, value);
		}
	}
}
