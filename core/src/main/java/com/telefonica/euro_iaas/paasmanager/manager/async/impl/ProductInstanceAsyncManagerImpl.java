/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TaskError;
import com.telefonica.euro_iaas.paasmanager.model.TaskReference;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

public class ProductInstanceAsyncManagerImpl implements ProductInstanceAsyncManager {

    private static Logger LOGGER = Logger.getLogger(ProductInstanceAsyncManagerImpl.class.getName());
    private ProductInstanceManager productInstanceManager;
    private TaskManager taskManager;
    private SystemPropertiesProvider propertiesProvider;
    private TaskNotificator taskNotificator;

    public void install(TierInstance tierInstance, ClaudiaData claudiaData, String envName,
            ProductRelease productRelease, List<Attribute> attributes, Task task, String callback) {

        try {
            ProductInstance productInstance = productInstanceManager.install(tierInstance, claudiaData, envName,
                    productRelease, attributes);
            LOGGER.info("Product " + productRelease.getProduct() + '-' + productRelease.getVersion()
                    + " installed successfully");
        } catch (InvalidProductInstanceRequestException e) {
            String errorMsg = e.getMessage();
            ProductInstance instance = getInstalledProduct(productRelease, tierInstance.getVM());
            if (instance != null) {
                updateErrorTask(instance, task, errorMsg, e);
            } else {
                updateErrorTask(task, errorMsg, e);
            }
        } catch (Exception e) {
            String errorMsg = "The product " + productRelease.getProduct() + "-" + productRelease.getVersion()
                    + " can not be installed in" + tierInstance.getVM();
            ProductInstance instance = getInstalledProduct(productRelease, tierInstance.getVM());
            if (instance != null) {
                updateErrorTask(instance, task, errorMsg, e);
            } else {
                updateErrorTask(task, errorMsg, e);
            }
        } finally {
            notifyTask(callback, task);
        }
    }

    public void uninstall(ProductInstance productInstance, Task task, String callback) {

        try {
            productInstanceManager.uninstall(productInstance);
            updateSuccessTask(task, productInstance);
            LOGGER.info("Product Release " + productInstance.getProductRelease().getProduct() + "-"
                    + productInstance.getProductRelease().getVersion() + " uninstalled successfully");
        } catch (ProductInstallatorException e) {
            String errorMsg = "Error Unistalling ProductInstance " + e.getMessage();
            LOGGER.info(errorMsg);

        } catch (Exception e) {
            String errorMsg = "The product " + productInstance.getProductRelease().getProduct() + "-"
                    + productInstance.getProductRelease().getVersion() + " can not be uninstalled";
        } finally {
            notifyTask(callback, task);
        }

        /*
         * try { productInstanceManager.uninstall(productInstance); updateSuccessTask(task, productInstance);
         * LOGGER.info("Product " + productInstance.getProduct().getProduct().getName() + "-" +
         * productInstance.getProduct().getVersion() + " uninstalled successfully"); } catch (FSMViolationException e) {
         * updateErrorTask(productInstance, task, "The product " + productInstance.getId() +
         * " can not be uninstalled due to previous status", e); } catch (ApplicationInstalledException e) {
         * updateErrorTask(productInstance, task, "The product " + productInstance.getId() +
         * " can not be uninstalled due to some applications are" + " installed on it.", e); } catch
         * (NodeExecutionException e) { updateErrorTask(productInstance, task, "The product " + productInstance.getId()
         * + " can not be uninstalled due to an error executing in node.", e); } catch (Throwable e) {
         * updateErrorTask(productInstance, task, "The product " + productInstance.getId() +
         * " can not be uninstalled due to unexpected error.", e); } finally { notifyTask(callback, task); }
         */

    }

    public ProductInstance load(String vdc, String name) throws EntityNotFoundException {
        return productInstanceManager.load(vdc, name);
    }

    // //////// PRIVATE METHODS ///////////

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, ProductInstance productInstance) {
        String piResource = MessageFormat.format(propertiesProvider.getProperty(PRODUCT_INSTANCE_BASE_URL),
                productInstance.getId(), // the id

                productInstance.getProductRelease().getProduct(), productInstance.getVdc()); // the product
        task.setResult(new TaskReference(piResource));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }

    /*
     * Update the task with necessary information when the task is wrong and the product instance exists in the system.
     */
    private void updateErrorTask(ProductInstance productInstance, Task task, String message, Throwable t) {
        String piResource = MessageFormat.format(propertiesProvider.getProperty(PRODUCT_INSTANCE_BASE_URL),
                productInstance.getId(), // the id

                productInstance.getProductRelease().getProduct(), productInstance.getVdc()); // the product
        task.setResult(new TaskReference(piResource));
        updateErrorTask(task, message, t);
    }

    /*
     * Update the task with necessary information when the task is wrong.
     */
    private void updateErrorTask(Task task, String message, Throwable t) {
        TaskError error = new TaskError(message);
        error.setMajorErrorCode(t.getMessage());
        error.setMinorErrorCode(t.getClass().getSimpleName());
        task.setEndTime(new Date());
        task.setStatus(TaskStates.ERROR);
        task.setError(error);
        taskManager.updateTask(task);
        LOGGER.info("An error occurs while executing a product action. See task " + task.getHref()
                + "for more information");
    }

    private ProductInstance getInstalledProduct(ProductRelease productRelease, VM vm) {
        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
        // criteria.setVm(vm);
        criteria.setProductRelease(productRelease);
        try {
            return productInstanceManager.loadByCriteria(criteria);
        } catch (EntityNotFoundException e) {
            return null;
        } catch (NotUniqueResultException e) {
            return null;
        }
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    /**
     * @param productInstanceManager
     *            the productInstanceManager to set
     */
    public void setProductInstanceManager(ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param taskNotificator
     *            the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

}
