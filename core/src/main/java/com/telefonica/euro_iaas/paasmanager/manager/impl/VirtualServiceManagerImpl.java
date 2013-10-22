/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
