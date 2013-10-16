package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

public interface SecurityGroupManager {

	/**
	 * Create an securityGroup
	 * 
	 * @param securityGroup
	 * @return the securityGroup.
	 */
	SecurityGroup create(ClaudiaData claudiaData, SecurityGroup securityGroup)
			throws InvalidEntityException, InvalidEnvironmentRequestException,
			AlreadyExistsEntityException, InfrastructureException;

	/**
	 * Destroy a previously creted securityGroup.
	 * 
	 * @param securityGroup
	 *            the candidate to securityGroup
	 */
	void destroy(ClaudiaData claudiaData, SecurityGroup securityGroup)
			throws InvalidEntityException, InfrastructureException;

	/**
	 * Find the securityGroup using the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the securityGroup
	 * @throws EntityNotFoundException
	 *             if the product instance does not exists
	 */
	SecurityGroup load(String name) throws EntityNotFoundException;

	void addRule(ClaudiaData claudiaData, SecurityGroup securityGroup, Rule rule)
			throws InvalidEntityException, AlreadyExistsEntityException,
			InfrastructureException;

	/**
	 * Retrieve all Environment created in the system.
	 * 
	 * @return the existent environments.
	 */
	List<SecurityGroup> findAll();
	
	/**
	 * Updates the securityGroup
	 * @param securityGroup
	 * @return
	 */
	SecurityGroup update(SecurityGroup securityGroup) throws InvalidEntityException;

}
