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

/**
 * 
 */
package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.RuleDao;
import com.telefonica.euro_iaas.paasmanager.dao.SecurityGroupDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.TokenDao;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.UserDao;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class SynchronizationBoostrap implements ServletContextListener {

    private static Logger log = Logger.getLogger(SynchronizationBoostrap.class);

    /** {@inheritDoc} */
    public void contextInitialized(ServletContextEvent event) {
        log.info("SynchronizationBoostrap. START");
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        SystemPropertiesProvider systemPropertiesProvider = (SystemPropertiesProvider) ctx
                .getBean("systemPropertiesProvider");

        Connection conn = null;
        TierDao tierDao = (TierDao) ctx.getBean("tierDao");
        TierInstanceDao tierInstanceDao = (TierInstanceDao) ctx.getBean("tierInstanceDao");
        FirewallingClient firewallingClient = (FirewallingClient) ctx.getBean("firewallingClient");
        ClaudiaClient claudiaClient = (ClaudiaClient) ctx.getBean("claudiaClient");
        EnvironmentInstanceDao environmentInstanceDao = (EnvironmentInstanceDao) ctx.getBean("environmentInstanceDao");
        RuleDao ruleDao = (RuleDao) ctx.getBean("ruleDao");
        SecurityGroupDao securityGroupDao = (SecurityGroupDao) ctx.getBean("securityGroupDao");
        UserDao userDao = (UserDao) ctx.getBean("userDao");
        TokenDao tokenDao = (TokenDao) ctx.getBean("tokenDao");

        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_DATABASE_URL);
        String driver = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_DATABASE_DRIVER);
        String userName = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_DATABASE_USERNAME);
        String password = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_DATABASE_PASSWORD);

        try {
            // Lineas comentadas por errores al arrancar el paasManager

            /*
             * Class.forName(driver).newInstance(); conn = DriverManager.getConnection(url, userName, password);
             * OpenStackSyncImpl openStackSync =new OpenStackSyncImpl(conn, false, tierDao, tierInstanceDao,
             * firewallingClient, claudiaClient, systemPropertiesProvider, environmentInstanceDao, ruleDao,
             * securityGroupDao, userDao, tokenDao); Thread myThread = new Thread(openStackSync); myThread.start();
             */

            // conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        log.info("SynchronizationBoostrap. FINISH");
    }

    /** {@inheritDoc} */
    public void contextDestroyed(ServletContextEvent event) {

    }
}
