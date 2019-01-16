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
import io.grpc.netty.shaded.io.netty.util.internal.InternalThreadLocalMap;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Gets notified of the app lifecycle changes (startup/shutdown)
 *
 * @author jsie
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(
            MyServletContextListener.class.getName());

    private InputStream serviceAccount;
    private FirebaseApp fa;
    private Firestore firestore;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.INFO,
                "====>>>> [{1}] is starting with contextpath [{2}] on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName(),
                    sce.getServletContext().getContextPath()});

        try {
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            this.serviceAccount = classloader.getResourceAsStream(
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

            this.fa = FirebaseApp.initializeApp(options);
            this.firestore = FirestoreClient.
                    getFirestore();
            LOG.info("FirebaseApp initialized");
        } catch (Exception ex) {
            LOG.
                    log(Level.SEVERE,
                            " Error during Servlet Context Initialization", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "====>>>> [{1}] is stopping on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName()
                });
        try {
            firestore.close();
//            fa.delete();
            serviceAccount.close();
            InternalThreadLocalMap.destroy();
            LOG.info("FirebaseApp and serviceAccount inputstream closed");
        } catch (Exception ex) {
            LOG.
                    log(Level.SEVERE, "Error during Servlet Context Destruction",
                            ex);
        }
    }

}
