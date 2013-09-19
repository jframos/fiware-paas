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

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;

/**
 * @author jesus.movilla
 *
 */
public interface OVFUtils {

	public final static String XSQL_ENVELOPE = "/InstantiateOvfParams/Envelope";
	public final static String XSQL_REFERENCES = "/InstantiateOvfParams/Envelope/References";
	public final static String XSQL_FILE = "/InstantiateOvfParams/Envelope/References/File";
	public final static String XSQL_DISKSECTION = "/InstantiateOvfParams/Envelope/DiskSection";
	public final static String XSQL_DISK = "/InstantiateOvfParams/Envelope/DiskSection/Disk";
	public final static String XSQL_VSCOLLECTION = "/InstantiateOvfParams/Envelope/" +
			"VirtualSystemCollection";
	public final static String XSQL_NEW_VS = "/InstantiateOvfParams/Envelope/VirtualSystem";
	public final static String XSQL_OLD_VS = "/InstantiateOvfParams/Envelope/" +
			"VirtualSystemCollection/VirtualSystem";
	
	/*public final static String XSQL_ENVELOPE = "/Envelope";
	public final static String XSQL_ENVELOPE_NOINIT = "/Envelope";
	public final static String XSQL_REFERENCES = "/Envelope/References";
	public final static String XSQL_FILE = "/Envelope/References/File";
	public final static String XSQL_DISKSECTION = "/Envelope/DiskSection";
	public final static String XSQL_DISK = "/Envelope/DiskSection/Disk";
	public final static String XSQL_VSCOLLECTION = "/Envelope/" +
			"VirtualSystemCollection";
	public final static String XSQL_NEW_VS = "/Envelope/VirtualSystem";
	public final static String XSQL_OLD_VS = "/Envelope/" +
			"VirtualSystemCollection/VirtualSystem";*/
	
	//public final static String OPERATINGSYSTEM_SECTION = "OperatingSystemSection";
	public final static String OPERATINGSYSTEM_SECTION = "ovf:OperatingSystemSection";
	public final static String OSTYPE_ID = "ovf:id";
	public final static String VIRTUAL_SYSTEM_ID = "ovf:id";
	public final static String INSTANTIATEOVFPARMS_SECTION = "InstantiateOvfParams";
	public final static String INSTANTIATEOVFPARMS_NAME_ATTRIBUTE = "name";
	
	public final static String FILE_TAG = "ovf:File";
	public final static String VIRTUAL_SYSTEM_TAG = "ovf:VirtualSystem";
	
	public final static String VIRTUALSYSTEM_TAG = "ovfenvelope:VirtualSystem";
	public final static String VIRTUALSYSTEMID_TAG = "ovfenvelope:id";
	
	List<String> getOvfsSingleVM(String ovf) throws InvalidOVFException ;
	
    String getServiceName (String ovf) throws InvalidOVFException ;
    
	String getOSType (String ovfVM) throws InvalidOVFException ;
	
	String getRECVMNameFromProductSection (String recProductSection) 
			throws InvalidOVFException ;
	

}
