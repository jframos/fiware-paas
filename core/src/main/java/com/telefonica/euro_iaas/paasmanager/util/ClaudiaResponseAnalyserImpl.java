/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

/**
 * @author jesus.movilla
 */

public class ClaudiaResponseAnalyserImpl implements ClaudiaResponseAnalyser {

    private SystemPropertiesProvider systemPropertiesProvider;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser#existVDC (java.lang.String)
     */
    /*
     * public boolean existVDC(String browseVDCResponse) { return !(browseVDCResponse.contains(systemPropertiesProvider
     * .getProperty(CLAUDIA_RESOURCE_NOTEXIST_PATTERN))); }
     */

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser#getTaskUrl (java.lang.String)
     */
    public String getTaskUrl(String xmlTask) {
        String urlSplit = xmlTask.split("href")[1];
        String url = urlSplit.split("\"")[1];

        return url;
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser# getTaskStatus(java.lang.String)
     */
    public String getTaskStatus(String xmlTask) {
        String statusSplit = xmlTask.split("status")[1];
        String status = statusSplit.split("\"")[1];
        return status;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
