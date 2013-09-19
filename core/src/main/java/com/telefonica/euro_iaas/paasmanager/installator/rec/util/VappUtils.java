/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.installator.rec.util;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;

/**
 * @author jesus.movilla
 *
 */
public interface VappUtils {

    public final static String VIRTUALSYSTEM_PATH = "/InstantiateOvfParams/" +
    		"Envelope/VirtualSystem";
    public final static String VS_PRODUCTSECTION_PATH = "/VirtualSystem/ProductSection";
    public final static String PRODUCTSECTION_PATH = "/ProductSection";
    public final static String INFOSECTION_PATH = "/Envelope/AnnotationSection/Info";
    
    public final static String NODENAME_TAG = "ovf:Name";
    public final static String OPERATINGSYSTEMSECTION_TAG = "OperatingSystemSection";
    public final static String ELASTICARRAYSECTION_TAG = "rsrvr:ElasticArraySection";
    public final static String VIRTUALHARDWARESECTION_TAG = "ovf:VirtualHardwareSection";
    public final static String PRODUCTSECTION_TAG = "ovfenvelope:ProductSection";
    public final static String VIRTUALSYSTEM_TAG = "VirtualSystem";
    public final static String VIRTUALSYSTEMID_TAG = "ovf:id";
    public final static String GOVERNANCE_RULE_TAG ="rsrvr:GovernanceRuleSection";
    
    public final static String OVFPPROPERTY_TAG = "ovfenvelope:Property";
    public final static String VAPPPROPERTY_TAG = "Property";
    public final static String VAPP_KEYATTRIBUTE_TAG ="key";
    public final static String VAPP_VALUEATTRIBUTE_TAG ="value";
    
    public final static String KEYATTRIBUTE_TAG ="ovfenvelope:key";
    public final static String KEYATTRIBUTE_VALUE= "org.fourcaast.instancecomponent.type";
    public final static String KEYATTRIBUTE_VALUE_ID= "org.fourcaast.instancecomponent.id";
    public final static String KEYATTRIBUTE_VALUE_ACID = "org.fourcaast.instancecomponent.id";
    public final static String KEYATTRIBUTE_VALUE_PICPARENT
    	= "org.fourcaast.instancecomponent.parent";
    public final static String KEYATTRIBUTE_VALUE_APPPARENT
		= "org.fourcaast.virtualservice.application";
    public final static String KEYATTRIBUTE_VALUE_VMPARENT
		= "org.fourcaast.virtualservice.vm";
    public final static String VALUEATTRIBUTE_TAG ="ovfenvelope:value";
    
    public final static String IPATTRIBUTE_VALUE="org.fourcaast.rec.address";
    public final static String USERNAMEATTRIBUTE_VALUE="org.fourcaast.rec.username";
    public final static String PASSWORDATTRIBUTE_VALUE="org.fourcaast.rec.password";
    //public final static String IPADDRESS_TAG = "iep:IPv4Address";
    public final static String IPADDRESS_TAG = "IPv4Address";
    public final static String NETWORK_TAG = "rasd:Connection";
    

	String getAppId (String vapp) throws InvalidOVFException;
	
	String getVmId (String vapp);
	
	String getLogin(String vapp) throws ProductInstallatorException;
	
	String getPassword(String vapp)throws ProductInstallatorException;
	
	String getIP (String vapp) throws ProductInstallatorException;
	
	String getNetworks (String vapp) throws InvalidOVFException;
	
	String getRECVapp(String vapp, String ip, String login,
			String password) throws ProductInstallatorException;
	
	/**
	 * Obtain all the productSections of type PIC
	 * @param vapp
	 * @return
	 * @throws ProductInstallatorException
	 */
	List<String> getPICProductSections (String vapp) 
			throws ProductInstallatorException;

	/**
	 * Obtain all the productSections of type AC
	 * @param vapp
	 * @return
	 * @throws ProductInstallatorException
	 */
	List<String> getACProductSections (String vapp) 
			throws ProductInstallatorException;
	
	/**
	 * Obtain all the productSections of type AC associated to the picId
	 * @param picId
	 * @return
	 * @throws ProductInstallatorException
	 */
	List<String> getACProductSectionsByPicId (String vapp, String picId) 
			throws ProductInstallatorException;
	
	/**
	 * Obtain the corresponding PIC whose id is picId
	 * @param vapp
	 * @return
	 * @throws ProductInstallatorException
	 */
	String getPICProductSection (String picId, String vapp) 
			throws ProductInstallatorException;

	
	String getAppIdFromAC(String acProductSection) throws InvalidOVFException;
	
	String getVmIdFromAC(String acProductSection) throws InvalidOVFException;
	
	String getPicId(String acProductSection) throws InvalidOVFException;
	
	String getAcId (String acProductSection) throws InvalidOVFException;
	
	String getHostname (String vappVS) throws InvalidOVFException;
	
	String getEnvelopeTypeSegment (String serviceFile, String appId) 
			throws InvalidOVFException;
	
	String getPicIdFromAC (String acProductSection) throws InvalidOVFException;
}
