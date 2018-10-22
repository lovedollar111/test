package cn.dogplanet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.login.AuthenticationInfoActivity;
import cn.dogplanet.ui.login.BaseInfoActivity;
import cn.dogplanet.ui.login.FirstActivity;

/**
 * 启动页
 * editor:ztr
 * package_name:cn.dogplanet
 * file_name:SplashActivity.java
 * date:2016-12-6
 */
public class SplashActivity extends Activity {


    private static final int SPLASH_DISPLAY_LENGHT = 500;  // 延迟0.5秒
    private Expert expert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        expert = WCache.getCacheExpert();
        LinearLayout lay_splash = this.findViewById(R.id.lay_splash);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.splash_in);
        lay_splash.setAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String expert_str = (String) SPUtils.get(WConstant.EXPERT_DATA, "");
                if (StringUtils.isBlank(expert_str)) {
                    Intent mainIntent = new Intent(SplashActivity.this,
                            FirstActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                } else {
                    syncExpertData();
                }
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    // 同步数据
    private void syncExpertData() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.EXPERT_PERSON_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                RespData respData = GsonHelper.parseObject(
                                        response, RespData.class);
                                if (null != respData) {
                                    if (respData.isValida()) {
                                        startActivity(FirstActivity.newIntent());
                                        finish();
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
                            redirectActivity();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            redirectActivity();
                            System.out.println("异常");
                        }
                    }, params);
        }
    }

    private void redirectActivity() {
        expert = WCache.getCacheExpert();
        if (null == expert) {
            Intent mainIntent = new Intent(SplashActivity.this,
                    FirstActivity.class);
            SplashActivity.this.startActivity(mainIntent);
        } else if (StringUtils.isBlank(expert.getExpert_icon())) {
            // 如果必填项为空即跳转到完善个人信息
            startActivity(BaseInfoActivity.newIntent());
        } else {
            SplashActivity.this.startActivity(MainActivity.newIntent(MainActivity.TYPE_HOME));
        }
        startActivity(AuthenticationInfoActivity.newIntent());
        SplashActivity.this.finish();
    }


}
