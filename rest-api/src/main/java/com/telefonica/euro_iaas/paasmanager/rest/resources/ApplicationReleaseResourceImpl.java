/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Path;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.ArtifactManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
