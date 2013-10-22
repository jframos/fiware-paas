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
