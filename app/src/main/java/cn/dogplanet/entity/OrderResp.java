package cn.dogplanet.entity;
import java.util.List;


/**
 * 订单
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:OrderResp.java
 * date:2016-12-6
 */
public class OrderResp extends Resp {
	private List<Order> order;

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

}
