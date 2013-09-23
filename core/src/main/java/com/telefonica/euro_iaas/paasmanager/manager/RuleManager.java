package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;

public interface RuleManager {

	/**
	 * Create an rule
	 * 
	 * @param rule
	 * @return the securityGroup.
	 */
	Rule create(ClaudiaData claudiaData, Rule rule)
			throws InvalidEntityException, AlreadyExistsEntityException,
			InfrastructureException;

	/**
	 * Destroy a previously creted rule.
	 * 
	 * @param rule
	 *            the candidate to rule
	 */
	void destroy(ClaudiaData claudiaData, Rule rule)
			throws InvalidEntityException, InfrastructureException;

	/**
	 * Find the rule using the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the rule
	 * @throws EntityNotFoundException
	 *             if the product instance does not exists
	 */
	Rule load(String name) throws EntityNotFoundException;

	/**
	 * Retrieve all Environment created in the system.
	 * 
	 * @return the existent environments.
	 */
	List<Rule> findAll();

}
