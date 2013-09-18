/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ArtifactSearchCriteria;

/**
 * Defines the methods needed to persist/to manage Artifact objects.
 *
 * @author Jesus M. Movilla.
 */

public class ArtifactDaoJpaImpl 
	extends AbstractBaseDao<Artifact, String>  implements ArtifactDao {

    /*
     * Find all the Artifacts in paas-manager database
     * @return artifacts, the list of artifacts
     */
	@Override
	public List<Artifact> findAll() {
		return super.findAll(Artifact.class);
	}
	
	/*
     * Find an artifact by name-searching
     * @param name, the name of the artifact
     * @return artifact, the artifact
     */
	@Override
	public Artifact load(String name) throws EntityNotFoundException {
        return super.loadByField(Artifact.class, "name", name);
	}
	/*
     * Find a list of Artifacts from the paas-manager database
     * @param crteria, the criteria to perform the search
     * @return artifacts, the list of artifacts
     */
	@Override
	public List<Artifact> findByCriteria(ArtifactSearchCriteria criteria) {
		Session session = (Session) getEntityManager().getDelegate();
	    Criteria baseCriteria = session.createCriteria(ProductRelease.class);
	    
	    if (criteria.getProductRelease() != null) {
	    	baseCriteria.add(Restrictions.eq("tier", criteria.getProductRelease()));
	    }
	    
	    List<Artifact> artifacts = setOptionalPagination(criteria, 
	        		baseCriteria).list();
	        
	    return artifacts;
	}
}
