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

import java.util.HashMap;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * @author jesus.movilla
 * 
 */
public interface VappUtils {

    public final static String PRIVATE_NETWORK = "management";
    public final static String PUBLIC_NETWORK = "service";
    public final static String NETWORK_ITEM = "rasd:Connection";

    public final static String NODENAME_TAG = "ovf:Name";
    public final static String OPERATINGSYSTEMSECTION_TAG = "OperatingSystemSection";
    public final static String ELASTICARRAYSECTION_TAG = "rsrvr:ElasticArraySection";
    public final static String VIRTUALHARDWARESECTION_TAG = "ovf:VirtualHardwareSection";
    public final static String PRODUCTSECTION_TAG = "ovfenvelope:ProductSection";
    public final static String VIRTUALSYSTEM_TAG = "VirtualSystem";

    public final static String IPADDRESS_TAG_FIWARE = "iep:IPv4Address";
    public final static String IPADDRESS_TAG = "IPv4Address";

    public final static String OPERATINGSYSTEM_SECTION = "ovf:OperatingSystemSection";
    public final static String OSTYPE_ID = "ovf:id";
    public final static String VIRTUAL_SYSTEM_ID = "ovf:id";
    public final static String INSTANTIATEOVFPARMS_SECTION = "InstantiateOvfParams";
    public final static String INSTANTIATEOVFPARMS_NAME_ATTRIBUTE = "name";

    public final static String FILE_TAG = "ovf:File";
    public final static String VIRTUAL_SYSTEM_TAG = "ovf:VirtualSystem";
    public final static String ITEM_TAG = "ovf:Item";

    /**
     * Extract FqnId from Vapp
     * 
     * @param vapp
     * @return
     * @throws InvalidVappException
     */
    String getFqnId(String vapp) throws InvalidVappException;

    /**
     * Captures the list of ips associated to the VApp (first private, last
     * public)
     * 
     * @param vapp
     * @return
     * @throws InvalidVappException
     */
    List<String> getIP(String vapp) throws InvalidVappException;

    String getMacroVapp (String ovf, EnvironmentInstance envIns, TierInstance tierInstance) throws InvalidOVFException ;

    HashMap<String, String> getNetworkAndIP(String vappReplica) throws InvalidVappException;

    /**
     * Extract Replica from fqn
     * 
     * @param fqn
     * @return
     */
    String getReplica(String fqnId);

    /**
     * Gets the VApps of the different VMs that forms the vapp
     * 
     * @param claudiaData
     * @param vapp
     * @return
     * @throws InvalidVappException
     */
    List<String> getVappsSingleVM(ClaudiaData claudiaData, String vapp)
    throws InvalidVappException;

    /**
     * Extract Vmname from fqn
     * 
     * @param fqn
     * @return
     */
    String getVMName(String fqn);
}
