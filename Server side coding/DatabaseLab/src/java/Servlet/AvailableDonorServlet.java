/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.AllInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nowshad Hasan
 */
public class AvailableDonorServlet extends HttpServlet {
    
    private PrintWriter printWriter;
    private String bloodGroup;
    private int length;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        printWriter = response.getWriter();
        AllInfo allInfo=new AllInfo();

        
        bloodGroup=request.getParameter("BloodGroup");
        length=bloodGroup.length();

        if(bloodGroup.charAt(length-1)!='-')
        {
         
            bloodGroup=bloodGroup.replace(" ","+");
        }
        System.out.println(bloodGroup);
        try{
            printWriter.println(allInfo.getAvailableDonorInfo(bloodGroup));
        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }



}
