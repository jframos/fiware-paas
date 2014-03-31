/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.sdc.impl;



import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ProductReleaseSdcDaoImpl implements ProductReleaseSdcDao {

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(ProductReleaseSdcDaoImpl.class);
    Client client;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao#findAll()
     */
    public List<ProductRelease> findAll(String token, String tenant) throws SdcException {
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

        List<String> pNames = findAllProducts(token, tenant);

        for (int i = 0; i < pNames.size(); i++) {
            final String pName = pNames.get(i);
            try {
                List<ProductRelease> productReleasesProduct = findAllProductReleasesOfProduct(pName, token, tenant);

                for (int j = 0; j < productReleasesProduct.size(); j++) {
                    productReleases.add(productReleasesProduct.get(j));
                }
            } catch (Exception ex) {
                log.warn("Error loading product from sdc:" + pName);
            }
        }
        return productReleases;
    }

    public ProductRelease load(String product, String version, ClaudiaData data) throws EntityNotFoundException, SdcException {
        ProductRelease productRelease = new ProductRelease();
        String productReleaseString = loadByName(product, version, data.getUser().getToken(), data.getVdc());
        productRelease.fromSdcJson(JSONObject.fromObject(productReleaseString));

        return productRelease;
    }

    public List<String> findAllProducts(String token, String tenant) throws SdcException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL) + "/catalog/product";

        log.debug("url: " + url);

        Builder builder = createWebResource (url, token ,tenant);
        builder = builder.type(MediaType.APPLICATION_JSON);

        InputStream inputStream = builder.get(InputStream.class);
        String response;
        try {
            response = IOUtils.toString(inputStream);
        } catch (IOException e) {
            String message = "Error calling SDC to obtain the products ";
            log.error(message);
            throw new SdcException(message);
        }

        return fromSDCToProductNames(response);
    }

    public List<ProductRelease> findAllProductReleasesOfProduct(String pName, String token, String tenant) throws SdcException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)
                + "/catalog/product/" + pName + "/release";
        log.debug("url: " + url);

       
        Builder builder = createWebResource (url, token ,tenant);
        builder = builder.type(MediaType.APPLICATION_JSON);

        InputStream inputStream = builder.get(InputStream.class);
        String response;
        try {
            response = IOUtils.toString(inputStream);
        } catch (IOException e) {
            String message = "Error calling SDC to obtain the products ";
            log.error(message);
            throw new SdcException(message);
        }
        return fromSDCToPaasManager(response);
    }

    private String loadByName(String product, String version, String token, String tenant) throws EntityNotFoundException, SdcException {
        log.debug ("Load by name " + product + " " + version );
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)
                + "/catalog/product/" + product + "/release/" + version;
        log.debug("the url: " + url);
       

        ClientResponse response = null;
        Builder builder = createWebResource (url, token ,tenant);
        
   
     //   Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
        
 
        response = builder.get(ClientResponse.class);

        if (response.getStatus() == 404) {
            String message = "The Product Release " + product + "-" + version + " is not present in SDC DataBase ";
            log.error(message);
            throw new EntityNotFoundException(ProductRelease.class, "name", "product" + "-" + "version");
        }

        if (response.getStatus() != 200) {
        	
            InputStream input = response.getEntityInputStream();
            StringWriter writer = new StringWriter();
            try {
				IOUtils.copy(input, writer, "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String theString = writer.toString();
          
            
            String message = "Error calling SDC to recover all product Releases. " + "Status " + response.getStatus() + " " + theString;
            throw new SdcException(message);
        }

        return response.getEntity(String.class);
    }

    private List<String> fromSDCToProductNames(String sdcproducts) {

        JSONObject jsonNodeProducts = JSONObject.fromObject(sdcproducts);
        List<String> productNames = new ArrayList<String>();

        JSONArray jsonproductList = jsonNodeProducts.getJSONArray("product");

        for (Object o : jsonproductList) {
            JSONObject jsonProduct = (JSONObject) o;
            String productName = jsonProduct.getString("name");
            productNames.add(productName);
        }
        return productNames;
    }

    private List<ProductRelease> fromSDCToPaasManager(String sdcproductReleases) {
        List<ProductRelease> paasManagerProductReleases = fromStringToProductReleases(sdcproductReleases);
        return paasManagerProductReleases;
    }

    /**
     * Converting from a string (list of secGrous in json) to a list of SecurityGroups
     * 
     * @param jsonProductReleases
     * @return List of ProductReleases
     */
    public List<ProductRelease> fromStringToProductReleases(String sdcproductReleases) {
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        JSONObject json = (JSONObject) JSONSerializer.toJSON(sdcproductReleases);
        Object obj = json.get("productRelease");
        if (obj instanceof JSONObject) {
            JSONObject jsonProductRelease = (JSONObject) obj;
            ProductRelease productRelease = new ProductRelease();
            productRelease.fromSdcJson(jsonProductRelease);
            productReleases.add(productRelease);
        } else {
            JSONArray ja = (JSONArray) obj;
            JSONArray jsonproductReleasesList = ja;
            for (Object o : jsonproductReleasesList) {
                ProductRelease productRelease = new ProductRelease();
                JSONObject jsonProductRelease = (JSONObject) o;
                productRelease.fromSdcJson(jsonProductRelease);
                productReleases.add(productRelease);
            }
        }

        return productReleases;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    private Builder createWebResource (String url, String token, String tenant) {
    	client.addFilter(new LoggingFilter(System.out));
    
    	WebResource webResource = client.resource(url);
    	Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
    	 System.out.println (url);
    	 System.out.println ("token  " + token);
    	 System.out.println ("tenant " + tenant);
    	 
    	
    	 builder.header("X-Auth-Token", token);
    	 builder.header("Tenant-Id", tenant);
 
    	return builder;

    }
}
