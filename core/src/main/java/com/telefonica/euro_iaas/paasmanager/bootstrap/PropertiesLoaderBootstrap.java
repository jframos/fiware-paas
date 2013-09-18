package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderFactoryImpl;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;

/**
 * Class in charge to persist the system properties in data base on bootstrap
 * time.
 *
 * @author Jesus M. Movilla
 * 
 *  */
public class PropertiesLoaderBootstrap implements ServletContextListener {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private static final String NAMESPACE = "/SystemConfiguration.properties";
    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());
        EntityManagerFactory emf =
            (EntityManagerFactory) ctx.getBean("entityManagerFactory");

        PropertiesProvider propertiesProvider =
            new PropertiesProviderFactoryImpl().createPropertiesProvider(emf);
        Properties properties = propertiesProvider.load(NAMESPACE);
        try {
            LOGGER.info("store namespace: " + NAMESPACE);
            propertiesProvider.store(properties, NAMESPACE);
        } catch (Exception e) {
            throw new PaasManagerServerRuntimeException(e);
        }
    }

}

