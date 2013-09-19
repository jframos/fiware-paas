/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

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


/**
 * @author jesus.movilla
 *
 */
public class OVFUtilsDomImpl implements OVFUtils {

	/** The log. */
    private static Logger log = Logger.getLogger(OVFUtilsDomImpl.class);
 

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.util.OVFUtils#getOvfsSingleVM(java.lang.String)
	 */
	public List<String> getOvfsSingleVM(String ovf) throws InvalidOVFException {
		List<String> ovfs = new ArrayList<String>();
		List<String> ovfFiles = new ArrayList<String>();
		List<String> ovfDisks = new ArrayList<String>();
		List<String> ovfVirtualSystems = new ArrayList<String>();
		List<String> ovfStartUpOrder = new ArrayList<String>();
		
		if (ovf == null)
			return null;
		//String ovf = removeInitOvfParams(ovfInstantParams);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;
        
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(ovf)));
			root = doc.getDocumentElement();
			
			/*root = doc.getElementsByTagName("Envelope").item(0);
			String rootString = nodeToString(root);*/
			
			ovfFiles = getOvfFiles(root);
			ovfDisks = getOvfDisks(root);
			ovfVirtualSystems =	getVirtualSystems(root);
			
			//Node parentNode = findNode (root, XSQL_ENVELOPE_NOINIT);
			Node parentNode = findNode (root, XSQL_ENVELOPE);
			Node instantiateOvfParamsNode = parentNode.getParentNode();
			Node rootNode = instantiateOvfParamsNode.getParentNode();
			
			String instantiateOvfParamsNodeString 
				= nodeToString(instantiateOvfParamsNode);
			String rootNodeString 
				= nodeToString(rootNode);
			
			for (int i=0; i < ovfFiles.size(); i++) {
					
				Node nodeReferences = findNode(root,XSQL_REFERENCES);
				String nodeReferencesString = nodeToString(nodeReferences);
				parentNode = updateNode(builder, doc, parentNode, 
					ovfFiles.get(i), nodeReferences);
				String parentNodeString = nodeToString(parentNode);
				
				Node nodeDiskSection = findNode(root,XSQL_DISKSECTION);
				String nodeDiskSectionString = nodeToString(nodeDiskSection);
				parentNode = updateNode(builder, doc, parentNode, 
						ovfDisks.get(i), nodeDiskSection);
				parentNodeString = nodeToString(parentNode);
				
				Node nodeVirtualSystemCollection = findNode(root,
						XSQL_VSCOLLECTION);
				
				if (nodeVirtualSystemCollection == null)
					nodeVirtualSystemCollection = findNode(root, XSQL_NEW_VS);
				String nodeVirtualSystemCollectionString 
					= nodeToString(nodeVirtualSystemCollection);
				parentNode = updateNode(builder, doc, parentNode, 
						ovfVirtualSystems.get(i), nodeVirtualSystemCollection);
				parentNodeString= nodeToString(parentNode);
			
				/*rootNode = updateNode(builder, doc, rootNode, 
						parentNodeString, instantiateOvfParamsNode);
				rootNodeString= nodeToString(rootNode);*/
				
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
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (SAXException e) {
			String msg = "SAXException with  ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (IOException e) {
			String msg = "IOException with  ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (TransformerException e) {
			String msg = "TransformerException with ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		}       
		return ovfs;
	}

	private String removeInitOvfParams (String ovf) throws InvalidOVFException {
		String ovfSingleVM;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;
        
       
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(ovf)));
			root = doc.getDocumentElement();
			
			Node parentNode = findNode (root, XSQL_ENVELOPE);
			Node instantiateOvfParamsNode = parentNode.getParentNode();
			Node rootNode = instantiateOvfParamsNode.getParentNode();
			
			rootNode = updateNode(builder, doc, rootNode, 
					nodeToString(parentNode), instantiateOvfParamsNode);
			String rootNodeString= nodeToString(rootNode);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new StringWriter());
			
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			ovfSingleVM = result.getWriter().toString();
			
        } catch (ParserConfigurationException e) {
			String msg = "Error parsing ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (SAXException e) {
			String msg = "SAXException with  ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (IOException e) {
			String msg = "IOException with  ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		} catch (TransformerException e) {
			String msg = "TransformerException with ovf " + e.getMessage();
			System.out.println(msg);
			throw new InvalidOVFException (msg);
		}   
        
        return ovfSingleVM;			
	}
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.util.OVFUtils#getOSType(java.lang.String)
	 */
	public String getOSType(String ovfVM) throws InvalidOVFException {
		
		String osType = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;
        
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(ovfVM)));
		
			osType = doc.
						getElementsByTagName(OPERATINGSYSTEM_SECTION).item(0)
						.getAttributes().getNamedItem(OSTYPE_ID).getTextContent();
							
		} catch (SAXException e) {
			String errorMessage = "SAXException when obtaining OsType." +
					" Desc: " +	e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining " +
					"OsType. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining " +
					"OsType. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		}
        return osType;
	}
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.util.OVFUtils#getOSType(java.lang.String)
	 */
	public String getServiceName(String ovf) throws InvalidOVFException {
		
		String osType = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        Element root;
        
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(ovf)));
		
			osType = doc.
						getElementsByTagName(INSTANTIATEOVFPARMS_SECTION).item(0)
						.getAttributes().getNamedItem(INSTANTIATEOVFPARMS_NAME_ATTRIBUTE)
						.getTextContent();
							
		} catch (SAXException e) {
			String errorMessage = "SAXException when obtaining ServiceName." +
					" Desc: " +	e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (ParserConfigurationException e) {
			String errorMessage = "ParserConfigurationException when obtaining " +
					"ServiceName. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException when obtaining " +
					"ServiceName. Desc: " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected exception : " + e.getMessage();
			log.error(errorMessage);
			throw new InvalidOVFException (errorMessage);
		}
        return osType;
	}
	
	private List<String> getOvfFiles (Node root) 
			throws TransformerException {
		List<String> ovfReferences = new ArrayList<String>();
		NodeList references = findNodeList(root, XSQL_FILE);

		for (int i =0; i < references.getLength(); i++){
			ovfReferences.add("<References> \n"
					+ nodeToString(references.item(i)) +
					"\n</References>\n");
		}
		return ovfReferences;
	}
	
	private List<String> getOvfDisks (Node root) throws TransformerException {
		List<String> ovfDisks = new ArrayList<String>();
		NodeList disks = findNodeList(root, XSQL_DISK);
		for (int i =0; i < disks.getLength(); i++){
			ovfDisks.add("<DiskSection>\n"
					+ nodeToString(disks.item(i)) +
					"\n</DiskSection>\n");
		}
		return ovfDisks;
	}
	
	private List<String> getVirtualSystems (Node root) 
			throws TransformerException {
		List<String> ovfVirtualSystems = new ArrayList<String>();
		NodeList virtualSystems = findNodeList(root, XSQL_OLD_VS);
		for (int i =0; i < virtualSystems.getLength(); i++){
			ovfVirtualSystems.add(nodeToString(virtualSystems.item(i)));
		}
		return ovfVirtualSystems;
	}
	
	/**
	 * Update a Node from a parentNode
	 * @param docBuilder
	 * @param doc
	 * @param parentNode
	 * @param newNode
	 * @param oldNode
	 * @return
	 */
	private Node updateNode (DocumentBuilder docBuilder, Document doc, 
			Node parentNode, String newNode, Node oldNode){
		try {
			Node fragmentNode = docBuilder.parse(new InputSource(new StringReader(newNode))).getDocumentElement();
			fragmentNode = doc.importNode(fragmentNode, true);
			//System.out.println("newNode:" + nodeToString(fragmentNode));
			parentNode.replaceChild(fragmentNode,oldNode);
		}catch (SAXException se){
			se.printStackTrace();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}	
		return parentNode;		
	}
	
	private Node findNode (Node node, String xql) throws TransformerException {
		//System.out.println("Node:" + xql);
		return (XPathAPI.selectSingleNode(node,xql));
	}
	
	private NodeList findNodeList (Node node, String xql) 
			throws TransformerException {
		//System.out.println("Node:" + xql);
		return (XPathAPI.selectNodeList(node,xql));
	}

	//sdtartNode: Nodo a partir del cual se empieza
	// Value: valor del nodo
	//xql: seleccion exacta del nodo a cambiar su valor
	private Node setValue(Node startNode, String value, String xql)	
			throws Exception {
		Node targetNode = XPathAPI.selectSingleNode( startNode,xql );
		NodeList children = targetNode.getChildNodes();
		int index = 0;
		int length = children.getLength();

		// Remove all of the current contents
		for(index = 0; index < length; index++){
			targetNode.removeChild( children.item( index ) );
		}

		// Add in the new value
		Document doc = startNode.getOwnerDocument();
		targetNode.appendChild( doc.createTextNode(value) );
	    return targetNode;
	}
	
	private String getTextContents (Node node ){
		NodeList childNodes;
		StringBuffer contents = new StringBuffer();
	    
	    childNodes =  node.getChildNodes();
	    for(int i=0; i < childNodes.getLength(); i++ ){
	    	if( childNodes.item(i).getNodeType() == Node.TEXT_NODE ){
	    		contents.append(childNodes.item(i).getNodeValue());
	    	}
	    }
	    return contents.toString();
	} 
	
	//From http://projectwownow.blogspot.com/2008/08/java-node-to-string-conversion.html
	private String nodeToString(Node node) 
	{
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



	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils#getHostname(java.lang.String)
	 */
	public String getRECVMNameFromProductSection(String recProductSection) 
			throws InvalidOVFException {
		String vmname = null;
        DocumentBuilder builder;
		Document doc;
        
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(recProductSection)));
			
			NodeList virtualSystemNodes = doc.
					getElementsByTagName(VIRTUALSYSTEM_TAG);
			
			vmname = virtualSystemNodes.item(0).getAttributes()
					.getNamedItem(VIRTUALSYSTEMID_TAG).getTextContent();
			
		} catch (ParserConfigurationException e) {
			String msg ="Error obtaining vmname from ProductSection . Desc: " 
					+ e.getMessage();
			log.info(msg);
			throw new InvalidOVFException(msg);
		} catch (SAXException e) {
			String msg ="Error obtaining vmname from ProductSection . Desc: " 
					+ e.getMessage();
			log.info(msg);
			throw new InvalidOVFException(msg);
		} catch (IOException e) {
			String msg ="IOException .Error obtaining vmname from ProductSection: " 
					+ e.getMessage();
			log.info(msg);
			throw new InvalidOVFException(msg);
		}
		return vmname;
	}


}
