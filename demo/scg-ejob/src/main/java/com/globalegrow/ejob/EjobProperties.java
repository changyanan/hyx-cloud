package com.globalegrow.ejob;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.globalegrow.ejob.java.spring.bean.EjobCfg;

@ConfigurationProperties(prefix = "globalegrow.ejob")
public class EjobProperties extends EjobCfg{
	private String clustername = "ejob";

	private String zkrootnode = null;

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public String getZkrootnode() {
		return zkrootnode;
	}

	public void setZkrootnode(String zkrootnode) {
		this.zkrootnode = zkrootnode;
	}
	 
	
}
