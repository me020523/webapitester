package com.ibm.cpi.watcher.job.openstack;

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
}
