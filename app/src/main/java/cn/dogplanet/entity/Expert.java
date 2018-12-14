package cn.dogplanet.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家个人信息
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:Expert.java
 * date:2016-12-6
 */
public class Expert {
    // check_expert 审核达人 0：未提交 ，10：审核中 ，20：合格，30：不合格(弃用)
    // check_guide 审核导游 0：未提交 ，10：审核中 ， 20：合格，30：不合格（弃用）

    //达人审核状态：check_status 1:未提交 0:待审核 10:审核中 20:审核成功 30:审核失败
    //高级认证审核状态：authentication_status 1:未进行认证高级认证 0:待审核 10:审核中 20:审核成功 30:审核失败

    private String check_status;
    private String authentication_status;
    private String authentication_category;

    public static final String CHECK_0 = "0";
    public static final String CHECK_1 = "1";
    public static final String CHECK_10 = "10";
    public static final String CHECK_20 = "20";
    public static final String CHECK_30 = "30";

    public static final String AUTHENTICATION_0 = "0";
    public static final String AUTHENTICATION_1 = "1";
    public static final String AUTHENTICATION_10 = "10";
    public static final String AUTHENTICATION_20 = "20";
    public static final String AUTHENTICATION_30 = "30";

    public static final String CATEGORY_WORKERS = "40";
    public static final String CATEGORY_BUSINESS = "30";
    public static final String CATEGORY_DRIVER = "20";

    public static final String CHANGE_STATUS = "30";

    private Integer id;
    private String expert_account;
    private String expert_password;
    private String access_token;
    // 名称
    private String expert_name;
    // 头像
    private String expert_icon;
    private String expert_icon_id;
    // 封面
    private String expert_cover;
    private List<CardPhoto> id_card_photo;
    private Integer expert_category;

    private String expert_tel;
    private String expert_email;
    private String expert_id_card;
    private String expert_introduce;
    private String expert_images;
    private String create_time;
    private String age;

    private String check_expert;
    private String check_guide;

    private String guide_number;
    private String travel_agency_id;
    private String travel_agency_name;
    private String expert_gender;

    private String most_person;

    //国省市区id
    private String country_id;
    private String province_id;
    private String city_id;
    private String district_id;


    private ArrayList<String> expert_specialty;
    private String expert_stars;
    private String price;
    private String guide_photo;

    private String shop_url;

    private String job;

    // 虚拟账户是否设置过支付密码
    private boolean payment_password;

    //30 原公司拒绝审核
    private int change_status;

    //手机区号id
    private String area_id;

    //达人选择地区
    private String switch_area_id;

    //达人证件类型
    private String id_card_type;

    private String open_id;

    private Expert.CardPhoto driver_license;
    private Expert.CardPhoto vehicle_license;
    private Expert.CardPhoto operational_qualification;

    public CardPhoto getDriver_license() {
        return driver_license;
    }

    public void setDriver_license(CardPhoto driver_license) {
        this.driver_license = driver_license;
    }

    public CardPhoto getVehicle_license() {
        return vehicle_license;
    }

    public void setVehicle_license(CardPhoto vehicle_license) {
        this.vehicle_license = vehicle_license;
    }

    public CardPhoto getOperational_qualification() {
        return operational_qualification;
    }

    public void setOperational_qualification(CardPhoto operational_qualification) {
        this.operational_qualification = operational_qualification;
    }

    public boolean getPassord() {
        return payment_password;
    }

    public void setPassord(boolean payment_password) {
        this.payment_password = payment_password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpert_account() {
        return expert_account;
    }

    public void setExpert_account(String expert_account) {
        this.expert_account = expert_account;
    }

    public String getExpert_password() {
        return expert_password;
    }

    public void setExpert_password(String expert_password) {
        this.expert_password = expert_password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpert_name() {
        return expert_name;
    }

    public void setExpert_name(String expert_name) {
        this.expert_name = expert_name;
    }

    public String getExpert_icon() {
        return expert_icon;
    }

    public void setExpert_icon(String expert_icon) {
        this.expert_icon = expert_icon;
    }

    public String getExpert_cover() {
        return expert_cover;
    }

    public void setExpert_cover(String expert_cover) {
        this.expert_cover = expert_cover;
    }

    public List<CardPhoto> getId_card_photo() {
        return id_card_photo;
    }

    public void setId_card_photo(List<CardPhoto> id_card_photo) {
        this.id_card_photo = id_card_photo;
    }

    public Integer getExpert_category() {
        return expert_category;
    }

    public void setExpert_category(Integer expert_category) {
        this.expert_category = expert_category;
    }

    public String getExpert_tel() {
        return expert_tel;
    }

    public void setExpert_tel(String expert_tel) {
        this.expert_tel = expert_tel;
    }

    public String getExpert_email() {
        return expert_email;
    }

    public void setExpert_email(String expert_email) {
        this.expert_email = expert_email;
    }

    public String getExpert_id_card() {
        return expert_id_card;
    }

    public void setExpert_id_card(String expert_id_card) {
        this.expert_id_card = expert_id_card;
    }

    public String getExpert_introduce() {
        return expert_introduce;
    }

    public void setExpert_introduce(String expert_introduce) {
        this.expert_introduce = expert_introduce;
    }

    public String getExpert_images() {
        return expert_images;
    }

    public void setExpert_images(String expert_images) {
        this.expert_images = expert_images;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCheck_expert() {
        return check_expert;
    }

    public void setCheck_expert(String check_expert) {
        this.check_expert = check_expert;
    }

    public String getCheck_guide() {
        return check_guide;
    }

    public void setCheck_guide(String check_guide) {
        this.check_guide = check_guide;
    }

    public String getGuide_number() {
        return guide_number;
    }

    public void setGuide_number(String guide_number) {
        this.guide_number = guide_number;
    }

    public String getTravel_agency_id() {
        return travel_agency_id;
    }

    public void setTravel_agency_id(String travel_agency_id) {
        this.travel_agency_id = travel_agency_id;
    }

    public String getExpert_gender() {
        return expert_gender;
    }

    public void setExpert_gender(String expert_gender) {
        this.expert_gender = expert_gender;
    }

    public String getMost_person() {
        return most_person;
    }

    public void setMost_person(String most_person) {
        this.most_person = most_person;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public ArrayList<String> getExpert_specialty() {
        return expert_specialty;
    }

    public void setExpert_specialty(ArrayList<String> expert_specialty) {
        this.expert_specialty = expert_specialty;
    }

    public String getExpert_stars() {
        return expert_stars;
    }

    public void setExpert_stars(String expert_stars) {
        this.expert_stars = expert_stars;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGuide_photo() {
        return guide_photo;
    }

    public void setGuide_photo(String guide_photo) {
        this.guide_photo = guide_photo;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getExpert_icon_id() {
        return expert_icon_id;
    }

    public void setExpert_icon_id(String expert_icon_id) {
        this.expert_icon_id = expert_icon_id;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public String getAuthentication_status() {
        return authentication_status;
    }

    public void setAuthentication_status(String authentication_status) {
        this.authentication_status = authentication_status;
    }

    public String getAuthentication_category() {
        return authentication_category;
    }

    public void setAuthentication_category(String authentication_category) {
        this.authentication_category = authentication_category;
    }

    public int getChange_status() {
        return change_status;
    }

    public void setChange_status(int change_status) {
        this.change_status = change_status;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getSwitch_area_id() {
        return switch_area_id;
    }

    public void setSwitch_area_id(String switch_area_id) {
        this.switch_area_id = switch_area_id;
    }

    public String getId_card_type() {
        return id_card_type;
    }

    public void setId_card_type(String id_card_type) {
        this.id_card_type = id_card_type;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public void setTravel_agency_name(String travel_agency_name) {
        this.travel_agency_name = travel_agency_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public static class CardPhoto {
        private String id;
        private String url;
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "CardPhoto{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Expert{" +
                ", access_token='" + access_token + '\'' +
                ", expert_name='" + expert_name + '\'' +
                ", id_card_photo=" + id_card_photo +
                ", expert_id_card='" + expert_id_card + '\'' +
                ", expert_introduce='" + expert_introduce + '\'' +
                ", travel_agency_id='" + travel_agency_id + '\'' +
                ", travel_agency_name='" + travel_agency_name + '\'' +
                ", driver_license=" + driver_license +
                ", vehicle_license=" + vehicle_license +
                ", operational_qualification=" + operational_qualification +
                '}';
    }

    // // 擅长
    // public List<String> getSpecialty() {
    // List<String> specialtys = new ArrayList<String>();
    // if (null != expert_specialty
    // && StringUtil.isNotBlank(expert_specialty.toString())) {
    // Type type = new TypeToken<List<String>>() {
    // }.getType();
    // specialtys = GsonHelper.parseObject(expert_specialty.toString(),
    // type);
    // }
    // if (null == specialtys) {
    // specialtys = new ArrayList<String>();
    // }
    // return specialtys;
    // }
    //
    // // 导游证
    // public List<String> getGuides() {
    // List<String> guides = new ArrayList<String>();
    // if (null != guide_photo
    // && StringUtil.isNotBlank(guide_photo.toString())) {
    // Type type = new TypeToken<List<String>>() {
    // }.getType();
    // guides = GsonHelper.parseObject(guide_photo.toString(), type);
    // }
    // if (null == guides) {
    // guides = new ArrayList<String>();
    // }
    // return guides;
    // }
    //
    // // 身份证
    // public List<String> getIdCars() {
    // List<String> idCards = new ArrayList<String>();
    // if (null != id_card_photo
    // && StringUtil.isNotBlank(id_card_photo.toString())) {
    // Type type = new TypeToken<List<String>>() {
    // }.getType();
    // idCards = GsonHelper.parseObject(id_card_photo.toString(), type);
    // }
    // if (null == idCards) {
    // idCards = new ArrayList<String>();
    // }
    // return idCards;
    // }
}
