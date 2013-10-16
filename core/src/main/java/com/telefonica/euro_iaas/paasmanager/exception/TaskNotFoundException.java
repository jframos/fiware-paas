/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.exception;

import com.telefonica.euro_iaas.paasmanager.model.Task;

/**
 * @author jesus.movilla
 */
public class TaskNotFoundException extends Exception {

    private Task task;

    public TaskNotFoundException(String msg) {
        super(msg);
    }

    public TaskNotFoundException(String msg, Task task) {
        super(msg);
        this.task = task;
    }

    /**
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * @param task
     *            the task to set
     */
    public void setTask(Task task) {
        this.task = task;
    }

}
