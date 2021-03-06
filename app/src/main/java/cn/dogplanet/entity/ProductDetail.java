package cn.dogplanet.entity;

import java.util.List;

/**
 * 
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:ProductDetail.java
 * date:2016-12-6
 */
public class ProductDetail {
	private Integer pro_id;
	private String product_id;//自订单id
	private String name;
	private List<String> feature;
	private List<String> images;
	private String pro_describe;
	private String create_time;
	private Integer price;
	private Integer package_id;//打包产品id
	private Integer subCategory;
	private Integer category;
	private Integer base_price;//底价
	private Integer most;//最多购买数量
	private Integer least;//最少购买数量
	private boolean hasIdCard;//是否需要身份证
	private String expert_id;
	private String poi_id;//景区id

	private String tel;
	// limit_date 参数，如果为true需要提前一天购买
	private Boolean limit_date;

	private String date;
	private Boolean recommend;

	// "money_explain": "", //费用说明
	// "instructions": "", //产品说明
	// "directions": "", //使用方法
	// "returns": "", //退改规则

	// 主产品使用
	private String instructions;
	private String money_explain;
	// 子产品使用
	private String introduction;
	private String directions;
	private String returns;
	private int market_price;//门市价

	private String buy_status;//门票是否售罄
	private boolean time_status;//门票是否可购买购买


	private int time;//门票购买间隔时间


	private String url;

	private boolean no_ticket;//是否需要门票 true表示不需要购买门票，false表示需要先购买门票

    private canBuyNum canBuyNum;
    private canBuy canBuy;

    private boolean travel_agency_status; //true 此旅行社状态可以下单 false 不能下单
    private String travel_agency_message;
    private boolean authentication_status;  //true 可以下单 false 不能下单
    private String authentication_message;
    private boolean can_join_cart;
    private boolean is_package_ticket; // true 是门船票 false 不是门船票

    public boolean getTravel_agency_status() {
        return travel_agency_status;
    }

    public void setTravel_agency_status(boolean travel_agency_status) {
        this.travel_agency_status = travel_agency_status;
    }

    public String getTravel_agency_message() {
        return travel_agency_message;
    }

    public void setTravel_agency_message(String travel_agency_message) {
        this.travel_agency_message = travel_agency_message;
    }

    public boolean getAuthentication_status() {
        return authentication_status;
    }

    public void setAuthentication_status(boolean authentication_status) {
        this.authentication_status = authentication_status;
    }

    public String getAuthentication_message() {
        return authentication_message;
    }

    public void setAuthentication_message(String authentication_message) {
        this.authentication_message = authentication_message;
    }


	public String getBuy_status() {
		return buy_status;
	}

	public void setBuy_status(String buy_status) {
		this.buy_status = buy_status;
	}

	public boolean getTime_status() {
		return time_status;
	}

	public void setTime_status(boolean time_status) {
		this.time_status = time_status;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}


	public String getExpert_id() {
		return expert_id;
	}

	public void setExpert_id(String expert_id) {
		this.expert_id = expert_id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}

	public boolean isHasIdCard() {
		return hasIdCard;
	}

	public void setHasIdCard(boolean hasIdCard) {
		this.hasIdCard = hasIdCard;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public Integer getPro_id() {
		return pro_id;
	}

	public void setPro_id(Integer pro_id) {
		this.pro_id = pro_id;
	}

	public Integer getMost() {
		return most;
	}

	public void setMost(Integer most) {
		this.most = most;
	}

	public Integer getLeast() {
		return least;
	}

	public void setLeast(Integer least) {
		this.least = least;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFeature() {
		return feature;
	}

	public void setFeature(List<String> feature) {
		this.feature = feature;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getPro_describe() {
		return pro_describe;
	}

	public void setPro_describe(String pro_describe) {
		this.pro_describe = pro_describe;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getBase_price() {
		return base_price;
	}

	public void setBase_price(Integer base_price) {
		this.base_price = base_price;
	}

	public Integer getPackage_id() {
		return package_id;
	}

	public void setPackage_id(Integer package_id) {
		this.package_id = package_id;
	}

	public Integer getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Integer subCategory) {
		this.subCategory = subCategory;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getMoney_explain() {
		return money_explain;
	}

	public void setMoney_explain(String money_explain) {
		this.money_explain = money_explain;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getLimit_date() {
		return limit_date;
	}

	public void setLimit_date(Boolean limit_date) {
		this.limit_date = limit_date;
	}

	public int getMarket_price() {
		return market_price;
	}

	public void setMarket_price(int market_price) {
		this.market_price = market_price;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public boolean isNo_ticket() {
		return no_ticket;
	}

	public void setNo_ticket(boolean no_ticket) {
		this.no_ticket = no_ticket;
	}

    public boolean getCan_join_cart() {
        return can_join_cart;
    }

    public void setCan_join_cart(boolean can_join_cart) {
        this.can_join_cart = can_join_cart;
    }

    public ProductDetail.canBuy getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(ProductDetail.canBuy canBuy) {
        this.canBuy = canBuy;
    }

    public ProductDetail.canBuyNum getCanBuyNum() {
        return canBuyNum;
    }

    public void setCanBuyNum(ProductDetail.canBuyNum canBuyNum) {
        this.canBuyNum = canBuyNum;
    }

	public boolean isIs_package_ticket() {
		return is_package_ticket;
	}

	public void setIs_package_ticket(boolean is_package_ticket) {
		this.is_package_ticket = is_package_ticket;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}


	public class canBuy{
        private boolean status;
        private int time;
        private String msg;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public class canBuyNum{

        private int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}