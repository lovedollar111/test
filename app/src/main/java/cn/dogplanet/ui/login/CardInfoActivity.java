package cn.dogplanet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;

public class CardInfoActivity extends BaseActivity {



    private Expert expert;
    private long mExitTime;


    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),CardInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_info_activity);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        expert= WCache.getCacheExpert();
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void eventBus(Expert expert){
        this.expert.setDriver_license(expert.getDriver_license());
        this.expert.setExpert_name(expert.getExpert_name());
        this.expert.setExpert_id_card(expert.getExpert_id_card());
        this.expert.setTravel_agency_id(expert.getTravel_agency_id());
        this.expert.setVehicle_license(expert.getVehicle_license());
        this.expert.setOperational_qualification(expert.getOperational_qualification());
    }

    @OnClick({R.id.btn_ok})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_ok:
                saveData();
                break;
        }
    }


    private void saveData() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", String.valueOf(expert.getId()));
        params.put("access_token", expert.getAccess_token());
        params.put("type", WConstant.TYPE_DRIVER);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
