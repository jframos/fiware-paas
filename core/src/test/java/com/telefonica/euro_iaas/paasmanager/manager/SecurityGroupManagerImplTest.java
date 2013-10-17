/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.SecurityGroupDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.SecurityGroupManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

/**
 * @author jesus.movilla
 * 
 */
public class SecurityGroupManagerImplTest extends TestCase {

	private SecurityGroupManagerImpl securityGroupManager;
	private FirewallingClient firewallingClient;
	private RuleManager ruleManager;
	private SecurityGroupDao securityGroupDao;

	private ProductReleaseManager productReleaseManager;

	@Before
	public void setUp() throws Exception {

		firewallingClient = mock(FirewallingClient.class);
		ruleManager = mock(RuleManager.class);
		securityGroupDao = mock(SecurityGroupDao.class);
		securityGroupManager = new SecurityGroupManagerImpl();
		securityGroupManager.setFirewallingClient(firewallingClient);
		securityGroupManager.setRuleManager(ruleManager);
		securityGroupManager.setSecurityGroupDao(securityGroupDao);

		when(
				firewallingClient.deploySecurityGroup(any(ClaudiaData.class),
						any(SecurityGroup.class))).thenReturn("2");

	}

	@Test
	public void testCreateSecurityGroup() throws Exception {

		SecurityGroup securityGroup = new SecurityGroup("name", "description");

		Rule rule = new Rule("TCP", "8080", "8080", "", "0.0.0.0/0");

		securityGroup.addRule(rule);

		ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "dd");

		Mockito.doThrow(
				new EntityNotFoundException(SecurityGroup.class, "test",
						securityGroup)).when(securityGroupDao).load(
				any(String.class));
		when(ruleManager.create(any(ClaudiaData.class), any(Rule.class)))
				.thenReturn(rule);
		when(securityGroupDao.create(any(SecurityGroup.class))).thenReturn(
				securityGroup);

		SecurityGroup securityGroup2 = securityGroupManager.create(claudiaData,
				securityGroup);

		System.out.println(securityGroup2.toJSON());

		assertNotNull(securityGroup2);
		assertEquals(securityGroup2.getName(), "name");
		assertEquals(securityGroup2.getDescription(), "description");
		assertEquals(securityGroup2.getRules().size(), 1);

	}

	@Test
	public void testDeleteSecurityGroup() throws Exception {

		SecurityGroup securityGroup = new SecurityGroup("name", "description");

		Rule rule = new Rule("TCP", "8080", "8080", "", "0.0.0.0/0");

		securityGroup.addRule(rule);

		ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "dd");

		Mockito.doNothing().doThrow(new RuntimeException()).when(ruleManager)
				.destroy(any(ClaudiaData.class), any(Rule.class));

		Mockito.doNothing().doThrow(new RuntimeException()).when(
				securityGroupDao).remove(any(SecurityGroup.class));

		Mockito.doNothing().doThrow(new RuntimeException()).when(
				firewallingClient).destroySecurityGroup(any(ClaudiaData.class),
				any(SecurityGroup.class));

		when(securityGroupDao.create(any(SecurityGroup.class))).thenReturn(
				securityGroup);

		securityGroupManager.destroy(claudiaData, securityGroup);

	}

}
