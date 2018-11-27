package cn.dogplanet.net;

/**
 * 请求返回数据
 *
 * @author sh
 */
public class RespData {
    // 成功
    public final static Integer CODE_SUCCESS = 0;
    // 失败
    public final static Integer CODE_FAIL = 1001;

    // 验证失败
    public final static Integer CODE_VALIDA_FAIL = 1010;

    public final static Integer CODE_REGISTERED_SUCCESS = 1007;

    //微信登陆未绑定账号
    public final static Integer CODE_UN_REG = 1009;


    // 状态码
    private Integer code;
    // 消息
    private String msg;
    // 数据
    private Object expert;
    private Integer id;

    private String result;

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

    public Object getExpert() {
        return expert;
    }

    public void setExpert(Object expert) {
        this.expert = expert;
    }

    public boolean isSuccess() {
        return this.code.intValue() == CODE_SUCCESS;
    }

    public boolean isUnReg() {
        return this.code.intValue() == CODE_UN_REG;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // 验证失败
    public boolean isValida() {
        return this.code.intValue() == CODE_VALIDA_FAIL;
    }

    //已注册
    public boolean isRegistered() {
        return this.code.intValue() == CODE_REGISTERED_SUCCESS;
    }

    @Override
    public String toString() {
        return "RespData [code=" + code + ", msg=" + msg + ", expert=" + expert
                + ", id=" + id + ", result=" + result + "]";
    }


}
