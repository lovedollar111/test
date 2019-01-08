package cn.dogplanet.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.app.widget.GraphicCodeDialog;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.ui.login.BaseInfoActivity;

public class SafeActivity extends BaseActivity {

    @BindView(R.id.img_phone)
    ImageView imgPhone;
    @BindView(R.id.et_phone)
    EditTextWithDel etPhone;
    @BindView(R.id.img_verification)
    ImageView imgVerification;
    @BindView(R.id.tv_send_verification)
    TextView tvSendVerification;
    @BindView(R.id.et_verification)
    EditTextWithDel etVerification;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.et_password)
    EditTextWithDel etPassword;
    @BindView(R.id.img_again_password)
    ImageView imgAgainPassword;
    @BindView(R.id.et_again_password)
    EditTextWithDel etAgainPassword;
    @BindView(R.id.lay_again_password)
    RelativeLayout layAgainPassword;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.img_see_again_password)
    ImageView imgSeeAgainPassword;
    @BindView(R.id.img_see_password)
    ImageView imgSeePassword;

    private GraphicCodeDialog graphicCodeDialog;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), SafeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        etVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {
                    imgVerification.setImageResource(R.drawable.ic_verification_select);
                } else {
                    imgVerification.setImageResource(R.drawable.ic_verification_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {

                    imgPassword.setImageResource(R.drawable.ic_password_select);
                } else {
                    imgPassword.setImageResource(R.drawable.ic_password_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAgainPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
                if (StringUtils.isNotBlank(s.toString())) {
                    imgAgainPassword.setImageResource(R.drawable.ic_password_again_select);
                } else {
                    imgAgainPassword.setImageResource(R.drawable.ic_password_again_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_back, R.id.tv_send_verification, R.id.btn_ok, R.id.img_password, R.id.img_see_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_send_verification:
                KeyBoardUtils.closeKeybord(etPhone, this);
                if (checkPhone()) {
                    graphicCodeDialog = new GraphicCodeDialog(this, etPhone.getText().toString(),
                            (type, code) -> {
                                if (type == GraphicCodeDialog.TYPE_OK) {
                                    getVerifyCode(code);
                                }
                            });
                    graphicCodeDialog.show();
                }
                break;
            case R.id.btn_ok:
                if (checkInput()) {
                    forgetPassword();
                }
                break;
            case R.id.img_password:
                if (etPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (etPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.img_see_password:
                if (etAgainPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    etAgainPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (etAgainPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    etAgainPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    private void getVerifyCode(String code) {
        if (checkPhone()) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_account", etPhone.getText().toString());
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
                                    KeyBoardUtils.openKeybord(etVerification, SafeActivity.this);
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


    private boolean checkInput() {
        if (!checkPhone()) {
            return false;
        }
        // 验证码4位
        String vCode = etVerification.getText().toString();
        // 密码不少6位
        String pass = etPassword.getText().toString();
        String again_pass = etAgainPassword.getText().toString();
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

    private boolean checkPhone() {
        // 必须手机号码
        String phone = etPhone.getText().toString();
        if (!ComUtils.isMobileNo(phone)) {
            ToastUtil.showError(R.string.tip_phone);
            return false;
        }
        return true;
    }

    private void forgetPassword() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_account", etPhone.getText().toString());
        params.put("expert_password", etPassword.getText().toString());
        params.put("confirm_password", etAgainPassword.getText().toString());
        params.put("verification_code", etVerification.getText().toString());
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
                                ToastUtil.showMes("修改成功");
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


    private void updateButton() {
        if (StringUtils.isNotBlank(etPassword.getText().toString()) && StringUtils.isNotBlank(etVerification.getText().toString()) && StringUtils.isNotBlank(etPhone.getText().toString()) && StringUtils.isNotBlank(etAgainPassword.getText().toString())) {
            btnOk.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btnOk.setBackgroundResource(R.drawable.gradient_c7_ab);
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
            tvSendVerification.setText(R.string.reset_verify_code);
            tvSendVerification.setTextColor(getResources().getColor(R.color.login_color));
            tvSendVerification.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvSendVerification.setTextColor(getResources().getColor(R.color.color_c7));
            tvSendVerification.setClickable(false);
            tvSendVerification.setText(String.format("%ds", millisUntilFinished / 1000));
        }
    }
}
