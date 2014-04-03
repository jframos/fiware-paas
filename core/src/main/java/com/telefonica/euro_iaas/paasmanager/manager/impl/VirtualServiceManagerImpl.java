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

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.REC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.REC_SERVER_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.VirtualServiceInstallationException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.RECManagerClient;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECVirtualServiceService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.manager.VirtualServiceManager;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class VirtualServiceManagerImpl implements VirtualServiceManager {

    private VappUtils vappUtils;
    private OVFUtils ovfUtils;
    private SystemPropertiesProvider systemPropertiesProvider;

    private RECVirtualServiceService recVirtualServiceService;

    /** The log. */
    private static Logger log = Logger.getLogger(VirtualServiceManagerImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.VirtualServiceManager#create (java.lang.String,
     * java.lang.String)
     */
    public void create(String virtualServiceName, String payload) throws VirtualServiceInstallationException {
        String picId = null;
        String acId = null;
        String appId = null;
        String vmId = null;

        String baseUrl = systemPropertiesProvider.getProperty(REC_SERVER_URL);
        String mediaType = systemPropertiesProvider.getProperty(REC_SERVER_MEDIATYPE);
        List<String> acProductSections = new ArrayList<String>();

        try {
            acProductSections = vappUtils.getACProductSections(payload);

            if (acProductSections.size() == 0) {
                String msg = "No AC PoductSection type in the Virtual Service Payload";
                log.info(msg);
                throw new VirtualServiceInstallationException(msg);
            }
        } catch (ProductInstallatorException e) {
            String msg = "Error getting AC ProductSections from ovf. " + "Desc: " + e.getMessage();
            log.info(msg);
            throw new VirtualServiceInstallationException(msg);
        }

        RECManagerClient client = new RECManagerClient();
        recVirtualServiceService = client.getRECVirtualServiceService(baseUrl, mediaType);
        for (int i = 0; i < acProductSections.size(); i++) {

            try {
                appId = vappUtils.getAppIdFromAC(acProductSections.get(i));
                vmId = vappUtils.getVmIdFromAC(acProductSections.get(i));
                picId = vappUtils.getPicIdFromAC(acProductSections.get(i));
                acId = vappUtils.getAcId(acProductSections.get(i));
            } catch (InvalidOVFException e) {
                String msg = "Error getting picId from ProductSection of type AC. " + "Desc: " + e.getMessage();
                log.info(msg);
                throw new VirtualServiceInstallationException(msg);
            }
            recVirtualServiceService.createVirtualService(appId, vmId, picId, acId, acProductSections.get(i));
        }
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param vappUtils
     *            the vappUtils to set
     */
    public void setVappUtils(VappUtils vappUtils) {
        this.vappUtils = vappUtils;
    }

    /**
     * @param OVFUtils
     *            the OVFUtils to set
     */
    public void setOvfUtils(OVFUtils ovfUtils) {
        this.ovfUtils = ovfUtils;
    }

}
