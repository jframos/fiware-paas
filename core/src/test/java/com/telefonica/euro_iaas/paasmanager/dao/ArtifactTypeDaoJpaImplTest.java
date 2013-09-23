/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;

public class ArtifactTypeDaoJpaImplTest extends AbstractJpaDaoTest {
	// implements ArtifactTypeDao {

	// private ArtifactTypeDao artifactTypeDao;

	/**
	 * Create the artifactType row in the database
	 * 
	 * @param, the artifactType to be inserted
	 * @return artifactType
	 */
	/*
	 * public ArtifactType create(ArtifactType artifactType) throws
	 * InvalidEntityException, AlreadyExistsEntityException { artifactType =
	 * artifactTypeDao.create(artifactType);
	 * assertNotNull(artifactType.getId()); return artifactType; }
	 */

	/**
	 * List all the artifactTypes stored in the paas-maager system
	 * 
	 * @return artifactTypes
	 */
	/*
	 * public List<ArtifactType> findAll() { return artifactTypeDao.findAll(); }
	 */

	/**
	 * Load an artifactTpe by the name
	 * 
	 * @param name
	 *            , the name
	 * @return artifactType
	 */
	/*
	 * public ArtifactType load(String arg0) throws EntityNotFoundException {
	 * ArtifactType artifactType =
	 * artifactTypeDao.load(artifactTypeDao.findAll().get(0).getName());
	 * assertNotNull(artifactType.getId()); return artifactType; }
	 */

	/**
	 * Update the artifactType object in paas-manager system
	 * 
	 * @param artifactType
	 *            , the artifactType object
	 * @return artifactType
	 */
	/*
	 * public ArtifactType update(ArtifactType arg0) throws
	 * InvalidEntityException { ArtifactType artifactType =
	 * artifactTypeDao.findAll().get(0);
	 * artifactType.setDescription("Description2");
	 * 
	 * artifactType = artifactTypeDao.update(artifactType);
	 * assertEquals(artifactType.getDescription(), "Description2");
	 * 
	 * return artifactType; }
	 */

	/**
	 * Remove the artifactType object from the paas-manager system
	 * 
	 * @param artifactType
	 *            , the artifactType object
	 */
	/*
	 * public void remove(ArtifactType artifactType) {
	 * artifactTypeDao.remove(artifactType); }
	 */

	/**
	 * @param artifactTypeDao
	 *            the artifactTypeDao to set
	 */
	/*
	 * public void setArtifactTypeDao(ArtifactTypeDao artifactTypeDao) {
	 * this.artifactTypeDao = artifactTypeDao; }
	 */

}
