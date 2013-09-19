/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.manager.ArtifactManager;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;

/**
 * @author jesus.movilla
 *
 */
public class ArtifactManagerImpl implements ArtifactManager {

	private ArtifactDao artifactDao;
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.manager.ArtifactManager#load(java.lang.String)
	 */
	public Artifact load(String name) throws EntityNotFoundException {
		return artifactDao.load(name);
	}
	
	
	   /**
     * @param artifactDao the artifactDao to set
     */
    public void setArtifactDao(ArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
    }


}
