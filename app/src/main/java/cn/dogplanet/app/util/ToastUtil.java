package cn.dogplanet.app.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;


public class ToastUtil {

	private static final int RES_REEOR = R.mipmap.icon_error;

	private static final float width = 200F;

	public static LinearLayout TOAST_VIEW = null;

	public static LinearLayout getToastView() {
		if (null == TOAST_VIEW) {
			LayoutInflater inflater = (LayoutInflater) GlobalContext
					.getInstance().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			TOAST_VIEW = (LinearLayout) inflater.inflate(R.layout.toast_layout,
					null);
		}
		return TOAST_VIEW;
	}

	/**
	 * 显示错误toast
	 * 
	 * @param text
	 */
	public static final void showError(String text) {
		if (StringUtils.isBlank(text)) {
			return;
		}
		LinearLayout toastView = getToastView();
		TextView msg = (TextView) toastView.findViewById(R.id.tv_msg);
		msg.setText(text);
		Toast toast = new Toast(GlobalContext.getInstance());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastView);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.show();
	}

	// public static final void showError(String text) {
	// Toast toast = Toast.makeText(GlobalContext.getInstance(), text,
	// Toast.LENGTH_SHORT);
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// LinearLayout toastView = (LinearLayout) toast.getView();
	// int padding = AndroidUtil.dip2px(30F);
	// int top = AndroidUtil.dip2px(35F);
	// toastView.getLayoutParams().width = AndroidUtil.dip2px(width);
	// toastView.setBackgroundResource(R.drawable.toast_back);
	// toastView.setPadding(padding, top, padding, padding);
	// ImageView imageiview = new ImageView(GlobalContext.getInstance());
	// imageiview.setImageResource(RES_REEOR);
	// imageiview.setPadding(0, 0, 0, padding);
	// toastView.addView(imageiview, 0);
	// toast.show();
	// }

	/**
	 * 显示错误toast
	 */
	public static final void showError(int resId) {
		LinearLayout toastView = getToastView();
		TextView msg = (TextView) toastView.findViewById(R.id.tv_msg);
		msg.setText(resId);
		Toast toast = new Toast(GlobalContext.getInstance());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastView);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.show();
	}

	public static final void showMes(String text) {
		if (StringUtils.isBlank(text)) {
			return;
		}
		LinearLayout toastView = getToastView();
		TextView msg = (TextView) toastView.findViewById(R.id.tv_msg);
		msg.setText(text);
		Toast toast = new Toast(GlobalContext.getInstance());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastView);
		toast.setGravity(Gravity.BOTTOM,0,50);
		toast.show();
	}

	public static void showMesLong(String text){
		if (StringUtils.isBlank(text)) {
			return;
		}
		LinearLayout toastView = getToastView();
		TextView msg = (TextView) toastView.findViewById(R.id.tv_msg);
		msg.setText(text);
		Toast toast = new Toast(GlobalContext.getInstance());
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastView);
		toast.show();
	}

	public static final void showMes(int resId) {
		LinearLayout toastView = getToastView();
		TextView msg = (TextView) toastView.findViewById(R.id.tv_msg);
		msg.setText(resId);
		Toast toast = new Toast(GlobalContext.getInstance());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastView);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.show();
	}
}
