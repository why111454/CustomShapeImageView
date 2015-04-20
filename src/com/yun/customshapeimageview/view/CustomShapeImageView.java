package com.yun.customshapeimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yun.customshapeimageview.R;

/**
 * 自定义形状的imageview
 * 
 * @author yunye
 * 
 */
public class CustomShapeImageView extends ImageView {
	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;
	private int shape;
	private static final int ROUND = 0;
	private static final int SECTOR = 1;
	private static final int ELLIPSE = 2;
	private static final int STAR = 3;
	private static final int ROUND_RECT = 4;
	private static final int EQUILTERAL_TRIANGLE = 5;

	static {
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}

	public CustomShapeImageView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		TypedArray typedArray = paramContext.obtainStyledAttributes(
				paramAttributeSet, R.styleable.customshape);
		shape = typedArray.getInteger(R.styleable.customshape_shape, ROUND);
	}

	private float degree2Radian(float degree){
		return (float) (Math.PI * degree / 180);
	}
	
	public Bitmap createShape() {
		Bitmap localBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint(1);
		localPaint.setColor(-16777216);
		switch (shape) {
		case ROUND:
			RectF rectf2 = new RectF(0.0F, 0.0F, getWidth(), getWidth());
			localCanvas.drawOval(rectf2, localPaint);
			break;
		case ELLIPSE:
			RectF rectf3 = new RectF(0.0F, 0.0F, getWidth(), getHeight());
			localCanvas.drawOval(rectf3, localPaint);
			break;
		case STAR:
			Path path = new Path();
			float radian = degree2Radian(36);
			float radius = getWidth()/2;
			float radius_in = (float) (radius * Math.sin(radian / 2) / Math
					.cos(radian));
			path.moveTo((float) (radius * Math.cos(radian / 2)), 0);
			path.lineTo((float) (radius * Math.cos(radian / 2) + radius_in
					* Math.sin(radian)),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) * 2),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) + radius_in
					* Math.cos(radian / 2)),
					(float) (radius + radius_in * Math.sin(radian / 2)));
			path.lineTo(
					(float) (radius * Math.cos(radian / 2) + radius
							* Math.sin(radian)), (float) (radius + radius
							* Math.cos(radian)));
			path.lineTo((float) (radius * Math.cos(radian / 2)),
					(float) (radius + radius_in));
			path.lineTo(
					(float) (radius * Math.cos(radian / 2) - radius
							* Math.sin(radian)), (float) (radius + radius
							* Math.cos(radian)));
			path.lineTo((float) (radius * Math.cos(radian / 2) - radius_in
					* Math.cos(radian / 2)),
					(float) (radius + radius_in * Math.sin(radian / 2)));
			path.lineTo(0, (float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) - radius_in
					* Math.sin(radian)),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.close();
			localCanvas.drawPath(path, localPaint);
			break;
		case ROUND_RECT:
			RectF rectf1 = new RectF(0.0F, 0.0F, getWidth(), getHeight());
			localCanvas.drawRoundRect(rectf1, 10, 10, localPaint);
			break;
		case EQUILTERAL_TRIANGLE:
			Path path1 = new Path();
			path1.moveTo(getWidth() / 2, 0);
			path1.lineTo(0, getWidth());
			path1.lineTo(getWidth(), getWidth());
			path1.close();
			localCanvas.drawPath(path1, localPaint);
			break;
		case SECTOR:
			localCanvas.drawArc(new RectF(0, 0, getWidth(), getHeight()), 150,
					160, true, localPaint);
			break;
		default:
			break;
		}

		return localBitmap;
	}

	protected void onDraw(Canvas paramCanvas) {
		Drawable localDrawable = getDrawable();
		if (localDrawable == null)
			return;
		try {
			if (this.paint == null) {
				Paint localPaint1 = new Paint();
				this.paint = localPaint1;
				this.paint.setFilterBitmap(false);
				this.paint.setXfermode(MASK_XFERMODE);
			}
			int count = paramCanvas.saveLayer(0.0F, 0.0F, getWidth(),
					getHeight(), null, 31);
			localDrawable.setBounds(0, 0, getWidth(), getHeight());
			localDrawable.draw(paramCanvas);
			if ((this.mask == null) || (this.mask.isRecycled())) {
				Bitmap localBitmap1 = createShape();
				this.mask = localBitmap1;
			}
			paramCanvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
			paramCanvas.restoreToCount(count);
			return;
		} catch (Exception localException) {
			StringBuilder localStringBuilder = new StringBuilder()
					.append("Attempting to draw with recycled bitmap. View ID = ");
			System.out.println("localStringBuilder==" + localStringBuilder);
		}
	}
}