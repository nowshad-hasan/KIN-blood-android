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

This servlet add new donor with his all information.

http://localhost:8080/DatabaseLab/AddNewDonorServlet?DonorName=Apu&&DonorBloodGroup=B+&&DonorDept=CSE&&DonorSession=2012-13&&DonorPhone1=165413&&DonorPhone2=35345&&DonorAddress=Surma



*/

public class AddNewDonorServlet extends HttpServlet {
    
    
    

    


    private PrintWriter printWriter;
    private String donorName, donorBloodGroup, donorDept, donorSession, donorPhone1, donorPhone2, donorAddress;
    private int length;
    
    

    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        printWriter = response.getWriter();

        donorName = request.getParameter("DonorName");
        donorBloodGroup = request.getParameter("DonorBloodGroup");
        
        
                length=donorBloodGroup.length();

        if(donorBloodGroup.charAt(length-1)!='-')
        {
         
            donorBloodGroup=donorBloodGroup.replace(" ","+");
        }
        
        donorDept = request.getParameter("DonorDept");
        donorSession = request.getParameter("DonorSession");
        donorPhone1 = request.getParameter("DonorPhone1");
        donorPhone2 = request.getParameter("DonorPhone2");
        if(donorPhone2==null)
            donorPhone2="";
        donorAddress = request.getParameter("DonorAddress");
        if(donorAddress==null)
            donorAddress="";
        
       // System.out.println(donorName+" "+donorBloodGroup+" "+donorDept+" "+donorSession+" "+donorPhone1+" "+donorPhone2+" "+donorAddress);
        
        AddInfo addInfo=new AddInfo();
       printWriter.println(addInfo.addNewDonor(donorName,donorBloodGroup,donorDept,donorSession,donorPhone1,donorPhone2,donorAddress));
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
