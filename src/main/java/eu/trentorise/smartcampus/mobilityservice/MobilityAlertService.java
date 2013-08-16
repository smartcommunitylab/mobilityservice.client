/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.mobilityservice;

import it.sayservice.platform.smartplanner.data.message.alerts.Alert;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import eu.trentorise.smartcampus.mobilityservice.model.JSONHelper;
import eu.trentorise.smartcampus.network.RemoteConnector;


/**
 * Mobility alert service API. Provides a possibility to signal a transport problem 
 * (strike, delay, parking, etc.).
 * 
 * @author raman
 * 
 */
public class MobilityAlertService {

	private static final String USER_ALERT = "alert/user";
	private static final String SERVICE_ALERT = "alert/service";
	private String serviceUrl;

	/**
	 * 
	 * @param serviceUrl service address
	 */
	public MobilityAlertService(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		if (!serviceUrl.endsWith("/")) {
			this.serviceUrl += '/';
		}
	}

	/**
	 * Send an alert (strike, transport delay, parking, road works) on behalf of the user 
	 * @param alert
	 * @param token user access token
	 * @throws MobilityServiceException
	 */
	public void sendUserAlert(Alert alert, String token) throws MobilityServiceException {
		if (alert == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			alert.setCreatorType(CreatorType.USER);
			RemoteConnector.postJSON(serviceUrl, USER_ALERT, JSONHelper.toJSON(alert), token);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Send an alert (strike, transport delay, parking, road works) 
	 * @param alert
	 * @param token client access token
	 * @throws MobilityServiceException
	 */
	public void sendServiceAlert(Alert alert, String token) throws MobilityServiceException {
		if (alert == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			alert.setCreatorType(CreatorType.SERVICE);
			RemoteConnector.postJSON(serviceUrl, SERVICE_ALERT, JSONHelper.toJSON(alert), token);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}

	}

}
