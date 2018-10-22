package cn.dogplanet.net;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.app.cache.LruImageCache;
import cn.dogplanet.net.volley.RequestQueue;
import cn.dogplanet.net.volley.toolbox.ImageLoader;
import cn.dogplanet.net.volley.toolbox.Volley;


public class VolleyHelper {

	public static RequestQueue getRequestQueue() {
		return Volley.newRequestQueue(GlobalContext.getInstance());
	}

	public static ImageLoader getImageLoader() {
		return new ImageLoader(GlobalContext.getInstance().getRequestQueue(),
				LruImageCache.instance());
	}
}
