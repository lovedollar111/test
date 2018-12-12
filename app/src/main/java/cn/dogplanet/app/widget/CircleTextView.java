package cn.dogplanet.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;

import cn.dogplanet.R;

/**
 * 圆形背景TextView
 * @author sh
 *
 */
public class CircleTextView extends android.support.v7.widget.AppCompatTextView {

	private Paint mBgPaint = new Paint();

	PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

	public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBgPaint.setColor(getResources().getColor(R.color.txt_selected_color));
		mBgPaint.setAntiAlias(true);
	}

	public CircleTextView(Context context) {
		super(context);
		mBgPaint.setColor(getResources().getColor(R.color.txt_selected_color));
		mBgPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		int max = Math.max(measuredWidth, measuredHeight);
		setMeasuredDimension(max, max);
	}

	@Override
	public void setBackgroundColor(int color) {
		mBgPaint.setColor(color);
	}

	/**
	 * 设置通知个数显示
	 * 
	 * @param text
	 */
	public void setNotifiText(int text) {
		setText(String.format("%d", text));
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.setDrawFilter(pfd);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2,
				Math.max(getWidth(), getHeight()) / 2, mBgPaint);
		super.draw(canvas);
	}
}