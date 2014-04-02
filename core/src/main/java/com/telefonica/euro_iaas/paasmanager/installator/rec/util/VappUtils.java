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

package com.telefonica.euro_iaas.paasmanager.installator.rec.util;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;

/**
 * @author jesus.movilla
 */
public interface VappUtils {

    public final static String VIRTUALSYSTEM_PATH = "/InstantiateOvfParams/" + "Envelope/VirtualSystem";
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
    public final static String GOVERNANCE_RULE_TAG = "rsrvr:GovernanceRuleSection";

    public final static String OVFPPROPERTY_TAG = "ovfenvelope:Property";
    public final static String VAPPPROPERTY_TAG = "Property";
    public final static String VAPP_KEYATTRIBUTE_TAG = "key";
    public final static String VAPP_VALUEATTRIBUTE_TAG = "value";

    public final static String KEYATTRIBUTE_TAG = "ovfenvelope:key";
    public final static String KEYATTRIBUTE_VALUE = "org.fourcaast.instancecomponent.type";
    public final static String KEYATTRIBUTE_VALUE_ID = "org.fourcaast.instancecomponent.id";
    public final static String KEYATTRIBUTE_VALUE_ACID = "org.fourcaast.instancecomponent.id";
    public final static String KEYATTRIBUTE_VALUE_PICPARENT = "org.fourcaast.instancecomponent.parent";
    public final static String KEYATTRIBUTE_VALUE_APPPARENT = "org.fourcaast.virtualservice.application";
    public final static String KEYATTRIBUTE_VALUE_VMPARENT = "org.fourcaast.virtualservice.vm";
    public final static String VALUEATTRIBUTE_TAG = "ovfenvelope:value";

    public final static String IPATTRIBUTE_VALUE = "org.fourcaast.rec.address";
    public final static String USERNAMEATTRIBUTE_VALUE = "org.fourcaast.rec.username";
    public final static String PASSWORDATTRIBUTE_VALUE = "org.fourcaast.rec.password";
    public final static String IPADDRESS_TAG_FIWARE = "iep:IPv4Address";
    public final static String IPADDRESS_TAG = "IPv4Address";
    public final static String NETWORK_TAG = "rasd:Connection";

    /**
     * Get tha Appid from an ovf
     * 
     * @param ovf
     * @return
     * @throws InvalidOVFException
     */

    String getAppId(String ovf) throws InvalidOVFException;

    /**
     * Get the Login of an VM that is in a Property
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    String getLogin(String ovf) throws ProductInstallatorException;

    /**
     * * Get the Password of an VM that is in a Property
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    String getPassword(String ovf) throws ProductInstallatorException;

    /**
     * Get the ip from a Vapp
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    String getIP(String vapp) throws ProductInstallatorException;

    /**
     * Get the networks from an ovf
     * 
     * @param vapp
     * @return
     * @throws InvalidOVFException
     */
    String getNetworks(String ovf) throws InvalidOVFException;

    /**
     * Get the RECVApp setting ip, login and passwords values in ovf
     * 
     * @param ovf
     * @param ip
     * @param login
     * @param password
     * @return
     * @throws ProductInstallatorException
     */
    String getRECVapp(String ovf, String ip, String login, String password) throws ProductInstallatorException;

    /**
     * Obtain all the productSections of type PIC
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    List<String> getPICProductSections(String ovf) throws ProductInstallatorException;

    /**
     * Obtain all the productSections of type AC
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    List<String> getACProductSections(String ovf) throws ProductInstallatorException;

    /**
     * Obtain all the productSections of type AC associated to the picId
     * 
     * @param ovf
     * @param picId
     * @return
     * @throws ProductInstallatorException
     */
    List<String> getACProductSectionsByPicId(String ovf, String picId) throws ProductInstallatorException;

    /**
     * Obtain the corresponding PIC whose id is picId
     * 
     * @param ovf
     * @return
     * @throws ProductInstallatorException
     */
    String getPICProductSection(String picId, String ovf) throws ProductInstallatorException;

    /**
     * Obtain the appId from a AC ProductSection (for VirtualService)
     * 
     * @param acProductSection
     * @return
     * @throws InvalidOVFException
     */
    String getAppIdFromAC(String acProductSection) throws InvalidOVFException;

    /**
     * Obtain the appId from a AC ProductSection (for VirtualService)
     * 
     * @param acProductSection
     * @return
     * @throws InvalidOVFException
     */
    String getVmIdFromAC(String acProductSection) throws InvalidOVFException;

    /**
     * Obtain the picId from an PICProductSection (for VirtualService)
     * 
     * @param acProductSection
     * @return
     * @throws InvalidOVFException
     */
    String getPicId(String picProductSection) throws InvalidOVFException;

    /**
     * Obtain the acId from an ACProductSection
     * 
     * @param acProductSection
     * @return
     * @throws InvalidOVFException
     */
    String getAcId(String acProductSection) throws InvalidOVFException;

    /**
     * Obtain envelopeSegment from a file
     * 
     * @param serviceFile
     * @param appId
     * @return
     * @throws InvalidOVFException
     */
    String getEnvelopeTypeSegment(String serviceFile, String appId) throws InvalidOVFException;

    /**
     * Obtain the picId from an ACProductSection
     * 
     * @param acProductSection
     * @return
     * @throws InvalidOVFException
     */
    String getPicIdFromAC(String acProductSection) throws InvalidOVFException;

    public String getFqnId(String vapp) throws InvalidVappException;

}
