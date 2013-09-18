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

public class ArtifactDaoJpaImpl 
	extends AbstractBaseDao<Artifact, Long>  implements ArtifactDao {

	@Override
	public List<Artifact> findAll() {
		return super.findAll(Artifact.class);
	}

	@Override
	public Artifact load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(Artifact.class, "id", arg0);
	}

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
