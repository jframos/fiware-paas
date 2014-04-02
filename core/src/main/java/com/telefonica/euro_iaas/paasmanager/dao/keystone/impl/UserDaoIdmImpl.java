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
public class UserDaoIdmImpl implements UserDao {

    private static final String FINDALL_USERS = "SELECT * FROM `fi-ware-idm_production`.actors where subject_type='User'";

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
                    // cambiar
                    String userid = toOpenstack(rs.getString("id"));
                    user.setId(userid);
                    String name = rs.getString("slug");
                    user.setName(name);
                    // user.setName(userid);
                    // user.setExtra("");

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

    private String toOpenstack(String userId) {
        int lenUserId = userId.length();
        for (int i = lenUserId; i < 32; i++) {
            userId = "0" + userId;
        }
        return userId;

        /*
         * int len = "00000000000000000000000000000001".length(); user = "0000000000000000000000000000" + user; while
         * (user.length()!=len) { user = "0"+ user; } return user;
         */
    }

}
