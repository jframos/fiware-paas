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

package com.telefonica.euro_iaas.paasmanager.installator.rec.client;

/**
 * @author jesus.movilla
 */
public class ClientConstants {

    // public static final String BASE_SERVICE_PATH = "/service";
    public static final String BASE_SERVICE_PATH = "/applications";
    public static final String BASE_VM_PATH = "/applications/{0}/vms";
    public static final String BASE_PIC_PATH = "/applications/{0}/vms/{1}/pics";
    public static final String BASE_AC_PATH = "/applications/{0}/vms/{1}/pics/{2}/acs";
    public static final String BASE_VIRTUALSERVICE_PATH = "/applications/{0}/vms/{1}/pics/{2}/acs";
}
