package com.ibm.cpi.watcher.openstack;

import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.SecurityGroup;
import org.openstack4j.model.network.SecurityGroupRule;
import org.openstack4j.model.network.Subnet;

public class NetworkTest 
{
	private JobInfo jobInfo = JobInfo.getInstance();
	
	protected Router getRouterByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Router> routers = (List<Router>) OSClientManager.getInstance().networking().router().list();
		for(Router r : routers)
		{
			if(r.getName().equals(name))
				return r;
		}
		return null;
	}
	public boolean createRouter()
	{
		String name = "Test OS Router Network";
		Router route = null;
		int i = 0;
		while(i < 10)
		{
			try
			{
				Network network = getNetworkByName("ext-net");
				if(network == null)
					return false;
				route = getRouterByName(name);
				if(route == null)
				{
					route = OSClientManager.getInstance()
							.networking()
							.router()
							.create(Builders.router()
									.name(name)
									.externalGateway(network.getId())
									.build());
				}
				OSClientManager.getInstance().networking().router()
                        .attachInterface(route.getId(), AttachInterfaceType.SUBNET, jobInfo.getSubnetId());
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this router already exsits, so must wait until it has been deleted
					try 
					{
						Router f = getRouterByName(name);
						if(f != null)
							OSClientManager.getInstance().networking().router().delete(f.getId());
						System.out.println("the network already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					return false;
				}
			}
			i++;
		}
		if(i == 10) return false;
		
		jobInfo.setRouterId(route.getId());
		return true;
	}
	public boolean deleteRouter()
	{
		try
		{
			String id = jobInfo.getRouterId();
			RouterInterface ri = OSClientManager.getInstance().networking().router()
	                .detachInterface(id,jobInfo.getSubnetId(),null);
			ActionResponse ar = OSClientManager.getInstance().networking().router().delete(id);
			jobInfo.setRouterId(null);
			return ar.isSuccess();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	protected Network getNetworkByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Network> networks = (List<Network>)OSClientManager.getInstance().networking().network().list();
		for(Network f : networks)
		{
			if(f.getName().equals(name))
				return f;
		}
		return null;
	}
	public boolean createNetwork()
	{
		String name = "Test OS Internal Network";
		boolean admin_state_up = true;
		Network network = getNetworkByName(name);
		if(network != null)
		{
			jobInfo.setNetworkId(network.getId());
			return true;
		}
		network = Builders.network()
							.name(name)
							.adminStateUp(admin_state_up)
							.build();
		int i = 0;
		while(i < 10)
		{
			try
			{
				network = OSClientManager.getInstance().networking().network().create(network);
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this network already exsits, so must wait until it has been deleted
					try 
					{
						Network f = getNetworkByName(name);
						if(f != null)
							OSClientManager.getInstance().networking().network().delete(f.getId());
						System.out.println("the network already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					return false;
				}
			}
			i++;
		}
		if(i == 10) return false;
		
		jobInfo.setNetworkId(network.getId());
		return true;
	}
	
	public boolean deleteNetwork()
	{
		String id = jobInfo.getNetworkId();
		ActionResponse ar = OSClientManager.getInstance().networking().network().delete(id);
		jobInfo.setNetworkId(null);
		return ar.isSuccess();
	}
	
	protected Subnet getSubnetByName(String name)
	{
		@SuppressWarnings("unchecked")
		List<Subnet> subnets = (List<Subnet>) OSClientManager.getInstance().networking().subnet().list();
		for(Subnet r : subnets)
		{
			if(r.getName().equals(name))
				return r;
		}
		return null;
	}
	public boolean createSubnet()
	{
		String name = "Test OS Sub Internal Network";
		int IPVersion = 4;
		String cidr = "10.0.0.0/24";
		boolean enableDHCP = true;
		
		
		Subnet subNet = getSubnetByName(name);
		if(subNet != null)
		{
			jobInfo.setSubnetId(subNet.getId());
			return true;
		}
		
		subNet = Builders.subnet()
							.name(name)
							.ipVersion(IPVersionType.V4)
							.cidr(cidr)
							.enableDHCP(true)
							.networkId(jobInfo.getNetworkId())
							.build();
		int i = 0;
		while(i < 10)
		{
			try
			{
				subNet = OSClientManager.getInstance().networking().subnet().create(subNet);
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this flavor already exsits, so must wait until it has been deleted
					try 
					{
						Subnet f = getSubnetByName(name);
						if(f != null)
							OSClientManager.getInstance().networking().subnet().delete(f.getId());
						System.out.println("the subnet already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					return false;
				}
			}
			i++;
		}
		if(i == 10) return false;
		
		jobInfo.setSubnetId(subNet.getId());
		return true;
		
	}
	public boolean deleteSubnet()
	{
		String id = jobInfo.getSubnetId();
		ActionResponse ar = OSClientManager.getInstance().networking().subnet().delete(id);
		jobInfo.setSubnetId(null);
		return ar.isSuccess();
	}
	
	public boolean createSecurityGroup()
	{
		String name = "Test OS SG";
		String desc = "Test OS SG";
		SecurityGroup sg = Builders.securityGroup()
							.name(name)
							.description(desc)
							.build();
		
		int i = 0;
		while(i < 10)
		{
			try
			{
				sg = OSClientManager.getInstance().networking().securitygroup().create(sg);
				SecurityGroupRule rule = Builders.securityGroupRule()
										.securityGroupId(sg.getId())
										.direction("ingress")
										.portRangeMin(1)
										.portRangeMax(65535)
										.protocol("tcp")
										.ethertype("IPv4")
										.remoteIpPrefix("0.0.0.0/0")
										.build();
				OSClientManager.getInstance().networking().securityrule().create(rule);
				rule = Builders.securityGroupRule()
								.securityGroupId(sg.getId())
								.protocol("udp")
								.direction("ingress")
								.portRangeMin(1)
								.portRangeMax(65535)
								.ethertype("IPv4")
								.remoteIpPrefix("0.0.0.0/0")
								.build();
				OSClientManager.getInstance().networking().securityrule().create(rule);
				rule = Builders.securityGroupRule()
								.securityGroupId(sg.getId())
								.protocol("icmp")
								.direction("ingress")
								.remoteIpPrefix("0.0.0.0/0")
								.build();
				OSClientManager.getInstance().networking().securityrule().create(rule);
				break;
			} 
			catch (ResponseException e)
			{
				if(e.getStatus() == 409)
				{
					//this security group already exsits, so must wait until it has been deleted
					try 
					{
						SecurityGroup tmp = OSClientManager.getInstance().networking().securitygroup().get(sg.getId());
						if(tmp != null)
							OSClientManager.getInstance().networking().securitygroup().get(sg.getId());
						System.out.println("the security group already exsits, so we must sleep for a short time: " + i);
						Thread.sleep(500);
					} 
					catch (InterruptedException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					return false;
				}
			}
			i++;
		}
		if(i == 10) return false;
		
		jobInfo.setSecurityGroupId(sg.getId());
		jobInfo.setSecurityGroupName(name);
		return true;
	}
	public boolean deleteSecurityGroup()
	{
		String id = jobInfo.getSecurityGroupId();
		ActionResponse ar = OSClientManager.getInstance().networking().securitygroup().delete(id);
		jobInfo.setSecurityGroupId(null);
		jobInfo.setSecurityGroupName(null);
		return ar.isSuccess();
	}
}
