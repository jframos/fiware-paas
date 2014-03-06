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
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
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

    private ExtendedOVFUtil extendedOVFUtil;

    private static Logger log = Logger.getLogger(EnvironmentManagerImpl.class);

    /**
     * Convert a list of tierDtos to a list of Tiers
     * 
     * @return
     */
    private Set<Tier> convertToTiers(Set<TierDto> tierDtos, String environmentName, String vdc) {
        Set<Tier> tiers = new HashSet<Tier>();
        for (TierDto tierDto : tierDtos) {
            Tier tier = tierDto.fromDto(vdc, environmentName);
            // tier.setSecurity_group("sg_"
            // +environmentName+"_"+vdc+"_"+tier.getName());
            tiers.add(tier);
        }
        return tiers;
    }

    public void delete(String org, String vdc, String envName) throws APIException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, envName);
        try {
            environmentResourceValidator.validateDelete(envName, vdc, systemPropertiesProvider);

            addCredentialsToClaudiaData(claudiaData);

            Environment env = environmentManager.load(envName, vdc);
            environmentManager.destroy(claudiaData, env);
        } catch (Exception e) {
            throw new APIException(e);
        }

    }

    /*
     * private List<Environment> filterEqualTiers(List<Environment> environments) { // List<Tier> tierResult = new
     * ArrayList<Tier>(); List<Environment> result = new ArrayList<Environment>(); for (Environment environment :
     * environments) { Set<Tier> tierResult = new HashSet<Tier>(); Set<Tier> tiers = environment.getTiers(); for (Tier
     * tier: tiers) { int i=0; List<Tier> tierAux = new ArrayList<Tier>(); for (int j = i + 1; j < tiers.size(); j++) {
     * tierAux.add(tiers.get(j)); } if (!tierAux.contains(tier)) { tierResult.add(tier); } i++; }
     * environment.setTiers(tierResult); result.add(environment); } return result; }
     */

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
        // List<Environment> envs = filterEqualTiers(env);

        List<EnvironmentDto> envsDto = new ArrayList<EnvironmentDto>();
        for (int i = 0; i < env.size(); i++) {
            envsDto.add(env.get(i).toDto());

        }
        return envsDto;
    }

    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }

    }

    /**
     * Add PaasManagerUser to claudiaData.
     * 
     * @param claudiaData
     */
    public void addCredentialsToClaudiaData(ClaudiaData claudiaData) {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {

            claudiaData.setUser(extendedOVFUtil.getCredentials());
            claudiaData.getUser().setTenantId(claudiaData.getVdc());
        }

    }

    public void insert(String org, String vdc, EnvironmentDto environmentDto) throws APIException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentDto.getName());

        log.debug("Create a environment " + environmentDto.getName() + " " + environmentDto.getDescription() + " "
                + environmentDto.getVdc() + " " + environmentDto.getOrg() + " " + environmentDto.getTierDtos());

        try {
            addCredentialsToClaudiaData(claudiaData);
            environmentResourceValidator.validateCreate(claudiaData, environmentDto, vdc);

            // try {
            environmentManager.create(claudiaData, environmentDto.fromDto(org, vdc));
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    public EnvironmentDto load(String org, String vdc, String name) throws APIException {

        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
        criteria.setVdc(vdc);
        criteria.setOrg(org);
        criteria.setEnvironmentName(name);

        List<Environment> env = environmentManager.findByCriteria(criteria);

        // Solve the tier-environment duplicity appeared at database due to hibernate problems
        // List<Environment> envs = filterEqualTiers(env);

        if (env == null || env.size() == 0) {
            throw new WebApplicationException(new EntityNotFoundException(Environment.class, "Environment " + name
                    + " not found", ""), ERROR_NOT_FOUND);
        } else {
            EnvironmentDto envDto = env.get(0).toDto();
            // EnvironmentDto envDto = env.get(0).toDto();
            return envDto;
        }

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

    public ExtendedOVFUtil getExtendedOVFUtil() {
        return extendedOVFUtil;
    }

    public void setExtendedOVFUtil(ExtendedOVFUtil extendedOVFUtil) {
        this.extendedOVFUtil = extendedOVFUtil;
    }

}
