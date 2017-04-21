package com.example.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SeekBar;

public class MySeekBar extends SeekBar {
	private final static String TAG = "MySeekBar";
	private final static int mMaxWidth = Integer.MAX_VALUE;
	private final static int mMaxHeight = Integer.MAX_VALUE;

	/**
	 * Thumb是否将track分离开
	 * */
	private boolean mSplitTrack = false;

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSplitTrack(true);
	}

	@SuppressLint("NewApi")
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawThumb(canvas);
		drawTrackUpper(canvas);
		if (isPressed()) {
			drawSeekText(canvas);
		}

	}

	private void drawSeekText(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
						.getDisplayMetrics()));
		Drawable thumbDrawable = getThumb();
		Log.i(TAG, "x is :" + thumbDrawable.getBounds().left + " y is :"
				+ thumbDrawable.getBounds().top);
		canvas.drawText(
				"" + getProgress(),
				thumbDrawable.getBounds().left
						+ (thumbDrawable.getBounds().right - thumbDrawable
								.getBounds().left)
						/ 2
						- TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
										.getDisplayMetrics()), getY(), mPaint);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		Drawable d = getProgressDrawable();
		Drawable mThumb = getThumb();

		int thumbHeight = (int) (mThumb == null ? 0 : mThumb
				.getIntrinsicHeight()
				+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
						getResources().getDisplayMetrics()));
		Log.i(TAG, "thumb height is :" + thumbHeight);
		int dw = 0;
		int dh = 0;
		if (d != null) {
			dw = Math.max(getMinimumWidth(),
					Math.min(mMaxWidth, d.getIntrinsicWidth()));
			dh = Math.max(getMinimumHeight(),
					Math.min(mMaxHeight, d.getIntrinsicHeight()));
			dh = Math.max(thumbHeight, dh);
		}
		dw += getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
		dh += thumbHeight + getPaddingTop() + getPaddingBottom();
		Log.i(TAG, "background height is :" + dh + "  background width is :"
				+ dh);

		setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
				resolveSizeAndState(dh, heightMeasureSpec, 0));

	}

	/**
	 * Draw the thumb.
	 */
	void drawThumb(Canvas canvas) {
		Drawable mThumb = getThumb();
		if (mThumb != null) {
			canvas.save();
			// Translate the padding. For the x, we need to allow the thumb to
			// draw in its extra space
			canvas.translate(
					getPaddingLeft() - getThumbOffset(),
					getPaddingTop()
							+ getMeasuredHeight()
							/ 2
							- (mThumb.getBounds().bottom - mThumb.getBounds().top)
							/ 2);
			mThumb.draw(canvas);
			canvas.restore();
		}
	}

	// void drawTrack(Canvas canvas) {
	// final Drawable thumbDrawable = getThumb();
	// if (thumbDrawable != null && mSplitTrack) {
	//
	//
	// Class<?> clazz = Drawable.class;
	// Method method = clazz.getDeclaredMethod("getOpticalInsets",
	// Insets.class);
	//
	// final Insets insets = (Insets) method.invoke(thumbDrawable, null);
	// final Rect tempRect = mTempRect;
	// thumbDrawable.copyBounds(tempRect);
	// tempRect.offset(getPaddingLeft() - getThumbOffset(),
	// getPaddingTop());
	// tempRect.left += insets.;
	// tempRect.right -= insets.right;
	//
	// final int saveCount = canvas.save();
	// canvas.clipRect(tempRect, Op.DIFFERENCE);
	// super.drawTrack(canvas);
	// canvas.restoreToCount(saveCount);
	// } else {
	// super.drawTrack(canvas);
	// }
	// }

	/**
	 * Draws the progress bar track.
	 */
	void drawTrackUpper(Canvas canvas) {
		final Drawable d = getProgressDrawable();
		if (d != null) {
			// Translate canvas so a indeterminate circular progress bar with
			// padding
			// rotates properly in its animation
			final int saveCount = canvas.save();
			canvas.translate(getPaddingLeft(), getPaddingTop());
			// if (isLayoutRtl() && mMirrorForRtl) {
			// canvas.translate(getWidth() - mPaddingRight, mPaddingTop);
			// canvas.scale(-1.0f, 1.0f);
			// } else {
			// canvas.translate(mPaddingLeft, mPaddingTop);
			// }

			// final long time = getDrawingTime();
			// if (mHasAnimation) {
			// mAnimation.getTransformation(time, mTransformation);
			// final float scale = mTransformation.getAlpha();
			// try {
			// mInDrawing = true;
			// d.setLevel((int) (scale * MAX_LEVEL));
			// } finally {
			// mInDrawing = false;
			// }
			// postInvalidateOnAnimation();
			// }

			d.draw(canvas);
			canvas.restoreToCount(saveCount);

			// if (mShouldStartAnimationDrawable && d instanceof Animatable) {
			// ((Animatable) d).start();
			// mShouldStartAnimationDrawable = false;
			// }
		}

	}
}
