package cn.dogplanet.app.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import cn.dogplanet.net.volley.toolbox.ImageLoader.ImageCache;


public class LruImageCache implements ImageCache {

	private static LruCache<String, Bitmap> mMemoryCache;

	private static LruImageCache lruImageCache;

	private LruImageCache() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount()/1024;
			}
		};
	}
		
	public static LruImageCache instance() {
		if (lruImageCache == null) {
			lruImageCache = new LruImageCache();
		}
		return lruImageCache;
	}
	

	@Override
	public Bitmap getBitmap(String arg0) {
		return mMemoryCache.get(arg0);
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		mMemoryCache.put(arg0, arg1);
	}
}
