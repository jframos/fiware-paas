/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.AbstractJpaDaoTest;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;

import com.telefonica.euro_iaas.paasmanager.model.Environment;



public class EnvironmenDaoTest extends AbstractJpaDaoTest{
	EnvironmentDao environmentDao;
	public static String ENVIRONMENT_NAME = "ENVIRONMENT_NAME";
	public static String ORG = "org";
	 
	/**
     * Test the create  method
     */
    @Test
    public void testEnvironmentNoTiers() throws Exception {

        Environment environment = new Environment();
        environment.setName(ENVIRONMENT_NAME);
        environment.setOrg(ORG);
        environment.setDescription("description");
        environment = environmentDao.create(environment);     
        assertNotNull(environment);
        assertNotNull(environment.getId());

    }
    
	/**
     * Test the load  method
     */
    @Test
    public void testLoadNoTiers() throws Exception {

        Environment environment = new Environment ();
        environment.setName(ENVIRONMENT_NAME);
        environment.setOrg(ORG);
        environment.setVdc("vdc");
        environment.setDescription("description");
        environment = environmentDao.create(environment);    
        environment = environmentDao.load(environment.getName(), "vdc");
        assertNotNull(environment);
        assertNotNull(environment.getId());

    }
    
	/**
     * Test the load  method
	 * @throws AlreadyExistsEntityException 
	 * @throws InvalidEntityException 
	 * @throws EntityNotFoundException 
     */
    @Test(expected = com.telefonica.euro_iaas.commons.dao.EntityNotFoundException.class)
    public void testDeleteNoTiers() throws InvalidEntityException, AlreadyExistsEntityException  {

        Environment environment = new Environment ();
        environment.setName(ENVIRONMENT_NAME);
        environment.setDescription("description");
        environment.setOrg(ORG);
        environment.setVdc("vdc");
        environment = environmentDao.create(environment);    
        environmentDao.remove(environment);
        try {
        	environmentDao.load(environment.getName(), "vdc");
        	fail("Should have thrown an EntityNotFoundException because the environment does not exit!");
        } catch (EntityNotFoundException e) {
        	assertNotNull(e);
        }
        

    }
  
    
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
    	this.environmentDao=environmentDao;
    }

}
