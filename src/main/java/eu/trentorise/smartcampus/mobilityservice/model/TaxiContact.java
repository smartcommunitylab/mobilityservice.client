/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
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
 * @author raman
 *
 */
public class TaxiContact {

	private String id;
		private String name;
		private String agencyId;
		private List<String> phone;
		private List<String> sms;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAgencyId() {
			return agencyId;
		}

		public void setAgencyId(String agencyId) {
			this.agencyId = agencyId;
		}

		public List<String> getPhone() {
			return phone;
		}

		public void setPhone(List<String> phone) {
			this.phone = phone;
		}

		public List<String> getSms() {
			return sms;
		}

		public void setSms(List<String> sms) {
			this.sms = sms;
		}

	}