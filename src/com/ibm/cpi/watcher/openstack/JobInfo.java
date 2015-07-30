package com.ibm.cpi.watcher.openstack;

public class JobInfo
{
	private static final JobInfo instance = new JobInfo();
	public static JobInfo getInstance()
	{
		return instance;
	}
	
	private String flavorId = null;

	public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}
	
	private String keypairName = "bmx-test-keypair";

	public String getKeypairName() {
		return keypairName;
	}

	public void setKeypairName(String keypairName) {
		this.keypairName = keypairName;
	}
	
	private String volumeId = null;

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	
	private String imageId = null;
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	private String routerId = null;
	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}
	
	private String networkId = null;
	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	
	private String subnetId = null;
	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	
	private String securityGroupId = null;
	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	
	private String securityGroupName = null;
	public String getSecurityGroupName() {
		return securityGroupName;
	}

	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}
	
	private String serverId = null;
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
}
