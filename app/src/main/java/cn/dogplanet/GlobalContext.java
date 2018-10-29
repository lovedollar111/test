package cn.dogplanet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import cn.dogplanet.app.cache.LruImageCache;
import cn.dogplanet.app.util.Config;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.net.volley.Request;
import cn.dogplanet.net.volley.RequestQueue;
import cn.dogplanet.net.volley.toolbox.ImageLoader;
import cn.dogplanet.net.volley.toolbox.Volley;


public class GlobalContext extends Application {


    private static GlobalContext globalContext;
    private Config mConfig;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private static PushAgent mPushAgent;


    public static GlobalContext getInstance() {
        return globalContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
        mConfig = new Config(this);
        initThirdService();

    }

    private void initThirdService() {
        mPushAgent = PushAgent.getInstance(globalContext);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                // TODO Auto-generated method stub
                SPUtils.put("deviceToken",deviceToken);
            }

            @Override
            public void onFailure(String arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        new Thread(new Runnable() {
            @Override
            public void run() {

                mConfig = new Config(globalContext);

                CrashHandler crashHandler = CrashHandler.getInstance();
                crashHandler.init(getApplicationContext(), mConfig.getCrashLogDir());

            }
        }).start();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public Config getConfig() {
        return mConfig;
    }


    public void addRequest(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(this);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        if (null == imageLoader) {
            imageLoader = new ImageLoader(GlobalContext.getInstance()
                    .getRequestQueue(), LruImageCache.instance());
        }
        return imageLoader;
    }

}
