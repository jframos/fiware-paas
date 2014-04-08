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

package com.telefonica.euro_iaas.paasmanager.dao.keystone;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.keystone.impl.UserDaoKeystoneImpl;
import com.telefonica.euro_iaas.paasmanager.model.keystone.User;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jesus.movilla
 */
public class UserDaoTest extends TestCase {

    private UserDaoKeystoneImpl userDao;
    private Connection connection;
    private ResultSet rs;
    private Statement statement;

    @Override
    @Before
    public void setUp() throws Exception {
        userDao = new UserDaoKeystoneImpl();
        List<User> usrs = new ArrayList<User>();
        User usr = new User("userId", "userNname", "extras");
        usrs.add(usr);

        rs = mock(ResultSet.class);
        rs.last();
        when(rs.getString("id")).thenReturn("userId");
        when(rs.getString("name")).thenReturn("userNname");
        when(rs.getString("extra")).thenReturn("extras");

        statement = mock(Statement.class);
        when(statement.executeQuery(any(String.class))).thenReturn(rs);

        connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(statement);

    }

    @Test
    public void testfindAll() {
        List<User> users = userDao.findAll(connection);
        assertNotNull(users);
        /*
         * assertEquals(1, users.size()); assertEquals("userId", users.get(0).getId()); assertEquals("userNname",
         * users.get(0).getName()); assertEquals("extras", users.get(0).getExtra());
         */
    }

}
