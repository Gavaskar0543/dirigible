/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.api.v3.http.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.dirigible.commons.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for working with proxies
 */
public class HttpClientProxyUtils {

	/** The HTTP_PROXY_HOST. */
	public static final String HTTP_PROXY_HOST = "http.proxyHost"; //$NON-NLS-1$

	/** The HTTP_PROXY_PORT. */
	public static final String HTTP_PROXY_PORT = "http.proxyPort"; //$NON-NLS-1$

	/** The HTTPS_PROXY_HOST. */
	public static final String HTTPS_PROXY_HOST = "https.proxyHost"; //$NON-NLS-1$

	/** The HTTPS_PROXY_PORT. */
	public static final String HTTPS_PROXY_PORT = "https.proxyPort"; //$NON-NLS-1$

	/** The HTTP_NON_PROXY_HOSTS. */
	public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts"; //$NON-NLS-1$

	private static final Logger logger = LoggerFactory.getLogger(HttpClientProxyUtils.class);

	{
		try {
			setProxySettings();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Sets the proxy settings.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void setProxySettings() throws IOException {
		setTrustAllSSL();
	}

	/**
	 * Returns the http client.
	 *
	 * @param trustAll
	 *            if no SSL verification should be done
	 * @return the http client
	 */
	public static CloseableHttpClient getHttpClient(boolean trustAll) {
		CloseableHttpClient httpClient = null;

		if (trustAll) {
			try {
				SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
				sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
				SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
						(hostName, sslSession) -> true);
				HttpClientBuilder httpClientBuilder = HttpClients.custom();
				httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
				setProxyIfNeeded(httpClientBuilder);
				httpClient = httpClientBuilder.build();
			} catch (Exception e) {
				logger.error("Error occurred when trying to create a TRUST ALL HTTP Client", e);
				httpClient = HttpClients.createDefault();
			}
		} else {
			HttpClientBuilder httpClientBuilder = HttpClients.custom();
			setProxyIfNeeded(httpClientBuilder);
			httpClient = httpClientBuilder.build();
		}

		return httpClient;
	}

	/**
	 * Sets the proxy if needed.
	 *
	 * @param httpClientBuilder
	 *            the client build
	 */
	private static void setProxyIfNeeded(HttpClientBuilder httpClientBuilder) {
		String httpProxyHost = Configuration.get(HTTP_PROXY_HOST);
		String httpProxyPort = Configuration.get(HTTP_PROXY_PORT);

		if ((httpProxyHost != null) && (httpProxyPort != null) && !"".equals(httpProxyHost.trim()) && !"".equals(httpProxyPort.trim())) {
			HttpHost httpProxy = new HttpHost(httpProxyHost, Integer.parseInt(httpProxyPort));
			httpClientBuilder.setProxy(httpProxy);
		}
	}

	/**
	 * Sets the trust all SSL.
	 *
	 * @throws IOException
	 *             in case an error occurs while setting the SSL socket factory
	 */
	private static void setTrustAllSSL() throws IOException {
		try {
			HttpsURLConnection.setDefaultSSLSocketFactory(createTrustAllSSLContext().getSocketFactory());
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (KeyManagementException e) {
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Creates the trust all SSL context.
	 *
	 * @return the SSL context
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws KeyManagementException
	 *             the key management exception
	 */
	private static SSLContext createTrustAllSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("SSL");

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Set up a TrustManager that trusts everything
		sslContext.init(null, trustAllCerts, new SecureRandom());
		return sslContext;
	}
}
