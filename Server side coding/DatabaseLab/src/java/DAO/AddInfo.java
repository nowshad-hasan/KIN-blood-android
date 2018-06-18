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
 * @author Nowshad Hasan
 */
public class AddInfo {

    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public Connection connection;
    JsonObject jsonObject;
    JsonArray jsonArray;

    public AddInfo() {
        super();
        AllInfo allInfo = new AllInfo();
        connection = allInfo.getConnection();
    }
    
    //This method is used for adding new donor, where his ID is auto generated setting NULL and remaining things needed.

    public JsonObject addNewDonor(String donorName,String donorBloodGroup,String donorDept,String donorSession,String donorPhone1,
            String donorPhone2,String donorAddress)
    {
        jsonObject=new JsonObject();
        sql=" INSERT INTO `donor_table` (`donor_id`, `donor_name`, `donor_blood_group`, `donor_dept`, `donor_session`, \n" +
"   `donor_phone1`,`donor_phone2`, `donor_address`, `donor_availability`) VALUES (NULL, ? , ?, ?, ?, ?, ?,?, 'yes');";
        

        
        try
        {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, donorName);
            preparedStatement.setString(2, donorBloodGroup);
            preparedStatement.setString(3, donorDept);
            preparedStatement.setString(4, donorSession);
            preparedStatement.setString(5, donorPhone1);
            preparedStatement.setString(6, donorPhone2);
            preparedStatement.setString(7, donorAddress);
            int executeUpdate=preparedStatement.executeUpdate();
            if(executeUpdate>0)
                jsonObject.addProperty("addNewDonor", "successful");
            else
                jsonObject.addProperty("addNewDonor", "unsuccessful");
           // System.out.println("executeUpdate "+executeUpdate);
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return jsonObject;
    }
    
    // This method is used for adding new donation update 
    
    public JsonObject addNewDonationUpdate(int donorID,String donationDate,String donationPlace)
    {
        jsonObject=new JsonObject();
        sql="insert into donation_table (donation_id,donor_id,donation_date,donation_place)values(NULL,?,?,?)";
        
        
                try
        {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, donorID);
            preparedStatement.setString(2, donationDate);
            preparedStatement.setString(3, donationPlace);
            

            int executeUpdate=preparedStatement.executeUpdate();
            if(executeUpdate>0)
                jsonObject.addProperty("addNewDonationUpdate", "successful");
            else
                jsonObject.addProperty("addNewDonationUpdate", "unsuccessful");
           // System.out.println("executeUpdate "+executeUpdate);
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return jsonObject;
    }
    
}
