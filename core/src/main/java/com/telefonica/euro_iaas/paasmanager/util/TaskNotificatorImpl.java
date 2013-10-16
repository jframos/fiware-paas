package com.telefonica.euro_iaas.paasmanager.util;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.model.Task;

/**
 * TaskNotificator rest implementation.
 * 
 * @author Jesus M. Movilla
 */
public class TaskNotificatorImpl implements TaskNotificator {

    private Client client;

    /**
     * {@inheritDoc}
     */
    public void notify(String url, Task task) {
        WebResource webResource = client.resource(url);
        webResource.type(MediaType.APPLICATION_XML).entity(task).post();
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
