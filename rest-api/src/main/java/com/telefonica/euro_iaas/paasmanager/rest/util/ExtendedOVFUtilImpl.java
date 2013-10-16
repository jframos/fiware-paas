/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 * 
 */
public class ExtendedOVFUtilImpl implements ExtendedOVFUtil {

	private ClaudiaUtil claudiaUtil;
	private SystemPropertiesProvider systemPropertiesProvider;
	private OVFUtils ovfUtils;

	/** The log. */
	private static Logger log = Logger.getLogger(ExtendedOVFUtilImpl.class);
	private final String saxException = "SAXException when obtaining ";

	/**
	 * Get the EnvironmentName from the ovf (Instantiateovfparams.Name)
	 * 
	 * @param ovf
	 * @return the environmentName
	 */
	public String getEnvironmentName(String payload)
			throws InvalidEnvironmentRequestException {

		String envInstanceName = null;
		try {
			Document doc = claudiaUtil.stringToDom(payload);

			Node virtualSystemCollection = doc.getElementsByTagName(
					INSTANTIATEOVFPARAMS).item(0);
			envInstanceName = virtualSystemCollection.getAttributes()
					.getNamedItem(INSTANTIATEOVFPARAMS_NAME).getTextContent();

		} catch (SAXException e) {
			String errorMessage = saxException
					+ "EnvironmentInstanceName. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining "
					+ "EnvironmentInstanceName. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining "
					+ "EnvironmentInstanceName. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpcted exception. Maybe the  " + "tag "
					+ INSTANTIATEOVFPARAMS + "is not present anymore "
					+ "in the payload: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}
		return envInstanceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil#getTiers
	 * (java.lang.String)
	 */
	public List<Tier> getTiers(String payload, String vdc)
			throws InvalidEnvironmentRequestException {
		List<Tier> tiers = new ArrayList<Tier>();
		List<String> ovfSingleVM = null;
		try {
			ovfSingleVM = ovfUtils.getOvfsSingleVM(payload);
		} catch (InvalidOVFException e) {
			String errorMessage = "Error splitting up the main ovf in single"
					+ "VM ovfs. Description. " + e.getMessage();
			log.error(errorMessage);
			// throw new InfrastructureException(errorMessage);
		}
		try {
			Document doc = claudiaUtil.stringToDom(payload);
			NodeList virtualSystems = doc
					.getElementsByTagName(VIRTUALSYTEM_SECTION);

			for (int i = 0; i < virtualSystems.getLength(); i++) {
				Tier tier = new Tier();
				Node tierNode = virtualSystems.item(i);

				String tierName = virtualSystems.item(i).getAttributes()
						.getNamedItem(GENERAL_ID).getTextContent();
				tier.setName(tierName);

				Integer numberMaxInstances = Integer.parseInt(tierNode
						.getAttributes().getNamedItem(NUMBER_MAX_INSTANCES)
						.getTextContent());
				tier.setMaximumNumberInstances(numberMaxInstances);

				Integer numberMinInstances = Integer.parseInt(tierNode
						.getAttributes().getNamedItem(NUMBER_MIN_INSTANCES)
						.getTextContent());
				tier.setMinimumNumberInstances(numberMinInstances);

				Integer initialNumberInstances = Integer.parseInt(tierNode
						.getAttributes().getNamedItem(INITIAL_NUMBER_INSTANCES)
						.getTextContent());
				tier.setInitialNumberInstances(initialNumberInstances);

				String osType = doc
						.getElementsByTagName(OPERATINGSYTEM_SECTION).item(0)
						.getAttributes().getNamedItem(GENERAL_ID)
						.getTextContent();

				List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

				productReleases = getPICProductReleases(tierNode);

				tier.setProductReleases(productReleases);
				tier.setVdc(vdc);
				tier.setPayload(ovfSingleVM.get(i));
				tiers.add(tier);
			}

		} catch (SAXException e) {
			String errorMessage = saxException + " ProductRelease. Desc: "
					+ e.getMessage();
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
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}
		return tiers;
	}

	/**
	 * Get the Credentials (FIWARE) from the Spring SecurityContext
	 */
	public PaasManagerUser getCredentials() {
		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
			return (PaasManagerUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		} else {
			return null;
		}
	}

	/**
	 * Get ProductRelease of type=PIC
	 * 
	 * @param tierNode
	 * @return List of PIC ProductReleases
	 * @throws InvalidEnvironmentRequestException
	 */
	private List<ProductRelease> getPICProductReleases(Node tierNode)
			throws InvalidEnvironmentRequestException {

		List<ProductRelease> productReleasesPICs = new ArrayList<ProductRelease>();

		DocumentBuilder builder;
		Document doc;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(
					nodeToString(tierNode))));

			NodeList productSectionNodes = doc
					.getElementsByTagName(PRODUCT_SECTION);

			for (int i = 0; i < productSectionNodes.getLength(); i++) {
				Node productSectionNode = productSectionNodes.item(i);

				NodeList propertyList = productSectionNode.getChildNodes();

				for (int j = 0; j < propertyList.getLength(); j++) {
					Node property = propertyList.item(j);
					String propertyString = nodeToString(property);

					if (propertyString.contains(PROPERTY_TAG)) {
						if ((property.getAttributes().getNamedItem(
								KEYATTRIBUTE_TAG).getTextContent()
								.equals(KEYATTRIBUTE_VALUE))
								&& (property.getAttributes().getNamedItem(
										VALUEATTRIBUTE_TAG).getTextContent()
										.equals("PIC"))) {
							// productSecionPIC =
							// claudiaUtil.nodeToString(productSectionNode);
							// vappPICs.add(productSecionPIC);
							productReleasesPICs
									.add(getProductRelease(productSectionNode));
						}
					}
				}

			}

			/*
			 * if (productReleasesPICs.size() < 1) { String msg
			 * ="No Product Releases PIC typed in the request"; log.info(msg);
			 * throw new InvalidEnvironmentRequestException(msg); }
			 */

		} catch (ParserConfigurationException e) {
			String msg = "Error parsing vapp . Desc: " + e.getMessage();
			log.info(msg);
			throw new InvalidEnvironmentRequestException(msg);
		} catch (SAXException e) {
			String msg = "Error parsing vapp . Desc: " + e.getMessage();
			log.info(msg);
			throw new InvalidEnvironmentRequestException(msg);
		} catch (IOException e) {
			String msg = "IOException . Desc: " + e.getMessage();
			log.info(msg);
			throw new InvalidEnvironmentRequestException(msg);
		} catch (Exception e) {
			String msg = "Exception . Desc: " + e.getMessage();
			log.info(msg);
			throw new InvalidEnvironmentRequestException(msg);
		}
		return productReleasesPICs;
	}

	private ProductRelease getProductRelease(Node productNode)
			throws InvalidEnvironmentRequestException {

		ProductRelease productRelease = null;
		String productName = null;
		String productVersion = null;

		NodeList productNodeList = productNode.getChildNodes();
		for (int i = 0; i < productNodeList.getLength(); i++) {
			Node node = productNodeList.item(i);

			if (node.getNodeName().equals(PRODUCTNAME_TAG)) {
				productName = productNodeList.item(i).getTextContent();
			}

			if (node.getNodeName().equals(PRODUCTVERSION_TAG)) {
				productVersion = productNodeList.item(i).getTextContent();
			}

		}

		productRelease = new ProductRelease(productName, productVersion);
		// productRelease = productReleaseManager.load(productName+"-"+
		// productVersion);
		productRelease.setAttributes(getProductSectionProperties(productNode));

		/*
		 * } /*catch (EntityNotFoundException e) { String errorMessage =
		 * "ProductRelease " + productName + " and version : " + productVersion
		 * + " not Found " + " in the PaasManager Database " + " Desc. " +
		 * e.getMessage(); log.error(errorMessage); throw new
		 * InvalidEnvironmentRequestException (errorMessage); }
		 */
		return productRelease;
	}

	private List<Attribute> getProductSectionProperties(Node productSectionNode) {

		List<Attribute> attributes = new ArrayList<Attribute>();
		NodeList propertyList = productSectionNode.getChildNodes();

		if (propertyList == null) {
			return attributes;
		}

		for (int j = 0; j < propertyList.getLength(); j++) {
			Node property = propertyList.item(j);
			String propertyString = nodeToString(property);

			if (propertyString.contains(PROPERTY_TAG)) {
				Attribute attribute = new Attribute();
				attribute.setKey(property.getAttributes().getNamedItem(
						KEYATTRIBUTE_TAG).getTextContent());
				attribute.setValue(property.getAttributes().getNamedItem(
						VALUEATTRIBUTE_TAG).getTextContent());

				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil#
	 * isVirtualServicePayload(java.lang.String)
	 */
	public boolean isVirtualServicePayload(String extendedovf)
			throws InvalidEnvironmentRequestException {
		try {
			Document doc = claudiaUtil.stringToDom(extendedovf);
			NodeList virtualServices = doc
					.getElementsByTagName(VIRTUALSERVICE_SECTION);

			if (virtualServices.getLength() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SAXException e) {
			String errorMessage = saxException + " VirtualService. Desc: "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining "
					+ "VirtualService. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining "
					+ "VirtualService. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil#
	 * getVirtualServices(java.lang.String)
	 */
	public List<String> getVirtualServices(String extendedovf)
			throws InvalidEnvironmentRequestException {
		List<String> virtualServices = new ArrayList<String>();

		try {
			Document doc = claudiaUtil.stringToDom(extendedovf);
			NodeList virtualServicesNodes = doc
					.getElementsByTagName(VIRTUALSERVICE_SECTION);

			for (int i = 0; i < virtualServicesNodes.getLength(); i++) {
				Node virtualServiceNode = virtualServicesNodes.item(i);
				String virtualService = claudiaUtil
						.nodeToString(virtualServiceNode);
				virtualServices.add(virtualService);
			}

		} catch (SAXException e) {
			String errorMessage = saxException + " VirtualServices. Desc: "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining "
					+ "VirtualServices. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining "
					+ "VirtualServices. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}

		return virtualServices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil#
	 * getVirtualServiceName(java.lang.String)
	 */
	public String getVirtualServiceName(String extendedovf)
			throws InvalidEnvironmentRequestException {

		String serviceName;
		try {
			Document doc = claudiaUtil.stringToDom(extendedovf);
			NodeList virtualServices = doc
					.getElementsByTagName(VIRTUALSERVICE_SECTION);

			serviceName = virtualServices.item(0).getAttributes().getNamedItem(
					VIRTUALSERVICE_ID).getTextContent();

		} catch (SAXException e) {
			String errorMessage = saxException + " VirtualService. Desc: "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining "
					+ "VirtualService. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining "
					+ "VirtualService. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidEnvironmentRequestException(errorMessage);
		}

		return serviceName;
	}

	/**
	 * @param claudiaUtil
	 *            the claudiaUtil to set
	 */
	public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
		this.claudiaUtil = claudiaUtil;
	}

	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 */
	public void setSystemPropertiesProvider(
			SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}

	public void setOvfUtils(OVFUtils ovfUtils) {
		this.ovfUtils = ovfUtils;
	}

	private String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}
}
