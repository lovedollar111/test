package cn.dogplanet.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class MyScrollview extends ScrollView {
	private int downX;
	private int downY;
	private int mTouchSlop;
	private Boolean isCancel = true;
	private OnScrollListener listener;

	public MyScrollview(Context context) {
		super(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public MyScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (isCancel) {
			return super.onTouchEvent(ev);
		} else {
			super.onTouchEvent(ev);
			return true;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		if (isCancel) {
			int action = e.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) e.getRawX();
				downY = (int) e.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) e.getRawY();
				if (Math.abs(moveY - downY) > mTouchSlop) {
					return true;
				}
			}
			return super.onInterceptTouchEvent(e);
		} else {
			return false;
		}
	}

	public interface OnScrollListener{
		void onScroll(int scrollY);
	}

	public void setOnScrollListener(OnScrollListener listener) {
		this.listener = listener;
	}

	//重写原生onScrollChanged方法，将参数传递给接口，由接口传递出去
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(listener != null){
			//这里我只传了垂直滑动的距离
			listener.onScroll(t-oldt);
		}
	}
}
