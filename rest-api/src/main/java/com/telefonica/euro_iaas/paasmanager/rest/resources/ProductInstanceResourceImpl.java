/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Default EnvironmentInstanceResource implementation.
 * 
 * @author bmmanso
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance/{environmentInstance}/tierInstance/{tierInstance}/productInstance")
@Component
@Scope("request")
public class ProductInstanceResourceImpl implements ProductInstanceResource {

    @Autowired
    private ProductInstanceAsyncManager productInstanceAsyncManager;
    @Autowired
    private TierInstanceManager tierInstanceManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProductInstanceManager productInstanceManager;

    public Task install(String org, String vdc, String environmentInstanceName, String tierInstanceName,
            ProductInstanceDto product, String callback) {

        // TODO Auto-generated method stub
        try {
            TierInstance tierInstance = tierInstanceManager.load(tierInstanceName);
            ClaudiaData data = new ClaudiaData(org, vdc, environmentInstanceName);

            ProductInstance productInstance = new ProductInstance();
            productInstance.setProductRelease(new ProductRelease(product.getProductReleaseDto().getProductName(),
                    product.getProductReleaseDto().getVersion()));
            productInstance.setVdc(product.getVdc());
            productInstance.setPrivateAttributes(product.getAttributes());

            Task task = createTask(
                    MessageFormat.format("Installing product instance {0}", productInstance.getProductRelease()
                            .getName() + "-" + productInstance.getProductRelease().getVersion()), vdc);
            productInstanceAsyncManager.install(tierInstance, data, environmentInstanceName,
                    productInstance.getProductRelease(), productInstance.getPrivateAttributes(), task, null);
            return task;
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block
            throw new WebApplicationException(e, 404);
        }
    }

    public ProductInstanceDto load(String vdc, String name) {
        try {

            ProductInstance productInstance = productInstanceManager.load(name);
            return convertToDto(productInstance);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    public List<ProductInstanceDto> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc,
            String environmentInstanceName, String tierInstanceName) {
        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();

        TierInstance tierInstance = null;
        try {
            tierInstance = tierInstanceManager.load(tierInstanceName);

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

        // criteria.setEnvironmentInstance(envInstance);
        criteria.setTierInstance(tierInstance);
        // criteria.setVdc(vdc);

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        List<ProductInstanceDto> productInstancesDto = new ArrayList<ProductInstanceDto>();
        List<ProductInstance> productInstances = productInstanceManager.findByCriteria(criteria);

        for (int i = 0; i < productInstances.size(); i++) {

            productInstancesDto.add(convertToDto(productInstances.get(i)));

        }
        return productInstancesDto;

    }

    public Task uninstall(String vdc, Long id, String callback) {
        // TODO Auto-generated method stub
        return null;
    }

    private Task createTask(String description, String enviromentName) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(enviromentName);
        return taskManager.createTask(task);
    }

    private ProductInstanceDto convertToDto(ProductInstance productInstance) {
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();

        if (productInstance.getName() != null)
            productInstanceDto.setName(productInstance.getName());
        if (productInstance.getProductRelease() != null)
            productInstanceDto.setProductReleaseDto(convertToDto(productInstance.getProductRelease()));

        productInstanceDto.setVdc(productInstance.getVdc());
        return productInstanceDto;
    }

    private ProductReleaseDto convertToDto(ProductRelease productRelease) {
        ProductReleaseDto productReleaseDto = new ProductReleaseDto();

        productReleaseDto.setProductName(productRelease.getName());
        productReleaseDto.setVersion(productRelease.getVersion());
        productReleaseDto.setPrivateAttributes(productRelease.getAttributes());

        return productReleaseDto;
    }
}
