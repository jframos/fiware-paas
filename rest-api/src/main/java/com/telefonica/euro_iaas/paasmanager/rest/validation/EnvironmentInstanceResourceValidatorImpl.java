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

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;

/**
 * * @author jesus.movilla
 * 
 */
public class EnvironmentInstanceResourceValidatorImpl implements
		EnvironmentInstanceResourceValidator {

	private ClaudiaUtil claudiaUtil;
	private ExtendedOVFUtil extendedOVFUtil;
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
	public void validateCreate(EnvironmentDto environmentDto)
			throws InvalidEnvironmentRequestException {
		if (environmentDto.getName() == null)
			throw new InvalidEnvironmentRequestException("EnvironamentName "
					+ "from EnviromentDto is null");

		if (environmentDto.getEnvironmentType() == null)
			throw new InvalidEnvironmentRequestException("EnvironamentType "
					+ "from EnviromentDto is null");

		if (environmentDto.getTierDtos() == null)
			throw new InvalidEnvironmentRequestException("There are no tiers "
					+ "defined in EnviromentDto object");
	}

	/**
	 * @param claudiaUtil
	 *            the claudiaUtil to set
	 */
	public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
		this.claudiaUtil = claudiaUtil;
	}

}
