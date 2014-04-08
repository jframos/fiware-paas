/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.util.Properties;

/**
 * <p>
 * PropertiesProvider interface.
 * </p>
 * 
 * @author Jesus M. Movilla
 */
public interface SystemPropertiesProvider {

    String TASK_BASE_URL = "taskBaseUrl";

    String PRODUCT_INSTANCE_BASE_URL = "productInstanceBaseUrl";
    String PRODUCT_RELEASE_BASE_URL = "productReleaseBaseUrl";
    String APPLICATION_INSTANCE_BASE_URL = "applicationInstanceBaseUrl";
    String ENVIRONMENT_INSTANCE_BASE_URL = "environmentInstanceBaseUrl";
    String ENVIRONMENT_BASE_URL = "environmentBaseUrl";
    String APPLICATION_RELEASE_BASE_URL = "applicationReleaseBaseUrl";
    String APPLICATION_TYPE_BASE_URL = "applicationTypeBaseUrl";
    String VIRTUAL_SERVICE_BASE_URL = "virtualServiceBaseUrl";
    String TIER_INSTANCE_BASE_URL = "tierInstanceBaseUrl";

    String SDC_SERVER_URL = "sdcServerUrl";
    String SDC_SERVER_MEDIATYPE = "sdcServerMediaType";

    String REC_SERVER_URL = "recServerUrl";
    String REC_SERVER_MEDIATYPE = "recServerMediaType";

    String FQN1 = "fqn1";
    String FQN2 = "fqn2";
    String IP1 = "ip1";
    String IP2 = "ip2";
    String HOSTNAME1 = "hostname1";
    String HOSTNAME2 = "hostname2";
    String DOMAINNAME1 = "domainname1";
    String DOMAINNAME2 = "domainname2";
    String OSTYPE = "osType";

    String NEOCLAUDIA_IP = "neoclaudiaIP";
    String NEOCLAUDIA_PORT = "neoclaudiaPort";
    String NEOCLAUDIA_BASEURL = "neoclaudiaBaseUrl";
    String NEOCLAUDIA_ORG = "neoclaudiaOrg";
    String NEOCLAUDIA_SERVICE = "neoclaudiaService";
    String VM_NAME_PREFIX = "vmNamePrefix";
    String COLLECTOR_IP = "collectorIP";
    String COLLECTOR_PORT = "collectorPORT";
    String COLLECTOR_BASEURL = "collectorBaseUrl";
    String COLLECTOR_MYSQL = "collectorMysql";
    String NEOCLAUDIA_VDC_CPU = "neoclaudiaVDCcpu";
    String NEOCLAUDIA_VDC_MEM = "neoclaudiaVDCmem";
    String NEOCLAUDIA_VDC_DISK = "neoclaudiaVDCdisk";
    String NEOCLAUDIA_NETWORK_NAME = "neoClaudiaNetworkName";

    String NEOCLAUDIA_VDCTEMPLATE_LOCATION = "neoclaudiaVDCTemplateLocation";
    String NEOCLAUDIA_OVFSERVICE_LOCATION = "neoclaudiaOvfServiceLocation";
    String NEOCLAUDIA_OVFVM_LOCATION = "neoclaudiaOvfVMLocation";
    String CLAUDIA_RESOURCE_NOTEXIST_PATTERN = "claudiaResourceNotExistPattern";
    String ENVELOPE_TEMPLATE_LOCATION = "envelopeTemplateRECManager";
    String OVF_TEMPLATE_LOCATION = "ovfTemplateLocation";

    String VM_DEPLOYMENT_DELAY = "vmDeploymentDelay";
    String TCLOUD_METADATA_TOKEN = "tcloud_metadata_token";
    String TCLOUD_METADATA_TENANT = "tcloud_metadata_tenant";
    String TCLOUD_METADATA_USER = "tcloud_metadata_user";

    /** The Constant KEYSTONE_URL. */
    String KEYSTONE_URL = "openstack-tcloud.keystone.url";

    /** The Constant CLOUD_SYSTEM. */
    String CLOUD_SYSTEM = "openstack-tcloud.cloudSystem";

    /** The Constant KEYSTONE_USER. */
    String KEYSTONE_USER = "openstack-tcloud.keystone.user";

    /** The Constant KEYSTONE_PASS. */
    String KEYSTONE_PASS = "openstack-tcloud.keystone.pass";

    /** The Constant KEYSTONE_TENANT. */
    String KEYSTONE_TENANT = "openstack-tcloud.keystone.tenant";

    /** The Constant SYSTEM_FASTTRACK. */
    String VALIDATION_TIME_THRESHOLD = "openstack-tcloud.keystone.threshold";

    /**
     * default region name.
     */
    String DEFAULT_REGION_NAME = "openstack-tcloud.region.default";

    /*** OPENSTACK CONSTANTS */
    String URL_PROPERTY = "management.provisioning.openstack.url";
    String URL_KEYSTONE_PROPERTY = "management.provisioning.openstack.keystone.url";
    String USER_PROPERTY = "management.provisioning.openstack.user";
    String PASSWORD_PROPERTY = "management.provisioning.openstack.password";

    String URL_OPENSTACK_DISTRIBUTION = "openstack.distribution";

    String OPENSTACK_SYNCHRONIZATION_POLLING_PERIOD = "openstack.sync.polling.period";

    String KEYSTONE_DATABASE_URL = "keystone.database.url";
    String KEYSTONE_DATABASE_DRIVER = "keystone.database.driver";
    String KEYSTONE_DATABASE_USERNAME = "keystone.database.username";
    String KEYSTONE_DATABASE_PASSWORD = "keystone.database.password";

    String PUBLIC_NETWORK_ID = "openstack.network.public";
    String PUBLIC_ROUTER_ID = "openstack.router.public";

    /**
     * pool name in nova *
     */
    String NOVA_IPFLOATING_POOLNAME = "nova.ipfloating.poolname";

    /**
     * Get the property for a given key.
     * 
     * @param key
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    Integer getIntProperty(String key);

    /**
     * Get the property for a given key.
     * 
     * @param key
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getProperty(String key);

    /**
     * Find all system configuration properties.
     * 
     * @return
     */
    Properties loadProperties();

    /**
     * Persist the configuration properties in the SystemConfiguration namespace.
     * 
     * @param configuration
     *            the properties to store∫
     */
    void setProperties(Properties configuration);

}
