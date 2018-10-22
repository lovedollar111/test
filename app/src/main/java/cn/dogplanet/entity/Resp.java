package cn.dogplanet.entity;

public class Resp {
	// 成功
	public final static Integer CODE_SUCCESS = 0;
	// 失败
	public final static Integer CODE_FAIL = 1001;

	// 验证失败
	public final static Integer CODE_VALIDA_FAIL = 1010;

	// 状态码
	private Integer code;
	// 消息
	private String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return this.code.intValue() == CODE_SUCCESS.intValue();
	}

	// 验证失败
	public boolean isValida() {
		return this.code.intValue() == CODE_VALIDA_FAIL.intValue();
	}
}
