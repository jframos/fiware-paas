/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.sdc.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ProductReleaseSdcDaoImpl implements ProductReleaseSdcDao {

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(ProductReleaseSdcDaoImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao#findAll()
     */
    public List<ProductRelease> findAll() throws SdcException {

        String sdcproductReleases = findAllProductReleases();

        return fromSDCToPaasManager(sdcproductReleases);
    }

    public ProductRelease load(String product, String version) throws EntityNotFoundException, SdcException {
        ProductRelease productRelease = new ProductRelease();
        String productReleaseString = loadByName(product, version);
        productRelease.fromSdcJson(JSONObject.fromObject(productReleaseString));

        return productRelease;
    }

    private String findAllProductReleases() throws SdcException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)
                + "/catalog/product/release";
        log.debug("url: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);
        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

        response = builder.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            String message = "Error calling SDC to recover all product Releases. " + "Status " + response.getStatus();
            throw new SdcException(message);
        }

        return response.getEntity(String.class);
    }

    private String loadByName(String product, String version) throws EntityNotFoundException, SdcException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)
                + "/catalog/product/" + product + "/release/" + version;
        log.debug("url: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);
        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

        response = builder.get(ClientResponse.class);

        if (response.getStatus() == 404) {
            String message = "The Product Release " + product + "-" + version + " is not present in SDC DataBase ";
            log.error(message);
            throw new EntityNotFoundException(ProductRelease.class, "name", "product" + "-" + "version");
        }

        if (response.getStatus() != 200) {
            String message = "Error calling SDC to recover all product Releases. " + "Status " + response.getStatus();
            throw new SdcException(message);
        }

        return response.getEntity(String.class);
    }

    private List<ProductRelease> fromSDCToPaasManager(String sdcproductReleases) {

        JSONObject jsonNode = JSONObject.fromObject(sdcproductReleases);
        List<ProductRelease> paasManagerProductReleases = fromStringToProductReleases(jsonNode);

        return paasManagerProductReleases;
    }

    /**
     * Converting from a string (list of secGrous in json) to a list of SecurityGroups
     * 
     * @param jsonSecGroups
     * @return
     */
    private List<ProductRelease> fromStringToProductReleases(JSONObject jsonProductReleases) {
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        JSONArray jsonproductReleasesList = jsonProductReleases.getJSONArray("productRelease");

        for (Object o : jsonproductReleasesList) {
            ProductRelease productRelease = new ProductRelease();
            JSONObject jsonProductRelease = (JSONObject) o;
            productRelease.fromSdcJson(jsonProductRelease);
            productReleases.add(productRelease);
        }
        return productReleases;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
}
