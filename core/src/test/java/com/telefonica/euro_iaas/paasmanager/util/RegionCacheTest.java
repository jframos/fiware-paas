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
        regionCache.putUrl("region", "service", "http://localhost:8080");

        // when
        String url = regionCache.getUrl("region2", "service2");

        // then
        assertNull(url);
    }

    @Test
    public void shouldReturnNullAfterClearCache() {
        // given

        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("region", "service", "http://localhost:8080");
        // when
        regionCache.clear();

        // then
        String url = regionCache.getUrl("region", "service");
        assertNull(url);

    }

    @Test
    public void shouldReturnValueWithNullServiceAndNullRegion() {
        // given

        RegionCache regionCache = new RegionCache();
        regionCache.putUrl(null, null, "value");
        // when
        String value = regionCache.getUrl(null, null);

        // then
        assertEquals("value", value);
    }

    @Test
    public void shouldReturnNullUrlAfterPutWithNullValue() {
        // given
        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("region", "service", null);
        // when
        String value = regionCache.getUrl("region", "service");

        // then
        assertNull(value);
    }

    @Test
    public void shouldReturnNullWhenElementIsExpired() throws InterruptedException {
        // given
        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("region", "service", "http://localhost");

        // regionCache.getConfiguration().setTimeToIdleSeconds(1);
        regionCache.getConfiguration().setTimeToLiveSeconds(1);

        Thread.sleep(3000);
        // when
        String nullValue = regionCache.getUrl("region", "service");

        // then

        assertNull(nullValue);
    }

}
