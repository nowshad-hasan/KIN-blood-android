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
public class EditInfo {
        private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public Connection connection;
    JsonObject jsonObject;
    JsonArray jsonArray;
    
    public EditInfo()
    {
                super();
        AllInfo allInfo = new AllInfo();
        connection = allInfo.getConnection();
    }
    
    
    // This function edit existing donor with their new location, phone, availability (that means he may not be avilable from now) etc.
    
    public JsonObject editExistingDonor(int donorID,String donorName,String donorBloodGroup,String donorDept,String donorSession,
            String donorPhone1,String donorPhone2,String donorAddress,String donorAvailability)
    {
        
        jsonObject=new JsonObject();
        
        sql="UPDATE donor_table SET donor_name =?, donor_blood_group =?, donor_dept = ?,\n" +
" donor_session = ?, donor_phone1 =?, donor_phone2 = ?,\n" +
" donor_address =?, donor_availability =? WHERE donor_id =?";
        
        
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
            preparedStatement.setString(8, donorAvailability);
            preparedStatement.setInt(9, donorID);
            int executeUpdate=preparedStatement.executeUpdate();
            if(executeUpdate>0)
                jsonObject.addProperty("editExistingDonor", "successful");
            else
                jsonObject.addProperty("editExistingDonor", "unsuccessful");
           // System.out.println("executeUpdate "+executeUpdate);
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return jsonObject;
        
    }
    
    // This function edit any existing donation with new donation place and donation date.
    
    public JsonObject editExistingDonationUpdate(int donationID,String donationDate,String donationPlace)
    {
        jsonObject=new JsonObject();
        sql="UPDATE donation_table SET donation_date =?, donation_place = ? WHERE donation_id = ?";
        
                
                try
        {
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, donationDate);
            preparedStatement.setString(2, donationPlace);
            preparedStatement.setInt(3, donationID);

            int executeUpdate=preparedStatement.executeUpdate();
            if(executeUpdate>0)
                jsonObject.addProperty("editExistingDonationUpdate", "successful");
            else
                jsonObject.addProperty("editExistingDonationUpdate", "unsuccessful");
           // System.out.println("executeUpdate "+executeUpdate);
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return jsonObject;
        
    }
}
