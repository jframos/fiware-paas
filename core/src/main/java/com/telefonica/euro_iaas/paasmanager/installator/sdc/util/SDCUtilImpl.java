/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.sdc.util;

import static com.telefonica.euro_iaas.paasmanager.util.Configuration.SDC_SERVER_MEDIATYPE;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackRegion;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.model.Task;

/**
 * @author jesus.movilla
 */
public class SDCUtilImpl implements SDCUtil {

    private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private Client client;
    private OpenStackRegion openStackRegion;

    private static Logger log = Logger.getLogger(SDCUtilImpl.class);
    private static String VM_PATH = "/rest/vm";
    private int MAX_TIME = 60000;

    public void checkTaskStatus(ClaudiaData claudiaData, Task task, String vdc) throws ProductInstallatorException {

        String msgerror = null;
        String sdcServerUrl;
        try {
            sdcServerUrl = getSdcUtil (claudiaData.getUser().getToken());
        } catch (OpenStackException e1) {
            msgerror = "Error to obtain the SDC endpoint or the default region: " + e1.getMessage();
            log.error(msgerror);
            throw new ProductInstallatorException(msgerror);
        }

        com.telefonica.euro_iaas.sdc.client.services.TaskService taskService = sDCClient.getTaskService(sdcServerUrl,
                SDC_SERVER_MEDIATYPE);

        while (true) {

            task = taskService.load(task.getHref());

            if (task.getStatus().equals(com.telefonica.euro_iaas.sdc.model.Task.TaskStates.ERROR)) {
                msgerror = "SDCException. " + task.getError().getMajorErrorCode() + ": "
                        + task.getError().getMajorErrorCode() + ". " + task.getError().getMessage();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);
            } else if ((task.getStatus().equals(com.telefonica.euro_iaas.sdc.model.Task.TaskStates.RUNNING))
                    || (task.getStatus().equals(com.telefonica.euro_iaas.sdc.model.Task.TaskStates.PENDING))
                    || (task.getStatus().equals(com.telefonica.euro_iaas.sdc.model.Task.TaskStates.QUEUED))) {
                msgerror = "Task status is : " + task.getStatus();
                log.info(msgerror);
            } else if (task.getStatus().equals(com.telefonica.euro_iaas.sdc.model.Task.TaskStates.SUCCESS)) {
                break;
            } else {
                msgerror = "Unexpected task Status SDCException. Description: " + task.getStatus();
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                msgerror = "SDCException. ThreadInterruptedException when polling SDCIntallation Task";
                log.error(msgerror);
                throw new ProductInstallatorException(msgerror);
            }
        }
    }
    
    public String getSdcUtil (String token) throws OpenStackException {
        String regionName = openStackRegion.getDefaultRegion(token);
        return openStackRegion.getSdcEndPoint(regionName, token);
    }

    /**
     * @param sDCClient
     *            the sDCClient to set
     */
    public void setSDCClient(SDCClient sDCClient) {
        this.sDCClient = sDCClient;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void setOpenStackRegion (OpenStackRegion openStackRegion) {
        this.openStackRegion=openStackRegion;
    }

}
