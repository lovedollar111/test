package cn.dogplanet.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.MainActivity;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
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
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;

public class EditInfoActivity extends BaseActivity {


    private static final int DRIVING_LICENSE = 1;
    private static final int VEHICLE_LICENSE = 2;
    private static final int OPERATIONAL_LICENSE = 3;

    @BindView(R.id.et_name)
    EditTextWithDel etName;
    @BindView(R.id.et_id_card)
    EditTextWithDel etIdCard;
    @BindView(R.id.et_driving_licence_time)
    EditText etDrivingLicenceTime;
    @BindView(R.id.et_vehicle_license_time)
    EditText etVehicleLicenseTime;
    @BindView(R.id.et_operational_qualification_time)
    EditText etOperationalQualificationTime;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Expert expert;
    private boolean isLongTime = false;
    private String driving_licence_time, vehicle_license_time, operational_qualification_time;

    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),EditInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        initView();
    }

    private void initView() {
        etOperationalQualificationTime.setText(expert.getOperational_qualification().getDate());
        etVehicleLicenseTime.setText(expert.getVehicle_license().getDate());
        etDrivingLicenceTime.setText(expert.getDriver_license().getDate());
        etName.setText(expert.getExpert_name());
        etIdCard.setText(expert.getExpert_id_card());
        etName.addTextChangedListener(new TextWatcher() {
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
        etIdCard.addTextChangedListener(new TextWatcher() {
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
    }


    @OnClick({R.id.btn_back, R.id.btn_next, R.id.lay_driving_licence_time, R.id.lay_vehicle_license_time, R.id.lay_operational_qualification_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                saveData();
                break;
            case R.id.lay_driving_licence_time:
                showTimeDialog(DRIVING_LICENSE);
                break;
            case R.id.lay_vehicle_license_time:
                showTimeDialog(VEHICLE_LICENSE);
                break;
            case R.id.lay_operational_qualification_time:
                showTimeDialog(OPERATIONAL_LICENSE);
                break;
        }
    }

    private void showTimeDialog(final int i) {
        final NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
        builder.withEffect(Effectstype.Fadein);
        View view = LayoutInflater.from(this).inflate(R.layout.date_picker_dialog, null);
        builder.setCustomView(view, this);
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

        view.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            int y1 = datePicker.getYear();
            int m1 = datePicker.getMonth() + 1;
            int d1 = datePicker.getDayOfMonth();
            String time;
            if (m1 < 10) {
                if (d1 < 10) {
                    time = y1 + "-0" + m1 + "-0" + d1;
                } else {
                    time = y1 + "-0" + m1 + "-" + d1;
                }
            } else {
                if (d1 < 10) {
                    time = y1 + "-" + m1 + "-0" + d1;
                } else {
                    time = y1 + "-" + m1 + "-" + d1;
                }
            }
            switch (i) {
                case DRIVING_LICENSE:
                    driving_licence_time = time;
                    if (isLongTime) {
                        etDrivingLicenceTime.setText("长期");
                    } else {
                        etDrivingLicenceTime.setText(time);
                    }
                    break;
                case VEHICLE_LICENSE:
                    vehicle_license_time = time;
                    etVehicleLicenseTime.setText(time);
                    break;
                case OPERATIONAL_LICENSE:
                    operational_qualification_time = time;
                    etOperationalQualificationTime.setText(time);
                    break;
            }
            builder.cancel();
            updateBtn();
        });
        builder.show();
        updateBtn();
    }


    private void saveData() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("type", WConstant.TYPE_DRIVER);
        params.put("expert_name", etName.getText().toString());
        params.put("expert_id_card", etIdCard.getText().toString());
        params.put("travel_agency_id", expert.getTravel_agency_id());
        params.put("driver_license", expert.getDriver_license().getId());
        params.put("driver_license_date", etDrivingLicenceTime.getText().toString());
        params.put("vehicle_license", expert.getVehicle_license().getId());
        params.put("vehicle_license_date", etVehicleLicenseTime.getText().toString());
        params.put("operational_qualification", expert.getOperational_qualification().getId());
        params.put("operational_qualification_date", etOperationalQualificationTime.getText().toString());
        params.put("id_card_images[0]", expert.getId_card_photo().get(0).getId());
        params.put("id_card_images[1]", expert.getId_card_photo().get(1).getId());
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


    private void updateBtn() {
        if (StringUtils.isNotBlank(etName.getText().toString()) && StringUtils.isNotBlank(etIdCard.getText().toString()) && StringUtils.isNotBlank(etDrivingLicenceTime.getText().toString()) && StringUtils.isNotBlank(etOperationalQualificationTime.getText().toString()) && StringUtils.isNotBlank(etVehicleLicenseTime.getText().toString())) {
            btnNext.setBackgroundResource(R.drawable.gradient_f1_e0);
            btnNext.setEnabled(true);
        } else {
            btnNext.setBackgroundResource(R.drawable.gradient_c7_ab);
            btnNext.setEnabled(false);
        }
    }

}
