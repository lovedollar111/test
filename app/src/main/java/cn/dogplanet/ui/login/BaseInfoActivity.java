package cn.dogplanet.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.ComUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.IdcardUtils;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.TravelAgencyIdArrResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.user.CompanyListActivity;

public class BaseInfoActivity extends BaseActivity {

    private static final int DRIVING_LICENSE = 1;
    private static final int VEHICLE_LICENSE = 2;
    private static final int OPERATIONAL_LICENSE = 3;

    @BindView(R.id.et_name)
    EditTextWithDel et_name;
    @BindView(R.id.et_id_card)
    EditTextWithDel et_id_card;
    @BindView(R.id.et_company)
    EditText et_company;
    @BindView(R.id.et_driving_licence_time)
    EditText et_driving_licence_time;
    @BindView(R.id.et_vehicle_license_time)
    EditText et_vehicle_license_time;
    @BindView(R.id.et_operational_qualification_time)
    EditText et_operational_qualification_time;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.img_id_card)
    ImageView img_id_card;
    @BindView(R.id.img_company)
    ImageView img_company;
    @BindView(R.id.img_driving_licence_time)
    ImageView img_driving_licence_time;
    @BindView(R.id.img_vehicle_license_time)
    ImageView img_vehicle_license_time;
    @BindView(R.id.img_operational_qualification_time)
    ImageView img_operational_qualification_time;

    private String company_id,company_name;
    private Expert expert;
    private boolean isLongTime = false;
    private String driving_licence_time, vehicle_license_time, operational_qualification_time;
    private long mExitTime;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), BaseInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        initView();
        getCompanyByInvite();

    }

    private void getCompanyByInvite() {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.GET_INVITE_TRAVEL_AGENCY_ID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TravelAgencyIdArrResp resp = GsonHelper.parseObject(response, TravelAgencyIdArrResp.class);
                    if (resp != null && resp.isSuccess()) {
                        company_id = resp.getTravelAgencyIdArr().getTravel_agency_id();
                        et_company.setText(resp.getTravelAgencyIdArr().getTravel_agency_name());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ToastUtil.showError(R.string.network_error);
                }
            }, params);
        }
    }

    private void initView() {
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        KeyBoardUtils.openKeybord(et_name,this);
        et_id_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_driving_licence_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_vehicle_license_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_operational_qualification_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_next, R.id.lay_company, R.id.lay_driving_licence_time, R.id.et_driving_licence_time,R.id.lay_vehicle_license_time, R.id.et_vehicle_license_time,R.id.lay_operational_qualification_time,R.id.et_operational_qualification_time})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (checkInput()) {
                    saveData();
                }
                break;
            case R.id.lay_company:
                startActivityForResult(CompanyListActivity.newIntent(company_id), CompanyListActivity.COMPANY_LIST_CODE);
                break;
            case R.id.lay_driving_licence_time:
            case R.id.et_driving_licence_time:
                showTimeDialog(DRIVING_LICENSE);
                break;
            case R.id.lay_vehicle_license_time:
            case R.id.et_vehicle_license_time:
                showTimeDialog(VEHICLE_LICENSE);
                break;
            case R.id.lay_operational_qualification_time:
            case R.id.et_operational_qualification_time:
                showTimeDialog(OPERATIONAL_LICENSE);
                break;
        }
    }

    private boolean checkInput() {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(et_name.getText().toString());
        if (StringUtils.isBlank(et_name.getText().toString()) || ComUtils.compileExChar(et_name.getText().toString())) {
            ToastUtil.showError(R.string.err_name);
            return false;
        }
        if (StringUtils.isBlank(et_id_card.getText().toString())) {
            ToastUtil.showError(R.string.err_id_card_null);
            return false;
        }
        if (!IdcardUtils.validateCard(et_id_card.getText().toString())) {
            ToastUtil.showError(R.string.err_id_card);
            return false;
        }
        if (StringUtils.isBlank(company_id)) {
            ToastUtil.showError(R.string.err_company);
            return false;
        }
        if (StringUtils.isBlank(et_driving_licence_time.getText().toString())) {
            ToastUtil.showError(R.string.err_driving_licence_time);
            return false;
        }
        if (StringUtils.isBlank(et_vehicle_license_time.getText().toString())) {
            ToastUtil.showError(R.string.err_vehicle_license_time);
            return false;
        }
        if (StringUtils.isBlank(et_operational_qualification_time.getText().toString())) {
            ToastUtil.showError(R.string.err_operational_qualification_time);
            return false;
        }
        return true;
    }

    private void saveData() {
        Expert expert = new Expert();
        expert.setExpert_name(et_name.getText().toString());
        expert.setExpert_id_card(et_id_card.getText().toString());
        expert.setTravel_agency_id(company_id);

        Expert.CardPhoto driver_license = new Expert.CardPhoto();
        if (isLongTime) {
            driver_license.setDate("长期");
        } else {
            driver_license.setDate(driving_licence_time);
        }

        expert.setDriver_license(driver_license);

        Expert.CardPhoto vehicle_license = new Expert.CardPhoto();
        vehicle_license.setDate(vehicle_license_time);
        expert.setDriver_license(vehicle_license);

        Expert.CardPhoto operational_qualification = new Expert.CardPhoto();
        operational_qualification.setDate(operational_qualification_time);
        expert.setDriver_license(operational_qualification);

        EventBus.getDefault().postSticky(expert);
        startActivity(CardInfoActivity.newIntent());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CompanyListActivity.COMPANY_LIST_CODE) {
            if (StringUtils.isNotBlank(data.getStringExtra(CompanyListActivity.COMPANY_ID))) {
                company_id=data.getStringExtra(CompanyListActivity.COMPANY_ID);
                company_name=data.getStringExtra(CompanyListActivity.COMPANY_NAME);
                et_company.setText(company_name);
            }
        }
    }

    private void updateBtn() {
        if (StringUtils.isNotBlank(et_name.getText().toString()) && StringUtils.isNotBlank(et_id_card.getText().toString()) && StringUtils.isNotBlank(et_company.getText().toString()) && StringUtils.isNotBlank(et_driving_licence_time.getText().toString()) && StringUtils.isNotBlank(et_vehicle_license_time.getText().toString()) && StringUtils.isNotBlank(et_operational_qualification_time.getText().toString())) {
            btn_next.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btn_next.setBackgroundResource(R.drawable.gradient_c7_ab);
        }
    }

    private void showTimeDialog(final int i) {
        final NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
        builder.withEffect(Effectstype.Fadein);
        View view = LayoutInflater.from(this).inflate(R.layout.date_picker_dialog, null);
        builder.setCustomViewWithoutClose(view, this);
        LinearLayout lay_ck = view.findViewById(R.id.lay_chk);
        view.findViewById(R.id.lay_main).setEnabled(false);
        if (i == DRIVING_LICENSE) {
            lay_ck.setVisibility(View.VISIBLE);
        } else {
            lay_ck.setVisibility(View.GONE);
        }
        final CheckBox checkBox = view.findViewById(R.id.ck_checkbox);
        if (isLongTime) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        lay_ck.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                isLongTime = false;
            } else {
                checkBox.setChecked(true);
                isLongTime = true;
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> isLongTime = isChecked);

        final DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        builder.isCancelableOnTouchOutside(false);
        int y = new Date().getYear();
        int m = new Date().getMonth();
        int d = new Date().getDate();
        switch (i) {
            case DRIVING_LICENSE:
                if (StringUtils.isNotBlank(driving_licence_time) && !driving_licence_time.equals("长期")) {
                    y = Integer.parseInt(driving_licence_time.substring(0, 4));
                    m = Integer.parseInt(driving_licence_time.substring(5, 7)) - 1;
                    d = Integer.parseInt(driving_licence_time.substring(8, 10));
                }
                break;
            case VEHICLE_LICENSE:
                if (StringUtils.isNotBlank(vehicle_license_time)) {
                    y = Integer.parseInt(vehicle_license_time.substring(0, 4));
                    m = Integer.parseInt(vehicle_license_time.substring(5, 7)) - 1;
                    d = Integer.parseInt(vehicle_license_time.substring(8, 10));
                }
                break;
            case OPERATIONAL_LICENSE:
                if (StringUtils.isNotBlank(operational_qualification_time)) {
                    y = Integer.parseInt(operational_qualification_time.substring(0, 4));
                    m = Integer.parseInt(operational_qualification_time.substring(5, 7)) - 1;
                    d = Integer.parseInt(operational_qualification_time.substring(8, 10));
                }
                break;
        }
        datePicker.init(y, m, d, (view1, year, monthOfYear, dayOfMonth) -> {
            if (checkBox.isChecked() && i == DRIVING_LICENSE) {
                isLongTime = false;
                checkBox.setChecked(false);
            }
        });

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y = datePicker.getYear();
                int m = datePicker.getMonth() + 1;
                int d = datePicker.getDayOfMonth();
                String time;
                if (m < 10) {
                    if (d < 10) {
                        time = y + "-0" + m + "-0" + d;
                    } else {
                        time = y + "-0" + m + "-" + d;
                    }
                } else {
                    if (d < 10) {
                        time = y + "-" + m + "-0" + d;
                    } else {
                        time = y + "-" + m + "-" + d;
                    }
                }
                switch (i) {
                    case DRIVING_LICENSE:
                        driving_licence_time = time;
                        if (isLongTime) {
                            et_driving_licence_time.setText("长期");
                        } else {
                            et_driving_licence_time.setText(time);
                        }
                        break;
                    case VEHICLE_LICENSE:
                        vehicle_license_time = time;
                        et_vehicle_license_time.setText(time);
                        break;
                    case OPERATIONAL_LICENSE:
                        operational_qualification_time = time;
                        et_operational_qualification_time.setText(time);
                        break;
                }
                builder.cancel();
                updateBtn();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> builder.cancel());
        builder.show();
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

}
