/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
