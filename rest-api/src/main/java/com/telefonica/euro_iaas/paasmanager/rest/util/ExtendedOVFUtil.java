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

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 * 
 */
public interface ExtendedOVFUtil {

	public final static String INSTANTIATEOVFPARAMS = "InstantiateOvfParams";
	public final static String INSTANTIATEOVFPARAMS_NAME = "name";

	public final static String VIRTUALSYSTEMCOLLECTION = "ovf:VirtualSystemCollection";

	public final static String VIRTUALSYTEM_SECTION = "ovf:VirtualSystem";
	public final static String GENERAL_ID = "ovf:id";
	public final static String GENERAL_HREF = "ovf:href";
	public final static String NUMBER_MAX_INSTANCES = "rsrvr:max";
	public final static String NUMBER_MIN_INSTANCES = "rsrvr:min";
	public final static String INITIAL_NUMBER_INSTANCES = "rsrvr:initial";

	public final static String OPERATINGSYTEM_SECTION = "ovf:OperatingSystemSection";

	public final static String PRODUCT_SECTION = "ovfenvelope:ProductSection";
	public final static String PRODUCTNAME_TAG = "ovfenvelope:Product";
	public final static String PRODUCTVERSION_TAG = "ovfenvelope:Version";
	public final static String KEYATTRIBUTE_TAG = "ovfenvelope:key";
	public final static String KEYATTRIBUTE_VALUE = "org.fourcaast.instancecomponent.type";
	public final static String PROPERTY_TAG = "ovfenvelope:Property";
	public final static String VALUEATTRIBUTE_TAG = "ovfenvelope:value";

	public final static String VIRTUALHARDWARE_SECTION = "ovf:VirtualHardwareSection";

	public final static String FILE_TAG = "ovf:File";
	public final static String DIGEST_ATTRIBUTE = "rsrvr:digest";
	public final static String DISK_SECTION = "ovf:DiskSection";
	public final static String DISK_TAG = "ovf:Disk";
	public final static String DISKID_ATTRIBUTE = "ovf:diskId";
	public final static String FILEREF_ATTRIBUTE = "ovf:fileRef";
	public final static String DISKCAPACITY_ATTRIBUTE = "ovf:capacity";
	public final static String DISKOVFFORMAT_ATTRIBUTE = "ovf:format";

	public final static String VIRTUALSERVICE_SECTION = "VirtualService";
	public final static String VIRTUALSERVICE_ID = "ovf:id";

	/**
	 * Get the environmentName from payload
	 * 
	 * @param payload
	 * @return EnvironmentName
	 * @throws InvalidEnvironmentRequestException
	 */
	String getEnvironmentName(String payload)
			throws InvalidEnvironmentRequestException;

	/**
	 * Get the Tiers form payload
	 * 
	 * @param payload
	 * @return
	 * @throws InvalidEnvironmentRequestException
	 */
	List<Tier> getTiers(String payload)
			throws InvalidEnvironmentRequestException;

	/**
	 * Get the name of the virtualService
	 * 
	 * @param extendedovf
	 * @return
	 * @throws InvalidEnvironmentRequestException
	 */
	String getVirtualServiceName(String extendedovf)
			throws InvalidEnvironmentRequestException;

	/**
	 * Finds out if the ovf has virtualservice information
	 * 
	 * @param extendedovf
	 * @return
	 * @throws InvalidEnvironmentRequestException
	 */
	boolean isVirtualServicePayload(String extendedovf)
			throws InvalidEnvironmentRequestException;

	/**
	 * Retrieves the virtual services in the extendedovf
	 * 
	 * @param extendedovf
	 * @return
	 * @throws InvalidEnvironmentRequestException
	 */
	List<String> getVirtualServices(String extendedovf)
			throws InvalidEnvironmentRequestException;

	PaasManagerUser getCredentials();
	
	
}
