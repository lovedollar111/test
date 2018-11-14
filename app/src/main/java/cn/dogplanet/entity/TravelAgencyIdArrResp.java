package cn.dogplanet.entity;

/**
 * Created by zhangtianrui on 17/4/1.
 */
public class TravelAgencyIdArrResp extends Resp {

    private TravelAgencyIdArr travelAgencyIdArr;

    public TravelAgencyIdArr getTravelAgencyIdArr() {
        return travelAgencyIdArr;
    }

    public void setTravelAgencyIdArr(TravelAgencyIdArr travelAgencyIdArr) {
        this.travelAgencyIdArr = travelAgencyIdArr;
    }


    public class TravelAgencyIdArr{
        private String travel_agency_id;
        private String travel_agency_name;

        public String getTravel_agency_id() {
            return travel_agency_id;
        }

        public void setTravel_agency_id(String travel_agency_id) {
            this.travel_agency_id = travel_agency_id;
        }

        public String getTravel_agency_name() {
            return travel_agency_name;
        }

        public void setTravel_agency_name(String travel_agency_name) {
            this.travel_agency_name = travel_agency_name;
        }
    }
}
