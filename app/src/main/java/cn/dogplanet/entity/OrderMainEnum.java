package cn.dogplanet.entity;

/**
 * 主订单状态
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:OrderMainEnum.java
 * date:2016-12-6
 */
public enum OrderMainEnum {

	OS_1("10", "待付款", "#FFBE29"), OS_2("20", "已付款", "#F96468"), OS_3("50", "退款中", "#49D688"),OS_4("60", "已退款", "#FFB8B8"),OS_6("40",
			"已成功", "#66CDD7"), OS_7("500", "已关闭", "#D8D8D8");
	private String staus;
	private String text;
	private String color;

	OrderMainEnum(String staus, String text, String color) {
		this.staus = staus;
		this.text = text;
		this.color = color;
	}

	public String getStaus() {
		return staus;
	}

	public void setStaus(String staus) {
		this.staus = staus;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public static OrderMainEnum getOrderStausBySCode(String staus) {
		for (OrderMainEnum e : OrderMainEnum.values()) {
			if (e.getStaus().equals(staus)) {
				return e;
			}
		}
		return null;
	}
}
