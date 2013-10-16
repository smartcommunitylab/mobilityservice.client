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
package eu.trentorise.smartcampus.mobilityservice.model;

import java.io.Serializable;

/**
 * Representation of a single trip data: agency ID, route ID, route names, trip ID,
 * time when the trip passes the associated stop and the real-time delay (if any).
 * @author raman
 *
 */
public class TripData implements Serializable {
	private static final long serialVersionUID = -6416720376610856881L;

	private String agencyId;
	private String routeId;
	private String routeName;
	private String routeShortName;
	private String tripId;
	private long time;
	private Delay delay;

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

}
