/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Metadata;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * * @author Henar Munoz
 */
public class TierResourceValidatorImpl implements TierResourceValidator {

    private TierManager tierManager;
    private static Logger log = Logger.getLogger(TierResourceValidatorImpl.class);
    private EnvironmentInstanceManager environmentInstanceManager;
    private EnvironmentManager environmentManager;

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.rest.validation. EnvironmentInstanceResourceValidator
     * #validateCreate(com.telefonica.euro_iaas .paasmanager.model.dto.EnvironmentDto)
     */
    public void validateCreate(TierDto tierDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException,
            AlreadyExistEntityException {

        if (tierDto == null) {
            log.error("Tier Name  is null");
            throw new InvalidEntityException(tierDto, new Exception("Tier Name " + " is null"));
        }

        try {
            tierManager.load(tierDto.getName(), vdc, environmentName);
            log.error("The tier " + tierDto.getName() + " already exists in ");
            throw new AlreadyExistEntityException("The tier " + tierDto.getName() + " already exists in vdc " + vdc
                    + " and environmentName " + environmentName);

        } catch (EntityNotFoundException e) {
            log.debug("Entity not found. It is possible to create it ");
            validateCreateTier(tierDto, systemPropertiesProvider);
        }

    }

    private void validateCreateTier(TierDto tierDto, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEntityException {
        String system = systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);
        if (tierDto.getName() == null) {
            log.error("Tier name is Null");
            throw new InvalidEntityException(tierDto, new Exception("Tier Name " + "from tierDto is null"));
        }
        if (tierDto.getMaximumNumberInstances() == null || tierDto.getMinimumNumberInstances() == null
                || tierDto.getInitialNumberInstances() == null) {
            log.error("Number initial, maximun o minimul from tierDto " + tierDto.getName() + " is null");
            throw new InvalidEntityException(tierDto, new Exception("Number initial, maximun o minimul  "
                    + "from tierDto is null"));
        }

        if (!(tierDto.getMinimumNumberInstances() <= tierDto.getInitialNumberInstances() && tierDto
                .getMaximumNumberInstances() >= tierDto.getInitialNumberInstances())) {
            String men = "Error in the Number initial " + tierDto.getInitialNumberInstances() + " with number min "
                    + tierDto.getMinimumNumberInstances() + " and number max " + tierDto.getMaximumNumberInstances();
            log.error(men);
            throw new InvalidEntityException(tierDto, new Exception(men));
        }

        if ("FIWARE".equals(system)) {
            if (tierDto.getImage() == null) {
                throw new InvalidEntityException(tierDto, new Exception("Tier Image " + "from tierDto is null"));
            }
            if (tierDto.getFlavour() == null) {
                throw new InvalidEntityException(tierDto, new Exception("Tier Flavour " + "from tierDto is null"));
            }

        }
    }

    public void validateUpdate(TierDto tierDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException, EntityNotFoundException {

        if (tierDto == null) {
            log.error("Tier Name  is null");
            throw new InvalidEntityException(tierDto, new Exception("Tier Name " + " is null"));
        }

        try {
            validateTierInEnvInstance(environmentName, vdc);
        } catch (InvalidEnvironmentRequestException e) {
            log.error("Invalid tier in env instance");
            throw new InvalidEntityException(tierDto, e);
        }

        String system = systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);

        try {
            Tier tier = tierManager.load(tierDto.getName(), vdc, environmentName);

        } catch (EntityNotFoundException e1) {
            log.error("The tier " + tierDto.getName() + " does not  exists vdc " + vdc + " environmentName "
                    + environmentName);
            throw new EntityNotFoundException(Tier.class, "The tier " + tierDto.getName() + " does not  exists vdc "
                    + vdc + " environmentName " + environmentName, e1.getMessage());
        }

        if (tierDto.getName() == null) {
            throw new InvalidEntityException(tierDto, new Exception("Tier Name " + "from tierDto is null"));
        }
        if (tierDto.getMaximumNumberInstances() == null || tierDto.getMinimumNumberInstances() == null
                || tierDto.getInitialNumberInstances() == null) {
            throw new InvalidEntityException(tierDto, new Exception("Number initial, maximun o minimul  "
                    + "from tierDto is null"));
        }
        if ("FIWARE".equals(system)) {
            if (tierDto.getImage() == null) {
                throw new InvalidEntityException(tierDto, new Exception("Tier Image " + "from tierDto is null"));
            }
            if (tierDto.getFlavour() == null) {
                throw new InvalidEntityException(tierDto, new Exception("Tier Flavour " + "from tierDto is null"));
            }

        }

    }

    public void validateDelete(String vdc, String environmentName, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEntityException, EntityNotFoundException {

        try {
            validateTierInEnvInstance(environmentName, vdc);
        } catch (InvalidEnvironmentRequestException e) {
            log.error("Invalid tier in env instance");
            throw new InvalidEntityException(new Tier(), e);
        }

    }

    private void validateTierInEnvInstance(String environmentName, String vdc)
            throws InvalidEnvironmentRequestException {
        Environment environment = null;

        try {
            environment = environmentManager.load(environmentName, vdc);
        } catch (EntityNotFoundException e) {
            log.error("The enviornment " + environmentName + " does not exist");
            throw new InvalidEnvironmentRequestException("The enviornment " + environmentName + " does not exist");
        }

        EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

        criteria.setVdc(vdc);
        criteria.setEnvironment(environment);

        List<EnvironmentInstance> envInstances = environmentInstanceManager.findByCriteria(criteria);

        if (envInstances != null && envInstances.size() != 0) {
            throw new InvalidEnvironmentRequestException("The enviornmetn is being used by an env instance");
        }

    }

    public void validateTiersDependencies(String environmentName, String vdc, List<TierDto> tierDtoList)
            throws InvalidEnvironmentRequestException {

        List<Tier> tiers = new ArrayList<Tier>(2);
        try {
            for (TierDto tierDto : tierDtoList) {

                Tier tier = tierManager.loadTierWithProductReleaseAndMetadata(tierDto.getName(), environmentName, vdc);
                tiers.add(tier);
            }
        } catch (EntityNotFoundException e) {
            log.error("Some tier in vdc " + vdc + " with environment " + environmentName + " does not exist");
        }

        List<String> allDependencies = createDependenciesForTiers(tiers);
        Map<String, String> allProducts = createProductList(tiers);
        boolean result = checkTierProductsInDependencyList(allDependencies, allProducts);
        if (!result) {
            String message = "Please review dependencies. Some productrelease is mandatory";
            throw new InvalidEnvironmentRequestException(message);
        }

    }

    /**
     * Check all the dependencies for a product in a map of dependencies.
     * 
     * @param productDependencies
     * @param dependenciesMap
     * @return
     */
    public boolean checkTierProductsInDependencyList(List<String> productDependencies,
            Map<String, String> dependenciesMap) {

        boolean exist = true;

        final Iterator<String> iterator = productDependencies.iterator();
        while (exist && iterator.hasNext()) {
            String productDependency = iterator.next();

            if (!dependenciesMap.containsKey(productDependency)) {
                exist = false;
            }
        }

        return exist;

    }

    public List<String> createDependenciesForTiers(List<Tier> tiers) {
        List<String> dependenciesList = new ArrayList<String>();

        for (Tier tier : tiers) {
            for (ProductRelease productRelease : tier.getProductReleases()) {

                List<Metadata> metadataList = productRelease.getMetadatas();
                for (Metadata metadata : metadataList) {
                    if (metadata.getKey().startsWith("dep")) {
                        dependenciesList.add(metadata.getValue());
                    }
                }
            }
        }

        return dependenciesList;
    }

    public Map<String, String> createProductList(List<Tier> tiers) {
        Map<String, String> productNameList = new HashMap<String, String>();

        for (Tier tier : tiers) {
            for (ProductRelease productRelease : tier.getProductReleases()) {

                productNameList.put(productRelease.getName() + "-" + productRelease.getVersion(), "");
            }
        }

        return productNameList;
    }

    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }

    public void setEnvironmentInstanceManager(EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

}
