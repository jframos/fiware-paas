/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Environment implementation
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/org/{org}/vdc/{vdc}/environment")
@Component
@Scope("request")
public class EnvironmentResourceImpl implements EnvironmentResource {

    public static final int ERROR_NOT_FOUND = 404;
    public static final int ERROR_REQUEST = 500;

    private EnvironmentManager environmentManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    private EnvironmentResourceValidator environmentResourceValidator;

    private OVFGeneration ovfGeneration;

    private static Logger log = Logger.getLogger(EnvironmentManagerImpl.class);

    /**
     * Convert a list of tierDtos to a list of Tiers
     * 
     * @return
     */
    private List<Tier> convertToTiers(List<TierDto> tierDtos, String environmentName, String vdc) {
        List<Tier> tiers = new ArrayList<Tier>();
        for (int i = 0; i < tierDtos.size(); i++) {
            Tier tier = tierDtos.get(i).fromDto();
            // tier.setSecurity_group("sg_"
            // +environmentName+"_"+vdc+"_"+tier.getName());
            tiers.add(tier);
        }
        return tiers;
    }

    public void delete(String org, String vdc, String envName) throws EnvironmentInstanceNotFoundException,
    InvalidEntityException, InvalidEnvironmentRequestException, AlreadyExistEntityException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, envName);
        environmentResourceValidator.validateDelete(envName, vdc, systemPropertiesProvider);

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(getCredentials());
        }
        try {
            Environment env = environmentManager.load(envName, vdc);
            environmentManager.destroy(claudiaData, env);

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, ERROR_NOT_FOUND);
        } catch (InfrastructureException e) {
            throw new WebApplicationException(e, ERROR_REQUEST);
        }

    }

    private List<Environment> filterEqualTiers(List<Environment> environments) {
        // List<Tier> tierResult = new ArrayList<Tier>();
        List<Environment> result = new ArrayList<Environment>();

        for (Environment environment : environments) {
            List<Tier> tierResult = new ArrayList<Tier>();
            List<Tier> tiers = environment.getTiers();
            for (int i = 0; i < tiers.size(); i++) {
                Tier tier = tiers.get(i);
                List<Tier> tierAux = new ArrayList<Tier>();
                for (int j = i + 1; j < tiers.size(); j++) {
                    tierAux.add(tiers.get(j));
                }
                if (!tierAux.contains(tier))
                    tierResult.add(tier);
            }
            environment.setTiers(tierResult);
            result.add(environment);
        }
        return result;
    }

    public List<EnvironmentDto> findAll(String org, String vdc, Integer page, Integer pageSize, String orderBy,
            String orderType) {
        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();

        criteria.setVdc(vdc);
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

        // Solve the tier-environment duplicity appeared at database due to hibernate problems
        List<Environment> envs = filterEqualTiers(env);

        List<EnvironmentDto> envsDto = new ArrayList<EnvironmentDto>();
        for (int i = 0; i < envs.size(); i++) {
            envsDto.add(envs.get(i).toDto());

        }
        return envsDto;
    }

    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        else
            return null;
    }

    public void insert(String org, String vdc, EnvironmentDto environmentDto)
    throws InvalidEnvironmentRequestException, AlreadyExistEntityException, InvalidEntityException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentDto.getName());

        log.debug("Create a environment " + environmentDto.getName() + " " + environmentDto.getDescription() + " "
                + environmentDto.getVdc() + " " + environmentDto.getOrg() + " " + environmentDto.getTierDtos());

        // try
        // {
        environmentResourceValidator.validateCreate(environmentDto, vdc, systemPropertiesProvider);
        /*
         * } catch (InvalidEnvironmentRequestException e) { throw new WebApplicationException(e, ERROR_REQUEST); } catch
         * (AlreadyExistEntityException e) { throw new WebApplicationException(e, ERROR_REQUEST); } catch
         * (InvalidEntityException e) { throw new WebApplicationException(e, ERROR_REQUEST); }
         */

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(getCredentials());
        }

        Environment environment = new Environment();
        environment.setName(environmentDto.getName());
        environment.setDescription(environmentDto.getDescription());

        /*
         * String payload = ovfGeneration.createOvf(environmentDto); environment.setOvf(payload);
         */
        if (environmentDto.getTierDtos() != null) {
            environment.setTiers(convertToTiers(environmentDto.getTierDtos(), environment.getName(), vdc));
        }
        environment.setOrg(org);
        environment.setVdc(vdc);
        // try {
        environmentManager.create(claudiaData, environment);
        // } catch (InvalidEnvironmentRequestException e) {
        // TODO Auto-generated catch block
        // throw new WebApplicationException(e.getCause(), ERROR_REQUEST);
        // }
    }

    public EnvironmentDto load(String org, String vdc, String name) throws EnvironmentInstanceNotFoundException {

        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
        criteria.setVdc(vdc);
        criteria.setOrg(org);
        criteria.setEnvironmentName(name);

        List<Environment> env = environmentManager.findByCriteria(criteria);

        // Solve the tier-environment duplicity appeared at database due to hibernate problems
        List<Environment> envs = filterEqualTiers(env);

        if (env == null || env.size() == 0) {
            throw new WebApplicationException(new EntityNotFoundException(Environment.class, "Environmetn " + name
                    + " not found", ""), ERROR_NOT_FOUND);
        } else {
            EnvironmentDto envDto = envs.get(0).toDto();
            // EnvironmentDto envDto = env.get(0).toDto();
            return envDto;
        }

        /*
         * try { return environmentManager.load(name, vdc).toDto(); } catch (EntityNotFoundException e) { throw new
         * EnvironmentInstanceNotFoundException (e); }
         */

    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public void setEnvironmentResourceValidator(EnvironmentResourceValidator environmentResourceValidator) {
        this.environmentResourceValidator = environmentResourceValidator;
    }

    /**
     * @param ovfGeneration
     *            the ovfGeneration to set
     */
    public void setOvfGeneration(OVFGeneration ovfGeneration) {
        this.ovfGeneration = ovfGeneration;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
