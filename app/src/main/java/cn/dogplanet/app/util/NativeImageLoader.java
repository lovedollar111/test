package cn.dogplanet.app.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.dogplanet.app.cache.LruImageCache;

/**
 * 本地图片加载器,采用的是异步解析本地图片，单例模式利用getInstance()获取NativeImageLoader实例
 * 调用loadNativeImage()方法加载本地图片，此类可作为一个加载本地图片的工具类
 */
public class NativeImageLoader {

	// private LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

	private NativeImageLoader() {
		// 获取应用程序的最大内存
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// 用最大内存的1/4来存储图片
		final int cacheSize = maxMemory / 4;
		// mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
		//
		// //获取每张图片的大小
		// @Override
		// protected int sizeOf(String key, Bitmap bitmap) {
		// return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
		// }
		// };
	}

	/**
	 * 通过此方法来获取NativeImageLoader的实例
	 * 
	 * @return
	 */
	public static NativeImageLoader getInstance() {
		return mInstance;
	}

	/**
	 * 加载本地图片，对图片不进行裁剪
	 * 
	 * @param path
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(final String path,
			final NativeImageCallBack mCallBack) {
		return this.loadNativeImage(path, null, mCallBack);
	}

	/**
	 * 此方法来加载本地图片，这里的mPoint是用来封装ImageView的宽和高，我们会根据ImageView控件的大小来裁剪Bitmap
	 * 如果你不想裁剪图片，调用loadNativeImage(final String path, final NativeImageCallBack
	 * mCallBack)来加载
	 * 
	 * @param path
	 * @param mPoint
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(String path, Point mPoint,
			NativeImageCallBack mCallBack) {
		return loadNativeImage(path, mPoint, true, mCallBack);
	}

	public Bitmap loadNativeImage(final String path, final Point mPoint,
			final boolean cache, final NativeImageCallBack mCallBack) {
		Bitmap bitmap = null;
		if (cache) {
			// 先获取内存中的Bitmap
			bitmap = getBitmapFromMemCache(path);
		}
		// 若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
		final Handler mHander = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mCallBack.onImageLoader((Bitmap) msg.obj, path);
			}
		};
		if (bitmap == null) {
			mImageThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					// 先获取图片的缩略图
					Bitmap mBitmap = decodeThumbBitmapForFile(path,
							mPoint == null ? 0 : mPoint.x, mPoint == null ? 0
									: mPoint.y);
					Message msg = mHander.obtainMessage();
					msg.obj = mBitmap;
					mHander.sendMessage(msg);
					// 将图片加入到内存缓存
					if (cache)
						addBitmapToMemoryCache(path, mBitmap);
				}
			});
		} else {
			Message msg = mHander.obtainMessage();
			msg.obj = bitmap;
			mHander.sendMessage(msg);
		}
		return bitmap;
	}

	/**
	 * 往内存缓存中添加Bitmap
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			double maxSize = 4.00;
			// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
			// 将字节换成KB
			double mid = b.length / 1024;
			// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
					bitmap.getHeight() / Math.sqrt(i));
			LruImageCache.instance().putBitmap(key, bitmap);
		}
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 根据key来获取内存中的图片
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromMemCache(String key) {
		return LruImageCache.instance().getBitmap(key);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = NativeImageLoader.getInstance().computeScale(
				options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * 
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public Bitmap decodeThumbBitmapForFile(String path, int viewWidth,
			int viewHeight) {
		String filepath = path;
		File file = new File(filepath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 设置为true,表示解析Bitmap对象，该对象不占内存
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			// 设置缩放比例
			options.inSampleSize = computeScale(options, viewWidth, viewHeight);
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			// 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
			options.inPurgeable = true;
			// 设置解码位图的尺寸信息
			options.inInputShareable = true;
			// 设置为false,解析Bitmap对象加入到内存中
			options.inJustDecodeBounds = false;
			InputStream is;
			try {
				is = new FileInputStream(filepath);
				return BitmapFactory.decodeStream(is, null, options);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// return BitmapFactory.decodeFile(path, options);
		}
		return null;
	}

	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * 
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public Bitmap decodeBitmapForFile(String path, int inSampleSize) {
		String filepath = path;
		File file = new File(filepath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 设置为true,表示解析Bitmap对象，该对象不占内存
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			// 设置缩放比例
			options.inSampleSize = inSampleSize;
			// 设置为false,解析Bitmap对象加入到内存中
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}
		return null;
	}

	/**
	 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
	 * 
	 * @param options
	 * @param width
	 * @param height
	 */
	public int computeScale(BitmapFactory.Options options, int viewWidth,
			int viewHeight) {
		int inSampleSize = 1;
		if (viewWidth == 0 && viewHeight == 0) {
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		// 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
		if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
			int widthScale = Math
					.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight
					/ (float) viewHeight);
			// 为了保证图片不缩放变形，我们取宽高比例最小的那个
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}

	/**
	 * 加载本地图片的回调接口
	 */
	public interface NativeImageCallBack {

		/**
		 * 当子线程加载完了本地的图片，将Bitmap和图片路径回调在此方法中
		 * 
		 * @param bitmap
		 * @param path
		 */
		void onImageLoader(Bitmap bitmap, String path);
	}
}
