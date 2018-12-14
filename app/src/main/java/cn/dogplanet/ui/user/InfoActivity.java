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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;

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
import cn.dogplanet.app.util.BitmapUtils;
import cn.dogplanet.app.util.ComUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ImageUtil;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.util.UploadUtilsAsync;
import cn.dogplanet.app.widget.CircleImageView;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.entity.UploadImage;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.toolbox.ImageRequest;
import cn.dogplanet.net.volley.toolbox.ListImageListener;
import cn.dogplanet.ui.login.BaseInfoActivity;
import cn.dogplanet.ui.login.LoginActivity;
import cn.dogplanet.ui.popup.CameraDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class InfoActivity extends BaseActivity implements PlatformActionListener, Handler.Callback {


    private static final int MSG_ACTION_CCALLBACK = 0;
    private final int TYPE_LOGIN_WX_SUCCESS = 1;
    private final int TYPE_LOGIN_WX_FALSE = 2;
    private final int TYPE_LOGIN_WX_CANCEL = 3;

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_wx)
    TextView tvWx;

    private Expert expert;
    private String open_id;
    private CameraDialog cameraDialog;
    private Uri uritempFile;


    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), InfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        initView();
        initDialog();
        registerBoradcastReceiver();
    }

    private void initDialog() {
        cameraDialog = new CameraDialog(this);
        cameraDialog.setActionName(ConstantSet.ACTION_NAME_2);
    }

    private void initView() {
        if (StringUtils.isNotBlank(expert.getExpert_icon())) {
            imgHead.setTag(expert.getExpert_icon());
            ListImageListener imageListener = new ListImageListener(
                    imgHead, R.mipmap.userimage, R.mipmap.userimage,
                    expert.getExpert_icon());
            GlobalContext.getInstance().getImageLoader()
                    .get(expert.getExpert_icon(), imageListener);
        } else {
            imgHead.setImageResource(R.mipmap.userimage);
        }
        tvName.setText(expert.getExpert_name());
        if (StringUtils.isNotBlank(expert.getOpen_id())) {
            tvWx.setText("已绑定");
        } else {
            tvWx.setText("未绑定");
        }

    }

    @OnClick({R.id.btn_back, R.id.lay_head, R.id.lay_name, R.id.lay_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.lay_head:
                cameraDialog.show();
                break;
            case R.id.lay_wx:
                if (StringUtils.isBlank(expert.getOpen_id())){
                    bindWx();
                }
                break;
        }
    }

    private void bindWx() {
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);//授权并获取用户信息
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
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_SUCCESS;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
        ToastUtil.showMesLong(hashMap.toString());
    }

    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_FALSE;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = TYPE_LOGIN_WX_CANCEL;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
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
            cropImage(cameraDialog.getPhotoUri());
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
        params.put("category", "20");
        showProgress();
        UploadUtilsAsync uploadUtilsAsync = new UploadUtilsAsync(this, params, paths);
        uploadUtilsAsync.setListener(result -> {
            UploadImage resultImg = GsonHelper.parseObject(result,
                    UploadImage.class);
            if (null != resultImg) {
                if (resultImg.isSuccess()) {
                    String tUrl = resultImg.getImage().getImage_url();
                    String head_id = resultImg.getImage()
                            .getImage_id().toString();
                    saveHead(head_id);
                    ImageRequest imageRequest = new ImageRequest(tUrl,
                            bitmap -> {
                                imgHead.setImageBitmap(bitmap);
                                hideProgress();
                            }, 0, 0, Bitmap.Config.ARGB_4444, arg0 -> {

                    });
                    GlobalContext.getInstance().getRequestQueue().add(imageRequest);
                } else {
                    ToastUtil.showError(resultImg.getMsg());
                }
            } else {
                ToastUtil.showError(R.string.network_data_error);
            }
        });
        uploadUtilsAsync.execute();
    }

    private void saveHead(String head_id) {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            // 分享照片：10 ， 达人头像：20， 身份证或导游证照片：30， 商城封面：40
            params.put("expert_icon", head_id);
            PublicReq.request(HttpUrl.SAVE_BASE_INFO, response -> {
                if (StringUtils.isNotBlank(response)) {
                    Resp resp = GsonHelper.parseObject(response, Resp.class);
                    if (resp != null) {
                        if (resp.isSuccess()) {
                            ToastUtil.showMes("保存成功");
                        } else {
                            ToastUtil.showError(resp.getMsg());
                        }
                    } else {
                        ToastUtil.showError(R.string.network_data_error);
                    }
                }
            }, error -> {
                ToastUtil.showError(R.string.network_error);
            }, params);
        }
    }


    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*").putExtra("crop", "true")
                // 发送裁剪信号
                .putExtra("aspectX", 1)
                // X方向上的比例
                .putExtra("aspectY", 1)
                // Y方向上的比例
                .putExtra("outputX", 60)
                // 裁剪区的宽
                .putExtra("outputY", 60)
                // 裁剪区的高
                // .putExtra("return-data", true)
                // 是否返回数据
                .putExtra("scale", true)
                // 是否保留比例
                .putExtra("scaleUpIfNeeded", true)
                // 黑边
                // .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                .putExtra("noFaceDetection", true)
                // 关闭人脸检测
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        String iPath = "file://" + File.separator
                + Environment.getExternalStorageDirectory().getPath()
                + File.separator + "wangwang.jpg";
        System.out.println(iPath);
        uritempFile = Uri.parse(iPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        startActivityForResult(intent, ConstantSet.PHOTO_RESOULT);
    }

}
