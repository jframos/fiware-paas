/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RegionCacheTest {

    @Test
    public void shouldPutAnUrlInCache() {
        // given
        RegionCache regionCache = new RegionCache();

        // when
        regionCache.putUrl("region", "service", "http://localhost:8080");

        // then
        String url = regionCache.getUrl("region", "service");
        assertNotNull(url);
        assertEquals("http://localhost:8080", url);
    }

    @Test
    public void shouldReturnNullWithNotExistRegion() {
        // given
        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("region", "servicee", "http://localhost:8080");

        // when
        String url = regionCache.getUrl("region2", "service2");

        // then
        assertNull(url);
    }

}
