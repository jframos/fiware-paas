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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Environment implementation
 * 
 * @author Henar Munoz
 */
@Path("/catalog/org/{org}/environment")
@Component
@Scope("request")
public class AbstractEnvironmentResourceImpl implements AbstractEnvironmentResource {

    @InjectParam("environmentManager")
    private EnvironmentManager environmentManager;


    
    private EnvironmentResourceValidator environmentResourceValidator;
    
    private static Logger log = Logger.getLogger(AbstractEnvironmentResourceImpl.class);

    public void delete(String org, String envName) throws APIException {
        log.debug("Deleting env " + envName + " from org " + org);
        ClaudiaData claudiaData = new ClaudiaData(org, "", envName);
        try {
			environmentResourceValidator.validateDelete(envName,"");
		} catch (Exception e1) {
			 throw new APIException(e1);
		}

        try {
            Environment env = environmentManager.load(envName, "");
            environmentManager.destroy(claudiaData, env);
        } catch (Exception e) {
            throw new APIException(e);
        }

    }


    public List<EnvironmentDto> findAll(String org, Integer page, Integer pageSize, String orderBy, String orderType) {
        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();

        criteria.setOrg(org);

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

        List<Environment> env = environmentManager.findByCriteria(criteria);

        List<EnvironmentDto> envsDto = new ArrayList<EnvironmentDto>();
        for (int i = 0; i < env.size(); i++) {
            envsDto.add(env.get(i).toDto());

        }
        return envsDto;
       
    }

    public void insert(String org, EnvironmentDto environmentDto) throws APIException {
        log.debug("Inserting env " + environmentDto.getName() + " from org " + org);
        ClaudiaData claudiaData = new ClaudiaData (org, "",environmentDto.getName() );
        try {
            environmentManager.load(environmentDto.getName());
            throw new APIException(new AlreadyExistEntityException("The enviornment " + environmentDto.getName()
                    + " already exists"));

        } catch (EntityNotFoundException e1) {

            try {
                environmentResourceValidator.validateAbstractCreate(environmentDto);
                environmentManager.create(claudiaData, environmentDto.fromDto(org, ""));
            } catch (Exception e) {
                throw new APIException(e);
            }
        }

    }

    public EnvironmentDto load(String org, String name) throws APIException {
        log.debug("Loading env " + name + " from org " + org);
        try {
            Environment envInstance = environmentManager.load(name);
            EnvironmentDto envDto = envInstance.toDto();

            return envDto;
        } catch (EntityNotFoundException e) {
            throw new APIException(e);
        }
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    
    public void setEnvironmentResourceValidator(EnvironmentResourceValidator environmentResourceValidator) {
        this.environmentResourceValidator = environmentResourceValidator;
    }

}
