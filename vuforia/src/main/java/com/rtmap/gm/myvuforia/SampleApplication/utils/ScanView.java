package com.rtmap.gm.myvuforia.SampleApplication.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.rtmap.gm.myvuforia.R;

import java.lang.ref.SoftReference;


/**
 * Created by yxy
 * on 2017/7/3.
 */

public class ScanView extends View implements ValueAnimator.AnimatorUpdateListener {

    private SoftReference<Context> contexts;
    private Bitmap mBitmap;
    private Bitmap mScaledBitmap;
    private float mHalfCornerSize;
    private int mCornerSize;
    private int mCornerLength;
    private int mBorderColor;
    private int mBorderSize;
    private int mBarcodeRectHeight;
    private int mTopOffset;
    private int mCornerColor;
    private int mRectWidth;
    private int mMaskColor;
    private Paint mPaint;
    private Rect mFramingRect;
    private int[] mDisplay;
    private boolean scanShow = true;

    private DecelerateInterpolator mInterpolator;

    private ValueAnimator animatorLine;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contexts = new SoftReference<>(context);
        Log.e("vuforia", "ScanView()");
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMaskColor = Color.parseColor("#55000000");
        mCornerColor = Color.parseColor("#3A93FF");
        mCornerLength = ScreenUtil.dp2px(contexts.get(), 20);
        mCornerSize = ScreenUtil.dp2px(contexts.get(), 3);

        mDisplay = ScreenUtil.getDisplay(contexts.get());
        mRectWidth = mDisplay[0] * 2 / 3;
        mBarcodeRectHeight = mRectWidth;
        mTopOffset = mDisplay[1] / 2 - mRectWidth / 2;

        mBorderSize = ScreenUtil.dp2px(contexts.get(), 1);
        mBorderColor = Color.parseColor("#5499D5");
        mHalfCornerSize = 1.0f * mCornerSize / 2;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scan);
        mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mRectWidth, mBarcodeRectHeight, false);

        mInterpolator = new DecelerateInterpolator();
    }

    public void recycle() {
        if (mScaledBitmap != null)
            mScaledBitmap.recycle();
        if (mBitmap != null)
            mBitmap.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int leftOffset = (getWidth() - mRectWidth) / 2;
        mFramingRect = new Rect(leftOffset, mTopOffset, leftOffset + mRectWidth, mTopOffset + mBarcodeRectHeight);

        if (animatorLine != null) {
            animatorLine.cancel();
            animatorLine = null;
        }

        animatorLine = ValueAnimator.ofInt(
                0, mBarcodeRectHeight)
                .setDuration(2000);
        animatorLine.setRepeatMode(ValueAnimator.RESTART);
        animatorLine.setRepeatCount(ValueAnimator.INFINITE);
        animatorLine.setInterpolator(mInterpolator);
        animatorLine.addUpdateListener(this);
    }

    public void setScanShow(boolean scanShow) {
        this.scanShow = scanShow;
        if (scanShow) {
            startAnim();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        drawBitmap(canvas);

        canvas.restore();
        // 画遮罩层
        drawMask(canvas);

        drawCornerLine(canvas);

        // 画边框线
        drawBorderLine(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private void drawBitmap(Canvas canvas) {
        int transDis = 0;
        if (animatorLine != null) {
            transDis = (int) animatorLine.getAnimatedValue();
        }
        canvas.clipRect(mFramingRect.left, mFramingRect.top, mFramingRect.right, mFramingRect.bottom);
        canvas.translate(0, transDis - mBarcodeRectHeight + mFramingRect.top);
        canvas.drawBitmap(mScaledBitmap, mFramingRect.left, 0, mPaint);

    }

    /**
     * 画遮罩层
     *
     * @param canvas
     */
    private void drawMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (mMaskColor != Color.TRANSPARENT) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mMaskColor);
            canvas.drawRect(0, 0, width, mFramingRect.top, mPaint);
            canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom + 1, mPaint);
            canvas.drawRect(mFramingRect.right + 1, mFramingRect.top, width, mFramingRect.bottom + 1, mPaint);
            canvas.drawRect(0, mFramingRect.bottom + 1, width, height, mPaint);
        }
    }

    /**
     * 画边框线
     *
     * @param canvas
     */
    private void drawBorderLine(Canvas canvas) {
        if (mBorderSize > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mBorderColor);
            mPaint.setStrokeWidth(mBorderSize);
            canvas.drawRect(mFramingRect, mPaint);
        }
    }

    /**
     * @param canvas
     */
    private void drawCornerLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCornerColor);
        mPaint.setStrokeWidth(mCornerSize);
        canvas.drawLine(mFramingRect.left - mHalfCornerSize, mFramingRect.top, mFramingRect.left - mHalfCornerSize + mCornerLength, mFramingRect.top, mPaint);
        canvas.drawLine(mFramingRect.left, mFramingRect.top - mHalfCornerSize, mFramingRect.left, mFramingRect.top - mHalfCornerSize + mCornerLength, mPaint);
        canvas.drawLine(mFramingRect.right + mHalfCornerSize, mFramingRect.top, mFramingRect.right + mHalfCornerSize - mCornerLength, mFramingRect.top, mPaint);
        canvas.drawLine(mFramingRect.right, mFramingRect.top - mHalfCornerSize, mFramingRect.right, mFramingRect.top - mHalfCornerSize + mCornerLength, mPaint);

        canvas.drawLine(mFramingRect.left - mHalfCornerSize, mFramingRect.bottom, mFramingRect.left - mHalfCornerSize + mCornerLength, mFramingRect.bottom, mPaint);
        canvas.drawLine(mFramingRect.left, mFramingRect.bottom + mHalfCornerSize, mFramingRect.left, mFramingRect.bottom + mHalfCornerSize - mCornerLength, mPaint);
        canvas.drawLine(mFramingRect.right + mHalfCornerSize, mFramingRect.bottom, mFramingRect.right + mHalfCornerSize - mCornerLength, mFramingRect.bottom, mPaint);
        canvas.drawLine(mFramingRect.right, mFramingRect.bottom + mHalfCornerSize, mFramingRect.right, mFramingRect.bottom + mHalfCornerSize - mCornerLength, mPaint);
    }


    private void startAnim() {
        if (animatorLine != null && !animatorLine.isRunning()) {
            animatorLine.start();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        postInvalidate();
    }


}
