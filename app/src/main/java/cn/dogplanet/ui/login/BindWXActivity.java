package cn.dogplanet.ui.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.MainActivity;
import cn.dogplanet.R;
import cn.dogplanet.app.util.ComUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.app.widget.GraphicCodeDialog;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class BindWXActivity extends BaseActivity implements PlatformActionListener, Handler.Callback{

    private static final int MSG_ACTION_CALLBACK = 0;
    private final int TYPE_LOGIN_WX_SUCCESS = 1;
    private final int TYPE_LOGIN_WX_FALSE = 2;
    private final int TYPE_LOGIN_WX_CANCEL = 3;

    @BindView(R.id.et_phone)
    EditTextWithDel et_phone;
    @BindView(R.id.et_verification)
    EditTextWithDel et_verification;
    @BindView(R.id.tv_send_verification)
    TextView tv_send_verification;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.img_verification)
    ImageView img_verification;
    @BindView(R.id.tv_disclaimer)
    TextView tv_disclaimer;

    private GraphicCodeDialog graphicCodeDialog;
    private Expert expert;
    private String open_id;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), BindWXActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wx);
        expert=WCache.getCacheExpert();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_disclaimer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_disclaimer.getPaint().setAntiAlias(true);//抗锯齿

        et_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {
                    img_verification.setImageResource(R.drawable.ic_verification_select);
                } else {
                    img_verification.setImageResource(R.drawable.ic_verification_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void updateButton() {
        if (StringUtils.isNotBlank(et_phone.getText().toString()) && StringUtils.isNotBlank(et_verification.getText().toString())) {
            btn_ok.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btn_ok.setBackgroundResource(R.drawable.gradient_c7_ab);
        }
    }

    @OnClick({R.id.btn_ok, R.id.tv_directions, R.id.tv_send_verification, R.id.btn_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (checkInput()) {
                    bindWx();
                }
                break;
            case R.id.tv_send_verification:
                KeyBoardUtils.closeKeybord(et_phone, this);
                if (checkPhone()) {
                    graphicCodeDialog = new GraphicCodeDialog(this, et_phone.getText().toString(),
                            (type, code) -> {
                                if (type == GraphicCodeDialog.TYPE_OK) {
                                    getVerifyCode(code);
                                }
                            });
                    graphicCodeDialog.show();
                }
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_directions:
                startActivity(ParagraphActivity.newIntent());
                break;
        }
    }

    private void bindWx() {
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);//授权并获取用户信息
    }

    private void getVerifyCode(String code) {
        if (checkPhone()) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_account", et_phone.getText().toString());
            params.put("captcha_code", code);
//            showProgress();
            PublicReq.request(HttpUrl.EXPERT_SEND_VERIFY_CODE,
                    response -> {
//                            hideProgress();
                        RespData respData = GsonHelper.parseObject(
                                response, RespData.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                // 开始倒计时
                                graphicCodeDialog.dismiss();
                                setDownTimerStart();
                                KeyBoardUtils.openKeybord(et_verification, BindWXActivity.this);
                            } else {
                                graphicCodeDialog.refresh();
                                ToastUtil.showError(R.string.network_error);
                            }
                        }
                    }, error -> {
                        hideProgress();
                        ToastUtil.showError(R.string.network_error);
                    }, params);
        }
    }  private boolean checkPhone() {
        // 必须手机号码
        String phone = et_phone.getText().toString();
        if (!ComUtils.isMobileNo(phone)) {
            ToastUtil.showError(R.string.tip_phone);
            return false;
        }
        return true;
    }

    private boolean checkInput() {
        if (!checkPhone()) {
            return false;
        }
        // 验证码4位
        String vCode = et_verification.getText().toString();
        // 密码不少6位
        if (StringUtils.isBlank(vCode) || vCode.length() != 4) {
            ToastUtil.showError(R.string.tip_verify_code);
            return false;
        }
        return true;
    }

    private void setDownTimerStart() {
        TimeCount mTime = new TimeCount(60000, 1000);
        mTime.start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case TYPE_LOGIN_WX_SUCCESS: { // 成功
                ToastUtil.showMes("登陆成功");
                //获取用户资料
                Platform platform = (Platform) msg.obj;
                //获取用户账号
                open_id = platform.getDb().getUserId();
                loginWithWX();
            }
            break;
            case TYPE_LOGIN_WX_FALSE: { // 失败
                ToastUtil.showError("登陆失败");
            }
            break;
            case TYPE_LOGIN_WX_CANCEL: { // 取消
                ToastUtil.showMes("登陆取消");
            }
            break;
        }
        return false;
    }

    private void loginWithWX() {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", open_id);
            params.put("access_token", open_id);
            params.put("open_id", open_id);
            showProgress();
            PublicReq.request(HttpUrl.CHECK_OPEN_ID,
                    response -> {
                        hideProgress();
                        RespData respData = GsonHelper.parseObject(response,
                                RespData.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                Expert expert = GsonHelper.parseObject(
                                        GsonHelper.toJson(respData.getExpert()),
                                        Expert.class);
                                if (null != expert) {
                                    // 登录成功 缓存数据 并跳转到主界面
                                    SPUtils.put(WConstant.EXPERT_DATA,
                                            GsonHelper.toJson(expert));
                                    ToastUtil.showMes("绑定成功");
                                    startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));
                                    finish();
                                } else {
                                    ToastUtil.showError(R.string.network_error);
                                }
                            } else {
                                ToastUtil.showError(respData.getMsg());
                            }
                        }
                    }, error -> {
                        hideProgress();
                        ToastUtil.showError(R.string.network_error);
                    }, params);
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_SUCCESS;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
        ToastUtil.showMesLong(hashMap.toString());
    }

    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_FALSE;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_CANCEL;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    class TimeCount extends CountDownTimer {

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_send_verification.setText(R.string.reset_verify_code);
            tv_send_verification.setTextColor(getResources().getColor(R.color.login_color));
            tv_send_verification.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_send_verification.setTextColor(getResources().getColor(R.color.color_c7));
            tv_send_verification.setClickable(false);
            tv_send_verification.setText(String.format("%ds", millisUntilFinished / 1000));
        }
    }
}
