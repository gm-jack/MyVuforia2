package com.rtmap.gm.myvuforia.SampleApplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.rtmap.gm.myvuforia.R;

import java.lang.ref.SoftReference;

/**
 * Created by yxy
 * on 2017/7/29.
 */

public class ScanSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private LinearInterpolator mInterpolator;
    private SurfaceHolder mHolder;
    private Thread t;
    private boolean flag = true;

    private PorterDuffXfermode xfermode;
    private SoftReference<Context> contexts;
    private Bitmap mBitmap;
    private Bitmap mScaledBitmap;
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
    private float scale = 0.1f;
    private Matrix mMatrix1;
    private int totalW;
    private int totalH;
    private int mLength;
    private float mChange;
    private int alphas = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                alphas = 0;
                mReal = 0;
                mChange = mFramingRect.top - mBarcodeRectHeight;
            }
            postInvalidate();
        }
    };
    private float mReal;

    public ScanSurfaceView(Context context) {
        this(context, null);
    }

    public ScanSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHolder = getHolder();
        mHolder.addCallback(this);

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

        mLength = mBarcodeRectHeight;


        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mMatrix1 = new Matrix();
        mInterpolator = new LinearInterpolator();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public void recycle() {
        if (mScaledBitmap != null)
            mScaledBitmap.recycle();
        if (mBitmap != null)
            mBitmap.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        totalW = width;
        totalH = height;
        int leftOffset = (getWidth() - mRectWidth) / 2;
        mFramingRect = new Rect(leftOffset, mTopOffset, leftOffset + mRectWidth, mTopOffset + mBarcodeRectHeight);
        int change = mFramingRect.top - mBarcodeRectHeight - 20;
        if (mChange != change)
            mChange = change;
    }

    public void setScanShow(boolean scanShow) {
        this.scanShow = scanShow;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false; // 把线程运行的标识设置成false
    }

    @Override
    public void run() {
        while (flag) {
            Canvas canvas = null;
            try {
                flag = false;
                canvas = mHolder.lockCanvas(null);
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                    Log.e("scansurface", "scansurface   run");
                    doDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
                flag = true;
            }
        }
    }

    private void doDraw(Canvas canvas) {
        drawCornerLine(canvas);

        // 画边框线
        drawBorderLine(canvas);

        /**
         * 设置View的离屏缓冲。在绘图的时候新建一个“层”，所有的操作都在该层而不会影响该层以外的图像
         * 必须设置，否则设置的PorterDuffXfermode会无效，具体原因不明
         */
        int sc = canvas.saveLayer(0, 0, totalW, totalH, mPaint, Canvas.ALL_SAVE_FLAG);

        if (scanShow)
            drawBitmap(canvas);

        mPaint.setXfermode(xfermode);

        // 画遮罩层
        drawMask(canvas);

        mPaint.setXfermode(null);

        /**
         * 还原画布，与canvas.saveLayer配套使用
         */

        canvas.restoreToCount(sc);
    }

    private void drawBitmap(Canvas canvas) {
//        if (scale >= 1) {
//            scale = 0.1f;
//        }
//        scale += 0.01f;
//        mMatrix1.postScale(1f, scale);
        if (alphas > 255)
            alphas = 255;
        mPaint.setAlpha(alphas);
        if (mChange < mFramingRect.top) {
            Log.e("mChange", mInterpolator.getInterpolation(mReal) + "");
            mChange += mLength * mInterpolator.getInterpolation(mReal);
            mReal += 0.0005f;
//            mChange += mChanges;
//            mReal = mLength * mInterpolator.getInterpolation(mChange / mLength);
            alphas += 10;
            mHandler.sendEmptyMessage(0);
        } else {
            mHandler.sendEmptyMessageDelayed(1, 50);
        }

//        mMatrix1.postTranslate(mFramingRect.left, mChange);
        canvas.drawBitmap(mScaledBitmap, mFramingRect.left, mChange, mPaint);
//        mMatrix1.reset();
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
}
