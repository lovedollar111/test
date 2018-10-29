/**
*/
package cn.dogplanet.app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import cn.dogplanet.R;

/**
 *  带删除按钮的EditText
 * editor:ztr
 * package_name:cn.dogplanet.app.widget
 * file_name:EditTextWithDel.java
 * date:2016-12-6
 */
public class EditTextWithDel extends EditText {

	private Drawable imgAble;
	private Context mContext;
	//是否可以删除
	private boolean isDel=true;

	public EditTextWithDel(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public EditTextWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		imgAble = mContext.getResources().getDrawable(R.mipmap.clear);
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
	}

	//设置删除图片  
	private void setDrawable() {
		if (length() >= 1)
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
		else{
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
	}

	public void setIsDel(boolean isDel){
		this.isDel=isDel;
	}
	
	// 处理删除事件  
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP && isDel) {
			int eventX = (int) event.getRawX();
			int eventY = (int) event.getRawY();
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			rect.left = rect.right - 50;
			if (rect.contains(eventX, eventY)){
				setText("");
			}
			setDrawable();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
