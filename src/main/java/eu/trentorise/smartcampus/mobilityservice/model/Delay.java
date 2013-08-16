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

import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of public transport delay. May contain multiple values (one for each source type).
 * @author raman
 *
 */
public class Delay {

	Map<CreatorType, String> values = new HashMap<CreatorType, String>(2);

	/**
	 * @return the values
	 */
	public Map<CreatorType, String> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<CreatorType, String> values) {
		this.values = values;
	}
}
