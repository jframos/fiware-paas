/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;

/**
 * Defines the methods needed to persist/to manage Artifact Type objects.
 * 
 * @author Jesus M. Movilla.
 */

public class ArtifactTypeDaoJpaImpl extends AbstractBaseDao<ArtifactType, String> implements ArtifactTypeDao {

    /*
     * Find all the ArtifactTypes in paas-manager database
     * @return artifactTypes, the list of artifactTypes
     */
    public List<ArtifactType> findAll() {
        return super.findAll(ArtifactType.class);
    }

    /*
     * Find an artifactType by name-searching
     * @param name, the name of the artifactType
     * @return artifactType, the artifactType
     */
    public ArtifactType load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ArtifactType.class, "name", arg0);
    }

}
