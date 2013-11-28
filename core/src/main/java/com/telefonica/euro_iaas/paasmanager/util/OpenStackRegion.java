/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;

/**
 * Utilities for manage OpenStack regions.
 */
public interface OpenStackRegion {

    /**
     * How to get an endpoint by region.
     * 
     * @param name
     *            e.g. nova, quantum, glance, etc...
     * @param regionName
     * @param token
     * @return the http url with de endpoint.
     * @throws OpenStackException
     */
    String getEndPointByNameAndRegionName(String name, String regionName, String token) throws OpenStackException;

    /**
     * Get endpoint for nova services.
     * 
     * @param regionName
     * @param token
     * @return
     * @throws OpenStackException
     */
    String getNovaEndPoint(String regionName, String token) throws OpenStackException;

    /**
     * Get the endpoint for networks services.
     * 
     * @param regionName
     * @param token
     * @return
     * @throws OpenStackException
     */
    String getQuantumEndPoint(String regionName, String token) throws OpenStackException;

    /**
     * Get a list with the name of all regions.
     * 
     * @param token
     * @return
     */
    List<String> getRegionNames(String token) throws OpenStackException;
}
