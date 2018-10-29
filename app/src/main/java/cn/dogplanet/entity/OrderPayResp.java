package cn.dogplanet.entity;

/**
 * 订单
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:OrderPayResp.java
 * date:2016-12-6
 */
public class OrderPayResp extends Resp {
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
