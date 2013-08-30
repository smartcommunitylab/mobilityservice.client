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

import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourney;

import java.util.List;

import eu.trentorise.smartcampus.mobilityservice.model.BasicItinerary;
import eu.trentorise.smartcampus.mobilityservice.model.BasicRecurrentJourney;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;


/**
 * Mobility Service API for managing user data: user single and recurrent trips.
 * 
 * @author raman
 * 
 */
public class MobilityUserService {

	private static final String SINGLE_JOURNEY = "itinerary";
	private static final String SINGLE_JOURNEY_P = "itinerary/%s";
	private static final String SINGLE_JOURNEY_MONITOR = "itinerary/%s/monitor/%s";
	private static final String RECURRENT_JOURNEY = "recurrent";
	private static final String RECURRENT_JOURNEY_P = "recurrent/%s";
	private static final String RECURRENT_JOURNEY_MONITOR = "recurrent/%s/monitor/%s";

	private String serviceUrl;

	/**
	 * 
	 * @param serviceUrl service address
	 */
	public MobilityUserService(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		if (!serviceUrl.endsWith("/")) {
			this.serviceUrl += '/';
		}
	}

	/**
	 * Save an itinerary specifying the {@link BasicItinerary} as input. This should contain
	 *  the saved itinerary, name, and original (request-time) values for 'from' and 'to' fields.
	 * @param input
	 * @param token user access token
	 * @return saved object
	 * @throws MobilityServiceException
	 */
	public BasicItinerary saveSingleJourney(BasicItinerary input, String token) throws MobilityServiceException {
		if (input == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.postJSON(serviceUrl, SINGLE_JOURNEY, JsonUtils.toJSON(input), token);
			return JsonUtils.toObject(json, BasicItinerary.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Return all the user's single journeys (list of {@link BasicItinerary} instances)
	 * @param token user access token
	 * @return
	 * @throws MobilityServiceException
	 */
	public List<BasicItinerary> getSingleJourneys(String token) throws MobilityServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SINGLE_JOURNEY, token);
			return JsonUtils.toObjectList(json,BasicItinerary.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Return a user's single journey with the specified ID.
	 * @param id
	 * @param token user access token
	 * @return
	 * @throws MobilityServiceException
	 */
	public BasicItinerary getSingleJourney(String id, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.getJSON(serviceUrl, String.format(SINGLE_JOURNEY_P, id), token);
			return JsonUtils.toObject(json, BasicItinerary.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}
	
	/**
	 * Delete a user's single journey
	 * @param id
	 * @param token user access token
	 * @return true if operation succeeds
	 * @throws MobilityServiceException
	 */
	public Boolean deleteSingleJourney(String id, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, String.format(SINGLE_JOURNEY_P, id), token);
			return Boolean.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Set the monitoring status of a single journey
	 * @param id
	 * @param monitor to set monitoring on or off
	 * @param token user access token
	 * @return the new status
	 * @throws MobilityServiceException
	 */
	public Boolean monitorSingleJourney(String id, boolean monitor, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.getJSON(serviceUrl, String.format(SINGLE_JOURNEY_MONITOR, id, monitor), token);
			return Boolean.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Save a recurrent journey specifying the {@link BasicRecurrentJourney} as input. This should 
	 * contain {@link RecurrentJourney} instance, monitored status, name
	 * @param input
	 * @param token user access token
	 * @return
	 * @throws MobilityServiceException
	 */
	public BasicRecurrentJourney saveRecurrentJourney(BasicRecurrentJourney input, String token) throws MobilityServiceException {
		if (input == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.postJSON(serviceUrl, RECURRENT_JOURNEY, JsonUtils.toJSON(input), token);
			return JsonUtils.toObject(json,BasicRecurrentJourney.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Return all the user's recurrent journeys.
	 * @param token user access token
	 * @return
	 * @throws MobilityServiceException
	 */
	public List<BasicRecurrentJourney> getRecurrentJourneys(String token) throws MobilityServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, RECURRENT_JOURNEY, token);
			return JsonUtils.toObjectList(json, BasicRecurrentJourney.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Return a recurrent journey with the specified ID
	 * @param id
	 * @param token user access token
	 * @return
	 * @throws MobilityServiceException
	 */
	public BasicRecurrentJourney getRecurrentJourney(String id, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.getJSON(serviceUrl, String.format(RECURRENT_JOURNEY_P, id), token);
			return JsonUtils.toObject(json,BasicRecurrentJourney.class);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}
	
	/**
	 * Delete a recurrent journey with the specified ID
	 * @param id
	 * @param token user access token
	 * @return true if operation succeeds
	 * @throws MobilityServiceException
	 */
	public Boolean deleteRecurrentJourney(String id, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, String.format(RECURRENT_JOURNEY_P, id), token);
			return Boolean.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Set the monitoring status of a recurrent journey
	 * @param id
	 * @param monitor
	 * @param token user access token
	 * @return the new status
	 * @throws MobilityServiceException
	 */
	public Boolean monitorRecurrentJourney(String id, boolean monitor, String token) throws MobilityServiceException {
		if (id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.getJSON(serviceUrl, String.format(RECURRENT_JOURNEY_MONITOR, id, monitor), token);
			return Boolean.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	/**
	 * Update a recurrent journey with the specified ID
	 * @param input new data for recurrent journey
	 * @param id
	 * @param token user access token
	 * @return true if operation succeeds
	 * @throws MobilityServiceException
	 */
	public Boolean updateRecurrentJourney(BasicRecurrentJourney input, String id, String token) throws MobilityServiceException {
		if (input == null || id == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.putJSON(serviceUrl, String.format(RECURRENT_JOURNEY_P, id), JsonUtils.toJSON(input), token);
			return Boolean.valueOf(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}
}
