/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.AllInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nowshad Hasan
 */

/*
This servlet gives all the donor information that means not their all details but which is needed for status
*/
public class AllDonorServlet extends HttpServlet {

private PrintWriter printWriter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                printWriter = response.getWriter();
        AllInfo allInfo=new AllInfo();
        try
        {
        printWriter.println(allInfo.getAllDonorInfo());
        }catch(ParseException ex)
        {
            ex.printStackTrace();
        }
        
       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
    }


}
