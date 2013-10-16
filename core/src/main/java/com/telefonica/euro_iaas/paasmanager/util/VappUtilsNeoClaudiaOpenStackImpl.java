/**
 * 
 */
package com.telefonica.euro_iaas.paasmanager.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;
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
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

/**
 * @author jesus.movilla
 * 
 */
public class VappUtilsNeoClaudiaOpenStackImpl implements VappUtils {

	private SystemPropertiesProvider systemPropertiesProvider;
	private ClaudiaUtil claudiaUtil;
	private static Logger log = Logger.getLogger(VappUtilsImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.util.VappUtils#getIP(java.lang.String
	 * )
	 */
	public List<String> getIP(String vapp) throws InvalidVappException {
		List<String> ips = new ArrayList<String>();
		String publicIP = null;
		String privateIP = null;

		String ipTag = null;
		DocumentBuilder builder;
		Document doc;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(vapp)));

			if (systemPropertiesProvider.getProperty(
					SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
				ipTag = IPADDRESS_TAG_FIWARE;
			} else {
				ipTag = IPADDRESS_TAG;
			}
			NodeList nodeItems = doc.getElementsByTagName(ITEM_TAG);

			for (int i = 0; i < nodeItems.getLength(); i++) {

				Node itemElement = nodeItems.item(i);
				String itemElementString = nodeToString(itemElement);
				log.info(itemElementString);

				if ((itemElementString.contains(NETWORK_ITEM) && itemElementString
						.contains(PUBLIC_NETWORK))) {

					NodeList nodesItem = itemElement.getChildNodes();

					for (int j = 0; j < nodesItem.getLength(); j++) {
						Node elementNode = nodesItem.item(j);
						if (elementNode.getNodeName().equals(ipTag))
							publicIP = elementNode.getTextContent();
					}
				}

				if ((itemElementString.contains(NETWORK_ITEM) && itemElementString
						.contains(PRIVATE_NETWORK))) {

					NodeList nodesItem = itemElement.getChildNodes();

					for (int j = 0; j < nodesItem.getLength(); j++) {
						Node elementNode = nodesItem.item(j);
						if (elementNode.getNodeName().equals(ipTag))
							privateIP = elementNode.getTextContent();
					}
				}
			}
			// There are always ips present
			ips.add(privateIP);
			ips.add(publicIP);

		} catch (ParserConfigurationException e) {
			String msg = "Error parsing vapp to obtain ip . Desc: "
					+ e.getMessage();
			log.info(msg);
			throw new InvalidVappException(msg);
		} catch (SAXException e) {
			String msg = "Error parsing vapp to obtain ip. Desc: "
					+ e.getMessage();
			log.info(msg);
			throw new InvalidVappException(msg);
		} catch (IOException e) {
			String msg = "Error obatining ip  from Vapp IOException . Desc: "
					+ e.getMessage();
			log.info(msg);
			throw new InvalidVappException(msg);
		} catch (Exception e) {
			String msg = "Exception . Desc: There is not IP in the VApp ";
			log.info(msg);
			throw new InvalidVappException(msg);
		}

		return ips;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.util.VappUtils#getVappsSingleVM(
	 * java.lang.String)
	 */
	public List<String> getVappsSingleVM(ClaudiaData claudiaData, String vapp)
			throws InvalidVappException {
		List<String> vappsReplicas = new ArrayList<String>();
		String vappUrl = null;

		if (vapp == null) {
			return null;
		}
		// String ovf = removeInitOvfParams(ovfInstantParams);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		Element root;

		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(vapp)));
			root = doc.getDocumentElement();

			Node vaapService = findNode(root, "/VApp");

			NodeList lVaapVEE = findNodeList(vaapService, "/VApp/Children/VApp");

			for (int i = 0; i < lVaapVEE.getLength(); i++) {
				Element vappVEE = (Element) lVaapVEE.item(i);
				vappUrl = vappVEE.getAttribute("href");

				String vappReplica = claudiaUtil.getClaudiaResource(claudiaData
						.getUser(), vappUrl, MediaType.WILDCARD);
				vappsReplicas.add(vappReplica);

			}

		} catch (ParserConfigurationException e) {
			String msg = "Error parsing ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (SAXException e) {
			String msg = "SAXException with  ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (IOException e) {
			String msg = "IOException with  ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (TransformerException e) {
			String msg = "TransformerException with ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (ClaudiaRetrieveInfoException e) {
			String msg = "Error obtaining Vapp from  " + vappUrl
					+ "Description: " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (ClaudiaResourceNotFoundException e) {
			String msg = "Error obtaining Vapp from  " + vappUrl
					+ "Description: " + e.getMessage();
			throw new InvalidVappException(msg);
		}
		return vappsReplicas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.util.VappUtils#getFqnId(java.lang
	 * .String)
	 */
	public String getFqnId(String vapp) throws InvalidVappException {
		String fqnId = null;

		if (vapp == null) {
			return null;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		Element root;

		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(vapp)));
			root = doc.getDocumentElement();

			Node vaapNode = findNode(root, "/VApp");
			Element vappElement = (Element) vaapNode;
			fqnId = vappElement.getAttribute("name");

		} catch (ParserConfigurationException e) {
			String msg = "Error parsing ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (SAXException e) {
			String msg = "SAXException with  ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (IOException e) {
			String msg = "IOException with  ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		} catch (TransformerException e) {
			String msg = "TransformerException with ovf " + e.getMessage();
			throw new InvalidVappException(msg);
		}
		return fqnId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.util.VappUtils#getVMName(java.lang
	 * .String)
	 */
	public String getVMName(String fqnId) {
		return fqnId.substring(fqnId.indexOf("vees.") + "vees.".length(), fqnId
				.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.util.VappUtils#getReplica(java.lang
	 * .String)
	 */
	public String getReplica(String fqnId) {
		String replicas = null;
		if (!(fqnId.contains("replicas"))) {
			replicas = "1";
		} else {
			replicas = fqnId.substring(fqnId.indexOf("replicas.")
					+ "replicas.".length());
		}
		return replicas;
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

	private Node findNode(Node node, String xql) throws TransformerException {
		return (XPathAPI.selectSingleNode(node, xql));
	}

	private NodeList findNodeList(Node node, String xql)
			throws TransformerException {
		return (XPathAPI.selectNodeList(node, xql));
	}

	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 */
	public void setSystemPropertiesProvider(
			SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}

	/**
	 * @param claudiaUtil
	 *            the claudiaUtil to set
	 */
	public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
		this.claudiaUtil = claudiaUtil;
	}

	public String getMacroVapp(String ovf, EnvironmentInstance envIns)
			throws InvalidOVFException {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> getNetworkAndIP(String vappReplica)
			throws InvalidVappException {
		// TODO Auto-generated method stub
		return null;
	}

}
