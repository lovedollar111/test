package cn.dogplanet.app.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Tools for handler picture
 */
public final class BitmapUtils {

	private static final String TAG = BitmapUtils.class.getSimpleName();

	/**
	 * 压缩和旋转角度
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @param maxSize
	 */
	public static void compressBitmap(String sourcePath, String targetPath,
			float maxSize) {
		maxSize= AndroidUtil.dip2px(maxSize)/2;
		int degree = AndroidUtil.readPictureDegree(sourcePath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(sourcePath, options);
		final float originalWidth = options.outWidth;
		final float originalHeight = options.outHeight;
		float convertedWidth;
		float convertedHeight;

		if (originalWidth > originalHeight) {
			convertedWidth = maxSize;
			convertedHeight = maxSize / originalWidth * originalHeight;
		} else {
			convertedHeight = maxSize;
			convertedWidth = maxSize / originalHeight * originalWidth;
		}

		final int ratio = computeSampleSize(options, (int)maxSize, (int)(convertedWidth * convertedHeight));
		options.inPreferredConfig = Config.ARGB_4444;
		options.inSampleSize = ratio;
		options.inJustDecodeBounds = false;
		Bitmap convertedBitmap = BitmapFactory.decodeFile(sourcePath, options);
		if (convertedBitmap != null) {

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			convertedBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					byteArrayOutputStream);
			FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(new File(targetPath));
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (0 != degree) {
				Bitmap rotateBitmap = ImageUtil
						.rotateBitmap(targetPath, degree);
				ImageUtil.saveBitMap(targetPath, rotateBitmap);
			}
		}
	}
	
	public static void compressBitmap(String sourcePath, String targetPath) {

		int degree = AndroidUtil.readPictureDegree(sourcePath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(sourcePath, options);
		options.inJustDecodeBounds = false;

		Bitmap convertedBitmap = BitmapFactory.decodeFile(sourcePath, options);
		if (convertedBitmap != null) {

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			convertedBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					byteArrayOutputStream);
			FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(new File(targetPath));
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (0 != degree) {
				Bitmap rotateBitmap = ImageUtil
						.rotateBitmap(targetPath, degree);
				ImageUtil.saveBitMap(targetPath, rotateBitmap);
			}
		}
	}


	/**
	 * 压缩
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @param maxSize
	 */
	public static void compress(String sourcePath, String targetPath,
			float maxSize) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(sourcePath, options);

		final float originalWidth = options.outWidth;
		final float originalHeight = options.outHeight;

		float convertedWidth;
		float convertedHeight;

		if (originalWidth > originalHeight) {
			convertedWidth = maxSize;
			convertedHeight = maxSize / originalWidth * originalHeight;
		} else {
			convertedHeight = maxSize;
			convertedWidth = maxSize / originalHeight * originalWidth;
		}

		final float ratio = originalWidth / convertedWidth;

		options.inSampleSize = (int) ratio * 2;
		options.inJustDecodeBounds = false;

		Bitmap convertedBitmap = BitmapFactory.decodeFile(sourcePath, options);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		convertedBitmap.compress(Bitmap.CompressFormat.JPEG, 80,
				byteArrayOutputStream);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(new File(targetPath));
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据URI获取图片物理路径
	 */
	public static String getAbsoluteImagePath(Uri uri, Activity activity) {

		String[] proj = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 
	 * @param path
	 * @param maxSize
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int maxSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		final int originalWidth = options.outWidth;
		final int originalHeight = options.outHeight;

		int convertedWidth;
		int convertedHeight;

		if (originalWidth > originalHeight) {
			convertedWidth = maxSize;
			convertedHeight = maxSize / originalWidth * originalHeight;
		} else {
			convertedHeight = maxSize;
			convertedWidth = maxSize / originalHeight * originalWidth;
		}

		options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = computeSampleSize(options, maxSize,
				convertedWidth * convertedHeight);

		Bitmap convertedBitmap = BitmapFactory.decodeFile(path, options);

		if (convertedBitmap != null) {
			final int realWidth = convertedBitmap.getWidth();
			final int realHeight = convertedBitmap.getHeight();

		}

		return convertedBitmap;
	}

	/**
	 * 
	 * @param path
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		final int originalWidth = options.outWidth;
		final int originalHeight = options.outHeight;

		options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = computeSampleSize(options, maxWidth, maxWidth
				* maxHeight);

		Bitmap convertedBitmap = BitmapFactory.decodeFile(path, options);

		if (convertedBitmap != null) {
			final int realWidth = convertedBitmap.getWidth();
			final int realHeight = convertedBitmap.getHeight();

		}

		return convertedBitmap;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? minSideLength : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 生成8位16进制的缓存因子：规则的8位哈希码，不足前面补零
	 * 
	 * @param string
	 * @return
	 */
	public static String toRegularHashCode(String string) {
		final String hexHashCode = Integer.toHexString(string.hashCode());
		final StringBuilder stringBuilder = new StringBuilder(hexHashCode);
		while (stringBuilder.length() < 8) {
			stringBuilder.insert(0, '0');
		}
		return stringBuilder.toString();
	}

	// 等比缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 * 将图片切为：w:h的长方形 w必须大于h
	 * 
	 * @param bitmap
	 * @param w
	 *            长
	 * @param h
	 *            高
	 * @return
	 */
	public static Bitmap makeRectWH(Bitmap bitmap, int w, int h) {
		Bitmap result = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width > height) {
				if (width * h / w >= height) {
					result = Bitmap.createBitmap(bitmap, (width - height * w
							/ h) / 2, 0, height * w / h, height);
				} else {
					result = Bitmap.createBitmap(bitmap, 0, (height - width * h
							/ w) / 2, width, width * h / w);
				}
			} else if (width < height) {
				if (height * h / w >= width) {
					result = Bitmap.createBitmap(bitmap, 0, (height - width * w
							/ h) / 2, width, width * w / h);
				} else {
					result = Bitmap.createBitmap(bitmap, (width - height * h
							/ w) / 2, 0, height * h / w, height);
				}
			} else {
				result = Bitmap.createBitmap(bitmap, 0, (height - height * h
						/ w) / 2, width, height * h / w);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	 /** 
     * Compress image by pixel, this will modify image width/height.  
     * Used to get thumbnail 
     *  
     * @param imgPath image path 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @return 
     */  
    public Bitmap ratio(String imgPath, float pixelW, float pixelH) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容  
        newOpts.inJustDecodeBounds = true;  
        newOpts.inPreferredConfig = Config.RGB_565;  
        // Get bitmap info, but notice that bitmap is null now    
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);  
            
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        // 想要缩放的目标尺寸  
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        newOpts.inSampleSize = be;//设置缩放比例  
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);  
        // 压缩好比例大小后再进行质量压缩  
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;  
    }  
      
    /** 
     * Compress image by size, this will modify image width/height.  
     * Used to get thumbnail 
     *  
     * @param image 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @return 
     */  
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 50, os);  
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出      
            os.reset();//重置baos即清空baos    
            image.compress(Bitmap.CompressFormat.JPEG, 30, os);//这里压缩50%，把压缩后的数据存放到baos中    
        }    
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());    
        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了    
        newOpts.inJustDecodeBounds = true;  
        newOpts.inPreferredConfig = Config.RGB_565;  
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);    
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        newOpts.inSampleSize = be;//设置缩放比例    
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了    
        is = new ByteArrayInputStream(os.toByteArray());    
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);  
        //压缩好比例大小后再进行质量压缩  
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;  
    }


}
