package cn.dogplanet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.Ndk.NdkUtil;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.base.BaseFragmentActivity;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.RedDotNumResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.OrderFragment;
import cn.dogplanet.ui.ShopFragment;
import cn.dogplanet.ui.UserFragment;
import cn.dogplanet.ui.login.LoginActivity;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseFragmentActivity {

    private static final String SELECT_TYPE = "type";

    public static final String TYPE_HOME = "home";
    public static final String TYPE_ORDER = "order";
    public static final String TYPE_USER = "user";
    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.btn_home)
    Button btnHome;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.tv_bage)
    cn.dogplanet.app.widget.CircleTextView tvBage;
    @BindView(R.id.btn_user)
    Button btnUser;

    private String select_type = TYPE_HOME;
    private long mExitTime;
    private Expert expert;

    private OrderFragment orderFragment;
    private ShopFragment shopFragment;
    private UserFragment userFragment;

    public static Intent newIntent(String type) {
        Intent intent = new Intent(GlobalContext.getInstance(), MainActivity.class);
        intent.putExtra(SELECT_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FragmentManager fManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            orderFragment = (OrderFragment) fManager
                    .findFragmentByTag("orderFragment");
            shopFragment = (ShopFragment) fManager
                    .findFragmentByTag("shopFragment");
            userFragment = (UserFragment) fManager
                    .findFragmentByTag("userFragment");
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NdkUtil ndkUtil = new NdkUtil();
        ndkUtil.createWatcher(String.valueOf(Process.myUid()));
        if (StringUtils.isNotBlank(getIntent().getStringExtra(SELECT_TYPE))) {
            select_type = getIntent().getStringExtra(SELECT_TYPE);
        }
        updateVersion();
        ShareSDK.initSDK(this);
        select_type = getIntent().getStringExtra(SELECT_TYPE);
        switch (select_type) {
            case TYPE_HOME:
                btnHome.performClick();
                break;
            case TYPE_ORDER:
                btnOrder.performClick();
                break;
            case TYPE_USER:
                btnUser.performClick();
                break;
            default:
                btnHome.performClick();
                break;
        }
        expert = WCache.getCacheExpert();
        String deviceToken = (String) SPUtils.get("deviceToken", "");
        if (StringUtils.isNotBlank(deviceToken)) {
            saveUuid(deviceToken);
        }
        loadExpertData();
        registerBoradcastReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRedDotNum();
    }

    // 首页
    private void showHome() {
        btnHome.setSelected(true);
        btnOrder.setSelected(false);
        btnUser.setSelected(false);
    }

    // 订单
    private void showOrder() {
        btnHome.setSelected(false);
        btnOrder.setSelected(true);
        btnUser.setSelected(false);
    }

    // 用户
    private void showUser() {
        btnHome.setSelected(false);
        btnOrder.setSelected(false);
        btnUser.setSelected(true);
    }


    private void saveUuid(String deviceToken) {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            // 平台 ios:10 ,android :20
            params.put("plat", "20");
            params.put("android_token", deviceToken);
            PublicReq.request(HttpUrl.SAVE_EXPERT_UUID,
                    response -> {
                    }, error -> {

                    }, params);
        }
    }


    //自动更新
    private void updateVersion() {
        PgyUpdateManager.setIsForced(false);//强制更新
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {

            }

            @Override
            public void onUpdateAvailable(String s) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(s);
                View view = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.umeng_update_dialog, null);
                TextView textView = view
                        .findViewById(R.id.umeng_update_content);
                textView.setText(String.format("版本号：%s\n本次更新内容：\n%s",
                        appBean.getVersionName(), appBean.getReleaseNote()));
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .create();
                dialog.setCancelable(false);
                view.findViewById(R.id.umeng_update_id_cancel)
                        .setOnClickListener(v -> {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        });
                view.findViewById(R.id.umeng_update_id_ok).setOnClickListener(
                        v -> {
                            // TODO Auto-generated method stub
                            if (Build.VERSION.SDK_INT >= 23) {
                                int checkSelfPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WConstant.PERMISSION_READ_EXTERNAL_STORAGE);
                                } else {
                                    startDownloadTask(MainActivity.this,
                                            appBean.getDownloadURL());
                                }
                            } else {
                                startDownloadTask(MainActivity.this,
                                        appBean.getDownloadURL());
                            }
                            dialog.dismiss();
                        });
                dialog.show();
                dialog.setContentView(view);

            }
        });
    }



    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (shopFragment != null) {
            transaction.hide(shopFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    private BroadcastReceiver mUpdateExpertReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(ConstantSet.UPDATE_EXPERT)) {
                loadExpertData();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ConstantSet.UPDATE_EXPERT);
        // 注册广播
        registerReceiver(mUpdateExpertReceiver, myIntentFilter);
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
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> System.out.println("异常"), params);
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


    @OnClick({R.id.btn_home, R.id.btn_order, R.id.btn_user})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.btn_home:
                showHome();
                if (shopFragment == null) {
                    shopFragment = new ShopFragment();
                    transaction
                            .add(R.id.main_content, shopFragment, "homeFragment");
                } else {
                    transaction.show(shopFragment);
                }
                break;
            case R.id.btn_order:
                showOrder();
                getRedDotNum();
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    transaction.add(R.id.main_content, orderFragment,
                            "orderFragment");
                } else {
                    transaction.show(orderFragment);
                }
                break;
            case R.id.btn_user:
                showUser();
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction
                            .add(R.id.main_content, userFragment, "shopFragment");
                } else {
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void getRedDotNum() {
        if (null != expert) {
            // 默认获取分类商品
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.GET_NUM, response -> {
                RedDotNumResp respData = GsonHelper.parseObject(response,
                        RedDotNumResp.class);
                if (null != respData) {
                    if (respData.isSuccess()) {
                        updateRedHot(respData.getNum());
                    } else {
                        updateRedHot(null);
                    }
                } else {
                    updateRedHot(null);
                }
            }, error -> {
                // ToastUtil.showError(R.string.network_error);
            }, params);
        }
    }

    private void updateRedHot(RedDotNumResp.RedNum rum) {
        if (null == rum) {
            tvBage.setVisibility(View.INVISIBLE);
            return;
        }
        // 订单数量
        if (StringUtils.isNotBlank(rum.getOrder())
                && !"0".equals(rum.getOrder())) {
            tvBage.setBackgroundColor(ContextCompat.getColor(
                    this,R.color.txt_selected_color));
            tvBage.setText(rum.getOrder());
            tvBage.setVisibility(View.VISIBLE);
        } else {
            tvBage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
        unregisterReceiver(mUpdateExpertReceiver);
    }


}
