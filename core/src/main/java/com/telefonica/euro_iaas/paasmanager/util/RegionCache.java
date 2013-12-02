/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class RegionCache {

    public static final String CACHE_NAME = "regions";

    private Cache cache;

    public RegionCache() {

        CacheManager singletonManager = CacheManager.create();
        singletonManager.addCacheIfAbsent(CACHE_NAME);
        cache = singletonManager.getCache(CACHE_NAME);

    }

    public void putUrl(String region, String service, String url) {
        String key = getKey(region, service);
        cache.put(new Element(key, url));
    }

    public String getUrl(String region, String service) {

        String key = getKey(region, service);

        if (cache.isElementInMemory(key)) {

            return (String) cache.get(key).getObjectValue();
        } else {
            return null;
        }
    }

    private String getKey(String region, String service) {
        return region + "_" + service;
    }

    public void clear() {
        cache.removeAll();
    }
}
