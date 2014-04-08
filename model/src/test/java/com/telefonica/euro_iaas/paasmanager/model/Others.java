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

package com.telefonica.euro_iaas.paasmanager.model;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * Test other model classes.
 * @author henar
 *
 */
public class Others {
    
    public static String ORG ="org";
    public static String VDC ="vdc";
    public static String SERVICE ="service";

    @Test
    public void testClaudiaData() throws Exception {
        
        PaasManagerUser user = new PaasManagerUser("username", "myToken", new HashSet<GrantedAuthority>());
        user.setTenantName("FIWARE");
        ClaudiaData claudiaData = new ClaudiaData(ORG, VDC, SERVICE);
        claudiaData.setUser(user);
        
        assertEquals(claudiaData.getOrg(), ORG);
        assertEquals(claudiaData.getVdc(), VDC);
        assertEquals(claudiaData.getService(), SERVICE);
        assertEquals(claudiaData.getUser().getTenantName(), "FIWARE");
    }
    
    @Test
    public void testTasks() throws Exception {
        
        Task task = new Task(TaskStates.RUNNING);
        task.setHref("href");
        task.setDescription("description");

        assertEquals(task.getStatus(), TaskStates.RUNNING);
        assertEquals(task.getHref(), "href");
        assertEquals(task.getDescription(), "description");

    }
    
    @Test
    public void testTaskError() throws Exception {
        
        
        TaskError taskError = new TaskError ();
        taskError.setMessage("error");
        
        assertEquals(taskError.getMessage(), "error");

    }
}
