package cn.dogplanet.entity;

import java.util.List;

/**
 * 购物车数据
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:CartResp.java
 * date:2016-12-6
 */

public class CartResp extends Resp {

	private List<Cart> product;

	public List<Cart> getProduct() {
		return product;
	}

	public void setProduct(List<Cart> product) {
		this.product = product;
	}

	public class Cart {
		public static final String STATUS_NOR = "10";
		public static final String STATUS_INVALID = "30";
		private String id;
		private String product_id;
		private String category;
		private String begin_date;
		private String finish_date;
		private String num;
		private String price;
		private String thumb;
		// status 10 正常 30 失效
		private String status;
		private String name;
		private String primary_product_id;
		private boolean hasIdCard;
		private String poi_id;
		
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

		public String getPrimary_product_id() {
			return primary_product_id;
		}

		public void setPrimary_product_id(String primary_product_id) {
			this.primary_product_id = primary_product_id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}
}
