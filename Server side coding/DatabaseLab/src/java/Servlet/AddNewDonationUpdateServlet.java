/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.AddInfo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nowshad Hasan
 */

/*

This servlet is for updating a donation of a donor with his ID and donation date, place.
DonationDate can not be null because it is set from android app but ir user doesn't say the place then, that is set blank here.
That means date and place can't be null because it is handled carefully.

demo link -http://localhost:8080/DatabaseLab/AddNewDonationUpdateServlet?DonorID=7&&DonationDate=16-7-2016&&DonationPlace=Osmani

*/

public class AddNewDonationUpdateServlet extends HttpServlet {

private PrintWriter printWriter;
private String donationDate,donationPlace;
private int donorID;



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
   
        
        printWriter = response.getWriter();
        
        donorID=Integer.parseInt(request.getParameter("DonorID"));
        donationDate=request.getParameter("DonationDate");
        donationPlace=request.getParameter("DonationPlace");
        if(donationPlace==null)
            donationPlace="";
        AddInfo addInfo=new AddInfo();
        printWriter.println(addInfo.addNewDonationUpdate(donorID,donationDate, donationPlace));
       
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }



}
