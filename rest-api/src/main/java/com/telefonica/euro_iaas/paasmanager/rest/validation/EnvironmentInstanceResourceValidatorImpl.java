/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.GENERAL_ID;
import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.PRODUCTNAME_TAG;
import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.VIRTUALSYSTEMCOLLECTION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * * @author jesus.movilla
 */
public class EnvironmentInstanceResourceValidatorImpl implements EnvironmentInstanceResourceValidator {

    private ClaudiaUtil claudiaUtil;
    private ExtendedOVFUtil extendedOVFUtil;
    private EnvironmentInstanceDao environmentInstanceDao;
    private TierResourceValidator tierResourceValidator;

    private QuotaClient quotaClient;
    private ProductReleaseManager productReleaseManager;
    private ResourceValidator resourceValidator;

    /** The log. */
    private static Logger log = Logger.getLogger(EnvironmentInstanceResourceValidatorImpl.class);

    public void validateCreatePayload(String payload) throws InvalidEntityException {
        try {
            Document doc = claudiaUtil.stringToDom(payload);

            // EnvironmentName validation
            Node virtualSystem = doc.getElementsByTagName(VIRTUALSYSTEMCOLLECTION).item(0);
            if (virtualSystem == null)
                throw new InvalidEntityException("VirtualSystemCollection is null");

            Node environmentNameElement = virtualSystem.getAttributes().getNamedItem(GENERAL_ID);
            if (environmentNameElement == null)
                throw new InvalidEntityException("EnvironmentName is null");

            // ProductName and Version Validation
            NodeList productNameNodeList = doc.getElementsByTagName(extendedOVFUtil.PRODUCTNAME_TAG);

            NodeList productVersionNodeList = doc.getElementsByTagName(extendedOVFUtil.PRODUCTNAME_TAG);

            for (int i = 0; i < productNameNodeList.getLength(); i++) {
                Node productNameNode = doc.getElementsByTagName(extendedOVFUtil.PRODUCTNAME_TAG).item(i);
                if (productNameNode == null)
                    throw new InvalidEntityException("productName is null");
            }

            for (int i = 0; i < productVersionNodeList.getLength(); i++) {
                Node productVersionNode = doc.getElementsByTagName(PRODUCTNAME_TAG).item(i);
                if (productVersionNode == null)
                    throw new InvalidEntityException("productVersion is null");
            }

        } catch (SAXException e) {
            String errorMessage = "SAXException when obtaining ProductRelease." + " Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new InvalidEntityException(errorMessage);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException when obtaining " + "ProductRelease. Desc: "
                    + e.getMessage();
            log.error(errorMessage);
            throw new InvalidEntityException(errorMessage);
        } catch (IOException e) {
            String errorMessage = "IOException when obtaining " + "ProductRelease. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new InvalidEntityException(errorMessage);
        }

    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.rest.validation. EnvironmentInstanceResourceValidator
     * #validateCreate(com.telefonica.euro_iaas .paasmanager.model.dto.EnvironmentDto)
     */
    public void validateCreate(EnvironmentInstanceDto environmentInstanceDto,
            SystemPropertiesProvider systemPropertiesProvider, ClaudiaData claudiaData)
            throws InvalidEntityException, QuotaExceededException {

    	

        if (environmentInstanceDto.getEnvironmentDto() == null) {
            log.error("The environment to be deployed is null ");
            throw new InvalidEntityException("The environment to be deployed is null ");
        }


		resourceValidator.validateName (environmentInstanceDto.getBlueprintName());
		resourceValidator.validateDescription (environmentInstanceDto.getDescription());
	
        
        log.debug("Validate enviornment instance blueprint " + environmentInstanceDto.getBlueprintName()
                + " description " + environmentInstanceDto.getDescription() + " environment "
                + environmentInstanceDto.getEnvironmentDto());

        EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

        criteria.setVdc(claudiaData.getVdc());
        criteria.setEnviromentName(environmentInstanceDto.getBlueprintName());

        List<EnvironmentInstance> envInstances = environmentInstanceDao.findByCriteria(criteria);

        if (envInstances.size() != 0) {
            throw new InvalidEntityException("The environment instance "
                    + environmentInstanceDto.getBlueprintName() + " already exists");
        }

        if (environmentInstanceDto.getEnvironmentDto().getTierDtos() == null) {
            log.error("There are no tiers " + "defined in EnvironmentDto object");
            throw new InvalidEntityException("There are no tiers " + "defined in EnvironmentDto object");
        }

        String system = systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);
        if ("FIWARE".equals(system)) {
            for (TierDto tierDto : environmentInstanceDto.getEnvironmentDto().getTierDtos()) {
                validateTier(tierDto);
            }
            tierResourceValidator.validateTiersDependencies(environmentInstanceDto.getEnvironmentDto().getName(),
                    claudiaData.getVdc(), environmentInstanceDto.getEnvironmentDto().getTierDtos());
        }

        // Validating length of hostname (maximum =64)
        for (TierDto tierDto : environmentInstanceDto.getEnvironmentDto().getTierDtos()) {
            // String hostname = (claudiaData.getService() + "-"
            // + tier.getName() + "-"
            // + numReplica).toLowerCase();
            int hostnameLength = environmentInstanceDto.getBlueprintName().length() + tierDto.getName().length() + 5;
            if (hostnameLength > 64) {
                int exceed = hostnameLength - 64;
                String message = "Hostname is too long (over 64) exceeded by " + exceed + " characters . "
                        + "Please revise the length of " + "BluePrint Instance Name "
                        + environmentInstanceDto.getBlueprintName() + " and tierName " + tierDto.getName();
                log.error(message);
                throw new InvalidEntityException(message);
            }
        }

        validateQuota(claudiaData, environmentInstanceDto);
    }

    public void validateTier(TierDto tierDto) throws InvalidEntityException {

        if (tierDto.getMaximumNumberInstances() == null) {
            throw new InvalidEntityException("Maximum Number Instances " + "from tierDto is null");
        }
        if (tierDto.getMinimumNumberInstances() == null) {
            throw new InvalidEntityException("Minimum Number Instances " + "from tierDto is null");
        }
        if (tierDto.getInitialNumberInstances() == null) {
            throw new InvalidEntityException("Initial Number Instances " + "from tierDto is null");
        }
        if (tierDto.getName() == null) {
            throw new InvalidEntityException("Tier Name " + "from tierDto is null");
        }
        if (tierDto.getImage() == null) {
            throw new InvalidEntityException("Tier Image " + "from tierDto is null");
        }
        if (tierDto.getFlavour() == null) {
            throw new InvalidEntityException("Tier Flavour " + "from tierDto is null");
        }

    }

    public void validateQuota(ClaudiaData claudiaData, EnvironmentInstanceDto environmentInstanceDto)
            throws QuotaExceededException, InvalidEntityException {

        Map<String, Limits> limits = new HashMap<String, Limits>();

        Integer initialNumberInstances = 0;
        Integer floatingIPs = 0;
        List securityGroupList = new ArrayList<String>(2);

        if (environmentInstanceDto.getTierInstances() != null) {
            for (TierInstanceDto tierInstanceDto : environmentInstanceDto.getTierInstances()) {
                String region = tierInstanceDto.getTierDto().getRegion();
                if (!limits.containsKey(region)) {
                    try {
                        limits.put(region, quotaClient.getLimits(claudiaData, region));
                    } catch (InfrastructureException e) {
                        throw new InvalidEntityException("Failed in getLimits " + e.getMessage());
                    }
                }

                initialNumberInstances += tierInstanceDto.getTierDto().getInitialNumberInstances();
                if ("true".equals(tierInstanceDto.getTierDto().getFloatingip())) {
                    floatingIPs++;
                }
                String securityGroup = tierInstanceDto.getTierDto().getSecurityGroup();
                if (tierInstanceDto.getTierDto().getSecurityGroup() != null) {
                    if (!securityGroupList.contains(securityGroup)) {
                        securityGroupList.add(securityGroup);
                    }
                }

                Limits limitsRegion = limits.get(region);

                if (limitsRegion.checkTotalInstancesUsed()) {

                    if (initialNumberInstances + limitsRegion.getTotalInstancesUsed() > limitsRegion
                            .getMaxTotalInstances()) {
                        throw new QuotaExceededException("max number of instances exceeded: "
                                + limitsRegion.getMaxTotalInstances());
                    }
                }

                if (limitsRegion.checkTotalFloatingsIpsUsed()) {
                    if (floatingIPs + limitsRegion.getTotalFloatingIpsUsed() > limitsRegion.getMaxTotalFloatingIps()) {
                        throw new QuotaExceededException("max number of floating IPs exceeded: "
                                + limitsRegion.getMaxTotalFloatingIps());
                    }
                }

                if (limitsRegion.checkTotalSecurityGroupsUsed()) {
                    if (securityGroupList.size() + limitsRegion.getTotalSecurityGroups() > limitsRegion
                            .getMaxSecurityGroups()) {
                        throw new QuotaExceededException("max number of security groups exceeded: "
                                + limitsRegion.getMaxSecurityGroups());
                    }
                }

            }
        }
    }    

    /**
     * @param claudiaUtil
     *            the claudiaUtil to set
     */
    public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
        this.claudiaUtil = claudiaUtil;
    }

    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }

    public void setTierResourceValidator(TierResourceValidator tierResourceValidator) {
        this.tierResourceValidator = tierResourceValidator;
    }

    public QuotaClient getQuotaClient() {
        return quotaClient;
    }

    public void setQuotaClient(QuotaClient quotaClient) {
        this.quotaClient = quotaClient;
    }
    
    public void setResourceValidator (ResourceValidator resourceValidator) {
    	this.resourceValidator = resourceValidator;
    }

}
