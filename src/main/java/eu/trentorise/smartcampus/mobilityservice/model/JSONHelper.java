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

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Leg;
import it.sayservice.platform.smartplanner.data.message.LegGeometery;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.RoadElement;
import it.sayservice.platform.smartplanner.data.message.SimpleLeg;
import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.Transport;
import it.sayservice.platform.smartplanner.data.message.alerts.Alert;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertAccident;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertDelay;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertParking;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertRoad;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertRoadType;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertStrike;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertType;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourney;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourneyParameters;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Id;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Parking;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Stop;
import it.sayservice.platform.smartplanner.data.message.otpbeans.StopTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON conversion utility class
 * @author raman
 *
 */
public class JSONHelper {

	/**
	 * Convert to list of {@link Parking}
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Parking> toParkingList(String json) throws JSONException {
		List<Parking> res = new ArrayList<Parking>();
		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Parking p = new Parking();
			p.setDescription(obj.getString("description"));
			p.setMonitored(obj.getBoolean("monitored"));
			p.setName(obj.getString("name"));
			if (obj.has("position")) {
				JSONArray posArr = obj.getJSONArray("position");
				p.setPosition(new double[]{posArr.getDouble(0),posArr.getDouble(1)});
			}
			if (obj.has("slotsAvailable")) {
				p.setSlotsAvailable(obj.getInt("slotsAvailable"));
			}
			if (obj.has("slotsTotal")) {
				p.setSlotsTotal(obj.getInt("slotsTotal"));
			}
			res.add(p);
		}
		return res;
	}

	/**
	 * Convert to list of {@link AlertRoad}
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<AlertRoad> toRoadInfoList(String json) throws JSONException {
		JSONArray arr = new JSONArray(json);
		return toRoadInfoList(arr);
	}

	private static List<AlertRoad> toRoadInfoList(JSONArray arr)
			throws JSONException {
		List<AlertRoad> res = new ArrayList<AlertRoad>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject o = arr.getJSONObject(i);
			AlertRoad ar = new AlertRoad();
			toAlert(ar, o);
			ar.setAgencyId(o.getString("agencyId"));
			if (o.has("changeTypes")) {
				JSONArray typeArr = o.getJSONArray("changeTypes");
				ar.setChangeTypes(new AlertRoadType[typeArr.length()]);
				for (int j = 0; j < typeArr.length(); j++) {
					ar.getChangeTypes()[j] = AlertRoadType.valueOf(typeArr.getString(j));
				}	
						
			}
			if (o.has("road")) {
				JSONObject road = o.getJSONObject("road");
				RoadElement re = new RoadElement();
				re.setFromIntersection(road.getString("fromIntersection"));
				re.setFromNumber(road.getString("fromNumber"));
				re.setLat(road.getString("lat"));
				re.setLon(road.getString("lon"));
				re.setNote(road.getString("note"));
				re.setStreet(road.getString("street"));
				re.setStreetCode(road.getString("streetCode"));
				re.setToIntersection(road.getString("toIntersection"));
				re.setToNumber(road.getString("toNumber"));
				
				ar.setRoad(re);
			}
			ar.setType(AlertType.ROAD);
			res.add(ar);
		}
		return res;
	}

	/**
	 * Convert to list of {@link Route}
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<Route> toRouteList(String json) throws JSONException {
		List<Route> list = new ArrayList<Route>();

		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Route r = new Route();
			Id id = new Id();
			JSONObject idObj = obj.getJSONObject("id"); 
			id.setAgency(idObj.getString("agency"));
			id.setId(idObj.getString("id"));
			r.setId(id);
			r.setRouteLongName(obj.getString("routeLongName"));
			r.setRouteShortName(obj.getString("routeShortName"));
			list.add(r);
		}
		return list;
	}

	/**
	 * Convert to list of {@link Stop}
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Stop> toStopList(String json) throws JSONException {
		List<Stop> list = new ArrayList<Stop>();

		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Stop s = new Stop();
			s.setId(obj.getString("id"));
			s.setLatitude(obj.getDouble("latitude"));
			s.setLongitude(obj.getDouble("longitude"));
			s.setName(obj.getString("name"));
			list.add(s);
		}
		return list;
	}

	/**
	 * Convert to list of {@link StopTime}
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<StopTime> toStopTimeList(String json) throws JSONException {
		List<StopTime> list = new ArrayList<StopTime>();

		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			StopTime s = new StopTime();
			Id id = new Id();
			JSONObject idObj = obj.getJSONObject("trip"); 
			id.setAgency(idObj.getString("agency"));
			id.setId(idObj.getString("id"));
			s.setTrip(id);
			s.setTime(obj.getLong("time"));
			list.add(s);
		}
		return list;
	}

	/**
	 * Convert to list of {@link TripData}
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<TripData> toTripDataList(String json) throws JSONException {
		List<TripData> list = new ArrayList<TripData>();

		JSONObject map = new JSONObject(json);
		JSONArray keys = map.names(); 
		
		if (keys != null) {
			for (int i = 0; i < keys.length(); i++) {
				String routeId = keys.getString(i);
				JSONObject obj = map.getJSONObject(routeId);
				JSONArray timesArr = obj.getJSONArray("times");
				JSONObject delaysObj = obj.getJSONObject("delays");
				for (int j = 0; j < timesArr.length(); j++) {
					JSONObject rData = timesArr.getJSONObject(j);
					TripData t = new TripData();
					t.setRouteId(routeId);
					t.setRouteName(obj.getString("name"));
					t.setRouteShortName(obj.getString("route"));
					t.setTime(rData.getLong("time"));
					JSONObject tripObj = rData.getJSONObject("trip");
					t.setTripId(tripObj.getString("id"));
					t.setAgencyId(tripObj.getString("agency"));

					Delay delay = null;
					if (delaysObj.has(t.getTripId())) {
						delay = new Delay();
						JSONObject delayMap = delaysObj.getJSONObject(t
								.getTripId());
						JSONArray types = delayMap.names();
						if (types != null) {
							for (int k = 0; k < types.length(); k++) {
								String type = types.getString(k);
								delay.values.put(
										CreatorType.getAlertType(type),
										delayMap.getString(type));
							}
						}
					}
					t.setDelay(delay);
					list.add(t);
				}

			}
		}
		return list;
	}

	/**
	 * Convert to {@link TimeTable}
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static TimeTable toTimeTable(String json) throws JSONException {
		JSONObject tt = new JSONObject(json);
		TimeTable result = new TimeTable();
		JSONArray arr = tt.getJSONArray("stops");
		result.setStops(new ArrayList<String>(arr.length()));
		for (int i = 0; i < arr.length(); i++) {
			result.getStops().add(arr.getString(i));
		}
		arr = tt.getJSONArray("stopsId");
		result.setStopsId(new ArrayList<String>(arr.length()));
		for (int i = 0; i < arr.length(); i++) {
			result.getStopsId().add(arr.getString(i));
		}
		arr = tt.getJSONArray("tripIds").getJSONArray(0);
		result.setTripIds(new ArrayList<String>(arr.length()));
		for (int i = 0; i < arr.length(); i++) {
			result.getTripIds().add(arr.getString(i));
		}
		// take first day only
		arr = tt.getJSONArray("times").getJSONArray(0);
		result.setTimes(new ArrayList<List<String>>(arr.length()));
		for (int i = 0; i < arr.length(); i++) {
			JSONArray tripTimes = arr.getJSONArray(i);
			List<String> times = new ArrayList<String>(tripTimes.length());
			for (int j = 0; j < tripTimes.length(); j++) {
				times.add(tripTimes.getString(j));
			}
			result.getTimes().add(times);
		}

		// take first day only
		arr = tt.getJSONArray("delays").getJSONArray(0);
		result.setDelays(new ArrayList<Delay>(arr.length()));
		for (int i = 0; i < arr.length(); i++) {
			Delay delay = new Delay();
			JSONObject delayMap = arr.getJSONObject(i);
			JSONArray types = delayMap.names();
			if (types != null) {
				for (int k = 0; k < types.length(); k++) {
					String type = types.getString(k);
					delay.values.put(CreatorType.getAlertType(type), delayMap.getString(type));
				}
			}
			result.getDelays().add(delay);
		}
		
		return result;
	}

	/**
	 * Convert to list of {@link Delay}
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<Delay> toDelayList(String json) throws JSONException {
		JSONObject tt = new JSONObject(json);
		// take first day only
		JSONArray arr = tt.getJSONArray("delays").getJSONArray(0);
		List<Delay> delays = new ArrayList<Delay>();
		for (int i = 0; i < arr.length(); i++) {
			Delay delay = new Delay();
			JSONObject delayMap = arr.getJSONObject(i);
			JSONArray types = delayMap.names();
			if (types != null) {
				for (int k = 0; k < types.length(); k++) {
					String type = types.getString(k);
					delay.values.put(CreatorType.getAlertType(type), delayMap.getString(type));
				}
			}
			delays.add(delay);
		}
		return delays;
	}

	/**
	 * Convert {@link SingleJourney} instance to JSON representation
	 * @param request
	 * @return
	 * @throws JSONException 
	 */
	public static String toJSON(SingleJourney request) throws JSONException {
		JSONObject o = new JSONObject(request);
		// enum values incorrectly processed
		if (request.getRouteType() != null) {
			o.put("routeType", request.getRouteType().toString());
		}
		if (request.getTransportTypes()  != null && request.getTransportTypes().length > 0) {
			List<String> list = new ArrayList<String>(request.getTransportTypes().length);
			for (TType t : request.getTransportTypes()) list.add(t.toString());
			o.put("transportTypes", list);
		}
		return o.toString();
	}

	/**
	 * Convert JSON string to list of {@link Itinerary} instances
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<Itinerary> toItineraryList(String json) throws JSONException {
		List<Itinerary> list = new ArrayList<Itinerary>();
		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject o = arr.getJSONObject(i);
			Itinerary it = toItinerary(o);
			list.add(it);
		} 
		return list;
	}

	private static Itinerary toItinerary(JSONObject o) throws JSONException {
		Itinerary it = new Itinerary();
		it.setDuration(o.getLong("duration"));
		it.setEndtime(o.getLong("endtime"));
		JSONObject pObj = o.getJSONObject("from");
		Position p = toPosition(pObj);
		it.setFrom(p);
		List<Leg> legs = new ArrayList<Leg>();
		JSONArray legsArr = o.getJSONArray("leg");
		if (legsArr != null) {
			for (int j = 0; j < legsArr.length(); j++) {
				JSONObject lObj = legsArr.getJSONObject(j);
				Leg leg = new Leg();
				
				JSONArray alerts;
				if (!lObj.isNull("alertDelayList")) {
					alerts = lObj.getJSONArray("alertDelayList");
					leg.setAlertDelayList(new ArrayList<AlertDelay>());
					for (int k = 0; k < alerts.length(); k++) {
						AlertDelay ac = new AlertDelay();
						JSONObject acObj = alerts.getJSONObject(k);
						toAlert(ac, acObj);
						ac.setDelay(acObj.getLong("delay"));
						ac.setPosition(toPosition(acObj
								.getJSONObject("position")));
						ac.setTransport(toTransport(acObj
								.getJSONObject("transport")));
						leg.getAlertDelayList().add(ac);
					}
				}
				if (!lObj.isNull("alertParkingList")) {
					alerts = lObj.getJSONArray("alertParkingList");
					leg.setAlertParkingList(new ArrayList<AlertParking>());
					for (int k = 0; k < alerts.length(); k++) {
						AlertParking ac = new AlertParking();
						JSONObject acObj = alerts.getJSONObject(k);
						toAlert(ac, acObj);
						ac.setNoOfvehicles(acObj.getInt("NoOfvehicles"));
						JSONObject sIdObj = acObj.getJSONObject("stopId");
						ac.setPlace(new StopId(
								sIdObj.getString("agencyId"), sIdObj
										.getString("id")));
						ac.setPlacesAvailable(acObj
								.getInt("placesAvailable"));
						leg.getAlertParkingList().add(ac);
					}
				}
				if (!lObj.isNull("alertStrikeList")) {

					alerts = lObj.getJSONArray("alertStrikeList");
					leg.setAlertStrikeList(new ArrayList<AlertStrike>());
					for (int k = 0; k < alerts.length(); k++) {
						AlertStrike ac = new AlertStrike();
						JSONObject acObj = alerts.getJSONObject(k);
						toAlert(ac, acObj);
						JSONObject sIdObj = acObj.getJSONObject("stop");
						ac.setStop(new StopId(sIdObj.getString("agencyId"),
								sIdObj.getString("id")));
						ac.setTransport(toTransport(acObj
								.getJSONObject("transport")));
						leg.getAlertStrikeList().add(ac);
					}
				}
				if (!lObj.isNull("alertRoadList")) {
					alerts = lObj.getJSONArray("alertRoadList");
					leg.setAlertRoadList(toRoadInfoList(alerts));
				}
				if (!lObj.isNull("alertAccidentList")) {
					alerts = lObj.getJSONArray("alertAccidentList");
					leg.setAlertAccidentList(new ArrayList<AlertAccident>());
					for (int k = 0; k < alerts.length(); k++) {
						AlertAccident ac = new AlertAccident();
						JSONObject acObj = alerts.getJSONObject(k);
						toAlert(ac, acObj);
						ac.setPosition(toPosition(acObj
								.getJSONObject("position")));
						ac.setSeverity(acObj.optString("severity", null));
						leg.getAlertAccidentList().add(ac);
					}
				}
				leg.setDuration(lObj.getLong("duration"));
				leg.setEndtime(lObj.getLong("endtime"));
				leg.setFrom(toPosition(lObj.getJSONObject("from")));
				LegGeometery lg = new LegGeometery();
				JSONObject lgObj = lObj.getJSONObject("legGeometery");
				lg.setLength(lgObj.getLong("length"));
				lg.setLevels(lgObj.getString("levels"));
				lg.setPoints(lgObj.getString("points"));
				
				leg.setLegGeometery(lg);
				leg.setLegId(lObj.getString("legId"));
				leg.setStartime(lObj.getLong("startime"));
				leg.setTo(toPosition(lObj.getJSONObject("to")));
				JSONObject tObj = lObj.getJSONObject("transport");
				Transport t = toTransport(tObj);
				leg.setTransport(t);
				legs.add(leg);
			}
		}
		it.setLeg(legs);
		it.setStartime(o.getLong("startime"));
		pObj = o.getJSONObject("to");
		it.setTo(toPosition(pObj));
		it.setWalkingDuration(o.getLong("walkingDuration"));
		return it;
	}

	private static Transport toTransport(JSONObject tObj) throws JSONException {
		Transport t = new Transport();
		t.setAgencyId(tObj.getString("agencyId"));
		t.setRouteId(tObj.getString("routeId"));
		t.setRouteShortName(tObj.getString("routeShortName"));
		t.setTripId(tObj.getString("tripId"));
		if (tObj.has("type") && !tObj.isNull("type"))
		t.setType(TType.valueOf(tObj.getString("type")));
		return t;
	}

	/**
	 * @param ac
	 * @param acObj
	 * @throws JSONException 
	 */
	private static void toAlert(Alert ar, JSONObject o) throws JSONException {
		ar.setCreatorId(o.getString("creatorId"));
		ar.setDescription(o.getString("description"));
		ar.setEffect(o.getString("effect"));
		if (o.has("creatorType") && !o.isNull("creatorType")) {
			ar.setCreatorType(CreatorType.valueOf(o.getString("creatorType")));
		}
		ar.setFrom(o.getLong("from"));
		ar.setTo(o.getLong("to"));
		ar.setId(o.getString("id"));
		ar.setNote(o.getString("note"));
	}

	private static Position toPosition(JSONObject pObj) throws JSONException {
		Position p = new Position();
		p.setLat(pObj.getString("lat"));
		p.setLon(pObj.getString("lon"));
		p.setName(pObj.getString("name"));
		p.setStopCode(pObj.getString("stopCode"));
		if (pObj.has("stopId") && !pObj.isNull("stopId")) {
			JSONObject stopObj = pObj.getJSONObject("stopId");
			StopId id = new StopId();
			id.setAgencyId(stopObj.getString("agencyId"));
			id.setId(stopObj.getString("id"));
			p.setStopId(id);
		}
		return p;
	}

	/**
	 * Convert {@link RecurrentJourneyParameters} instance to JSON representation
	 * @param request
	 * @return
	 * @throws JSONException 
	 */
	public static String toJSON(RecurrentJourneyParameters r) throws JSONException {
		JSONObject o = toJSONObject(r);
		
		return o.toString();
	}

	private static JSONObject toJSONObject(RecurrentJourneyParameters r)
			throws JSONException {
		JSONObject o = new JSONObject();
		o.put("fromDate", r.getFromDate());
		o.put("interval", r.getInterval());
		
		o.put("resultsNumber", r.getResultsNumber());
		o.put("toDate", r.getToDate());
		o.put("recurrence", r.getRecurrence());
		if (r.getRouteType() != null) {
			o.put("routeType", r.getRouteType().toString());
		}
		o.put("time", r.getTime());
		o.put("from", new JSONObject(r.getFrom()));
		o.put("to", new JSONObject(r.getTo()));
		if (r.getTransportTypes() != null) {
			List<String> ttypes = new ArrayList<String>(r.getTransportTypes().length);
			for (TType t : r.getTransportTypes()) {
				ttypes.add(t.toString());
			}
			o.put("transportTypes", ttypes);
		}
		return o;
	}

	/**
	 * Convert JSON string to {@link RecurrentJourney} instance
	 * @return
	 * @throws JSONException 
	 */
	public static RecurrentJourney toRecurrentJourney(String json) throws JSONException {
		RecurrentJourney rj = new RecurrentJourney();
		JSONObject o = new JSONObject(json);
		
		JSONArray legsArr = o.isNull("legs") ? null : o.getJSONArray("legs");
		rj.setLegs(new ArrayList<SimpleLeg>());
		if (legsArr != null) {
			for (int j = 0; j < legsArr.length(); j++) {
				JSONObject lObj = legsArr.getJSONObject(j);
				SimpleLeg leg = new SimpleLeg();
				leg.setFrom(lObj.getString("from"));
				leg.setTo(lObj.getString("to"));
				if (!lObj.isNull("transport")) {
					leg.setTransport(toTransport(lObj.getJSONObject("transport")));
				}
				rj.getLegs().add(leg);
			}
		}

		if (!o.isNull("monitorLegs")) {
			JSONObject monitorLegs = o.getJSONObject("monitorLegs");
			JSONArray keys = monitorLegs.names();
			Map<String,Boolean> map = new HashMap<String, Boolean>();
			if (keys != null) {
				for (int i = 0; i < keys.length(); i++) {
					String key = keys.getString(i);
					map.put(key, monitorLegs.getBoolean(key));
				}
			}
			rj.setMonitorLegs(map);
		} 
		JSONObject paramObj = o.getJSONObject("parameters");
		
		RecurrentJourneyParameters parameters = new RecurrentJourneyParameters();
		parameters.setFrom(toPosition(paramObj.getJSONObject("from")));
		parameters.setFromDate(paramObj.getLong("fromDate"));
		parameters.setInterval(paramObj.getLong("interval"));
		if (!paramObj.isNull("recurrence")) {
			JSONArray arr = paramObj.getJSONArray("recurrence");
			parameters.setRecurrence(new ArrayList<Integer>(arr.length()));
			for (int i = 0; i < arr.length(); i++) {
				parameters.getRecurrence().add(arr.getInt(i));
			}
		}
		parameters.setResultsNumber(paramObj.getInt("resultsNumber"));
		if (!paramObj.isNull("routeType")) {
			parameters.setRouteType(RType.valueOf(paramObj.getString("routeType")));
		}
		parameters.setTime(paramObj.getString("time"));
		parameters.setTo(toPosition(paramObj.getJSONObject("to")));
		if (!paramObj.isNull("toDate")) parameters.setToDate(paramObj.getLong("toDate"));
		if (!paramObj.isNull("transportTypes")) {
			JSONArray arr = paramObj.getJSONArray("transportTypes");
			TType[] types = new TType[arr.length()];
			for (int i = 0; i < types.length; i++) {
				types[i] = TType.valueOf(arr.getString(i));
			} 
			parameters.setTransportTypes(types);
		}
		rj.setParameters(parameters);
		
		return rj;
	}

	/**
	 * @param alert
	 * @return
	 * @throws JSONException 
	 */
	public static String toJSON(Alert a) throws JSONException {
		JSONObject o = toJSONObject(a);
		return o.toString();
	}

	private static JSONObject toJSONObject(Alert a) throws JSONException {
		JSONObject o = new JSONObject();
		if (a.getEntity() != null) {
			o.put("entity", new JSONObject(a.getEntity()));
		}
		o.put("from", a.getFrom());
		o.put("to", a.getTo());
		o.put("creatorId", a.getCreatorId());
		if (a.getCreatorType() != null) {
			o.put("creatorType", a.getCreatorType().toString());
		}
		o.put("description", a.getDescription());
		o.put("effect", a.getEffect());
		o.put("id", a.getId());
		o.put("note", a.getNote());
		if (a.getType() != null) {
			o.put("type", a.getType().toString());
		}
		if (a instanceof AlertDelay) {
			o.put("delay", ((AlertDelay) a).getDelay());
			if (((AlertDelay) a).getPosition() != null) {
				o.put("position", new JSONObject(((AlertDelay) a).getPosition()));
			}
			if (((AlertDelay) a).getTransport() != null) {
				o.put("transport", toJSONObject(((AlertDelay) a).getTransport()));
			}
			o.put("type", AlertType.DELAY.toString());
		}
		if (a instanceof AlertParking) {
			o.put("NoOfvehicles", ((AlertParking) a).getNoOfvehicles());
			o.put("placesAvailable", ((AlertParking) a).getPlacesAvailable());
			o.put("place", new JSONObject(((AlertParking) a).getPlace()));
			o.put("type", AlertType.PARKING.toString());
		}
		if (a instanceof AlertStrike) {
			if (((AlertStrike) a).getTransport() != null) {
				o.put("transport", toJSONObject(((AlertStrike) a).getTransport()));
			}
			o.put("stop", new JSONObject(((AlertStrike) a).getStop()));
			o.put("type", AlertType.STRIKE.toString());
		}
		if (a instanceof AlertRoad) {
			o.put("agencyId", ((AlertRoad) a).getAgencyId());
			if (((AlertRoad) a).getChangeTypes() != null) {
				List<String> types = new ArrayList<String>();
				for (AlertRoadType t : ((AlertRoad) a).getChangeTypes()) {
					types.add(t.toString());
				}
				JSONArray arr = new JSONArray(types);
				o.put("changeTypes", arr);
			}
			if (((AlertRoad) a).getRoad() != null) {
				o.put("road", new JSONObject(((AlertRoad) a).getRoad()));
			}
			o.put("type", AlertType.ROAD.toString());
		}
		if (a instanceof AlertAccident) {
			if (((AlertAccident) a).getPosition() != null) {
				o.put("position", new JSONObject(((AlertAccident) a).getPosition()));
			}
			o.put("severity", ((AlertAccident) a).getSeverity());
			o.put("type", AlertType.ACCIDENT.toString());
		}
		return o;
	}

	/**
	 * @param transport
	 * @return
	 * @throws JSONException 
	 */
	private static JSONObject toJSONObject(Transport t) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("agencyId", t.getAgencyId());
		o.put("routeId", t.getRouteId());
		o.put("routeShortName", t.getRouteShortName());
		o.put("tripId", t.getTripId());
		o.put("type", t.getType().toString());
		return o;
	}

	/**
	 * @param input
	 * @return
	 * @throws JSONException 
	 */
	public static String toJSON(BasicItinerary i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("clientId", i.getClientId());
		o.put("name", i.getName());
		o.put("originalFrom", new JSONObject(i.getOriginalFrom()));
		o.put("originalTo", new JSONObject(i.getOriginalTo()));
		o.put("monitor", i.isMonitor());
		// assume data is empty
		o.put("data", toJSONObject(i.getData()));
		return o.toString();
	}

	/**
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	private static JSONObject toJSONObject(Itinerary i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("duration", i.getDuration());
		o.put("endtime", i.getEndtime());
		o.put("startime", i.getStartime());
		o.put("walkingDuration", i.getWalkingDuration());
		if (i.getFrom() != null)  {
			o.put("from", new JSONObject(i.getFrom()));
		}
		if (i.getTo() != null) {
			o.put("to", new JSONObject(i.getTo()));
		}
		if (i.getLeg() != null) {
			List<JSONObject> legs = new ArrayList<JSONObject>();
			for (Leg leg : i.getLeg())
				legs.add(toJSONObject(leg));
			o.put("leg", new JSONArray(legs));
		}
		return o;
	}

	/**
	 * @param leg
	 * @return
	 * @throws JSONException 
	 */
	private static JSONObject toJSONObject(Leg i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("duration", i.getDuration());
		o.put("endtime", i.getEndtime());
		o.put("startime", i.getStartime());
		if (i.getAlertDelayList() != null) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (AlertDelay ad : i.getAlertDelayList()) list.add(toJSONObject(ad));
			o.put("alertDelayList", new JSONArray(list));
		} 
		if (i.getAlertParkingList() != null) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (AlertParking ap : i.getAlertParkingList()) list.add(toJSONObject(ap));
			o.put("alertParkingList", new JSONArray(list));
		} 
		if (i.getAlertStrikeList() != null) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (AlertStrike ad : i.getAlertStrikeList()) list.add(toJSONObject(ad));
			o.put("alertStrikeList", new JSONArray(list));
		} 
		o.put("from", new JSONObject(i.getFrom()));
		o.put("to", new JSONObject(i.getTo()));
		o.put("legId", i.getLegId());
		o.put("legGeometery", new JSONObject(i.getLegGeometery()));
		o.put("transport", toJSONObject(i.getTransport()));
		return o;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static BasicItinerary toBasicItinerary(String json) throws JSONException {
		BasicItinerary i = new BasicItinerary();
		JSONObject iObj = new JSONObject(json);
		i.setClientId(iObj.getString("clientId"));
		i.setData(toItinerary(iObj.getJSONObject("data")));
		i.setMonitor(iObj.getBoolean("monitor"));
		i.setName(iObj.getString("name"));
		i.setOriginalFrom(toPosition(iObj.getJSONObject("originalFrom")));
		i.setOriginalTo(toPosition(iObj.getJSONObject("originalTo")));
		return i;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<BasicItinerary> toBasicItineraryList(String json) throws JSONException {
		JSONArray arr = new JSONArray(json);
		List<BasicItinerary> list = new ArrayList<BasicItinerary>();
		for (int i = 0; i < arr.length(); i++) {
			list.add(toBasicItinerary(arr.getJSONObject(i).toString()));
		}
		return list;
	}

	/**
	 * @param input
	 * @return
	 * @throws JSONException 
	 */
	public static String toJSON(BasicRecurrentJourney i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("clientId", i.getClientId());
		o.put("name", i.getName());
		o.put("data", toJSONObject(i.getData()));
		o.put("monitor", i.isMonitor());
		return o.toString();
	}

	/**
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	private static JSONObject toJSONObject(RecurrentJourney i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("parameters", toJSONObject(i.getParameters()));
		o.put("monitorLegs", new JSONObject(i.getMonitorLegs()));
		List<JSONObject> legs = new ArrayList<JSONObject>();
		for (SimpleLeg leg : i.getLegs()) {
			legs.add(toJSONObject(leg));
		}
		o.put("legs", new JSONArray(legs));
		return o;
	}

	/**
	 * @param leg
	 * @return
	 * @throws JSONException 
	 */
	private static JSONObject toJSONObject(SimpleLeg i) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("from", i.getFrom());
		o.put("to", i.getTo());
		o.put("transport", toJSONObject(i.getTransport()));
		return o;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static BasicRecurrentJourney toBasicRecurrentJourney(String json) throws JSONException {
		BasicRecurrentJourney o = new BasicRecurrentJourney();
		JSONObject iObj = new JSONObject(json);
		o.setClientId(iObj.getString("clientId"));
		o.setData(toRecurrentJourney(iObj.getJSONObject("data").toString()));
		o.setMonitor(iObj.getBoolean("monitor"));
		o.setName(iObj.getString("name"));
		return o;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<BasicRecurrentJourney> toBasicRecurrentJourneyList(String json) throws JSONException {
		JSONArray arr = new JSONArray(json);
		List<BasicRecurrentJourney> list = new ArrayList<BasicRecurrentJourney>();
		for (int i = 0; i < arr.length(); i++) {
			list.add(toBasicRecurrentJourney(arr.getJSONObject(i).toString()));
		}
		return list;
	}

}
