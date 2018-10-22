package cn.dogplanet.net.volley.toolbox;

import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.net.volley.toolbox.ImageLoader.ImageContainer;
import cn.dogplanet.net.volley.toolbox.ImageLoader.ImageListener;

public class ListImageListener implements ImageListener {

	public interface OnSetImageBitmap {
		void onSetImageBitmap(ImageView imageView, Bitmap bitmap);
	}

	private ImageView mImageView;
	private int mDefaultImageResId;
	private int mErrorImageResId;
	private String mTag;

	public OnSetImageBitmap mListener;

	public ListImageListener(ImageView imageView, int defaultImageResId,
                             int errorImageResId, String tag) {
		mImageView = imageView;
		mDefaultImageResId = defaultImageResId;
		mErrorImageResId = errorImageResId;
		mTag = tag;
	}

	public ListImageListener(ImageView imageView, int defaultImageResId,
                             int errorImageResId, String tag, OnSetImageBitmap l) {
		this(imageView, defaultImageResId, errorImageResId, tag);
		mListener = l;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (mErrorImageResId != 0 && mImageView.getTag() != null
				&& mTag.equals(mImageView.getTag())) {
			mImageView.setImageResource(mErrorImageResId);
		}
	}

	@Override
	public void onResponse(ImageContainer response, boolean isImmediate) {
		if (mImageView.getTag() != null && mTag.equals(mImageView.getTag())) {
			if (response.getBitmap() != null) {
				mImageView.setImageBitmap(response.getBitmap());
				if (mListener != null) {
					mListener.onSetImageBitmap(mImageView, response.getBitmap());
				}
			} else if (mDefaultImageResId != 0) {
				mImageView.setImageResource(mDefaultImageResId);
			}
		}
	}

}
