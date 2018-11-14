package cn.dogplanet.entity;

import java.util.List;

/**
 * 旅行社
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:Travel.java
 * date:2016-12-6
 */
public class TravelResp extends Resp{

	private List<Travel> recommendTravelAgency;
	private List<Travel> TravelAgency;

	public List<Travel> getRecommendTravelAgency() {
		return recommendTravelAgency;
	}

	public void setRecommendTravelAgency(List<Travel> recommendTravelAgency) {
		this.recommendTravelAgency = recommendTravelAgency;
	}

	public List<Travel> getTravelAgency() {
		return TravelAgency;
	}

	public void setTravelAgency(List<Travel> travelAgency) {
		TravelAgency = travelAgency;
	}
}
