/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nowshad
 */
public class AdminInfo {

    private MysqlConnection databaseConnection;
    public Connection connection;
    JsonObject jsonObject;
    JsonArray jsonArray;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;

    public AdminInfo() {

        super();

        databaseConnection = new MysqlConnection();
        connection = databaseConnection.getDbConnection();
    }

    public JsonObject getLoginStatus(String userEmail, String userPass) {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        sql = "select * from user_table where user_email=? and user_pass=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, userPass);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                jsonObject.addProperty("loginStatus", "success");
//                jsonObject.addProperty("userID", resultSet.getInt("user_id"));
//                jsonObject.addProperty("userRole", resultSet.getString("user_role"));
            } else {
                jsonObject.addProperty("loginStatus", "failed");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

}
