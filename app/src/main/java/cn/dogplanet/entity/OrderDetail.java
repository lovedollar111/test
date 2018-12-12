package cn.dogplanet.entity;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.dogplanet.app.util.StringUtils;

/**
 * 订单详情
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:OrderDetail.java
 * date:2016-12-6
 */
public class OrderDetail {

	public final static String ORDER_TYPE_ALL = "0";
	public final static String ORDER_TYPE_WAIT = "10";
	public final static String ORDER_TYPE_CLOSE = "30";
	public final static String ORDER_TYPE_SUCCESS = "40";
	public final static String ORDER_TYPE_BACK_MONEY = "50";
	public final static String ORDER_TYPE_BACK_ALL_MONEY = "60";

	// category 10：达人商品 20：达人服务
	private String id;
	private String user_id;
	private String order_num;
	private String price;
	private String status;
	private String contact_name;
	private String contact_tel;
	private String remark;
	private String create_time;
	private List<OrderProduct> products;
	private OrderService service;
	private String contact_id_card;

	
	public String getContact_id_card() {
		return contact_id_card;
	}

	public void setContact_id_card(String contact_id_card) {
		this.contact_id_card = contact_id_card;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public List<OrderProduct> getProducts() {
		return products;
	}

	public void setProducts(List<OrderProduct> products) {
		this.products = products;
	}

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}

	public class OrderProduct {
		private String id;
		private String pro_id;
		private String num;
		private String price;
		private String begin_date;
		private String finish_date;
		private String status;
		private String create_time;
		private String pro_name;
		private String thumb;
		private int refund_money;
		private int refund_number;
		private boolean show_refund;
		private String check_number;
		private String category;
		private boolean is_package_ticket;

		public String getCheck_number() {
			return check_number;
		}

		public void setCheck_number(String check_number) {
			this.check_number = check_number;
		}

		public boolean isShow_refund() {
			return show_refund;
		}

		public void setShow_refund(boolean show_refund) {
			this.show_refund = show_refund;
		}

		public int getRefund_number() {
			return refund_number;
		}

		public void setRefund_number(int refund_number) {
			this.refund_number = refund_number;
		}

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public int getRefund_money() {
			return refund_money;
		}

		public void setRefund_money(int refund_money) {
			this.refund_money = refund_money;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPro_id() {
			return pro_id;
		}

		public void setPro_id(String pro_id) {
			this.pro_id = pro_id;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getBegin_date() {
			return begin_date;
		}

		public void setBegin_date(String begin_date) {
			this.begin_date = begin_date;
		}

		public String getFinish_date() {
			return finish_date;
		}

		public void setFinish_date(String finish_date) {
			this.finish_date = finish_date;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public String getPro_name() {
			return pro_name;
		}

		public void setPro_name(String pro_name) {
			this.pro_name = pro_name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public boolean isIs_package_ticket() {
			return is_package_ticket;
		}

		public void setIs_package_ticket(boolean is_package_ticket) {
			this.is_package_ticket = is_package_ticket;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public class OrderService {
		private String name;
		private String persons;
		private Integer price;
		private String status;
		private List<OrderServiceDate> date;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPersons() {
			return persons;
		}

		public void setPersons(String persons) {
			this.persons = persons;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public List<OrderServiceDate> getDate() {
			return date;
		}

		public void setDate(List<OrderServiceDate> date) {
			this.date = date;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String toServiceDate() {
			String serviceDate = "";
			try {
				if (null == this.date || this.date.isEmpty()) {
					return serviceDate;
				}
				SimpleDateFormat enF = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat cnF = new SimpleDateFormat("yyyy年MM月dd日");
				Map<String, List<OrderServiceDate>> map = new TreeMap<String, List<OrderServiceDate>>();
				for (OrderServiceDate d : this.date) {
					String day = cnF.format(enF.parse(d.getDate()));
					if (map.containsKey(day)) {
						map.get(day).add(d);

					} else {
						List<OrderServiceDate> t = new ArrayList<OrderServiceDate>();
						t.add(d);
						map.put(day, t);
					}
				}
				List<String> list = new ArrayList<String>();
				for (String key : map.keySet()) {
					StringBuffer buffer = new StringBuffer();
					buffer.append(key + " ");
					List<OrderServiceDate> ds = map.get(key);
					for (OrderServiceDate od : ds) {
						if (null != od.getCategory()) {
							if (od.getCategory() == 10) {
								buffer.append("上午 ");
							} else if (od.getCategory() == 20) {
								buffer.append("下午 ");
							} else if (od.getCategory() == 30) {
								buffer.append("晚上 ");
							}
						}
					}
					list.add(buffer.toString().trim());
				}
				serviceDate = StringUtils.join(list.toArray(), ";");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return serviceDate;
		}
	}

	public class OrderServiceDate {
		private String date;
		private Integer category;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public Integer getCategory() {
			return category;
		}

		public void setCategory(Integer category) {
			this.category = category;
		}

	}

}
