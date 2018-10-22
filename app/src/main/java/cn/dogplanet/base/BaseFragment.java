package cn.dogplanet.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Fragment 基类
 * editor:ztr
 * package_name:cn.dogplanet.base
 * file_name:BaseFragment.java
 * date:2016-12-6
 */
public abstract class BaseFragment extends Fragment {

	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	private Toast mToast;

	public void showToast(int resId) {
		showToast(getResources().getString(resId));
	}

	public void showToast(String text) {
		if (null == mToast) {
			mToast = Toast
					.makeText(this.getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	public void showToast(String text, int duration) {
		if (null == mToast) {
			mToast = Toast.makeText(this.getActivity(), text, duration);
		} else {
			mToast.setText(text);
			mToast.setDuration(duration);
		}
		mToast.show();
	}

	public void cancelToast() {
		if (null != mToast) {
			mToast.cancel();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	@Override
	public void onPause() {
		super.onPause();
		cancelToast();
	}

}
