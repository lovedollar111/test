package cn.dogplanet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;

public class FirstActivity extends BaseActivity {

    @BindView(R.id.et_password)
    EditTextWithDel et_password;
    @BindView(R.id.et_again_password)
    EditTextWithDel et_again_password;
    @BindView(R.id.et_invite_code)
    EditTextWithDel et_invite_code;
    @BindView(R.id.img_password)
    ImageView img_password;
    @BindView(R.id.img_again_password)
    ImageView img_again_password;
    @BindView(R.id.img_invite_code)
    ImageView img_invite_code;
    @BindView(R.id.img_see_password)
    ImageView img_see_password;
    @BindView(R.id.img_see_again_password)
    ImageView img_see_again_password;
    @BindView(R.id.btn_next)
    Button btn_next;

    private String phone;


    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), FirstActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void phoneEventBus(Expert expert) {
        phone = expert.getExpert_account();
    }


    private void initView() {
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

        et_invite_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isNotBlank(s.toString())) {
                    img_invite_code.setImageResource(R.drawable.ic_invite_select);
                } else {
                    img_invite_code.setImageResource(R.drawable.ic_invite_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateButton() {
        if (StringUtils.isNotBlank(et_password.getText().toString()) && StringUtils.isNotBlank(et_again_password.getText().toString())) {
            btn_next.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btn_next.setBackgroundResource(R.drawable.gradient_c7_ab);
        }
    }

    @OnClick({R.id.img_see_password, R.id.img_see_again_password, R.id.btn_next})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_see_password:
                if (et_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (et_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.img_see_again_password:
                if (et_again_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    et_again_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (et_again_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    et_again_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_next:
                if (checkInput()) {
                    reg();
                }
                break;
        }
    }


    private boolean checkInput() {
        String pass = et_password.getText().toString();
        String again_pass = et_again_password.getText().toString();
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

    private void reg() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_account", phone);
        params.put("expert_password", et_password.getText().toString());
        params.put("confirm_password", et_again_password.getText().toString());
        if (StringUtils.isNotBlank(et_invite_code.getText().toString())) {
            params.put("invite_code", et_invite_code.getText().toString());
        }
        params.put("source", "20");
        showProgress();
        PublicReq.request(HttpUrl.EXPERT_REG, response -> {
            hideProgress();
            RespData respData = GsonHelper.parseObject(response,
                    RespData.class);
            if (null != respData) {
                if (respData.isSuccess()) {
                    Expert expert = GsonHelper.parseObject(
                            GsonHelper.toJson(respData.getExpert()),
                            Expert.class);
                    if (null != expert) {
                        // 注册成功 缓存数据 并跳转到主界面
                        SPUtils.put(WConstant.EXPERT_DATA,
                                GsonHelper.toJson(expert));
                        // 跳转都完善个人信息界面
                        startActivity(BaseInfoActivity.newIntent());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
