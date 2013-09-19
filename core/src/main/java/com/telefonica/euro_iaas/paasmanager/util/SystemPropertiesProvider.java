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

import java.util.Properties;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author Jesus M. Movilla
 */
public interface SystemPropertiesProvider {

    public final static String TASK_BASE_URL = "taskBaseUrl";
	
    public final static String PRODUCT_INSTANCE_BASE_URL = "productInstanceBaseUrl";
    public final static String PRODUCT_RELEASE_BASE_URL ="productReleaseBaseUrl";
    public final static String APPLICATION_INSTANCE_BASE_URL = "applicationInstanceBaseUrl";
    public final static String ENVIRONMENT_INSTANCE_BASE_URL = "environmentInstanceBaseUrl";
    public final static String ENVIRONMENT_BASE_URL = "environmentBaseUrl";
    public final static String APPLICATION_RELEASE_BASE_URL = "applicationReleaseBaseUrl";
    public final static String APPLICATION_TYPE_BASE_URL = "applicationTypeBaseUrl";
    public final static String VIRTUAL_SERVICE_BASE_URL =  "virtualServiceBaseUrl";
    public final static String TIER_INSTANCE_BASE_URL = "tierInstanceBaseUrl";
      
    public final static String SDC_SERVER_URL = "sdcServerUrl";
    public final static String SDC_SERVER_MEDIATYPE = "sdcServerMediaType";
    
    public final static String REC_SERVER_URL = "recServerUrl";
    public final static String REC_SERVER_MEDIATYPE = "recServerMediaType";
    
    public final static String FQN1 = "fqn1";
    public final static String FQN2 = "fqn2";
    public final static String IP1 = "ip1";
    public final static String IP2 = "ip2";
    public final static String HOSTNAME1 = "hostname1";
    public final static String HOSTNAME2 = "hostname2";
    public final static String DOMAINNAME1 = "domainname1";
    public final static String DOMAINNAME2 = "domainname2";
    public final static String OSTYPE = "osType";
    
    public final static String NEOCLAUDIA_IP = "neoclaudiaIP";
    public final static String NEOCLAUDIA_PORT = "neoclaudiaPort";
    public final static String NEOCLAUDIA_BASEURL = "neoclaudiaBaseUrl";
    public final static String NEOCLAUDIA_ORG = "neoclaudiaOrg";
    public final static String NEOCLAUDIA_SERVICE = "neoclaudiaService";
    public final static String VM_NAME_PREFIX = "vmNamePrefix";
    public final static String COLLECTOR_IP = "collectorIP";
    public final static String COLLECTOR_PORT = "collectorPORT";
    public final static String COLLECTOR_BASEURL = "collectorBaseUrl";
    
    public final static String NEOCLAUDIA_VDC_CPU = "neoclaudiaVDCcpu";
    public final static String NEOCLAUDIA_VDC_MEM = "neoclaudiaVDCmem";
    public final static String NEOCLAUDIA_VDC_DISK = "neoclaudiaVDCdisk";
    public final static String NEOCLAUDIA_NETWORK_NAME ="neoClaudiaNetworkName";
    
    public final static String NEOCLAUDIA_VDCTEMPLATE_LOCATION 
		= "neoclaudiaVDCTemplateLocation";
    public final static String NEOCLAUDIA_OVFSERVICE_LOCATION 
    	= "neoclaudiaOvfServiceLocation";
    public final static String NEOCLAUDIA_OVFVM_LOCATION 
		= "neoclaudiaOvfVMLocation";
    public final static String CLAUDIA_RESOURCE_NOTEXIST_PATTERN 
    	= "claudiaResourceNotExistPattern";   
    public final static String ENVELOPE_TEMPLATE_LOCATION 
    	= "envelopeTemplateRECManager";
    public final static String OVF_TEMPLATE_LOCATION 
		= "ovfTemplateLocation";
    
    public final static String VM_DEPLOYMENT_DELAY = "vmDeploymentDelay";
    public final static String TCLOUD_METADATA_TOKEN = "tcloud_metadata_token";
    public final static String TCLOUD_METADATA_TENANT = "tcloud_metadata_tenant";
    public final static String TCLOUD_METADATA_USER = "tcloud_metadata_user";
    
    /** The Constant KEYSTONE_URL. */
    public final static String KEYSTONE_URL = "openstack-tcloud.keystone.url";
 
    /** The Constant CLOUD_SYSTEM. */
    public final static String CLOUD_SYSTEM = "openstack-tcloud.cloudSystem";
    
    /** The Constant KEYSTONE_USER. */
    public final static String KEYSTONE_USER = "openstack-tcloud.keystone.user";

    /** The Constant KEYSTONE_PASS. */
    public final static String KEYSTONE_PASS = "openstack-tcloud.keystone.pass";

    /** The Constant KEYSTONE_TENANT. */
    public final static String KEYSTONE_TENANT = "openstack-tcloud.keystone.tenant";
    
    /** The Constant SYSTEM_FASTTRACK. */
    public final static String VALIDATION_TIME_THRESHOLD = "openstack-tcloud.keystone.threshold";
    
    /**
     * Get the property for a given key.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getProperty(String key);
    /**
     * Get the property for a given key.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    Integer getIntProperty(String key);

    /**
     * Persist the configuration properties in the SystemConfiguration namespace.
     * @param configuration the properties to store∫
     */
    void setProperties(Properties configuration);

    /**
     * Find all system configuration properties.
     * @return
     */
    Properties loadProperties();

}

