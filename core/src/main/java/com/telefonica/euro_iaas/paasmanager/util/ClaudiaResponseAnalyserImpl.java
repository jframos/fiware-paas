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
