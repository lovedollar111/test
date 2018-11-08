package cn.dogplanet.ui.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.MainActivity;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
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
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;

public class LoginActivity extends BaseActivity {


    private final int TYPE_LOGIN_WITH_VERIFICATION = 1;
    private final int TYPE_LOGIN_WITH_PASSWORD = 2;


    @BindView(R.id.et_phone)
    EditTextWithDel et_phone;

    @BindView(R.id.et_verification)
    EditTextWithDel et_verification;

    @BindView(R.id.et_password)
    EditTextWithDel et_password;

    @BindView(R.id.btn_get_verification)
    ImageButton btn_get_verification;

    @BindView(R.id.btn_log)
    ImageButton btn_log;

    @BindView(R.id.tv_log_with_password)
    TextView tv_log_with_password;

    @BindView(R.id.tv_log_with_wx)
    TextView tv_log_with_wx;

    @BindView(R.id.img_log_with_password)
    ImageView img_log_with_password;

    @BindView(R.id.img_log_with_wx)
    ImageView img_log_with_wx;

    @BindView(R.id.tv_disclaimer)
    TextView tv_disclaimer;

    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;

    @BindView(R.id.view_line)
    View view_line;

    @BindView(R.id.tv_time)
    TextView tv_time;


    private GraphicCodeDialog graphicCodeDialog;

    private int type = TYPE_LOGIN_WITH_VERIFICATION;//判断当前是验证码登陆还是密码登陆
    private long mExitTime;
    private boolean isSendVerification=false;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_disclaimer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_disclaimer.getPaint().setAntiAlias(true);//抗锯齿
        btn_log.setTag(TYPE_LOGIN_WITH_PASSWORD);
    }


    @OnClick({R.id.tv_disclaimer, R.id.btn_get_verification, R.id.btn_log, R.id.tv_log_with_password, R.id.tv_log_with_wx, R.id.img_log_with_password, R.id.img_log_with_wx,R.id.tv_forget_password})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_disclaimer:
                startActivity(ParagraphActivity.newIntent());
                break;
            case R.id.btn_get_verification:
//                KeyBoardUtils.closeKeybord(et_phone, this);
//                if (checkPhone()) {
//                    graphicCodeDialog = new GraphicCodeDialog(this, et_phone.getText().toString(),
//                            (type, code) -> {
//                                if (type == GraphicCodeDialog.TYPE_OK) {
//                                    getVerifyCode(code);
//                                }
//                            });
//                    graphicCodeDialog.show();
//                    hintView(TYPE_LOGIN_WITH_PASSWORD);
//                }

                startActivity(FirstActivity.newIntent());
                Expert expert=new Expert();
                expert.setExpert_account(et_password.getText().toString());
                EventBus.getDefault().postSticky(expert);
                break;
            case R.id.btn_log:
                if (checkInput((int) btn_log.getTag())) {
                    switch ((int) btn_log.getTag()) {
                        case TYPE_LOGIN_WITH_PASSWORD:
                            loginWithPassword();
                            break;
                        case TYPE_LOGIN_WITH_VERIFICATION:
                            loginWithVerification();
                            break;
                    }
                }
                break;
            case R.id.tv_log_with_password:
            case R.id.img_log_with_password:
                if (type == TYPE_LOGIN_WITH_PASSWORD) {
                    hintView(TYPE_LOGIN_WITH_PASSWORD);
                } else if (type == TYPE_LOGIN_WITH_VERIFICATION) {
                    hintView(TYPE_LOGIN_WITH_VERIFICATION);
                }
                break;
            case R.id.tv_log_with_wx:
            case R.id.img_log_with_wx:
                break;
            case R.id.tv_forget_password:
                startActivity(ForgetPasswordActivity.newIntent());
                break;

        }
    }

    private void hintView(int type) {
        et_verification.setVisibility(View.INVISIBLE);
        et_password.setVisibility(View.INVISIBLE);
        tv_forget_password.setVisibility(View.INVISIBLE);
        btn_log.setVisibility(View.INVISIBLE);
        btn_log.setTag(null);
        view_line.setVisibility(View.INVISIBLE);
        tv_time.setVisibility(View.INVISIBLE);
        btn_get_verification.setVisibility(View.INVISIBLE);
        switch (type) {
            case TYPE_LOGIN_WITH_VERIFICATION:
                this.type = TYPE_LOGIN_WITH_PASSWORD;
                et_password.setVisibility(View.VISIBLE);
                btn_log.setVisibility(View.VISIBLE);
                view_line.setVisibility(View.VISIBLE);
                btn_get_verification.setVisibility(View.INVISIBLE);
                tv_log_with_password.setText(R.string.log_with_verification);
                btn_log.setTag(TYPE_LOGIN_WITH_PASSWORD);
                tv_forget_password.setVisibility(View.VISIBLE);
                break;
            case TYPE_LOGIN_WITH_PASSWORD:
                this.type = TYPE_LOGIN_WITH_VERIFICATION;
                if(isSendVerification){
                    et_verification.setVisibility(View.VISIBLE);
                    btn_log.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    btn_get_verification.setVisibility(View.INVISIBLE);
                    tv_time.setVisibility(View.VISIBLE);
                }else{
                    et_verification.setVisibility(View.INVISIBLE);
                    btn_log.setVisibility(View.INVISIBLE);
                    view_line.setVisibility(View.INVISIBLE);
                    btn_get_verification.setVisibility(View.VISIBLE);
                    btn_get_verification.setVisibility(View.VISIBLE);
                }
                tv_log_with_password.setText(R.string.log_with_password);
                btn_log.setTag(TYPE_LOGIN_WITH_VERIFICATION);
                tv_forget_password.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean checkPhone() {
        // 必须手机号码
        String phone = et_phone.getText().toString();
        if (!ComUtils.isMobileNo(phone)) {
            ToastUtil.showError(R.string.tip_phone);
            return false;
        }
        return true;
    }

    /**
     * 检测输入合法性
     */
    private boolean checkInput(int type) {
        if (!checkPhone()) {
            return false;
        }
        if (type == TYPE_LOGIN_WITH_VERIFICATION) {
            // 验证码4位
            String vCode = et_verification.getText().toString();
            if (StringUtils.isBlank(vCode) || vCode.length() != 4) {
                ToastUtil.showError(R.string.tip_verify_code);
                return false;
            }
        } else if (type == TYPE_LOGIN_WITH_PASSWORD) {
            // 密码不少6位
            String pass = et_password.getText().toString();

            if (StringUtils.isBlank(pass) || pass.length() < 6) {
                ToastUtil.showError(R.string.tip_pass);
                return false;
            }
        }
        return true;
    }

    private void loginWithPassword() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_account", et_phone.getText().toString());
        params.put("expert_password", et_password.getText().toString());
        showProgress();
        PublicReq.request(HttpUrl.EXPERT_LOGIN,
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
                                KeyBoardUtils.closeKeybord(et_phone, LoginActivity.this);
                                SPUtils.put(WConstant.EXPERT_DATA,
                                        GsonHelper.toJson(expert));
                                if (StringUtils.isNotBlank(expert.getExpert_name())) {
                                    startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));
                                } else {
                                    startActivity(BaseInfoActivity.newIntent());
                                }
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

    private void loginWithVerification() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_account", et_phone.getText().toString());
        params.put("verification_code", et_verification.getText().toString());
        showProgress();
        PublicReq.request(HttpUrl.CHECK_EXPERT_PHONE,
                response -> {
                    hideProgress();
                    RespData respData = GsonHelper.parseObject(response,
                            RespData.class);
                    if (null != respData) {
                        if (respData.isRegistered()) {
                            Expert expert = GsonHelper.parseObject(
                                    GsonHelper.toJson(respData.getExpert()),
                                    Expert.class);
                            if (null != expert) {
                                // 登录成功 缓存数据 并跳转到主界面
                                KeyBoardUtils.closeKeybord(et_phone, LoginActivity.this);
                                SPUtils.put(WConstant.EXPERT_DATA,
                                        GsonHelper.toJson(expert));
                                if (StringUtils.isNotBlank(expert.getExpert_name())) {
                                    startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));
                                } else {
                                    startActivity(BaseInfoActivity.newIntent());
                                }
                            } else {
                                ToastUtil.showError(R.string.network_error);
                            }
                        } else if (respData.isSuccess()) {
                            startActivity(FirstActivity.newIntent());
                            Expert expert=new Expert();
                            expert.setExpert_account(et_password.getText().toString());
                            EventBus.getDefault().postSticky(expert);
                        } else {
                            ToastUtil.showError(respData.getMsg());
                        }
                    }
                }, error -> {
                    hideProgress();
                    ToastUtil.showError(R.string.network_error);
                }, params);
    }

    // 获取验证码
    private void getVerifyCode(String code) {
        if (checkPhone()) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_account", et_phone.getText().toString());
            params.put("captcha_code", code);
//            showProgress();
            PublicReq.request(HttpUrl.EXPERT_SEND_VERIFY_CODE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            hideProgress();
                            RespData respData = GsonHelper.parseObject(
                                    response, RespData.class);
                            if (null != respData) {
                                if (respData.isSuccess()) {
                                    // 开始倒计时
                                    graphicCodeDialog.dismiss();
                                    setDownTimerStart();
                                    KeyBoardUtils.closeKeybord(et_password,LoginActivity.this);
                                    isSendVerification=true;
                                    hintView(TYPE_LOGIN_WITH_PASSWORD);
                                } else if (1007 == respData.getCode()) {
                                    startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));
                                } else {
                                    graphicCodeDialog.refresh();
                                    ToastUtil.showError(R.string.network_error);
                                }
                            }
                        }
                    }, error -> {
                        hideProgress();
                        ToastUtil.showError(R.string.network_error);
                    }, params);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                AndroidUtil.showToast(R.string.app_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(GlobalContext.getInstance());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setDownTimerStart() {
        tv_time.setVisibility(View.VISIBLE);
        btn_get_verification.setVisibility(View.INVISIBLE);
        TimeCount mTime = new TimeCount(60000, 1000);
        mTime.start();
    }

    class TimeCount extends CountDownTimer {

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_time.setText(R.string.reset_verify_code);
            tv_time.setTextColor(getResources().getColor(R.color.login_color));
            tv_time.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_time.setTextColor(getResources().getColor(R.color.color_c7));
            tv_time.setClickable(false);
            tv_time.setText(String.format("%ds", millisUntilFinished / 1000));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
