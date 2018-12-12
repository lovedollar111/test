package cn.dogplanet.entity;

/**
 * 订单红点数量、购物车红点数量
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:RedDotNumResp.java
 * date:2016-12-6
 */
public class RedDotNumResp extends Resp {

	private RedNum num;

	public RedNum getNum() {
		return num;
	}

	public void setNum(RedNum num) {
		this.num = num;
	}

	public class RedNum {
		private String cart;
		private String order;

		public String getCart() {
			return cart;
		}

		public void setCart(String cart) {
			this.cart = cart;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

	}

}
