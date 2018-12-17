/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author pitpr
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 50)
public class Upload extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String prefix = getServletContext().getRealPath("/");
            Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
            String directoryName = request.getParameter("filecategory");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Internet Explorer getSubmittedFileName fix.
            new File(prefix + "../cloud/" + directoryName).mkdirs(); //creates the directory folder if it doesn't exist
            new File(prefix + "../../filesbackup/" + directoryName).mkdirs();
//            fileName = fileName.replaceAll("\\s", "");
            File tmpDir = new File(prefix + "../cloud/" + directoryName + fileName);
            boolean exists = tmpDir.exists();
            if (exists) {
                out.println("The file already exists");
            } else {
                filePart.write(prefix + "../cloud/" + directoryName + fileName);//save file to disk
                filePart.write(prefix + "../../filesbackup/" + directoryName + fileName);
                out.println(fileName);
                out.println("The file was saved successfully <br/>");
                out.println("You can find it at 'servername':'serverport'/cloud/"+directoryName+fileName);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
