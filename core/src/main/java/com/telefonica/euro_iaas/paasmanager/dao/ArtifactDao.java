/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

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
