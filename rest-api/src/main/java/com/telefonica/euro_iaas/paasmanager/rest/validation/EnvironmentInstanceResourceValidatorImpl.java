/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.rest.validation;

import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.GENERAL_ID;
import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.PRODUCTNAME_TAG;
import static com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil.VIRTUALSYSTEMCOLLECTION;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * * @author jesus.movilla
 * 
 */
public class EnvironmentInstanceResourceValidatorImpl implements
		EnvironmentInstanceResourceValidator {

	private ClaudiaUtil claudiaUtil;
	private ExtendedOVFUtil extendedOVFUtil;
	private EnvironmentInstanceDao environmentInstanceDao;
	private TierResourceValidator tierResourceValidator;
	/** The log. */
	private static Logger log = Logger
			.getLogger(EnvironmentInstanceResourceValidatorImpl.class);

	public void validateCreatePayload(String payload)
			throws InvalidEnvironmentRequestException {
		try {
			Document doc = claudiaUtil.stringToDom(payload);

			// EnvironmentName validation
			Node virtualSystem = doc.getElementsByTagName(
					VIRTUALSYSTEMCOLLECTION).item(0);
			if (virtualSystem == null)
				throw new InvalidEnvironmentRequestException(
						"VirtualSystemCollection is null");

			Node environmentNameElement = virtualSystem.getAttributes()
					.getNamedItem(GENERAL_ID);
			if (environmentNameElement == null)
				throw new InvalidEnvironmentRequestException(
						"EnvironmentName is null");

			// ProductName and Version Validation
			NodeList productNameNodeList = doc
					.getElementsByTagName(extendedOVFUtil.PRODUCTNAME_TAG);

			NodeList productVersionNodeList = doc
					.getElementsByTagName(extendedOVFUtil.PRODUCTNAME_TAG);

			for (int i = 0; i < productNameNodeList.getLength(); i++) {
				Node productNameNode = doc.getElementsByTagName(
						extendedOVFUtil.PRODUCTNAME_TAG).item(i);
				if (productNameNode == null)
					throw new InvalidEnvironmentRequestException(
							"productName is null");
			}

			for (int i = 0; i < productVersionNodeList.getLength(); i++) {
				Node productVersionNode = doc.getElementsByTagName(
						PRODUCTNAME_TAG).item(i);
				if (productVersionNode == null)
					throw new InvalidEnvironmentRequestException(
							"productVersion is null");
			}

		} catch (SAXException e) {
			String errorMessage = "SAXException when obtaining ProductRelease."
					+ " Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining "
					+ "ProductRelease. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining "
					+ "ProductRelease. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.telefonica.euro_iaas.paasmanager.rest.validation.
	 * EnvironmentInstanceResourceValidator
	 * #validateCreate(com.telefonica.euro_iaas
	 * .paasmanager.model.dto.EnvironmentDto)
	 */
	public void validateCreate(EnvironmentInstanceDto environmentInstanceDto,
			SystemPropertiesProvider systemPropertiesProvider)
			throws InvalidEnvironmentRequestException {

		log.debug("Validate enviornment instance blueprint "
				+ environmentInstanceDto.getBlueprintName() + " description "
				+ environmentInstanceDto.getDescription() + " environment "
				+ environmentInstanceDto.getEnvironmentDto());
		if (environmentInstanceDto.getBlueprintName() == null) {
			log.error("EnvironamentBlueprintName "
					+ "from EnviromentDto BlueprintName is null");
			throw new InvalidEnvironmentRequestException(
					"EnvironamentBlueprintName "
							+ "from EnviromentDto BlueprintName is null");
		}

		if (environmentInstanceDto.getEnvironmentDto() == null) {
			log.error("The environment to be deployed is null ");
			throw new InvalidEnvironmentRequestException(
					"The environment to be deployed is null ");
		}

		
			EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

			  criteria.setVdc(environmentInstanceDto.getVdc());
			  criteria.setEnviromentName(environmentInstanceDto.getBlueprintName());
				

			 List<EnvironmentInstance> envInstances = environmentInstanceDao.findByCriteria(criteria);
			
			 if (envInstances.size() != 0) {
				 throw new InvalidEnvironmentRequestException(
					new AlreadyExistsEntityException(EnvironmentInstance.class, new Exception ("The enviornment instance "
							+ environmentInstanceDto.getBlueprintName())));
			 }
		
			if (environmentInstanceDto.getDescription() == null) {
				log.error("EnvironamentDescription "
						+ "from EnviromentDto Description is null");
				throw new InvalidEnvironmentRequestException(
						"EnvironamentDescription "
								+ "from EnviromentDto Description is null");
			}

			if (environmentInstanceDto.getEnvironmentDto().getTierDtos() == null) {
				log.error("There are no tiers "
						+ "defined in EnviromentDto object");
				throw new InvalidEnvironmentRequestException(
						"There are no tiers "
								+ "defined in EnviromentDto object");
			}

			String system = systemPropertiesProvider
					.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);
			if ("FIWARE".equals(system)) {
				for (TierDto tierDto : environmentInstanceDto
						.getEnvironmentDto().getTierDtos())
					validateTiers(tierDto);
			}
		
			//Validating length of hostname (maximum =64)
			for (int i=0; i < environmentInstanceDto.getEnvironmentDto().getTierDtos().size(); 
					i++){
				TierDto tierDto = environmentInstanceDto.getEnvironmentDto()
						.getTierDtos().get(i);
				//String hostname = (claudiaData.getService() + "-"
				//					 + tier.getName() + "-"
				//					+ numReplica).toLowerCase();
				int hostnameLength = 
						environmentInstanceDto.getBlueprintName().length() + 
						tierDto.getName().length() + 5;
				if ( hostnameLength > 64 ) {
					int exceed = hostnameLength - 64;
					String message = "Hostname is too long (over 64) Exceded by " 
							+ exceed + " characters . " +
							"Please revise the length of " +
							"BluePrint Instance Name " + environmentInstanceDto.getBlueprintName() +
							" and tierName " + tierDto.getName();
					log.error(message);
					throw new InvalidEnvironmentRequestException(message);
				}
			}
	}

	public void validateTiers(TierDto tierDto)
			throws InvalidEnvironmentRequestException {

		if (tierDto.getName() == null)
			throw new InvalidEnvironmentRequestException("Tier Name "
					+ "from tierDto is null");
		if (tierDto.getImage() == null)
			throw new InvalidEnvironmentRequestException("Tier Image "
					+ "from tierDto is null");
		if (tierDto.getFlavour() == null)
			throw new InvalidEnvironmentRequestException("Tier Flavour "
					+ "from tierDto is null");

	}
	


	/**
	 * @param claudiaUtil
	 *            the claudiaUtil to set
	 */
	public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
		this.claudiaUtil = claudiaUtil;
	}

	public void setEnvironmentInstanceDao(
			EnvironmentInstanceDao environmentInstanceDao) {
		this.environmentInstanceDao = environmentInstanceDao;
	}

	public void setTierResourceValidator(
			TierResourceValidator tierResourceValidator) {
		this.tierResourceValidator = tierResourceValidator;
	}

}
