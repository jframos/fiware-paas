package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ArtifactSearchCriteria;

/**
 * Defines the methods needed to persist Artifact objects.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
public interface ArtifactDao extends BaseDAO<Artifact, String> {

    /**
     * Find the artifacts that matches with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Artifact> findByCriteria(ArtifactSearchCriteria criteria);

}
