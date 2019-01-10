package cn.dogplanet.ui.user;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.BitmapUtils;
import cn.dogplanet.app.util.ComUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ImageUtil;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.util.UploadUtilsAsync;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.UploadImage;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.ui.login.LoginActivity;
import cn.dogplanet.ui.popup.CameraDialog;

public class AuthenticationInfoActivity extends BaseActivity {

    private static final int IMG_LEFT_CARD = 1;
    private static final int IMG_RIGHT_CARD = 2;
    private static final int IMG_DRIVER = 3;
    private static final int IMG_VEHICLE = 4;
    private static final int IMG_OPERATIONAL = 5;

    @BindView(R.id.et_name)
    TextView etName;
    @BindView(R.id.et_id_card)
    TextView etIdCard;
    @BindView(R.id.et_company)
    TextView etCompany;
    @BindView(R.id.lay_company)
    RelativeLayout layCompany;
    @BindView(R.id.et_driving_licence_time)
    TextView etDrivingLicenceTime;
    @BindView(R.id.et_vehicle_license_time)
    TextView etVehicleLicenseTime;
    @BindView(R.id.et_operational_qualification_time)
    TextView etOperationalQualificationTime;
    @BindView(R.id.img_status)
    ImageView imgStatus;
    @BindView(R.id.lay_txt)
    LinearLayout layTxt;
    @BindView(R.id.lay_card_left)
    RelativeLayout layCardLeft;
    @BindView(R.id.lay_card_right)
    RelativeLayout layCardRight;
    @BindView(R.id.lay_driving_licence)
    RelativeLayout layDrivingLicence;
    @BindView(R.id.lay_vehicle_license)
    RelativeLayout layVehicleLicense;
    @BindView(R.id.lay_operational_qualification)
    RelativeLayout layOperationalQualification;
    @BindView(R.id.img_name)
    ImageView imgName;
    @BindView(R.id.img_id_card)
    ImageView imgIdCard;
    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.img_driving_licence_time)
    ImageView imgDrivingLicenceTime;
    @BindView(R.id.img_vehicle_license_time)
    ImageView imgVehicleLicenseTime;
    @BindView(R.id.img_operational_qualification_time)
    ImageView imgOperationalQualificationTime;

    private Expert expert;
    private int type;
    private CameraDialog cameraDialog;
    private String left_card_id, right_card_id, driver_id, vehicle_id, operational_id;
    private Uri uritempFile;
    private String company_id, company_name;


    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), AuthenticationInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_info);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        updateView(expert);
        initDialog();
        registerBoradcastReceiver();
    }

    private void initDialog() {
        cameraDialog = new CameraDialog(this);
        cameraDialog.setActionName(ConstantSet.ACTION_NAME_2);
    }


    @OnClick({R.id.lay_txt, R.id.btn_back, R.id.lay_company, R.id.lay_card_left, R.id.lay_card_right, R.id.lay_driving_licence, R.id.lay_vehicle_license, R.id.lay_operational_qualification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_company:
                startActivityForResult(CompanyListActivity.newIntent(expert.getTravel_agency_id(), CompanyListActivity.TYPE_CHANGE), CompanyListActivity.COMPANY_LIST_CODE);
                break;
            case R.id.lay_card_left:
                type = IMG_LEFT_CARD;
                cameraDialog.show();
                break;
            case R.id.lay_card_right:
                type = IMG_RIGHT_CARD;
                cameraDialog.show();
                break;
            case R.id.lay_driving_licence:
                type = IMG_DRIVER;
                cameraDialog.show();
                break;
            case R.id.lay_vehicle_license:
                type = IMG_VEHICLE;
                cameraDialog.show();
                break;
            case R.id.lay_operational_qualification:
                type = IMG_OPERATIONAL;
                cameraDialog.show();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.lay_txt:
                startActivity(EditInfoActivity.newIntent());
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode,
                                 final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode);
        } else if (resultCode == CompanyListActivity.COMPANY_LIST_CODE) {
            if (StringUtils.isNotBlank(data.getStringExtra(CompanyListActivity.COMPANY_ID))) {
                loadExpertData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doPhoto(int requestCode) {
        if (requestCode == ConstantSet.SELECT_PIC_BY_TACK_PHOTO) {
            if (cameraDialog.getPhotoUri() == null) {
                return;
            }
            String uriPath = AndroidUtil.imageUri2Path(cameraDialog
                    .getPhotoUri());
            if (!StringUtils.isBlank(uriPath)) {
                final String targetPath = getCacheDir()
                        + BitmapUtils.toRegularHashCode(uriPath) + ".jpg";
                BitmapUtils.compressBitmap(uriPath, targetPath, 600); // 压缩
                uploadImage(targetPath);
            }
        } else {
            if (requestCode == ConstantSet.PHOTO_RESOULT_2) {
                try {
                    if (uritempFile != null) {
                        Bitmap bp = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(uritempFile));
                        if (null != bp) {
                            String picN = ComUtils.getRandomAlphamericStr(8)
                                    + ".jpg";
                            File temp = new File(getCacheDir(), picN);

                            try {
                                if (temp.exists()) {
                                    temp.delete();
                                }
                                temp.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ImageUtil.saveBitMap2File(temp, bp);
                            if (!bp.isRecycled()) {
                                bp.recycle(); // 回收图片所占的内存
                                System.gc(); // 提醒系统及时回收
                            }
                            String imgUrl = temp.getAbsolutePath();
                            String targetPath = getCacheDir()
                                    + BitmapUtils.toRegularHashCode(imgUrl)
                                    + ".jpg";
                            BitmapUtils.compressBitmap(imgUrl, targetPath);
                            uploadImage(targetPath);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ConstantSet.ACTION_NAME_2);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(ConstantSet.ACTION_NAME_2)) {
                ArrayList<String> imagePaths = intent
                        .getStringArrayListExtra(ConstantSet.CHOOSE_DATA);
                if (null != imagePaths && !imagePaths.isEmpty()) {
                    String targetPath = GlobalContext.getInstance()
                            .getCacheDir()
                            + BitmapUtils.toRegularHashCode(imagePaths.get(0))
                            + ".jpg";
                    BitmapUtils.compressBitmap(imagePaths.get(0), targetPath,
                            800); // 压缩
                    uploadImage(targetPath);
                }
            }
        }
    };

    private void uploadImage(String path) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        // 分享照片：10 ， 达人头像：20， 身份证或导游证照片：30， 商城封面：40
        params.put("category", "30");
        showProgress();
        UploadUtilsAsync uploadUtilsAsync = new UploadUtilsAsync(this, params, paths);
        uploadUtilsAsync.setListener(result -> {
            UploadImage resultImg = GsonHelper.parseObject(result,
                    UploadImage.class);
            hideProgress();
            if (null != resultImg) {
                if (resultImg.isSuccess()) {
                    if (type == IMG_DRIVER) {
                        driver_id = resultImg.getImage()
                                .getImage_id().toString();
                    } else if (type == IMG_VEHICLE) {
                        vehicle_id = resultImg.getImage()
                                .getImage_id().toString();
                    } else if (type == IMG_OPERATIONAL) {
                        operational_id = resultImg.getImage()
                                .getImage_id().toString();
                    } else if (type == IMG_LEFT_CARD) {
                        left_card_id = resultImg.getImage().getImage_id().toString();
                    } else if (type == IMG_RIGHT_CARD) {
                        right_card_id = resultImg.getImage().getImage_id().toString();
                    }
                    ToastUtil.showMes("修改成功");
                } else {
                    ToastUtil.showError(resultImg.getMsg());
                }
            } else {
                ToastUtil.showError(R.string.network_data_error);
            }
        });
        uploadUtilsAsync.execute();
    }

    private void loadExpertData() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.EXPERT_PERSON_DATA,
                    response -> {
                        try {
                            RespData respData = GsonHelper.parseObject(
                                    response, RespData.class);
                            if (null != respData) {
                                if (respData.isValida()) {
                                    ToastUtil.showError("请重新登陆");
                                    SPUtils.clear();
                                    AppManager.getAppManager().finishAllActivity();
                                    startActivity(LoginActivity.newIntent());
                                } else if (respData.isSuccess()) {
                                    Expert et = GsonHelper.parseObject(
                                            GsonHelper.toJson(respData
                                                    .getExpert()),
                                            Expert.class);
                                    if (null != et) {
                                        SPUtils.put(WConstant.EXPERT_DATA,
                                                GsonHelper.toJson(et));
                                        expert = et;
                                        updateView(et);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> System.out.println("异常"), params);
        }
    }

    private void updateView(Expert et) {
        etName.setText(et.getExpert_name());
        etIdCard.setText(et.getExpert_id_card());
        etCompany.setText(et.getTravel_agency_name());
        company_id = et.getTravel_agency_id();
        etDrivingLicenceTime.setText(et.getDriver_license().getDate());
        etVehicleLicenseTime.setText(et.getVehicle_license().getDate());
        etOperationalQualificationTime.setText(et.getOperational_qualification().getDate());
        if (StringUtils.isNotBlank(etName.getText().toString())) {
            imgName.setImageResource(R.drawable.ic_name_select);
        } else {
            imgName.setImageResource(R.drawable.ic_name_normal);
        }
        if (StringUtils.isNotBlank(etIdCard.getText().toString())) {
            imgIdCard.setImageResource(R.drawable.ic_id_card_select);
        } else {
            imgIdCard.setImageResource(R.drawable.ic_id_card_normal);
        }
        if (StringUtils.isNotBlank(etCompany.getText().toString())) {
            imgCompany.setImageResource(R.drawable.ic_company_select);
        } else {
            imgCompany.setImageResource(R.drawable.ic_company_normal);
        }
        if (StringUtils.isNotBlank(etDrivingLicenceTime.getText().toString())) {
            imgDrivingLicenceTime.setImageResource(R.drawable.ic_driving_licence_select);
        } else {
            imgDrivingLicenceTime.setImageResource(R.drawable.ic_driving_licence_normal);
        }
        if (StringUtils.isNotBlank(etVehicleLicenseTime.getText().toString())) {
            imgVehicleLicenseTime.setImageResource(R.drawable.ic_vehicle_license_select);
        } else {
            imgVehicleLicenseTime.setImageResource(R.drawable.ic_vehicle_license_normal);
        }
        if (StringUtils.isNotBlank(etOperationalQualificationTime.getText().toString())) {
            imgOperationalQualificationTime.setImageResource(R.drawable.ic_operational_qualification_select);
        } else {
            imgOperationalQualificationTime.setImageResource(R.drawable.ic_operational_qualification_normal);
        }
        int status = et.getAuthentication_status();
        switch (status) {
            case Expert.AUTHENTICATION_0:
            case Expert.AUTHENTICATION_10:
                layTxt.setEnabled(false);
                layCardLeft.setEnabled(false);
                layCardRight.setEnabled(false);
                layCompany.setEnabled(false);
                etCompany.setTextColor(getResources().getColor(R.color.textColor));
                layDrivingLicence.setEnabled(false);
                layVehicleLicense.setEnabled(false);
                layOperationalQualification.setEnabled(false);
                imgStatus.setImageResource(R.mipmap.shenhezhong);
                break;
            case Expert.AUTHENTICATION_20:
                layTxt.setEnabled(false);
                layCardLeft.setEnabled(false);
                layCardRight.setEnabled(false);
                layCompany.setEnabled(true);
                etCompany.setTextColor(getResources().getColor(R.color.black));
                layDrivingLicence.setEnabled(false);
                layVehicleLicense.setEnabled(false);
                layOperationalQualification.setEnabled(false);
                imgStatus.setImageResource(R.mipmap.shenhechenggong);
                break;
            case Expert.AUTHENTICATION_30:
                layTxt.setEnabled(true);
                layCardLeft.setEnabled(true);
                layCardRight.setEnabled(true);
                layCompany.setEnabled(true);
                etCompany.setTextColor(getResources().getColor(R.color.textColor));
                layDrivingLicence.setEnabled(true);
                layVehicleLicense.setEnabled(true);
                layOperationalQualification.setEnabled(true);
                imgStatus.setImageResource(R.mipmap.shenheshibai);
                etName.setTextColor(getResources().getColor(R.color.color_33));
                etIdCard.setTextColor(getResources().getColor(R.color.color_33));
                etCompany.setTextColor(getResources().getColor(R.color.color_33));
                etDrivingLicenceTime.setTextColor(getResources().getColor(R.color.color_33));
                etVehicleLicenseTime.setTextColor(getResources().getColor(R.color.color_33));
                etOperationalQualificationTime.setTextColor(getResources().getColor(R.color.color_33));
                break;
        }
        left_card_id = expert.getId_card_photo().get(0).getId();
        right_card_id = expert.getId_card_photo().get(1).getId();
        driver_id = expert.getDriver_license().getId();
        vehicle_id = expert.getVehicle_license().getId();
        operational_id = expert.getOperational_qualification().getId();

    }

    private void saveData() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("type", WConstant.TYPE_DRIVER);
        params.put("expert_name", expert.getExpert_name());
        params.put("expert_id_card", expert.getExpert_id_card());
        params.put("travel_agency_id", company_id);
        params.put("driver_license", driver_id);
        params.put("driver_license_date", expert.getDriver_license().getDate());
        params.put("vehicle_license", vehicle_id);
        params.put("vehicle_license_date", expert.getVehicle_license().getDate());
        params.put("operational_qualification", operational_id);
        params.put("operational_qualification_date", expert.getOperational_qualification().getDate());
        params.put("id_card_images[0]", left_card_id);
        params.put("id_card_images[1]", right_card_id);
        PublicReq.request(HttpUrl.EXPERT_SAVE, response -> {

        }, error -> {
        }, params);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        saveData();
    }
}
