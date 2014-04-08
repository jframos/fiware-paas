/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;

/**
 * Defines the methods needed to persist/to manage Artifact Type objects.
 * 
 * @author Jesus M. Movilla.
 */

@Transactional(propagation = Propagation.REQUIRED)
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
