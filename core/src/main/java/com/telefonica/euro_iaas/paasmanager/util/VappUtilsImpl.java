/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

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
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * @author jesus.movilla
 */
public class VappUtilsImpl implements VappUtils {

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(VappUtilsImpl.class);

    public List<String> getVappsSingleVM(ClaudiaData claudiaData, String vapp) throws InvalidVappException {
        List<String> vappsReplicas = new ArrayList<String>();
        List<String> veeVapps = new ArrayList<String>();

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

            // Node parentNode = findNode (root, XSQL_ENVELOPE_NOINIT);
            Node vaapService = findNode(root, "/VApp");

            NodeList lVaapVEE = findNodeList(vaapService, "/VApp/Children/VApp");

            for (int i = 0; i < lVaapVEE.getLength(); i++) {
                Element vappVEE = (Element) lVaapVEE.item(i);
                String veeReplica = nodeToString(vappVEE);
                veeVapps.add(veeReplica);

                NodeList lVappReplica = vappVEE.getElementsByTagName("Children");

                for (int j = 0; j < lVappReplica.getLength(); j++) {
                    NodeList vapps = ((Element) lVappReplica.item(j)).getElementsByTagName("VApp");
                    String vappReplica = nodeToString(vapps.item(0));
                    vappsReplicas.add(vappReplica);
                }

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
        }
        return vappsReplicas;
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

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getIP (java.lang.String)
     */
    public List<String> getIP(String vappReplica) throws InvalidVappException {
        List<String> ips = new ArrayList<String>();
        String ipTag = null;
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

            NodeList nodeips = doc.getElementsByTagName(ipTag);

            for (int i = 0; i < nodeips.getLength(); i++) {
                Node ipNnode = doc.getElementsByTagName(ipTag).item(i);
                ips.add(ipNnode.getTextContent().trim());
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (Exception e) {
            String msg = "Exception . Desc: There is not IP in the VApp ";
            log.info(msg);
            throw new InvalidVappException(msg);
        }

        return ips;
    }

    public HashMap<String, String> getNetworkAndIP(String vappReplica) throws InvalidVappException {
        HashMap<String, String> netowrks = new HashMap<String, String>();
        String ipTag = null;
        String connectionTag = "Connection";
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

            NodeList nodeips = doc.getElementsByTagName(ipTag);
            NodeList nodeNetwork = doc.getElementsByTagName(connectionTag);

            for (int i = 0; i < nodeips.getLength(); i++) {
                Node ipNnode = doc.getElementsByTagName(ipTag).item(i);
                Node ipNnetwork = doc.getElementsByTagName(connectionTag).item(i);
                if (ipNnetwork == null) {
                    continue;
                }

                netowrks.put(ipNnetwork.getTextContent(), ipNnode.getTextContent());
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (SAXException e) {
            String msg = "Error parsing vapp . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (IOException e) {
            String msg = "IOException . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidVappException(msg);
        } catch (Exception e) {
            String msg = "Exception . Desc: There is not IP in the VApp ";
            log.info(msg);
            throw new InvalidVappException(msg);
        }

        return netowrks;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.VappUtils#getVMName(java.lang .String)
     */
    public String getVMName(String fqnId) {
        return fqnId.substring(fqnId.indexOf("vees.") + "vees.".length(), fqnId.indexOf(".replicas"));
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.VappUtils#getReplica(java.lang .String)
     */
    public String getReplica(String fqnId) {
        // TODO Auto-generated method stub
        return fqnId.substring(fqnId.indexOf("replicas.") + "replicas.".length());
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
        // System.out.println("Node:" + xql);
        return (XPathAPI.selectSingleNode(node, xql));
    }

    private NodeList findNodeList(Node node, String xql) throws TransformerException {
        return (XPathAPI.selectNodeList(node, xql));
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public String getMacroVapp(String ovf, EnvironmentInstance envIns) throws InvalidOVFException {
        if (ovf == null)
            return null;

        if (!ovf.toLowerCase().contains("@ip"))
            return ovf;
        log.info("Get macro ip");
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(ovf)));

            NodeList productSectionNodes = doc.getElementsByTagName("ovfenvelope:ProductSection");

            for (int i = 0; i < productSectionNodes.getLength(); i++) {

                Node productSectionNode = productSectionNodes.item(i);

                NodeList propertyList = productSectionNode.getChildNodes();

                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    String propertyString = nodeToString(property);

                    if (propertyString.contains("Property")) {
                        if ((property.getAttributes().getNamedItem("ovfenvelope:value").getTextContent().toLowerCase()
                                .contains("@ip"))) {

                            String ip = getIpfromMacro(property.getAttributes().getNamedItem("ovfenvelope:value")
                                    .getTextContent(), envIns);
                            log.debug("IP value in macro : "
                                    + property.getAttributes().getNamedItem("ovfenvelope:value").getTextContent() + " "
                                    + ip);
                            property.getAttributes().getNamedItem("ovfenvelope:value").setTextContent(ip);

                        }

                    }
                }
            }

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
        return this.toString(doc);
    }

    private String getIpfromMacro(String value, EnvironmentInstance envIns) {
        String prefix = "@IP" + "(";
        String macroValue = value.substring(value.indexOf(prefix) + prefix.length(), value.indexOf(")"));

        /*
         * Two possibilities to take into account: @IP(net) or @IP(net,alias). Note that alias = "" in the case @IP(net)
         * is used
         */
        String network = "";
        String vm = "";
        StringTokenizer macroValueTokenizer = new StringTokenizer(macroValue, ",");
        vm = macroValueTokenizer.nextToken();
        if (macroValueTokenizer.hasMoreElements()) {
            network = macroValueTokenizer.nextToken();
        }

        log.debug("IP@ = " + network);

        for (TierInstance tierInst : envIns.getTierInstances()) {
            Tier tier = tierInst.getTier();
            if (tier.getName().equals(vm)) {
                return (String) tierInst.getVM().getNetworks().get(network);
            }
        }
        return null;
    }

    public String toString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

}
