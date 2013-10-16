/**
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
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
