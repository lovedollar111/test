package cn.dogplanet.entity;

import java.util.List;

/**
 * 订单
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:Order.java
 * date:2016-12-6
 */
public class Order {

	// category 10：达人商品 20：达人服务
	public final static String CATE_MASTER_COMMDITY = "10";
	public final static String CATE_MASTER_SERVICE = "20";

	private String id;
	private String user_id;
	private String order_num;
	private String price;
	private String status;
	private String finishDate;
	private String travelAgencyName;
	private String category;
	private String create_time;

	private String name;
	private String detaill;
	private String pro_name;
	private String num;
	private String contact_tel;
	private String contact_name;

	private String sign;
	private String notify;

	private Boolean pay;
	private String is_read;
	
	private String begin_date;

	private List<OrderDetail.OrderProduct> orderProducts;

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}
	
	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetaill() {
		return detaill;
	}

	public void setDetaill(String detaill) {
		this.detaill = detaill;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Boolean getPay() {
		return pay;
	}

	public void setPay(Boolean pay) {
		this.pay = pay;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getTravelAgencyName() {
		return travelAgencyName;
	}

	public void setTravelAgencyName(String travelAgencyName) {
		this.travelAgencyName = travelAgencyName;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user_id=" + user_id + ", order_num="
				+ order_num + ", price=" + price + ", status=" + status
				+ ", finishDate=" + finishDate + ", travelAgencyName="
				+ travelAgencyName + ", category=" + category
				+ ", create_time=" + create_time + ", name=" + name
				+ ", detaill=" + detaill + ", contact_tel=" + contact_tel
				+ ", contact_name=" + contact_name + ", sign=" + sign
				+ ", notify=" + notify + ", pay=" + pay + ", is_read="
				+ is_read + ", begin_date=" + begin_date + "]";
	}

	public String getPro_name() {
		return pro_name;
	}

	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<OrderDetail.OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderDetail.OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}
}