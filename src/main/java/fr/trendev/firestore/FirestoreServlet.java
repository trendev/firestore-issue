/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@WebServlet(urlPatterns = {"/firestore"})
public class FirestoreServlet extends HttpServlet {

    private static final Logger LOG = Logger.
            getLogger(FirestoreServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Firestore Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Firestore connection status: " + this.connect()
                    + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
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
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
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

    private final String connect() {

        final String success = "SUCCESSFULLY_CONNECTED_AND_CLOSED";

        try {
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            InputStream serviceAccount = classloader.getResourceAsStream(
                    "service-key-account.json");

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setFirestoreOptions(FirestoreOptions.newBuilder()
                            .setCredentials(credentials)
                            .setTimestampsInSnapshotsEnabled(true)
                            .build())
                    .setCredentials(credentials)
                    .build();

            FirebaseApp fa = FirebaseApp.initializeApp(options);

            Firestore firestore = FirestoreClient.
                    getFirestore();

            fa.delete();
            firestore.close();
            serviceAccount.close();

            LOG.info("status : " + success);
            return success;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Impossible to connect to firestore "
                    + FirestoreServlet.class.getSimpleName(), ex);
        }
        return "ERROR";
    }

}
