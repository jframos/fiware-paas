package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.util.Properties;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderFactoryImpl;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Class in charge to persist the system properties in data base on bootstrap time.
 */
public class PropertiesLoaderBootstrapTest implements ServletContextListener {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private static final String NAMESPACE = "/SystemConfigurationTest.properties";

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        EntityManagerFactory emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");

        PropertiesProvider propertiesProvider = new PropertiesProviderFactoryImpl().createPropertiesProvider(emf);
        Properties properties = propertiesProvider.load(NAMESPACE);
        try {
            LOGGER.info("store namespace: " + NAMESPACE);
            propertiesProvider.store(properties, NAMESPACE);
        } catch (Exception e) {
            throw new PaasManagerServerRuntimeException(e);
        }
    }

}
