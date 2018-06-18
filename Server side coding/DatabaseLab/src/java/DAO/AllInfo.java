/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Nowshad Hasan
 */
public class AllInfo {

    private MysqlConnection databaseConnection;
    public Connection connection;
    private String sql, sql2;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    JsonObject jsonObject;
    JsonArray jsonArray;

    public AllInfo() {
        super();

        databaseConnection = new MysqlConnection();
        connection = databaseConnection.getDbConnection();

    }

    public Connection getConnection() {
        return connection;
    }

    // This function gives us all the donor info including their time from last blood donation.
    public JsonObject getAllDonorInfo() throws ParseException {
        sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                + "donation_date from donation_table,donor_table where donor_table.donor_id="
                + "donation_table.donor_id and donation_id in "
                + "(select max(donation_id) from donation_table group by donation_table.donor_id)";
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        try {

            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);
            int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

            int dayDiff, yearDiff, monthDiff;
            String donationTimeDiff;

            while (resultSet.next()) {

                Date donationDate = dateFormat.parse(resultSet.getString("donation_date"));
                Calendar donationCal = Calendar.getInstance();
                donationCal.setTime(donationDate);
                int donationYear = donationCal.get(Calendar.YEAR);
                int donationMonth = donationCal.get(Calendar.MONTH);
                int donationDay = donationCal.get(Calendar.DAY_OF_MONTH);

                if (currentDay < donationDay) {
                    dayDiff = currentDay + 30 - donationDay;
                    donationMonth++;
                } else {
                    dayDiff = currentDay - donationDay;
                }

                if (currentMonth < donationMonth) {
                    monthDiff = currentMonth + 12 - donationMonth;
                    donationYear++;
                } else {
                    monthDiff = currentMonth - donationMonth;
                }
                yearDiff = currentYear - donationYear;

                if (yearDiff > 0) {
                    donationTimeDiff = Integer.toString(yearDiff) + " year " + Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                } else {
                    donationTimeDiff = Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                }
                //   System.out.println(donationTimeDiff);

                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorPhone1 = resultSet.getString("donor_phone1");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorPhone1", donorPhone1);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationTimeDiff", donationTimeDiff);
                jsonArray.add(jObject);
            }

            jsonObject.add("allDonor", jsonArray);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Another Section
        sql = "SELECT * FROM donor_table  LEFT JOIN donation_table on donor_table.donor_id=donation_table.donor_id WHERE donation_id is null";

        try {

            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorPhone1 = resultSet.getString("donor_phone1");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorPhone1", donorPhone1);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationTimeDiff", "0 months");
                jsonArray.add(jObject);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;
    }

    //This function gives us all the available donor info which exceeds 4 months.
    public JsonObject getAvailableDonorInfo(String bloodGroup) throws ParseException {
        if (bloodGroup.equals("ALL")) {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='yes' and donation_id in "
                    + "(select max(donation_id) from donation_table group by donation_table.donor_id)";
        } else {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='yes' and donation_id in"
                    + " (select max(donation_id) from donation_table group by donation_table.donor_id) and donor_blood_group=?";
        }
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);
            int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

            int dayDiff, yearDiff, monthDiff;
            String donationTimeDiff;

            while (resultSet.next()) {

                Date donationDate = dateFormat.parse(resultSet.getString("donation_date"));
                Calendar donationCal = Calendar.getInstance();
                donationCal.setTime(donationDate);
                int donationYear = donationCal.get(Calendar.YEAR);
                int donationMonth = donationCal.get(Calendar.MONTH);
                int donationDay = donationCal.get(Calendar.DAY_OF_MONTH);

                if (currentDay < donationDay) {
                    dayDiff = currentDay + 30 - donationDay;
                    donationMonth++;
                } else {
                    dayDiff = currentDay - donationDay;
                }

                if (currentMonth < donationMonth) {
                    monthDiff = currentMonth + 12 - donationMonth;
                    donationYear++;
                } else {
                    monthDiff = currentMonth - donationMonth;
                }
                yearDiff = currentYear - donationYear;

                if (monthDiff >= 4 || yearDiff >= 1) {
                    if (yearDiff > 0) {
                        donationTimeDiff = Integer.toString(yearDiff) + " year " + Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                    } else {
                        donationTimeDiff = Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                    }
                    //   System.out.println(donationTimeDiff);

                    int donorId = resultSet.getInt("donor_id");
                    String donorName = resultSet.getString("donor_name");
                    String donorPhone1 = resultSet.getString("donor_phone1");
                    String donorBloodGroup = resultSet.getString("donor_blood_group");
                    String donorDept = resultSet.getString("donor_dept");
                    String donorSession = resultSet.getString("donor_session");

                    JsonObject jObject = new JsonObject();

                    jObject.addProperty("donorId", donorId);
                    jObject.addProperty("donorName", donorName);
                    jObject.addProperty("donorPhone1", donorPhone1);
                    jObject.addProperty("donorBloodGroup", donorBloodGroup);
                    jObject.addProperty("donorDept", donorDept);
                    jObject.addProperty("donorSession", donorSession);
                    jObject.addProperty("donationTimeDiff", donationTimeDiff);
                    jsonArray.add(jObject);
                }

            }

//            jsonObject.add("availableDonor", jsonArray);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Another Part
        if (bloodGroup.equals("ALL")) {

            sql = "SELECT * FROM donor_table LEFT JOIN donation_table on"
                    + " donor_table.donor_id=donation_table.donor_id WHERE donation_id is null and donor_availability='yes'";
        } else {

            sql = "SELECT * FROM donor_table LEFT JOIN donation_table on donor_table.donor_id="
                    + "donation_table.donor_id WHERE donation_id is null and donor_availability='yes' and donor_blood_group=?";
        }

        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorPhone1 = resultSet.getString("donor_phone1");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorPhone1", donorPhone1);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationTimeDiff", "0 months ");
                jsonArray.add(jObject);
            }

            jsonObject.add("availableDonor", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    // This function gives us the donor information which doesn't exceed 4 months.
    public JsonObject getUnavailableDonorInfo(String bloodGroup) throws ParseException {
        if (bloodGroup.equals("ALL")) {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='yes' and donation_id in "
                    + "(select max(donation_id) from donation_table group by donation_table.donor_id)";
        } else {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='yes' and donation_id in"
                    + " (select max(donation_id) from donation_table group by donation_table.donor_id) and donor_blood_group=?";
        }
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);
            int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

            int dayDiff, yearDiff, monthDiff;
            String donationTimeDiff;

            while (resultSet.next()) {

                Date donationDate = dateFormat.parse(resultSet.getString("donation_date"));
                Calendar donationCal = Calendar.getInstance();
                donationCal.setTime(donationDate);
                int donationYear = donationCal.get(Calendar.YEAR);
                int donationMonth = donationCal.get(Calendar.MONTH);
                int donationDay = donationCal.get(Calendar.DAY_OF_MONTH);

                if (currentDay < donationDay) {
                    dayDiff = currentDay + 30 - donationDay;
                    donationMonth++;
                } else {
                    dayDiff = currentDay - donationDay;
                }

                if (currentMonth < donationMonth) {
                    monthDiff = currentMonth + 12 - donationMonth;
                    donationYear++;
                } else {
                    monthDiff = currentMonth - donationMonth;
                }
                yearDiff = currentYear - donationYear;

                if (yearDiff == 0 && monthDiff < 4) {
                    if (yearDiff > 0) {
                        donationTimeDiff = Integer.toString(yearDiff) + " year " + Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                    } else {
                        donationTimeDiff = Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                    }
                    //   System.out.println(donationTimeDiff);

                    int donorId = resultSet.getInt("donor_id");
                    String donorName = resultSet.getString("donor_name");
                    String donorPhone1 = resultSet.getString("donor_phone1");
                    String donorBloodGroup = resultSet.getString("donor_blood_group");
                    String donorDept = resultSet.getString("donor_dept");
                    String donorSession = resultSet.getString("donor_session");

                    JsonObject jObject = new JsonObject();

                    jObject.addProperty("donorId", donorId);
                    jObject.addProperty("donorName", donorName);
                    jObject.addProperty("donorPhone1", donorPhone1);
                    jObject.addProperty("donorBloodGroup", donorBloodGroup);
                    jObject.addProperty("donorDept", donorDept);
                    jObject.addProperty("donorSession", donorSession);
                    jObject.addProperty("donationTimeDiff", donationTimeDiff);
                    jsonArray.add(jObject);
                }

            }

            jsonObject.add("unavailableDonor", jsonArray);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

    // This function gives us the donor information which gives us the donor information which availability is no.
    public JsonObject getExDonorInfo(String bloodGroup) throws ParseException {
        if (bloodGroup.equals("ALL")) {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='no' and donation_id in "
                    + "(select max(donation_id) from donation_table group by donation_table.donor_id)";
        } else {

            sql = "select donor_table.donor_id,donor_name,donor_blood_group,donor_dept,donor_session,donor_phone1,"
                    + "donation_date from donation_table,donor_table where donor_table.donor_id="
                    + "donation_table.donor_id and donor_availability='no' and donation_id in"
                    + " (select max(donation_id) from donation_table group by donation_table.donor_id) and donor_blood_group=?";
        }
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);
            int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

            int dayDiff, yearDiff, monthDiff;
            String donationTimeDiff;

            while (resultSet.next()) {

                Date donationDate = dateFormat.parse(resultSet.getString("donation_date"));
                Calendar donationCal = Calendar.getInstance();
                donationCal.setTime(donationDate);
                int donationYear = donationCal.get(Calendar.YEAR);
                int donationMonth = donationCal.get(Calendar.MONTH);
                int donationDay = donationCal.get(Calendar.DAY_OF_MONTH);

                if (currentDay < donationDay) {
                    dayDiff = currentDay + 30 - donationDay;
                    donationMonth++;
                } else {
                    dayDiff = currentDay - donationDay;
                }

                if (currentMonth < donationMonth) {
                    monthDiff = currentMonth + 12 - donationMonth;
                    donationYear++;
                } else {
                    monthDiff = currentMonth - donationMonth;
                }
                yearDiff = currentYear - donationYear;

                if (yearDiff > 0) {
                    donationTimeDiff = Integer.toString(yearDiff) + " year " + Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                } else {
                    donationTimeDiff = Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
                }
                //   System.out.println(donationTimeDiff);

                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorPhone1 = resultSet.getString("donor_phone1");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorPhone1", donorPhone1);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationTimeDiff", donationTimeDiff);
                jsonArray.add(jObject);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (bloodGroup.equals("ALL")) {

            sql = "SELECT * FROM donor_table LEFT JOIN donation_table on"
                    + " donor_table.donor_id=donation_table.donor_id WHERE donation_id is null and donor_availability='no'";
        } else {

            sql = "SELECT * FROM donor_table LEFT JOIN donation_table on donor_table.donor_id="
                    + "donation_table.donor_id WHERE donation_id is null and donor_availability='no' and donor_blood_group=?";
        }

        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorPhone1 = resultSet.getString("donor_phone1");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorPhone1", donorPhone1);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationTimeDiff", "0 months ");
                jsonArray.add(jObject);
            }

            jsonObject.add("exDonor", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    // This function gives us the donation history of any blood type or all blood type.
    public JsonObject getDonationHistoryInfo(String bloodGroup) {
        if (bloodGroup.equals("ALL")) {

            sql = "SELECT donation_id,donation_table.donor_id,donation_date,donation_place,donor_name,donor_blood_group,donor_session,donor_dept"
                    + " FROM donation_table,donor_table WHERE donation_table.donor_id=donor_table.donor_id  \n"
                    + "ORDER BY `donation_table`.`donation_id` desc";

        } else {

            sql = "SELECT donation_id,donation_table.donor_id,donation_date,donation_place,donor_name,donor_blood_group,donor_session,"
                    + "donor_dept FROM donation_table,donor_table WHERE donation_table.donor_id=donor_table.donor_id and donor_blood_group=?  \n"
                    + "ORDER BY `donation_table`.`donation_id` desc";
        }
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {

            preparedStatement = connection.prepareStatement(sql);
            if (!bloodGroup.equals("ALL")) {
                preparedStatement.setString(1, bloodGroup);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int donorId = resultSet.getInt("donor_id");
                String donorName = resultSet.getString("donor_name");
                String donorBloodGroup = resultSet.getString("donor_blood_group");
                String donorDept = resultSet.getString("donor_dept");
                String donorSession = resultSet.getString("donor_session");
                int donationID = resultSet.getInt("donation_id");
                String donationDate = resultSet.getString("donation_date");
                String donationPlace = resultSet.getString("donation_place");

                JsonObject jObject = new JsonObject();

                jObject.addProperty("donorId", donorId);
                jObject.addProperty("donorName", donorName);
                jObject.addProperty("donorBloodGroup", donorBloodGroup);
                jObject.addProperty("donorDept", donorDept);
                jObject.addProperty("donorSession", donorSession);
                jObject.addProperty("donationID", donationID);
                jObject.addProperty("donationDate", donationDate);
                jObject.addProperty("donationPlace", donationPlace);

                jsonArray.add(jObject);

            }

            jsonObject.add("donationHistory", jsonArray);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

    // This function gives us the personal details like- how many times he donated blood, location, phone number everything.
    public JsonObject getDonorPersonalInfo(int donorID) throws ParseException {

        sql = "select donation_table.donor_id,donation_date,donor_name,donor_session,donor_blood_group,donor_dept,"
                + "donor_phone1,donor_phone2,donor_availability,donor_address from donation_table,donor_table where donation_table.donor_id=donor_table.donor_id"
                + " and donation_id=(select max(donation_id) from donation_table where donor_id=?)";

        sql2 = "select count(donation_id) from donation_table where donor_id=?";
        jsonObject = new JsonObject();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int donorBloodCount = 0;
        String donorName = null, donorBloodGroup = null, donorDept = null, donorSession = null, donorPhone1 = null, donorPhone2 = null, donorAddress = null, donorAvailability = null;
        Date donationDate = new Date();
        try {

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, donorID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                donorName = resultSet.getString("donor_name");
                donorBloodGroup = resultSet.getString("donor_blood_group");
                donorDept = resultSet.getString("donor_dept");
                donorSession = resultSet.getString("donor_session");
                donorPhone1 = resultSet.getString("donor_phone1");
                donorPhone2 = resultSet.getString("donor_phone2");
                donorAddress = resultSet.getString("donor_address");
                donorAvailability = resultSet.getString("donor_availability");
                donationDate = dateFormat.parse(resultSet.getString("donation_date"));
            }
            preparedStatement = connection.prepareStatement(sql2);

            preparedStatement.setInt(1, donorID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                donorBloodCount = resultSet.getInt("count(donation_id)");
            }
            // String donationDate = resultSet.getString("donation_date");

            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);
            int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

            Calendar donationCal = Calendar.getInstance();
            donationCal.setTime(donationDate);
            int donationYear = donationCal.get(Calendar.YEAR);
            int donationMonth = donationCal.get(Calendar.MONTH);
            int donationDay = donationCal.get(Calendar.DAY_OF_MONTH);

            int dayDiff, yearDiff, monthDiff;
            String donationTimeDiff;

            if (currentDay < donationDay) {
                dayDiff = currentDay + 30 - donationDay;
                donationMonth++;
            } else {
                dayDiff = currentDay - donationDay;
            }

            if (currentMonth < donationMonth) {
                monthDiff = currentMonth + 12 - donationMonth;
                donationYear++;
            } else {
                monthDiff = currentMonth - donationMonth;
            }
            yearDiff = currentYear - donationYear;

            if (yearDiff > 0) {
                donationTimeDiff = Integer.toString(yearDiff) + " year " + Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
            } else {
                donationTimeDiff = Integer.toString(monthDiff) + " month " + Integer.toString(dayDiff) + " days";
            }

            jsonObject.addProperty("donorId", donorID);
            jsonObject.addProperty("donorName", donorName);
            jsonObject.addProperty("donorBloodGroup", donorBloodGroup);
            jsonObject.addProperty("donorDept", donorDept);
            jsonObject.addProperty("donorSession", donorSession);
            jsonObject.addProperty("donorPhone1", donorPhone1);
            jsonObject.addProperty("donorPhone2", donorPhone2);
            jsonObject.addProperty("donorAddress", donorAddress);
            jsonObject.addProperty("donorAvailability", donorAvailability);
            jsonObject.addProperty("donorBloodCount", donorBloodCount);
            jsonObject.addProperty("donationTimeDiff", donationTimeDiff);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        sql = "SELECT * FROM `donor_table` WHERE donor_id=?";

        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, donorID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                donorName = resultSet.getString("donor_name");
                donorBloodGroup = resultSet.getString("donor_blood_group");
                donorDept = resultSet.getString("donor_dept");
                donorSession = resultSet.getString("donor_session");
                donorPhone1 = resultSet.getString("donor_phone1");
                donorPhone2 = resultSet.getString("donor_phone2");
                donorAddress = resultSet.getString("donor_address");
                donorAvailability = resultSet.getString("donor_availability");
//                donationDate = dateFormat.parse(resultSet.getString("donation_date"));

                jsonObject.addProperty("donorId", donorID);
                jsonObject.addProperty("donorName", donorName);
                jsonObject.addProperty("donorBloodGroup", donorBloodGroup);
                jsonObject.addProperty("donorDept", donorDept);
                jsonObject.addProperty("donorSession", donorSession);
                jsonObject.addProperty("donorPhone1", donorPhone1);
                jsonObject.addProperty("donorPhone2", donorPhone2);
                jsonObject.addProperty("donorAddress", donorAddress);
                jsonObject.addProperty("donorAvailability", donorAvailability);
                jsonObject.addProperty("donorBloodCount", donorBloodCount);
                jsonObject.addProperty("donationTimeDiff", "0 months");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    //This function gives us donation history of any donor.
    public JsonObject getDonorDonationHistoryInfo(int donorID) {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        sql = "SELECT * FROM `donation_table` WHERE donor_id=?";

        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, donorID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                JsonObject jObject = new JsonObject();

                int donationID = resultSet.getInt("donation_id");
                String donationDate = resultSet.getString("donation_date");
                String donationPlace = resultSet.getString("donation_place");

                jObject.addProperty("donationID", donationID);
                jObject.addProperty("donationDate", donationDate);
                jObject.addProperty("donationPlace", donationPlace);

                jsonArray.add(jObject);
            }
            jsonObject.add("donorDonationHistory", jsonArray);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
}
