package cn.dogplanet.ui.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import cn.dogplanet.MainActivity;
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
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.UploadImage;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.toolbox.ImageRequest;
import cn.dogplanet.ui.popup.CameraDialog;

public class CardInfoActivity extends BaseActivity {

    private static final int IMG_LEFT_CARD = 1;
    private static final int IMG_RIGHT_CARD = 2;
    private static final int IMG_DRIVER = 3;
    private static final int IMG_VEHICLE = 4;
    private static final int IMG_OPERATIONAL = 5;

    @BindView(R.id.img_left_card)
    RoundCornerImageView imgLeftCard;
    @BindView(R.id.img_right_card)
    RoundCornerImageView imgRightCard;
    @BindView(R.id.img_driver)
    RoundCornerImageView imgDriver;
    @BindView(R.id.img_vehicle)
    RoundCornerImageView imgVehicle;
    @BindView(R.id.img_operational)
    RoundCornerImageView imgOperational;
    @BindView(R.id.btn_reg)
    Button btnReg;

    private Expert expert;
    private long mExitTime;

    private CameraDialog cameraDialog;

    private Uri uritempFile;

    private String left_card_id, right_card_id, driver_id, vehicle_id, operational_id;
    private String driver_date, vehicle_date, operational_date;
    private int type = 0;
    private String name, id_card, company_id;


    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), CardInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_info_activity);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        initDialog();
        registerBoradcastReceiver();
    }

    private void initDialog() {
        cameraDialog = new CameraDialog(this);
        cameraDialog.setActionName(ConstantSet.ACTION_NAME_2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(Expert expert) {
        name = expert.getExpert_name();
        id_card = expert.getExpert_id_card();
        company_id = expert.getTravel_agency_id();
        driver_date = expert.getDriver_license().getDate();
        vehicle_date = expert.getVehicle_license().getDate();
        operational_date = expert.getOperational_qualification().getDate();
    }


    private void saveData() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("type", WConstant.TYPE_DRIVER);
        params.put("expert_name", name);
        params.put("expert_id_card", id_card);
        params.put("travel_agency_id", company_id);
        params.put("driver_license", driver_id);
        params.put("driver_license_date", driver_date);
        params.put("vehicle_license", vehicle_id);
        params.put("vehicle_license_date", vehicle_date);
        params.put("operational_qualification", operational_id);
        params.put("operational_qualification_date", operational_date);
        params.put("id_card_images[0]", left_card_id);
        params.put("id_card_images[1]", right_card_id);
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
                        startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));

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
    public void onActivityResult(final int requestCode, int resultCode,
                                 final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode);
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
        Log.i("info", path + "\n" + expert);
//        if (null != expert) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        // 分享照片：10 ， 达人头像：20， 身份证或导游证照片：30， 商城封面：40
        params.put("category", "30");
        UploadUtilsAsync uploadUtilsAsync = new UploadUtilsAsync(this, params, paths);
        uploadUtilsAsync.setListener(result -> {
            UploadImage resultImg = GsonHelper.parseObject(result,
                    UploadImage.class);
            if (null != resultImg) {
                if (resultImg.isSuccess()) {
                    String tUrl = null;
                    if (type == IMG_DRIVER) {
                        driver_id = resultImg.getImage()
                                .getImage_id().toString();
                        tUrl = resultImg.getImage()
                                .getImage_url();
                    } else if (type == IMG_VEHICLE) {
                        vehicle_id = resultImg.getImage()
                                .getImage_id().toString();
                        tUrl = resultImg.getImage()
                                .getImage_url();
                    } else if (type == IMG_OPERATIONAL) {
                        operational_id = resultImg.getImage()
                                .getImage_id().toString();
                        tUrl = resultImg.getImage()
                                .getImage_url();
                    } else if (type == IMG_LEFT_CARD) {
                        left_card_id = resultImg.getImage().getImage_id().toString();
                        tUrl = resultImg.getImage().getImage_url();
                    } else if (type == IMG_RIGHT_CARD) {
                        right_card_id = resultImg.getImage().getImage_id().toString();
                        tUrl = resultImg.getImage().getImage_url();
                    }
                    updateCard(tUrl);
                } else {
                    ToastUtil.showError(resultImg.getMsg());
                }
            } else {
                ToastUtil.showError(R.string.network_data_error);
            }
        });
        uploadUtilsAsync.execute();
//        }
    }


    private void updateCard(String expertIcon) {
        Log.i("info", expertIcon);
        ImageRequest imageRequest = new ImageRequest(expertIcon,
                bitmap -> {
                    if (type == IMG_DRIVER) {
                        imgDriver.setImageBitmap(bitmap);
                    } else if (type == IMG_VEHICLE) {
                        imgVehicle.setImageBitmap(bitmap);

                    } else if (type == IMG_OPERATIONAL) {
                        imgOperational.setImageBitmap(bitmap);
                    } else if (type == IMG_LEFT_CARD) {
                        imgLeftCard.setImageBitmap(bitmap);
                    } else if (type == IMG_RIGHT_CARD) {
                        imgRightCard.setImageBitmap(bitmap);
                    }
                    updateBtn();
                }, 0, 0, Bitmap.Config.ARGB_4444, arg0 -> {

        });
        GlobalContext.getInstance().getRequestQueue().add(imageRequest);

    }


    @OnClick({R.id.img_left_card, R.id.img_right_card, R.id.img_driver, R.id.img_vehicle, R.id.img_operational, R.id.btn_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_card:
                cameraDialog.show();
                type = IMG_LEFT_CARD;
                break;
            case R.id.img_right_card:
                cameraDialog.show();
                type = IMG_RIGHT_CARD;
                break;
            case R.id.img_driver:
                cameraDialog.show();
                type = IMG_DRIVER;
                break;
            case R.id.img_vehicle:
                cameraDialog.show();
                type = IMG_VEHICLE;
                break;
            case R.id.img_operational:
                cameraDialog.show();
                type = IMG_OPERATIONAL;
                break;
            case R.id.btn_reg:
                if (checkInput()) {
                    saveData();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WConstant.PERMISSION_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    cameraDialog.allowTakeCamera();
                } else {
                    ToastUtil.showError("请开启相机使用权限。");
                }
                break;
            case WConstant.PERMISSION_READ_EXTERNAL_STORAGE_IN_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    cameraDialog.allowTakeCamera();
                } else {
                    ToastUtil.showError("请开启存储空间权限。");
                }
                break;
            case WConstant.PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    cameraDialog.allowChoose();
                } else {
                    ToastUtil.showError("请开启存储空间权限。");
                }
                break;
        }
    }

    private boolean checkInput() {
        if (StringUtils.isBlank(left_card_id)) {
            ToastUtil.showError("请上传手持身份证正面照片");
            return false;
        }

        if (StringUtils.isBlank(right_card_id)) {
            ToastUtil.showError("请上传手持身份证反面证照片");
            return false;
        }
        if (StringUtils.isBlank(driver_id)) {
            ToastUtil.showError("请上传手持驾驶证照片");
            return false;
        }

        if (StringUtils.isBlank(vehicle_id)) {
            ToastUtil.showError("请上传手持行驶证照片");
            return false;
        }

        if (StringUtils.isBlank(operational_id)) {
            ToastUtil.showError("请上传手持运营资质照片");
            return false;
        }

        return true;
    }


    private void updateBtn() {
        if (StringUtils.isNotBlank(left_card_id) && StringUtils.isNotBlank(right_card_id) && StringUtils.isNotBlank(driver_id) && StringUtils.isNotBlank(vehicle_id) && StringUtils.isNotBlank(operational_id)) {
            btnReg.setBackgroundResource(R.drawable.gradient_f1_e0);
        } else {
            btnReg.setBackgroundResource(R.drawable.gradient_c7_ab);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
