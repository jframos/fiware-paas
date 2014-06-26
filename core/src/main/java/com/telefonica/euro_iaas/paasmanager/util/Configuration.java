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

public interface Configuration {
    
    String TASK_PATH = "/rest/vdc/{1}/task/{0}";
  
    String ENVIRONMENT_PATH = "/rest/catalog/environment/{0}";
    String ENVIRONMENT_INSTANCE_PATH = "/rest/vdc/{0}/environmentInstance/{1}";
    String PRODUCT_INSTANCE_PATH = "/rest/vdc/{4}/product/{0}";
    String PRODUCT_RELEASE_PATH = "/rest/product/{0}";
    String APPLICATION_RELEASE_PATH = "/rest/application/{0}";
    
    String SDC_SERVER_MEDIATYPE = "application/json";
    
    long OPENSTACK_SYNCHRONIZATION_POLLING_PERIOD = 84000000;
    String VALIDATION_TIME_THRESHOLD = "84000000";

}

