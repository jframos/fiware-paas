package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Entity
public class Rule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String ipProtocol;
	private String fromPort;
	private String toPort;
	private String sourceGroup;
	private String cidr;

	private String idparent;
	private String idrule;

	public Rule() {
	}

	public Rule(String ipProtocol, String fromPort, String toPort,
			String sourceGroup, String cidr) {
		this.ipProtocol = ipProtocol;
		this.fromPort = fromPort;
		this.toPort = toPort;
		this.sourceGroup = sourceGroup;
		this.cidr = cidr;

	}

	public void setIpProtocol(String ipProtocol) {
		this.ipProtocol = ipProtocol;
	}

	public String getIpProtocol() {
		return ipProtocol;
	}

	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}

	public String getFromPort() {
		return fromPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public String getToPort() {
		return toPort;
	}

	public void setSourceGroup(String sourceGroup) {
		this.sourceGroup = sourceGroup;
	}

	public String getSourceGroup() {
		return sourceGroup;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getCidr() {
		return cidr;
	}

	public void setIdParent(String idparent) {
		this.idparent = idparent;
	}

	public String getIdParent() {
		return idparent;
	}

	public void setIdRule(String idrule) {
		this.idrule = idrule;
	}

	public String getIdRule() {
		return idrule;
	}

	public String toJSON() {
		String payload = "{\"security_group_rule\": \n"
				+ "{" +
					"\"ip_protocol\": \"" + getIpProtocol() + "\", \n"
					+ "\"from_port\": \"" + getFromPort() + "\", \n"
					+ "\"to_port\": \"" + getToPort() + "\", \n"
					+ "\"cidr\": \"" + getCidr() + "\", \n";

		if (!getSourceGroup().equals("")) {
			payload = payload + "\"group_id\": \"" + getSourceGroup()
					+ "\", \n";
		}
		payload = payload + "\"parent_group_id\": \"" + idparent + "\" }}";
		return payload;

	}
	
	@SuppressWarnings("unchecked")
	public void fromJson(JSONObject jsonNode) {
		ipProtocol = jsonNode.getString("ip_protocol");
		fromPort = jsonNode.getString("from_port");
		toPort = jsonNode.getString("to_port");
		sourceGroup = jsonNode.getString("group");
		idparent = jsonNode.getString("parent_group_id");
		cidr = (jsonNode.getJSONObject("ip_range")).getString("cidr");
	}

}
