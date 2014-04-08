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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.TokenDao;
import com.telefonica.euro_iaas.paasmanager.model.keystone.Token;

/**
 * @author jesus.movilla
 */
public class TokenDaoKeystoneImpl implements TokenDao {

    private static final String FIND_LAST_TOKEN_FROM_USER = "SELECT * FROM keystone.token "
            + "{0} extra like ''%{1}%'' order by expires DESC;";

    /*
     * @PersistenceContext(unitName = "keystone") private EntityManager entityManagerFactoryKeystone;
     */

    /**
     * Find Last Valid Token from user
     * 
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    public Token findLastTokenFromUser(Connection connection, String name) throws EntityNotFoundException {
        String query = MessageFormat.format(FIND_LAST_TOKEN_FROM_USER, "where", name);
        List<Token> tokens = new ArrayList<Token>();

        Statement statement = null;
        ResultSet rs = null;

        if (connection != null) {
            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(query);
                while (rs.next()) {
                    Token token = new Token();
                    token.setId(rs.getString("id"));
                    token.setExpires(rs.getString("expires"));
                    token.setExtra(rs.getString("extra"));

                    JSONObject jsonNode = JSONObject.fromObject(token.getExtra());
                    JSONObject jsonUser = jsonNode.getJSONObject("user");
                    token.setTenantId(jsonUser);

                    tokens.add(token);
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

        if (tokens.size() == 0)
            throw new EntityNotFoundException(Token.class, "user", name);
        else
            return tokens.get(0);
    }

}
