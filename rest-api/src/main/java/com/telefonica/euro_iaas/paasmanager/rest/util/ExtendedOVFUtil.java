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

package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public interface ExtendedOVFUtil {

    static final String INSTANTIATEOVFPARAMS = "InstantiateOvfParams";
    static final String INSTANTIATEOVFPARAMS_NAME = "name";

    static final String VIRTUALSYSTEMCOLLECTION = "ovf:VirtualSystemCollection";

    static final String VIRTUALSYTEM_SECTION = "ovf:VirtualSystem";
    static final String GENERAL_ID = "ovf:id";
    static final String GENERAL_HREF = "ovf:href";
    static final String NUMBER_MAX_INSTANCES = "rsrvr:max";
    static final String NUMBER_MIN_INSTANCES = "rsrvr:min";
    static final String INITIAL_NUMBER_INSTANCES = "rsrvr:initial";

    static final String OPERATINGSYTEM_SECTION = "ovf:OperatingSystemSection";

    static final String PRODUCT_SECTION = "ovfenvelope:ProductSection";
    static final String PRODUCTNAME_TAG = "ovfenvelope:Product";
    static final String PRODUCTVERSION_TAG = "ovfenvelope:Version";
    static final String KEYATTRIBUTE_TAG = "ovfenvelope:key";
    static final String KEYATTRIBUTE_VALUE = "org.fourcaast.instancecomponent.type";
    static final String PROPERTY_TAG = "ovfenvelope:Property";
    static final String VALUEATTRIBUTE_TAG = "ovfenvelope:value";

    static final String VIRTUALHARDWARE_SECTION = "ovf:VirtualHardwareSection";

    static final String FILE_TAG = "ovf:File";
    static final String DIGEST_ATTRIBUTE = "rsrvr:digest";
    static final String DISK_SECTION = "ovf:DiskSection";
    static final String DISK_TAG = "ovf:Disk";
    static final String DISKID_ATTRIBUTE = "ovf:diskId";
    static final String FILEREF_ATTRIBUTE = "ovf:fileRef";
    static final String DISKCAPACITY_ATTRIBUTE = "ovf:capacity";
    static final String DISKOVFFORMAT_ATTRIBUTE = "ovf:format";

    static final String VIRTUALSERVICE_SECTION = "VirtualService";
    static final String VIRTUALSERVICE_ID = "ovf:id";

    /**
     * Get the environmentName from payload
     * 
     * @param payload
     * @return EnvironmentName
     * @throws InvalidEnvironmentRequestException
     */
    String getEnvironmentName(String payload) throws InvalidEnvironmentRequestException;

    /**
     * Get the Tiers form payload
     * 
     * @param payload
     * @return
     * @throws InvalidEnvironmentRequestException
     */
    Set<Tier> getTiers(String payload, String vdc) throws InvalidEnvironmentRequestException;

    /**
     * Get the name of the virtualService
     * 
     * @param extendedovf
     * @return
     * @throws InvalidEnvironmentRequestException
     */
    String getVirtualServiceName(String extendedovf) throws InvalidEnvironmentRequestException;

    /**
     * Finds out if the ovf has virtualservice information
     * 
     * @param extendedovf
     * @return
     * @throws InvalidEnvironmentRequestException
     */
    boolean isVirtualServicePayload(String extendedovf) throws InvalidEnvironmentRequestException;

    /**
     * Retrieves the virtual services in the extendedovf
     * 
     * @param extendedovf
     * @return
     * @throws InvalidEnvironmentRequestException
     */
    List<String> getVirtualServices(String extendedovf) throws InvalidEnvironmentRequestException;

    PaasManagerUser getCredentials();

}
