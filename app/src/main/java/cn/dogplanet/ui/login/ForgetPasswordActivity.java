package cn.dogplanet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditTextWithDel et_phone;
    @BindView(R.id.et_verification)
    EditTextWithDel et_verification;
    @BindView(R.id.et_password)
    EditTextWithDel et_password;
    @BindView(R.id.et_again_password)
    EditTextWithDel et_again_password;
    @BindView(R.id.tv_send_verification)
    TextView tv_send_verification;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.img_verification)
    ImageView img_verification;
    @BindView(R.id.img_password)
    ImageView img_password;
    @BindView(R.id.img_again_password)
    ImageView img_again_password;
    @BindView(R.id.img_see_password)
    ImageView img_see_password;
    @BindView(R.id.img_see_again_password)
    ImageView img_see_again_password;

    private GraphicCodeDialog graphicCodeDialog;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), ForgetPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_froget_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        et_again_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {
                    img_again_password.setImageResource(R.drawable.ic_password_again_select);
                } else {
                    img_again_password.setImageResource(R.drawable.ic_password_again_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {
                    img_password.setImageResource(R.drawable.ic_password_select);
                } else {
                    img_password.setImageResource(R.drawable.ic_password_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
    }

    private void updateButton() {
        if (StringUtils.isNotBlank(et_phone.getText().toString()) && StringUtils.isNotBlank(et_verification.getText().toString()) && StringUtils.isNotBlank(et_password.getText().toString()) && StringUtils.isNotBlank(et_again_password.getText().toString())) {
            btn_ok.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btn_ok.setBackgroundResource(R.drawable.gradient_c7_ab);
        }
    }


    @OnClick({R.id.btn_ok, R.id.img_see_password, R.id.img_see_again_password, R.id.tv_send_verification, R.id.btn_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (checkInput()) {
                    forgetPassword();
                }
                break;
            case R.id.img_see_password:
                if (et_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_see_password.setImageResource(R.drawable.ic_can_see);
                } else if (et_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_see_password.setImageResource(R.drawable.ic_cant_see);
                }
                break;
            case R.id.img_see_again_password:
                if (et_again_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    et_again_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_again_password.setImageResource(R.drawable.ic_can_see);
                } else if (et_again_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    et_again_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_again_password.setImageResource(R.drawable.ic_cant_see);
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
        }
    }

    private void forgetPassword() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_account", et_phone.getText().toString());
        params.put("expert_password", et_password.getText().toString());
        params.put("confirm_password", et_again_password.getText().toString());
        params.put("verification_code", et_verification.getText().toString());
        PublicReq.request(HttpUrl.UPDATE_PWD,
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
                                KeyBoardUtils.closeKeybord(et_phone, ForgetPasswordActivity.this);
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
                            startActivity(BaseInfoActivity.newIntent());
                        } else {
                            ToastUtil.showError(respData.getMsg());
                        }
                    }
                }, error -> {
                    hideProgress();
                    ToastUtil.showError(R.string.network_error);
                }, params);

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

    private boolean checkInput() {
        if (!checkPhone()) {
            return false;
        }
        // 验证码4位
        String vCode = et_verification.getText().toString();
        // 密码不少6位
        String pass = et_password.getText().toString();
        String again_pass = et_again_password.getText().toString();
        if (StringUtils.isBlank(vCode) || vCode.length() != 4) {
            ToastUtil.showError(R.string.tip_verify_code);
            return false;
        }
        if (StringUtils.isBlank(pass) || pass.length() < 6) {
            ToastUtil.showError(R.string.tip_pass);
            return false;
        }
        if (StringUtils.isBlank(again_pass) || again_pass.length() < 6) {
            ToastUtil.showError(R.string.tip_pass);
            return false;
        }
        if (!again_pass.equals(pass)) {
            ToastUtil.showError(R.string.tip_tow_pass_error);
            return false;
        }
        return true;
    }

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
                                    KeyBoardUtils.openKeybord(et_verification, ForgetPasswordActivity.this);
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


    private void setDownTimerStart() {
        TimeCount mTime = new TimeCount(60000, 1000);
        mTime.start();
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
