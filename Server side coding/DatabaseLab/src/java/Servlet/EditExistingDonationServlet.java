/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.EditInfo;
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
This servlet edit the existing donation with new palce and new date.
 */
public class EditExistingDonationServlet extends HttpServlet {

    private PrintWriter printWriter;
    private String donationDate, donationPlace;
    private int donorID, donationID;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        printWriter = response.getWriter();

        donationID = Integer.parseInt(request.getParameter("DonationID"));
        donationDate = request.getParameter("DonationDate");
        donationPlace = request.getParameter("DonationPlace");

        EditInfo editInfo = new EditInfo();
        printWriter.println(editInfo.editExistingDonationUpdate(donationID, donationDate, donationPlace));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
