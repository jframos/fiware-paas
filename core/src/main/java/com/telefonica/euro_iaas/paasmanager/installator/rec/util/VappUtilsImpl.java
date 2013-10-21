/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.rec.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class VappUtilsImpl implements VappUtils {

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(VappUtilsImpl.class);

    public String getAppId(String vapp) throws InvalidOVFException {
        String appId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList productSectionNodes = doc.getElementsByTagName("ovf:VirtualSystemCollection");

            appId = productSectionNodes.item(0).getAttributes().getNamedItem("ovf:id").getTextContent();

        } catch (ParserConfigurationException e) {
            String msg = "Error obtaining hostname . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error obtaining hostname  . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException .Error obtaining hostname : " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return appId;
    }

    /*
     * Obtaining Login from Vapp (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getLogin (java.lang.String)
     */
    public String getLogin(String vapp) throws ProductInstallatorException {
        String login = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList nodes = doc.getElementsByTagName(VAPPPROPERTY_TAG);

            for (int i = 0; i < nodes.getLength(); i++) {
                Node property = nodes.item(i);

                if (property.getAttributes().getNamedItem(VAPP_KEYATTRIBUTE_TAG).getTextContent()
                        .equals(USERNAMEATTRIBUTE_VALUE))
                    login = property.getAttributes().getNamedItem(VAPP_VALUEATTRIBUTE_TAG).getTextContent();
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return login;
    }

    /*
     * Obtaining Password from Vapp (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPassword(java.lang.String)
     */
    public String getPassword(String vapp) throws ProductInstallatorException {
        String password = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList nodes = doc.getElementsByTagName(VAPPPROPERTY_TAG);

            for (int i = 0; i < nodes.getLength(); i++) {
                Node property = nodes.item(i);
                if (property.getAttributes().getNamedItem(VAPP_KEYATTRIBUTE_TAG).getTextContent()
                        .equals(PASSWORDATTRIBUTE_VALUE))
                    password = property.getAttributes().getNamedItem(VAPP_VALUEATTRIBUTE_TAG).getTextContent();
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return password;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getIP (java.lang.String)
     */
    public String getIP(String vappReplica) throws ProductInstallatorException {
        String ip, ipTag = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vappReplica)));

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                ipTag = IPADDRESS_TAG_FIWARE;
            } else {
                ipTag = IPADDRESS_TAG;
            }
            Node ipNnode = doc.getElementsByTagName(ipTag).item(0);
            ip = ipNnode.getTextContent().trim();

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }

        return ip;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getNetworks(java.lang.String)
     */
    public String getNetworks(String vapp) throws InvalidOVFException {

        String network = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            Node networkNode = doc.getElementsByTagName(NETWORK_TAG).item(0);
            network = networkNode.getTextContent();

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return network;
    }

    public String getRECVapp(String vapp, String ip, String login, String password) throws ProductInstallatorException {

        DocumentBuilder builder;
        Document doc;
        Element root;
        Node virtualSystemNode = null;
        Node nameNode = null;
        Node operatingSystemSectionNode = null;
        Node elasticArraySectionNode = null;
        Node virtualHardwareSectionNode = null;
        Node governanceRuleSectionNode = null;
        List<String> picProductSections = null;
        List<String> acProductSections = null;
        NodeList productSections = null;
        String virtualSystemNodeString = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));
            root = doc.getDocumentElement();

            virtualSystemNode = findNode(doc, VIRTUALSYSTEM_PATH);
            virtualSystemNodeString = nodeToString(virtualSystemNode);

            nameNode = doc.getElementsByTagName(NODENAME_TAG).item(0);
            String nameNodeString = nodeToString(nameNode);

            operatingSystemSectionNode = doc.getElementsByTagName(OPERATINGSYSTEMSECTION_TAG).item(0);
            String operatingSystemSectionNodeString = nodeToString(operatingSystemSectionNode);

            elasticArraySectionNode = doc.getElementsByTagName(ELASTICARRAYSECTION_TAG).item(0);
            String elasticArraySectionNodeString = nodeToString(elasticArraySectionNode);

            virtualHardwareSectionNode = doc.getElementsByTagName(VIRTUALHARDWARESECTION_TAG).item(0);
            String virtualHardwareSectionNodeString = nodeToString(virtualHardwareSectionNode);

            // Eliminating Governance tags
            governanceRuleSectionNode = doc.getElementsByTagName(GOVERNANCE_RULE_TAG).item(0);
            String governanceRuleSectionNodeString = nodeToString(governanceRuleSectionNode);

            productSections = doc.getElementsByTagName(PRODUCTSECTION_TAG);
            picProductSections = getPICProductSections(vapp);

            acProductSections = getACProductSections(vapp);

            // Removing Nodes
            if (operatingSystemSectionNode != null) {
                virtualSystemNodeString = nodeToString(virtualSystemNode);
                virtualSystemNode.removeChild(operatingSystemSectionNode);
                virtualSystemNodeString = nodeToString(virtualSystemNode);
            }

            if (nameNode != null) {
                virtualSystemNode.removeChild(nameNode);
                virtualSystemNodeString = nodeToString(virtualSystemNode);
            }

            if (elasticArraySectionNode != null) {
                virtualSystemNode.removeChild(elasticArraySectionNode);
                virtualSystemNodeString = nodeToString(virtualSystemNode);
            }

            if (virtualHardwareSectionNode != null) {
                virtualSystemNode.removeChild(virtualHardwareSectionNode);
                virtualSystemNodeString = nodeToString(virtualSystemNode);
            }

            // Removing governanceRules
            if (governanceRuleSectionNode != null) {
                virtualSystemNode.removeChild(governanceRuleSectionNode);
                virtualSystemNodeString = nodeToString(virtualSystemNode);
            }

            /*
             * for (int i=0; i < productSections.getLength(); i++) {
             * virtualSystemNode.removeChild(productSections.item(i)); virtualSystemNodeString =
             * nodeToString(virtualSystemNode); }
             */

            // virtualSystemNode.appendChild(stringToNode(productSection))
            for (int i = 0; i < picProductSections.size(); i++) {
                virtualSystemNodeString = virtualSystemNodeString.replaceAll(picProductSections.get(i), "");
            }

            for (int i = 0; i < acProductSections.size(); i++) {
                virtualSystemNodeString = virtualSystemNodeString.replaceAll(acProductSections.get(i), "");
            }
            // Adding the new productSectionNode
            /*
             * Node productSectionNode = findNode (root, PRODUCTSECTION_PATH); virtualSystemNode = updateNode(builder,
             * doc, virtualSystemNode, productSection, productSectionNode);
             */

            virtualSystemNodeString = setParamsREC(virtualSystemNodeString, ip, login, password);
        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (TransformerException e) {
            String msg = "TransformerException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (InvalidOVFException e) {
            String msg = "InvalidOVFException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }

        return virtualSystemNodeString;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPICProductSections(java.lang.String)
     */
    public List<String> getPICProductSections(String vapp) throws ProductInstallatorException {
        String productSecionPIC = null;
        List<String> vappPICs = new ArrayList<String>();

        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList productSectionNodes = doc.getElementsByTagName(PRODUCTSECTION_TAG);

            for (int i = 0; i < productSectionNodes.getLength(); i++) {
                Node productSectionNode = productSectionNodes.item(i);

                NodeList propertyList = productSectionNode.getChildNodes();

                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    String propertyString = nodeToString(property);

                    if (propertyString.contains("Property")) {
                        if ((property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                                .equals(KEYATTRIBUTE_VALUE))
                                && (property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent()
                                        .equals("PIC"))) {
                            productSecionPIC = nodeToString(productSectionNode);
                            vappPICs.add(productSecionPIC);
                        }
                    }
                }

            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return vappPICs;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPICProductSections(java.lang.String)
     */
    public String getPICProductSection(String picId, String vapp) throws ProductInstallatorException {
        String productSecionPIC = null;
        String vappPIC = null;

        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList productSectionNodes = doc.getElementsByTagName(PRODUCTSECTION_TAG);

            for (int i = 0; i < productSectionNodes.getLength(); i++) {
                Node productSectionNode = productSectionNodes.item(i);

                NodeList propertyList = productSectionNode.getChildNodes();

                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    String propertyString = nodeToString(property);

                    if (propertyString.contains("Property")) {
                        if ((property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                                .equals(KEYATTRIBUTE_VALUE_ID))
                                && (property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent()
                                        .equals(picId))) {

                            productSecionPIC = nodeToString(productSectionNode);
                            vappPIC = productSecionPIC;
                        }
                    }
                }

            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return vappPIC;
    }

    public List<String> getACProductSectionsByPicId(String vapp, String picId) throws ProductInstallatorException {
        String productSectionAC = null;
        List<String> vappACs = new ArrayList<String>();

        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList productSectionNodes = doc.getElementsByTagName(PRODUCTSECTION_TAG);

            for (int i = 0; i < productSectionNodes.getLength(); i++) {
                Node productSectionNode = productSectionNodes.item(i);

                NodeList propertyList = productSectionNode.getChildNodes();

                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    String propertyString = nodeToString(property);

                    if (propertyString.contains("Property")) {
                        if ((property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                                .equals(KEYATTRIBUTE_VALUE_PICPARENT))
                                && (property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent()
                                        .equals(picId))) {

                            productSectionAC = nodeToString(productSectionNode);
                            vappACs.add(productSectionAC);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return vappACs;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getACProductSections(java.lang.String)
     */
    public List<String> getACProductSections(String vapp) throws ProductInstallatorException {
        String productSectionAC = null;
        List<String> vappACs = new ArrayList<String>();

        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            NodeList productSectionNodes = doc.getElementsByTagName(PRODUCTSECTION_TAG);

            for (int i = 0; i < productSectionNodes.getLength(); i++) {
                Node productSectionNode = productSectionNodes.item(i);

                NodeList propertyList = productSectionNode.getChildNodes();

                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    String propertyString = nodeToString(property);

                    if (propertyString.contains("Property")) {
                        if ((property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                                .equals(KEYATTRIBUTE_VALUE))
                                && (property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent()
                                        .equals("AC"))) {
                            productSectionAC = nodeToString(productSectionNode);
                            vappACs.add(productSectionAC);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new ProductInstallatorException(msg);
        }
        return vappACs;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getPicId (java.lang.String)
     */
    public String getPicId(String vappPic) throws InvalidOVFException {
        String picId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vappPic)));

            String docString = nodeToString(doc);
            NodeList propertyListNodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);

            for (int j = 0; j < propertyListNodes.getLength(); j++) {
                Node property = propertyListNodes.item(j);
                String propertyString = nodeToString(property);

                if (propertyString.contains("Property")) {
                    if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                            .equals(KEYATTRIBUTE_VALUE_ID)) {
                        picId = property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent();
                    }
                }
            }
            /*
             * for (int j=0; j < propertyList.getLength(); j++) { Node property = propertyList.item(j); if
             * (property.getAttributes() .getNamedItem("ovfenvelope:key").equals ("org.fourcaast.instancecomponent.id"))
             * picId = property.getAttributes() .getNamedItem("ovfenvelope:value").getTextContent(); }
             */

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return picId;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getAcId (java.lang.String)
     */
    public String getAcId(String acProductSection) throws InvalidOVFException {
        String acId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(acProductSection)));

            String docString = nodeToString(doc);
            NodeList propertyListNodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);

            for (int j = 0; j < propertyListNodes.getLength(); j++) {
                Node property = propertyListNodes.item(j);
                String propertyString = nodeToString(property);

                if (propertyString.contains("Property")) {
                    if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                            .equals(KEYATTRIBUTE_VALUE_ACID)) {
                        acId = property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return acId;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPicIdFromAC(java.lang.String)
     */
    public String getPicIdFromAC(String acProductSection) throws InvalidOVFException {
        String picId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(acProductSection)));

            String docString = nodeToString(doc);
            NodeList propertyListNodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);

            for (int j = 0; j < propertyListNodes.getLength(); j++) {
                Node property = propertyListNodes.item(j);
                String propertyString = nodeToString(property);

                if (propertyString.contains("Property")) {
                    if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                            .equals(KEYATTRIBUTE_VALUE_PICPARENT)) {
                        picId = property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return picId;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPicIdFromAC(java.lang.String)
     */
    public String getAppIdFromAC(String acProductSection) throws InvalidOVFException {
        String appId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(acProductSection)));

            String docString = nodeToString(doc);
            NodeList propertyListNodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);

            for (int j = 0; j < propertyListNodes.getLength(); j++) {
                Node property = propertyListNodes.item(j);
                String propertyString = nodeToString(property);

                if (propertyString.contains("Property")) {
                    if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                            .equals(KEYATTRIBUTE_VALUE_APPPARENT)) {
                        appId = property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return appId;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getPicIdFromAC(java.lang.String)
     */
    public String getVmIdFromAC(String acProductSection) throws InvalidOVFException {
        String vmId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(acProductSection)));

            String docString = nodeToString(doc);
            NodeList propertyListNodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);

            for (int j = 0; j < propertyListNodes.getLength(); j++) {
                Node property = propertyListNodes.item(j);
                String propertyString = nodeToString(property);

                if (propertyString.contains("Property")) {
                    if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent()
                            .equals(KEYATTRIBUTE_VALUE_VMPARENT)) {
                        vmId = property.getAttributes().getNamedItem(VALUEATTRIBUTE_TAG).getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vappPic . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return vmId;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getEnvelopeTypeSegment(java.lang.String)
     */
    public String getEnvelopeTypeSegment(String serviceFile, String appId) throws InvalidOVFException {

        try {
            InputStream is = new FileInputStream(serviceFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer ruleFile = new StringBuffer();
            String actualString;

            while ((actualString = reader.readLine()) != null) {
                ruleFile.append(actualString).append("\n");
            }

            String envelopeVapp = ruleFile.toString();

            return setAppIdEnvelope(envelopeVapp, appId);

        } catch (IOException e) {
            String msg = " The file EnvelopeTemplate.xml could not be found in" + " the resource directory";
            log.info(msg);
            throw new InvalidOVFException(msg);
        }

    }

    private String setParamsREC(String vapp, String ip, String login, String password) throws InvalidOVFException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;

        String newnodeIp = "<ovfenvelope:Property " + "ovfenvelope:key=\"org.fourcaast.rec.address\" "
                + "ovfenvelope:value=\"" + ip + "\"/>";
        String newnodeLogin = "<ovfenvelope:Property " + "ovfenvelope:key=\"org.fourcaast.rec.username\" "
                + "ovfenvelope:value=\"" + login + "\"/>";
        String newnodePassword = "<ovfenvelope:Property " + "ovfenvelope:key=\"org.fourcaast.rec.password\" "
                + "ovfenvelope:value=\"" + password + "\"/>";

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));
            root = doc.getDocumentElement();

            Node nodeIp = findNodePropertyByAttribute(doc, IPATTRIBUTE_VALUE);
            Node nodeLogin = findNodePropertyByAttribute(doc, USERNAMEATTRIBUTE_VALUE);
            Node nodePassword = findNodePropertyByAttribute(doc, PASSWORDATTRIBUTE_VALUE);

            Node parentNode = findNode(root, VS_PRODUCTSECTION_PATH);
            parentNode = updateNode(builder, doc, parentNode, newnodeIp, nodeIp);
            String parentNodeString = nodeToString(parentNode);

            if (login != null) {
                parentNode = updateNode(builder, doc, parentNode, newnodeLogin, nodeLogin);
                parentNodeString = nodeToString(parentNode);
            }

            if (password != null) {
                parentNode = updateNode(builder, doc, parentNode, newnodePassword, nodePassword);
                parentNodeString = nodeToString(parentNode);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());

            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String envelopeVapp = result.getWriter().toString();
            return envelopeVapp;

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "SAXException with  ovf " + e.getMessage();
            log.info(msg);
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException with  ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (TransformerException e) {
            String msg = "TransformerException with ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }

    }

    private String setAppIdEnvelope(String vapp, String appId) throws InvalidOVFException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));
            root = doc.getDocumentElement();

            Node nodeInfo = setValue(root, appId, INFOSECTION_PATH);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());

            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String envelopeVapp = result.getWriter().toString();
            return envelopeVapp;

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "SAXException with  ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException with  ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (TransformerException e) {
            String msg = "TransformerException with ovf " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }

    }

    private Node findNodePropertyByAttribute(Document doc, String attributeName) {
        NodeList nodes = doc.getElementsByTagName(OVFPPROPERTY_TAG);
        Node property = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            property = nodes.item(i);
            if (property.getAttributes().getNamedItem(KEYATTRIBUTE_TAG).getTextContent().equals(attributeName))
                return property;
        }
        return property;
    }

    /**
     * Update a Node from a parentNode
     * 
     * @param docBuilder
     * @param doc
     * @param parentNode
     * @param newNode
     * @param oldNode
     * @return
     */
    private Node updateNode(DocumentBuilder docBuilder, Document doc, Node parentNode, String newNode, Node oldNode) {
        try {
            Node fragmentNode = docBuilder.parse(new InputSource(new StringReader(newNode))).getDocumentElement();
            fragmentNode = doc.importNode(fragmentNode, true);
            parentNode.replaceChild(fragmentNode, oldNode);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return parentNode;
    }

    private Node findNode(Node node, String xql) throws TransformerException {
        return (XPathAPI.selectSingleNode(node, xql));
    }

    /**
     * Mehtohd t modify the value of a particular Node
     * 
     * @param startNode
     *            Nodo a partir del cual se empieza
     * @param value
     *            valor del nodo
     * @param xql
     *            seleccion exacta del nodo a cambiar su valor
     * @return
     * @throws TransformerException
     * @throws Exception
     */
    private Node setValue(Node startNode, String value, String xql) throws TransformerException {

        Node targetNode = XPathAPI.selectSingleNode(startNode, xql);
        NodeList children = targetNode.getChildNodes();
        int index = 0;
        int length = children.getLength();

        // Remove all of the current contents
        for (index = 0; index < length; index++) {
            targetNode.removeChild(children.item(index));
        }
        // Add in the new value
        Document doc = startNode.getOwnerDocument();
        targetNode.appendChild(doc.createTextNode(value));
        return targetNode;
    }

    /**
     * Remove all Child Nodes
     * 
     * @param node
     * @return
     * @throws TransformerException
     */
    private Node removeChildNodes(Node node) throws TransformerException {

        NodeList children = node.getChildNodes();
        int index = 0;
        int length = children.getLength();

        for (index = 0; index < length; index++) {
            Node n = children.item(index);
            String nString = nodeToString(n);

            // for(Node n : node.getChildNodes()){
            if (n != null) {
                if (n.hasChildNodes()) {// edit to remove children of children
                    removeChildNodes(n);
                    node.removeChild(n);
                } else
                    node.removeChild(n);
            }
        }
        String nodeString = nodeToString(node);
        return node;
    }

    /**
     * Add new node
     * 
     * @param masterNode
     * @param childNode
     * @return
     */
    private Node addNewNode(Node masterNode, Node childNode) {
        // Add in the new value
        Document doc = masterNode.getOwnerDocument();
        masterNode.appendChild(childNode);
        return masterNode;
    }

    private String getTextContents(Node node) {
        NodeList childNodes;
        StringBuffer contents = new StringBuffer();

        childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                contents.append(childNodes.item(i).getNodeValue());
            }
        }
        return contents.toString();
    }

    private Node stringToNode(String xml) throws SAXException, IOException, ParserConfigurationException {
        Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes())).getDocumentElement();
        return node;
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            log.warn("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public String getFqnId(String vapp) throws InvalidVappException {

        String fqnId = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(vapp)));

            Element node = doc.getDocumentElement();
            fqnId = node.getAttributes().getNamedItem("name").getTextContent();

        } catch (ParserConfigurationException e) {
            String msg = "Error obtaining hostname . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (SAXException e) {
            String msg = "Error obtaining hostname  . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (IOException e) {
            String msg = "IOException .Error obtaining hostname : " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        }
        return fqnId;
    }

}
