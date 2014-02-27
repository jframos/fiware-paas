/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Environment implementation
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/org/{org}/environment")
@Component
@Scope("request")
public class AbstractEnvironmentResourceImpl implements AbstractEnvironmentResource {

    @InjectParam("environmentManager")
    private EnvironmentManager environmentManager;

    private SystemPropertiesProvider systemPropertiesProvider;
    
    private EnvironmentResourceValidator environmentResourceValidator;

    public void delete(String org, String envName) throws APIException {
        ClaudiaData claudiaData = new ClaudiaData(org, null, null);

        try {
            Environment env = environmentManager.load(envName);
            environmentManager.destroy(claudiaData, env);

        } catch (EntityNotFoundException e) {
            throw new APIException(e, 404);
        } catch (InfrastructureException e) {
            throw new APIException(e, 500);
        } catch (InvalidEntityException e) {
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

        List<Environment> envs = environmentManager.findByCriteria(criteria);

        List<EnvironmentDto> envsDto = new ArrayList<EnvironmentDto>();
        for (Environment e: envs) {          
            envsDto.add(e.toDto());
        }
        return envsDto;
    }

    public void insert(String org, EnvironmentDto environmentDto) throws APIException {
        
        try {
            environmentManager.load(environmentDto.getName());
            throw new APIException(new AlreadyExistEntityException("The enviornment " + environmentDto.getName()
                    + " already exists"));

        } catch (EntityNotFoundException e1) {

            try {
                environmentResourceValidator.validateAbstractCreate(environmentDto);
                environmentManager.create(null, environmentDto.fromDto());
            } catch (Exception e) {
                throw new APIException(e);
            }
        }

    }

    public EnvironmentDto load(String org, String name) throws APIException {
        try {
            Environment envInstance = environmentManager.load(name);
            EnvironmentDto envDto = envInstance.toDto();

            return envDto;
        } catch (EntityNotFoundException e) {
            throw new APIException(e, 404);
        }
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
    
    public void setEnvironmentResourceValidator(EnvironmentResourceValidator environmentResourceValidator) {
        this.environmentResourceValidator = environmentResourceValidator;
    }


    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        else
            return null;
    }
}
