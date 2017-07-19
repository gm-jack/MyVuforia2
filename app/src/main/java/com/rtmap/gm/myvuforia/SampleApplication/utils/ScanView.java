package com.rtmap.gm.myvuforia.SampleApplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rtmap.gm.myvuforia.R;


/**
 * Created by yxy
 * on 2017/7/3.
 */

public class ScanView extends View {

    private final Bitmap mBitmap;
    private float mHalfCornerSize;
    private int mCornerSize;
    private int mCornerLength;
    private int mBorderColor;
    private int mBorderSize;
    private int mBarcodeRectHeight;
    private float scaleHeight = 0;
    private int mTopOffset;
    private int mCornerColor;
    private int mRectWidth;
    private int mMaskColor;
    private Paint mPaint;
    private Rect mFramingRect;
    private float smallbig = 0;
    private int[] mDisplay;
    private boolean scanShow = true;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMaskColor = Color.parseColor("#55000000");
        mCornerColor = Color.parseColor("#3A93FF");
        mCornerLength = ScreenUtil.dp2px(context, 20);
        mCornerSize = ScreenUtil.dp2px(context, 3);

        mDisplay = ScreenUtil.getDisplay(context);
        mRectWidth = mDisplay[0] * 2 / 3;
        mBarcodeRectHeight = mRectWidth;
        mTopOffset = mDisplay[1] / 2 - mRectWidth / 2;

        mBorderSize = ScreenUtil.dp2px(context, 1);
        mBorderColor = Color.parseColor("#5499D5");
        mHalfCornerSize = 1.0f * mCornerSize / 2;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scan);
//        mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mRectWidth, mBarcodeRectHeight, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int leftOffset = (getWidth() - mRectWidth) / 2;
        mFramingRect = new Rect(leftOffset, mTopOffset, leftOffset + mRectWidth, mTopOffset + mBarcodeRectHeight);
    }

    public void setScanShow(boolean scanShow) {
        this.scanShow = scanShow;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画遮罩层
        drawMask(canvas);

        // 画边框线
        drawBorderLine(canvas);

        // 画四个直角的线
        drawCornerLine(canvas);
        if (scanShow)
            drawBitmap(canvas);
    }

    public Bitmap bigBitmap() {
        if (scaleHeight < mBarcodeRectHeight) {
            scaleHeight += mBarcodeRectHeight / 100;
        } else {
            scaleHeight = mBarcodeRectHeight / 100;
        }
        return Bitmap.createScaledBitmap(mBitmap, mRectWidth, (int) scaleHeight, false);
    }

    private void drawBitmap(Canvas canvas) {
//        Matrix matrix = new Matrix();
//        matrix.postScale()
        canvas.drawBitmap(bigBitmap(), mFramingRect.left, mFramingRect.top, mPaint);
        postInvalidate();
//        canvas.drawBitmap(mScaledBitmap,new Matrix(), mPaint);
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
     * 画四个直角的线
     *
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
}
