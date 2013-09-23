package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.SecurityGroupDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;

import com.telefonica.euro_iaas.paasmanager.manager.RuleManager;
import com.telefonica.euro_iaas.paasmanager.manager.SecurityGroupManager;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

public class SecurityGroupManagerImpl implements SecurityGroupManager {

	private SecurityGroupDao securityGroupDao = null;
	private RuleManager ruleManager = null;
	private FirewallingClient firewallingClient = null;

	private static Logger log = Logger
			.getLogger(SecurityGroupManagerImpl.class);

	public SecurityGroup create(ClaudiaData claudiaData,
			SecurityGroup securityGroup) throws InvalidEntityException,
			AlreadyExistsEntityException, InfrastructureException {

		SecurityGroup securityGroupDB = new SecurityGroup();
		String idSecurityGroup = firewallingClient.deploySecurityGroup(
				claudiaData, securityGroup);
		log.debug("Create security group " + securityGroup.getName()
				+ " with idSecuritygropu " + idSecurityGroup);

		if (securityGroup.getRules() != null) {
			for (Rule rule : securityGroup.getRules()) {
				rule.setIdParent(idSecurityGroup);
				rule = ruleManager.create(claudiaData, rule);
				securityGroupDB.addRule(rule);
			}
		}
		securityGroup.setIdSecurityGroup(idSecurityGroup);
		securityGroupDB = insert(securityGroup);
		return securityGroupDB;
	}

	public void addRule(ClaudiaData claudiaData, SecurityGroup securityGroup,
			Rule rule) throws InvalidEntityException,
			AlreadyExistsEntityException, InfrastructureException {

		rule.setIdParent(securityGroup.getIdSecurityGroup());
		rule = ruleManager.create(claudiaData, rule);
		securityGroup.addRule(rule);
		securityGroupDao.update(securityGroup);

	}

	public SecurityGroup insert(SecurityGroup securityGroup)
			throws AlreadyExistsEntityException, InvalidEntityException {
		SecurityGroup securityGroupDB = new SecurityGroup();
		try {
			securityGroupDB = securityGroupDao.load(securityGroup.getName());
			String errorMessage = "The securityGroup Object "
					+ securityGroup.getName() + " already exist in Database";
			throw new InvalidEntityException(errorMessage);

		} catch (EntityNotFoundException e) {
		}

		securityGroupDB.setName(securityGroup.getName());
		securityGroupDB.setDescription(securityGroup.getDescription());
		securityGroupDB.setIdSecurityGroup(securityGroup.getIdSecurityGroup());

		if (securityGroup.getRules() != null) {
			securityGroupDB.setRules(securityGroup.getRules());
		}

		try {
			securityGroupDB = securityGroupDao.create(securityGroupDB);
		} catch (InvalidEntityException iee) {
			throw new InvalidEntityException(securityGroupDB);
		} catch (AlreadyExistsEntityException aee) {
			throw new AlreadyExistsEntityException(securityGroup);
		} catch (Exception aee) {
			String errorMessage = "The securityGroup Object "
					+ securityGroupDB.getName() + " is " + "NOT valid";
			throw new InvalidEntityException(errorMessage);
		}

		return securityGroupDB;
	}

	public void destroy(ClaudiaData claudiaData, SecurityGroup securityGroup)
			throws InvalidEntityException, InfrastructureException {

		List<Rule> rules = new ArrayList<Rule>();
		for (Rule rule : securityGroup.getRules()) {
			rules.add(rule);
		}

		if (securityGroup.getRules() != null) {
			securityGroup.setRules(null);
			securityGroupDao.update(securityGroup);

			for (Rule rule : rules) {

				ruleManager.destroy(claudiaData, rule);

			}
		}

		firewallingClient.destroySecurityGroup(claudiaData, securityGroup);
		try {
			securityGroupDao.remove(securityGroup);
		} catch (Exception e) {
			throw new InfrastructureException(e.getMessage());
		}

	}

	public List<SecurityGroup> findAll() {
		return securityGroupDao.findAll();
	}

	public SecurityGroup load(String name) throws EntityNotFoundException {
		return securityGroupDao.load(name);
	}

	public void setSecurityGroupDao(SecurityGroupDao securityGroupDao) {
		this.securityGroupDao = securityGroupDao;
	}

	public void setRuleManager(RuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}

	public void setFirewallingClient(FirewallingClient firewallingClient) {
		this.firewallingClient = firewallingClient;
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.manager.SecurityGroupManager#update(com.telefonica.euro_iaas.paasmanager.model.SecurityGroup)
	 */
	public SecurityGroup update(SecurityGroup securityGroup) throws InvalidEntityException {
		return securityGroupDao.update(securityGroup);
	}

}
