package cn.dogplanet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import net.wequick.small.Small;

import cn.dogplanet.app.cache.LruImageCache;
import cn.dogplanet.app.util.Config;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.net.volley.Request;
import cn.dogplanet.net.volley.RequestQueue;
import cn.dogplanet.net.volley.toolbox.ImageLoader;
import cn.dogplanet.net.volley.toolbox.Volley;
import cn.sharesdk.framework.ShareSDK;


public class GlobalContext extends Application {


    private static GlobalContext globalContext;
    private Config mConfig;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private PushAgent mPushAgent;


    public static GlobalContext getInstance() {
        return globalContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
        Small.preSetUp(this);
        ShareSDK.initSDK(this);
        mConfig = new Config(this);
        UMConfigure.init(globalContext, UMConfigure.DEVICE_TYPE_PHONE, "181525f12a7a9b020bc8355d0742ce26");
        mPushAgent = PushAgent.getInstance(this);
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
        mConfig = new Config(globalContext);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), mConfig.getCrashLogDir());
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
