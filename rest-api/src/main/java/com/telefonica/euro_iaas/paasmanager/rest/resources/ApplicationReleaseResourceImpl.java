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

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.ArtifactManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;

/**
 * @author jesus.movilla
 */
@Path("/catalog/application")
@Component
@Scope("request")
public class ApplicationReleaseResourceImpl implements ApplicationReleaseResource {

    @InjectParam("applicationReleaseManager")
    private ApplicationReleaseManager applicationReleaseManager;
    @InjectParam("artifactManager")
    private ArtifactManager artifactManager;

    private static Logger LOGGER = Logger.getLogger("ApplicationReleaseResourceImpl");

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.rest.resources. ApplicationReleaseResource#findAll(java.lang.Integer,
     * java.lang.Integer, java.lang.String, java.lang.String)
     */
    public List<ApplicationRelease> findAll(String artifactName, Integer page, Integer pageSize, String orderBy,
            String orderType) {

        ApplicationReleaseSearchCriteria criteria = new ApplicationReleaseSearchCriteria();

        // For the artifact we must look for artifact in data base
        if (!StringUtils.isEmpty(artifactName)) {
            try {
                Artifact artifact = artifactManager.load(artifactName);
                criteria.setArtifact(artifact);
            } catch (EntityNotFoundException e) {
                throw new PaasManagerServerRuntimeException("Can not find the artifact " + artifactName, e);
            }
        }

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return applicationReleaseManager.findByCriteria(criteria);

    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.rest.resources. ApplicationReleaseResource#load(java.lang.String)
     */
    public ApplicationRelease load(String name) throws EntityNotFoundException {
        return applicationReleaseManager.load(name);
    }

}
