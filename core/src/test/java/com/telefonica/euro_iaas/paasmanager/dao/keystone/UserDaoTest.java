/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
