package com.telefonica.euro_iaas.paasmanager.monitoring;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.COLLECTOR_BASEURL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.COLLECTOR_IP;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.COLLECTOR_PORT;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.COLLECTOR_MYSQL;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.telefonica.euro_iaas.paasmanager.exception.MonitoringDeleteException;
import com.telefonica.euro_iaas.paasmanager.exception.MonitoringGetException;
import com.telefonica.euro_iaas.paasmanager.exception.MonitoringPutException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class MonitoringClientCollectdImpl implements MonitoringClient {

	private static Logger log = Logger
			.getLogger(MonitoringClientCollectdImpl.class);
	private SystemPropertiesProvider systemPropertiesProvider;
	private final static String MONITORING_ON = "ON";
	private final static String MONITORING_OFF = "OFF";

	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 */
	public void setSystemPropertiesProvider(
			SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}

	public String getMonitoringStatus(String fqn) {
		ClientResponse response = null;
		int vmResponse = 0;
		String monitState = null;
		String md5 = md5FQN(fqn);
		String uri = MessageFormat.format(systemPropertiesProvider
				.getProperty(COLLECTOR_BASEURL), systemPropertiesProvider
				.getProperty(COLLECTOR_IP), systemPropertiesProvider
				.getProperty(COLLECTOR_PORT), md5);

		log.info("url: " + uri);

		try {
			response = getResourceMonitoring(uri);
			vmResponse = response.getStatus();
			if (vmResponse == 200) {
				monitState = MONITORING_ON;
			} else if (vmResponse == 204) {
				monitState = MONITORING_OFF;
			} else {
				String errorMessage = "Exception when trying to start monitoring "
						+ "System from VM with the fqn: "
						+ fqn
						+ ". Invalid Monitoring" + "Response";
				log.error(errorMessage);
				// throw new MonitoringException(errorMessage);
			}
		} catch (MonitoringGetException e) {
			String errorMessage = "Exception when starting monitoring on "
					+ "machine" + fqn + " GET on " + uri + "FAILED. Invalid "
					+ "Monitoring Request";
			log.error(errorMessage);
			// throw new MonitoringException(errorMessage);
		}

		return monitState;
	}

	public void stopMonitoring(String fqn) {
		ClientResponse response = null;
		int vmResponse = 400;
		String md5 = md5FQN(fqn);
		String uri = MessageFormat.format(systemPropertiesProvider
				.getProperty(COLLECTOR_BASEURL), systemPropertiesProvider
				.getProperty(COLLECTOR_IP), systemPropertiesProvider
				.getProperty(COLLECTOR_PORT), md5);
		log.info("url: " + uri);

		try {
			if (getResourceMonitoring(fqn).getStatus() == 200) {
				response = deleteResourceMonitoring(uri);
				vmResponse = response.getStatus();
			}
			if ((vmResponse >= 300) || (vmResponse < 200)) {
				String errorMessage = "Exception when trying to start monitoring "
						+ "System from VM with the fqn: "
						+ fqn
						+ ". Invalid Monitoring" + "Response";
				log.error(errorMessage);
				// throw new MonitoringException(errorMessage);
			}

		} catch (MonitoringDeleteException e) {
			String errorMessage = "Exception when deleting monitoring on "
					+ "machine" + fqn + " DELETE on " + uri
					+ "FAILED. Invalid " + "Monitoring Request";
			log.error(errorMessage);
			// throw new MonitoringException (errorMessage);
		} catch (MonitoringGetException e) {
			String errorMessage = "Exception when getting monitoring on "
					+ "machine" + fqn + " GET on " + uri + "FAILED. Invalid "
					+ "Monitoring Request";
			log.error(errorMessage);
			// throw new MonitoringException (errorMessage);
		}
	}

	public void startMonitoring(String fqn, String producto) {
		ClientResponse response = null;
		String md5 = md5FQN(fqn);
		if(producto!= null){
			md5 = md5 + "_" + producto;
			fqn = fqn + ".pics." + producto;
		}
		String uri = MessageFormat.format(systemPropertiesProvider
				.getProperty(COLLECTOR_BASEURL), systemPropertiesProvider
				.getProperty(COLLECTOR_IP), systemPropertiesProvider
				.getProperty(COLLECTOR_PORT), md5);
		log.info("url: " + uri);
		if (!isCreatedBefore(fqn)) {
			try {
				response = putMonitoringResource(uri, fqn);
				int vmResponse = response.getStatus();
				log.info(vmResponse);
				if ((vmResponse >= 300) || (vmResponse < 200)) {
					String errorMessage = "Exception when trying to start monitoring "
							+ "System from VM with the fqn: "
							+ fqn
							+ ". Invalid Monitoring" + "Response";
					log.error(errorMessage);
					// throw new MonitoringException(errorMessage);
				}
			} catch (MonitoringPutException e) {
				String errorMessage = "Exception when  starting monitoring on "
						+ "machine" + fqn + " PUT on " + uri + " with payload "
						+ fqn + "FAILED. Invalid Monitoring Request";
				log.error(errorMessage);
				// throw new MonitoringException(errorMessage);
			}
		} else {
			log.info("The fqn: " + fqn + "has been registered in "
					+ "the monitoring systems before,");
		}
	}

	private ClientResponse putMonitoringResource(String url, String fqn)
			throws MonitoringPutException {
		Client client = new Client();
		log.info("url: " + url);
		ClientResponse response = null;
		try {
			WebResource wr = client.resource(url);

			response = wr.accept("text/plain").type("text/plain").entity(fqn)
					.put(ClientResponse.class);

			log.info(response);

		} catch (Exception e) {
			String errorMessage = "Error performing put on the resource: "
					+ url + " with fqn: " + fqn;
			log.error(errorMessage);
			throw new MonitoringPutException(errorMessage);
		}
		return response;

	}

	private String md5FQN(String fqn) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(fqn.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	private ClientResponse getResourceMonitoring(String url)
			throws MonitoringGetException {
		Client client = new Client();
		log.info("url: " + url);
		ClientResponse response = null;
		try {
			WebResource wr = client.resource(url);
			response = wr.accept("text/plain").type("text/plain").get(
					ClientResponse.class);
		} catch (Exception e) {
			String errorMessage = "Error performing get on the resource monitoring: "
					+ url;
			log.error(errorMessage);
			throw new MonitoringGetException(errorMessage);
		}
		return response;
	}

	private ClientResponse deleteResourceMonitoring(String url)
			throws MonitoringDeleteException {
		Client client = new Client();
		log.info("url: " + url);
		ClientResponse response = null;
		try {
			WebResource wr = client.resource(url);
			response = wr.accept("text/plain").type("text/plain").delete(
					ClientResponse.class);
		} catch (Exception e) {
			String errorMessage = "Error performing delete on the resource monitoring: "
					+ url;
			log.error(errorMessage);
			throw new MonitoringDeleteException(errorMessage);
		}
		return response;
	}

	private boolean isCreatedBefore(String fqn) {
		long count = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(
					systemPropertiesProvider.getProperty(COLLECTOR_MYSQL),
					"root", null);
			conexion.setAutoCommit(false);
			Statement s = conexion.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from nodedirectory "
					+ "where fqn like '" + fqn + "'");
			while (rs.next()) {
				count = (Long) rs.getObject(1);
			}
			rs.close();
		} catch (ClassNotFoundException e) {
			log.error("Class not find myslq. Description: " + e);
			count = 1;
		} catch (SQLException e) {
			log.error("Have a problem conection with the BD of the minitoring "
					+ "sistem. Describe: " + e);
			count = 1;
		}

		if (count == 0) {
			return false;
		} else {
			return true;
		}

	}
}
