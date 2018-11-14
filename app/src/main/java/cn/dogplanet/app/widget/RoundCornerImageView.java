package cn.dogplanet.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundCornerImageView extends android.support.v7.widget.AppCompatImageView{
	
	private final float density = getContext().getResources().getDisplayMetrics().density;
	private float roundness;
	private OnMeasureListener onMeasureListener;

	public RoundCornerImageView(Context context) {
		super(context);
		init();
	}

	public RoundCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundCornerImageView(Context context, AttributeSet attrs,
                                int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public void draw(Canvas canvas) {
		final Bitmap composedBitmap;
		final Bitmap originalBitmap;
		final Canvas composedCanvas;
		final Canvas originalCanvas;
		final Paint paint;
		final int height;
		final int width;
		width = getWidth();
		height = getHeight();
		composedBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_4444);
		originalBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_4444);
		composedCanvas = new Canvas(composedBitmap);
		originalCanvas = new Canvas(originalBitmap);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		super.draw(originalCanvas);
		composedCanvas.drawARGB(0, 0, 0, 0);
		composedCanvas.drawRoundRect(new RectF(0, 0, width, height),
				this.roundness, this.roundness, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);
		canvas.drawBitmap(composedBitmap, 0, 0, new Paint());
	}

	public float getRoundness() {
		return this.roundness / this.density;
	}

	public void setRoundness(float roundness) {
		this.roundness = roundness * this.density;
	}

	private void init() {
		setRoundness(5);
	}

	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		this.onMeasureListener = onMeasureListener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 将图片测量的大小回调到onMeasureSize()方法中
		if (onMeasureListener != null) {
			onMeasureListener.onMeasureSize(getMeasuredWidth(),
					getMeasuredHeight());
		}
	}

	public interface OnMeasureListener {

		void onMeasureSize(int width, int height);
	}
}
