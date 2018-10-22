package cn.dogplanet.entity;

import java.util.List;

/**
 * 省->市->区
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:Area.java
 * date:2016-12-6
 */
public class Area extends Resp {

	private List<Place> place;

	public List<Place> getPlace() {
		return place;
	}

	public void setPlace(List<Place> place) {
		this.place = place;
	}

	public class Place{
		private String id;
		private String name;
		private String parent_id;
		private List<Province> province;

		public String getArea_id() {
			return id;
		}

		public void setArea_id(String area_id) {
			this.id = area_id;
		}

		public String getArea_name() {
			return name;
		}

		public void setArea_name(String area_name) {
			this.name = area_name;
		}

		public List<Province> getProvinces() {
			return province;
		}

		public void setProvinces(List<Province> provinces) {
			this.province = provinces;
		}

		public String getParent_id() {
			return parent_id;
		}

		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}

		public class Province {
			private String id;
			private String name;
			private String parent_name;
			private List<City> city;

			public String getProvince_id() {
				return id;
			}

			public void setProvince_id(String province_id) {
				this.id = province_id;
			}

			public String getProvince_name() {
				return name;
			}

			public void setProvince_name(String province_name) {
				this.name = province_name;
			}

			public List<City> getCities() {
				return city;
			}

			public void setCities(List<City> cities) {
				this.city = cities;
			}

			public String getParent_name() {
				return parent_name;
			}

			public void setParent_name(String parent_name) {
				this.parent_name = parent_name;
			}
		}


		// 市
		public class City {
			private String id;
			private String name;
			private String parent_id;

			public String getCity_id() {
				return id;
			}

			public void setCity_id(String city_id) {
				this.id = city_id;
			}

			public String getCity_name() {
				return name;
			}

			public void setCity_name(String city_name) {
				this.name = city_name;
			}


			public String getParent_id() {
				return parent_id;
			}

			public void setParent_id(String parent_id) {
				this.parent_id = parent_id;
			}
		}
	}
}
