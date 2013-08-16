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

import java.util.List;

/**
 * Representation of daily timetable of the public transport. Defined by
 * list of stops (and corresponding stop IDs), trips due on this date, list 
 * of times for each trip at each stop (in 'HH:mm' format), and delays 
 * for those trips.
 * 
 * @author raman
 *
 */
public class TimeTable {
	
	private List<String> tripIds;
	private List<String> stops;
	private List<String> stopsId;
	private List<List<String>> times;
	private List<Delay> delays;
	/**
	 * @return the tripIds
	 */
	public List<String> getTripIds() {
		return tripIds;
	}
	/**
	 * @param tripIds the tripIds to set
	 */
	public void setTripIds(List<String> tripIds) {
		this.tripIds = tripIds;
	}
	/**
	 * @return the stops
	 */
	public List<String> getStops() {
		return stops;
	}
	/**
	 * @param stops the stops to set
	 */
	public void setStops(List<String> stops) {
		this.stops = stops;
	}
	/**
	 * @return the stopsId
	 */
	public List<String> getStopsId() {
		return stopsId;
	}
	/**
	 * @param stopsId the stopsId to set
	 */
	public void setStopsId(List<String> stopsId) {
		this.stopsId = stopsId;
	}
	/**
	 * @return the times
	 */
	public List<List<String>> getTimes() {
		return times;
	}
	/**
	 * @param times the times to set
	 */
	public void setTimes(List<List<String>> times) {
		this.times = times;
	}
	/**
	 * @return the delays
	 */
	public List<Delay> getDelays() {
		return delays;
	}
	/**
	 * @param delays the delays to set
	 */
	public void setDelays(List<Delay> delays) {
		this.delays = delays;
	}
}
