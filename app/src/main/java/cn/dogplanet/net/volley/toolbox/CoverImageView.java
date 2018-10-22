package cn.dogplanet.net.volley.toolbox;

import android.content.Context;
import android.util.AttributeSet;

import cn.dogplanet.R;


public class CoverImageView extends NetworkImageView {

	public CoverImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		inintData();
	}

	public CoverImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inintData();
	}

	public CoverImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inintData();
	}
	
	private void inintData() {
		setDefaultImageResId(R.mipmap.ic_launcher);
		setErrorImageResId(R.mipmap.ic_launcher);
	}

}
