package cn.dogplanet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.dogplanet.app.cache.LruImageCache;
import cn.dogplanet.app.util.Config;
import cn.dogplanet.net.volley.Request;
import cn.dogplanet.net.volley.RequestQueue;
import cn.dogplanet.net.volley.toolbox.ImageLoader;
import cn.dogplanet.net.volley.toolbox.Volley;


public class GlobalContext extends Application {


	private static GlobalContext globalContext;
	private Config mConfig;
	private RequestQueue mRequestQueue;
	private ImageLoader imageLoader;


	public static GlobalContext getInstance() {
		return globalContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
		mConfig = new Config(this);


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
