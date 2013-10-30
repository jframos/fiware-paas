/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

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
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;

public class OVFUtilsDomImpl implements OVFUtils {

    /** The log. */
    private static Logger log = Logger.getLogger(OVFUtilsDomImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.OVFUtils#getOvfsSingleVM(java .lang.String)
     */
    public List<String> getOvfsSingleVM(String ovf) throws InvalidOVFException {
        List<String> ovfs = new ArrayList<String>();
        List<String> ovfFiles = new ArrayList<String>();
        List<String> ovfDisks = new ArrayList<String>();
        List<String> ovfVirtualSystems = new ArrayList<String>();

        if (ovf == null)
            return null;
        // String ovf = removeInitOvfParams(ovfInstantParams);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(ovf)));
            root = doc.getDocumentElement();

            /*
             * root = doc.getElementsByTagName("Envelope").item(0); String rootString = nodeToString(root);
             */

            ovfFiles = getOvfFiles(root);
            ovfDisks = getOvfDisks(root);
            ovfVirtualSystems = getVirtualSystems(root);

            // Node parentNode = findNode (root, XSQL_ENVELOPE_NOINIT);
            Node parentNode = findNode(root, XSQL_ENVELOPE);
            // Node instantiateOvfParamsNode = parentNode.getParentNode();
            // Node rootNode = instantiateOvfParamsNode.getParentNode();

            // String instantiateOvfParamsNodeString = nodeToString(instantiateOvfParamsNode);
            // String rootNodeString = nodeToString(rootNode);

            for (int i = 0; i < ovfFiles.size(); i++) {

                Node nodeReferences = findNode(root, XSQL_REFERENCES);
                // String nodeReferencesString = nodeToString(nodeReferences);
                parentNode = updateNode(builder, doc, parentNode, ovfFiles.get(i), nodeReferences);
                String parentNodeString = nodeToString(parentNode);

                Node nodeDiskSection = findNode(root, XSQL_DISKSECTION);
                // String nodeDiskSectionString = nodeToString(nodeDiskSection);
                parentNode = updateNode(builder, doc, parentNode, ovfDisks.get(i), nodeDiskSection);
                parentNodeString = nodeToString(parentNode);

                Node nodeVirtualSystemCollection = findNode(root, XSQL_VSCOLLECTION);

                if (nodeVirtualSystemCollection == null)
                    nodeVirtualSystemCollection = findNode(root, XSQL_NEW_VS);
                // String nodeVirtualSystemCollectionString = nodeToString(nodeVirtualSystemCollection);
                parentNode = updateNode(builder, doc, parentNode, ovfVirtualSystems.get(i), nodeVirtualSystemCollection);
                parentNodeString = nodeToString(parentNode);

                /*
                 * rootNode = updateNode(builder, doc, rootNode, parentNodeString, instantiateOvfParamsNode);
                 * rootNodeString= nodeToString(rootNode);
                 */

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamResult result = new StreamResult(new StringWriter());

                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);
                String ovfSingleVM = result.getWriter().toString();

                ovfs.add(ovfSingleVM);
            }

        } catch (ParserConfigurationException e) {
            String msg = "Error parsing ovf " + e.getMessage();
            log.warn(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "SAXException with  ovf " + e.getMessage();
            log.warn(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException with  ovf " + e.getMessage();
            log.warn(msg);
            throw new InvalidOVFException(msg);
        } catch (TransformerException e) {
            String msg = "TransformerException with ovf " + e.getMessage();
            log.warn(msg);
            throw new InvalidOVFException(msg);
        }
        return ovfs;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.OVFUtils#getOSType(java.lang .String)
     */
    public String getServiceName(String ovf) throws InvalidOVFException {

        String serviceName = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(ovf)));

            serviceName = doc.getElementsByTagName(INSTANTIATEOVFPARMS_SECTION).item(0).getAttributes()
                    .getNamedItem(INSTANTIATEOVFPARMS_NAME_ATTRIBUTE).getTextContent();

        } catch (SAXException e) {
            String errorMessage = "SAXException when obtaining ServiceName." + " Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new InvalidOVFException(errorMessage);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException when obtaining " + "ServiceName. Desc: "
                    + e.getMessage();
            log.error(errorMessage);
            throw new InvalidOVFException(errorMessage);
        } catch (IOException e) {
            String errorMessage = "IOException when obtaining " + "ServiceName. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new InvalidOVFException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unexpected exception : " + e.getMessage();
            log.error(errorMessage);
            throw new InvalidOVFException(errorMessage);
        }
        return serviceName;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils# getHostname(java.lang.String)
     */
    public String getRECVMNameFromProductSection(String recProductSection) throws InvalidOVFException {
        String vmname = null;
        DocumentBuilder builder;
        Document doc;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(recProductSection)));

            NodeList virtualSystemNodes = doc.getElementsByTagName("ovfenvelope:" + VIRTUALSYSTEM_TAG);

            vmname = virtualSystemNodes.item(0).getAttributes().getNamedItem("ovfenvelope:id").getTextContent();

        } catch (ParserConfigurationException e) {
            String msg = "Error obtaining vmname from ProductSection . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (SAXException e) {
            String msg = "Error obtaining vmname from ProductSection . Desc: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        } catch (IOException e) {
            String msg = "IOException .Error obtaining vmname from ProductSection: " + e.getMessage();
            log.info(msg);
            throw new InvalidOVFException(msg);
        }
        return vmname;
    }

    public String deleteRules(String ovf) {
        String[] part_inicio = ovf.split("<rsrvr:GovernanceRuleSection", 2);
        String[] part_final = part_inicio[1].split("</rsrvr:GovernanceRuleSection>", 2);
        String ovfNew = part_inicio[0] + part_final[1];
        return ovfNew;
    }

    public String deleteProductSection(String ovf) {
        if (ovf == null) {
            return null;
        }
        String[] part_inicio = ovf.split("<ovfenvelope:ProductSection", 2);
        String[] part_final = part_inicio[1].split("</ovfenvelope:ProductSection>", 2);
        // Hay que comprobar que no sea un product section solo para
        // el registro en los sistemas de monitorización
        String producto = part_inicio[1] + part_final[0];
        String productInstalled = "<ovfenvelope:Info>installed" + "</ovfenvelope:Info>";
        if ((producto.contains(productInstalled)) && (producto.contains("PIC"))) {
            return ovf;
        }
        String ovfNew = part_inicio[0] + part_final[1];
        return ovfNew;
    }

    public List<String> getProductSectionName(String ovf) {
        if (ovf == null)
            return null;
        ovf = ovf.split(">", 2)[1];
        Document doc = null;
        int i, j, k;
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(ovf)));

        } catch (ParserConfigurationException e2) {
            e2.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NodeList aa = doc.getChildNodes();
        i = 0;
        while (aa.item(i).getNodeName() != ("InstantiateOvfParams")) {
            i++;
        }
        NodeList bb = aa.item(i).getChildNodes();
        i = 0;
        while (bb.item(i).getNodeName() != ("ovf:Envelope")) {
            i++;
        }
        NodeList dd = bb.item(i).getChildNodes();
        // Metemos en una lista los nodos del VirtualSystem
        List<String> virtualSys = new ArrayList<String>();
        NodeList nodeProduct = null;
        j = 0;
        k = 0;
        i = 0;
        NodeList ee = null;
        boolean pic = false;
        boolean total = false;
        String product = "";
        String attr = "";
        for (i = 0; i < dd.getLength(); i++) {
            if (dd.item(i).getNodeName() == ("ovf:VirtualSystem")) {
                ee = dd.item(i).getChildNodes();
                for (j = 0; j < ee.getLength(); j++) {
                    if (ee.item(j).getNodeName() == ("ovfenvelope:ProductSection")) {
                        nodeProduct = ee.item(j).getChildNodes();
                        for (k = 0; k < nodeProduct.getLength(); k++) {
                            if (nodeProduct.item(k).getNodeName() == ("ovfenvelope:Product")) {
                                product = nodeProduct.item(k).getTextContent();
                            }
                            if (nodeProduct.item(k).getNodeName() == ("ovfenvelope:Property")) {
                                try {
                                    attr = nodeProduct.item(k).getAttributes().getNamedItem("ovfenvelope:value")
                                            .getTextContent();
                                } catch (NullPointerException e) {
                                    // La propiedad a la que se accede no tiene el value
                                }
                                if (attr.equals("PIC")) {
                                    pic = true;
                                    total = true;
                                }
                            }
                        }
                        if (pic) {
                            virtualSys.add(product);
                            pic = false;
                        }
                    }
                }
            }
        }
        if (total)
            return virtualSys;
        else
            return null;
    }

    public String changeInitialResources(String ovf) {
        if (ovf == null || ovf.length() == 0) {
            return null;
        }
        String[] part_inicio = ovf.split("<ovf:VirtualSystem ovf:id=", 2);
        String[] part_final = part_inicio[1].split(">", 2);
        // Modificamos part final[0]
        String[] part_middle = part_final[0].split(" ", 2);// por un lado lo que
        // es, y por otro a cambiar
        String balancer = "";
        String middle = "";
        // Ponemos los recursos iniciales a lo que corresponda, teniendo en
        // Cuenta que el mínimo y el inicil tendrán que ser no, y el máximo
        // debe correspodner con el auténtico para que no
        String maximo = ovf.split("rsrvr:max=")[1].split(" ")[0];

        if (ovf.indexOf("rsrvr:balancer=\"true") != -1) {
            balancer = (ovf.split("rsrvr:balancer=\"")[1]).split("\"")[0];
            String portBalancer = (ovf.split("rsrvr:lbport=\"")[1]).split("\"")[0];
            middle = part_middle[0] + "  rsrvr:initial=\"1\" rsrvr:max=" + maximo + " rsrvr:min=\"1\" "
                    + "rsrvr:balancer=\"" + balancer + "\" rsrvr:lbport=\"" + portBalancer + "\">";
        } else {
            if (ovf.indexOf("rsrvr:balanced=") != -1) {
                balancer = (ovf.split("rsrvr:balanced=\"")[1]).split("\"")[0];
                middle = part_middle[0] + "  rsrvr:initial=\"1\" rsrvr:max=" + maximo + " rsrvr:min=\"1\" "
                        + "rsrvr:balanced=\"" + balancer + "\">";
            } else {
                middle = part_middle[0] + "  rsrvr:initial=\"1\" rsrvr:max=" + maximo + " rsrvr:min=\"1\">";
            }
        }
        String ovfChanged = part_inicio[0] + "<ovf:VirtualSystem ovf:id=" + middle + part_final[1];

        return ovfChanged;
    }

    private List<String> getOvfFiles(Node root) throws TransformerException {
        List<String> ovfReferences = new ArrayList<String>();
        NodeList references = findNodeList(root, XSQL_FILE);

        for (int i = 0; i < references.getLength(); i++) {
            ovfReferences.add("<References> \n" + nodeToString(references.item(i)) + "\n</References>\n");
        }
        return ovfReferences;
    }

    private List<String> getOvfDisks(Node root) throws TransformerException {
        List<String> ovfDisks = new ArrayList<String>();
        NodeList disks = findNodeList(root, XSQL_DISK);
        for (int i = 0; i < disks.getLength(); i++) {
            ovfDisks.add("<DiskSection>\n" + nodeToString(disks.item(i)) + "\n</DiskSection>\n");
        }
        return ovfDisks;
    }

    private List<String> getVirtualSystems(Node root) throws TransformerException {
        List<String> ovfVirtualSystems = new ArrayList<String>();
        NodeList virtualSystems = findNodeList(root, XSQL_OLD_VS);
        for (int i = 0; i < virtualSystems.getLength(); i++) {
            ovfVirtualSystems.add(nodeToString(virtualSystems.item(i)));
        }
        return ovfVirtualSystems;
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

    private NodeList findNodeList(Node node, String xql) throws TransformerException {
        return (XPathAPI.selectNodeList(node, xql));
    }

    // sdtartNode: Nodo a partir del cual se empieza
    // Value: valor del nodo
    // xql: seleccion exacta del nodo a cambiar su valor
    private Node setValue(Node startNode, String value, String xql) throws Exception {
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

    // From
    // http://projectwownow.blogspot.com/2008/08/java-node-to-string-conversion.html
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

}
