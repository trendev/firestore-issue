/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.firestore;

import io.grpc.netty.shaded.io.netty.util.internal.InternalThreadLocalMap;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;
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
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.INFO,
                "====>>>> [{1}] is starting with contextpath [{2}] on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName(),
                    sce.getServletContext().getContextPath()});
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "====>>>> [{1}] is stopping on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName()
                });
        LockSupport.parkNanos(2_000_000_000);
        InternalThreadLocalMap.destroy();
    }
    
}
