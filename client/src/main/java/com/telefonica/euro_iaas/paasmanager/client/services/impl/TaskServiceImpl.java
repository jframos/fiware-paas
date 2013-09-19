/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.client.services.impl;

import java.text.MessageFormat;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.client.services.TaskService;
import com.telefonica.euro_iaas.paasmanager.model.Task;


/**
 * @author jesus.movilla
 *
 */
public class TaskServiceImpl extends AbstractBaseService implements TaskService {

    private Long waitingPeriod = 5000l; // every 5*iter seconds try again
    private Long maxWaiting = 600000l; // max 10 minutes waiting

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public TaskServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    /**
     * @param client
     * @param baseHost
     * @param type
     * @param waitingPeriod
     * @param maxWaiting
     */
    public TaskServiceImpl(Client client, String baseHost, String type,
            Long waitingPeriod, Long maxWaiting) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
        this.waitingPeriod = waitingPeriod;
        this.maxWaiting = maxWaiting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String vdc, Long id) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.TASK_PATH, vdc, id);
        return this.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String url) {
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).get(Task.class);
    }
}
