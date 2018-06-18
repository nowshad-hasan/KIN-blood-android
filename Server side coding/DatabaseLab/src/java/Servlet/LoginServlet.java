/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.AdminInfo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
public class LoginServlet extends HttpServlet {
    
    
     private PrintWriter printWriter;
 private String userEmail;
 private String userPassword;
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                printWriter = response.getWriter();
        userEmail=request.getParameter("userEmail");
        userPassword=request.getParameter("userPass");
        AdminInfo adminInfo=new AdminInfo();
        printWriter.print(adminInfo.getLoginStatus(userEmail, userPassword));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        String adminEmail = request.getParameter("email");
//        String adminpassword = request.getParameter("password");
//        
//        AdminInfo adminInfo=new AdminInfo();

    }

}
