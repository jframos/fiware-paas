package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VMDto {

	private String domain;
	private String fqn;
	private String hostname;
	private String ip;
	private String id;

	/**
     */
	public VMDto() {
	}

	/**
	 * @param productName
	 * @param version
	 * @param releaseNotes
	 * @param privateAttributes
	 * @param supportedOS
	 * @param transitableReleases
	 */
	public VMDto(String domain, String fqn, String hostname, String ip, String id) {
		this.domain = domain;
		this.fqn = fqn;
		this.hostname = hostname;
		this.ip = ip;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFqn() {
		return fqn;
	}

	public void setFqn(String fqn) {
		this.fqn = fqn;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
