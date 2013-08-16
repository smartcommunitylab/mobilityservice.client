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

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourney;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourneyParameters;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;

import java.util.List;

import eu.trentorise.smartcampus.mobilityservice.model.JSONHelper;
import eu.trentorise.smartcampus.network.RemoteConnector;


/**
 * Multimodal planner API. Methods to plan a single journey or recurrent journey. In the latter 
 * case refers to obtaining the information about available trips for the specified recurrence
 * request (time interval, days of week, start and end points of a trip).
 * 
 * @author raman
 * 
 */
public class MobilityPlannerService {

	private static final String SINGLE = "plansinglejourney";
	private static final String RECURRENT = "planrecurrent";

	private String serviceUrl;

	/**
	 * 
	 * @param serviceUrl service address
	 */
	public MobilityPlannerService(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		if (!serviceUrl.endsWith("/")) {
			this.serviceUrl += '/';
		}
	}

	/**
	 * Plan a single journey given the {@link SingleJourney} request parameters.
	 * @param request
	 * @param token user or client access token 
	 * @return List of itineraries or empty list if no plan found.
	 * @throws MobilityServiceException
	 */
	public List<Itinerary> planSingleJourney(SingleJourney request, String token) throws MobilityServiceException {
		if (request == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.postJSON(serviceUrl, SINGLE, JSONHelper.toJSON(request), token);
			return JSONHelper.toItineraryList(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}
	
	/**
	 * Plan a recurrent journey given the {@link RecurrentJourneyParameters} request parameters.
	 * @param request
	 * @param token user or client access token 
	 * @return List of itineraries or empty list if no plan found.
	 * @throws MobilityServiceException
	 */
	public RecurrentJourney planRecurrentJourney(RecurrentJourneyParameters request, String token) throws MobilityServiceException {
		if (request == null)
			throw new MobilityServiceException("Incomplete request parameters");
		try {
			String json = RemoteConnector.postJSON(serviceUrl, RECURRENT, JSONHelper.toJSON(request), token);
			return JSONHelper.toRecurrentJourney(json);
		}catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

}
