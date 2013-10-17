/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.keystone.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.keystone.UserDao;
import com.telefonica.euro_iaas.paasmanager.model.keystone.User;

/**
 * @author jesus.movilla
 */
public class UserDaoKeystoneImpl implements UserDao {

    private static final String FINDALL_USERS = "SELECT * FROM keystone.user";

    // @PersistenceContext(unitName = "keystone")
    // private EntityManager entityManagerFactoryKeystone;
    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.PaasManagerUserDao#findAll()
     */
    public List<User> findAll(Connection connection) {

        Statement statement = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<User>();
        if (connection != null) {
            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(FINDALL_USERS);
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setExtra(rs.getString("extra"));

                    users.add(user);
                }

                rs.close();
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                    statement.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
        return users;
    }

}
