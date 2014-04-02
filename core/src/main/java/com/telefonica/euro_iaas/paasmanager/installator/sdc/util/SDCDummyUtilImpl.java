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

package com.telefonica.euro_iaas.paasmanager.installator.sdc.util;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.sdc.model.Task;

public class SDCDummyUtilImpl implements SDCUtil {

<<<<<<< HEAD
    public void checkTaskStatus(ClaudiaData data, Task task, String vdc) throws ProductInstallatorException {
=======
    public void checkTaskStatus(Task task, String token, String vdc) throws ProductInstallatorException {
>>>>>>> 6b6090e4bc049aedcdc17e08d97dc30e5da4729a

    }

    public String getSdcUtil(String token) throws OpenStackException {
        // TODO Auto-generated method stub
        return null;
    }


}
