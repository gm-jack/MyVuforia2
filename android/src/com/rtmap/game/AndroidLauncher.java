package com.rtmap.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.SPUtil;
import com.rtmap.gm.myvuforia.ImageTargets.ImageTargets;

import java.lang.ref.SoftReference;


public class AndroidLauncher extends AndroidApplication {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    protected int origWidth;
    protected int origHeight;
    private AndroidDeviceCameraController androidDeviceCameraController;

    private static AndroidLauncher context;
    private static SoftReference<Context> contexts;
    private AssetManager asset;
    private String mPhone = "";
    private String mToken;
    private String mUserId;
    private int mPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        context = this;
        contexts = new SoftReference<Context>(context);

        Intent intent = getIntent();
        mPhone = intent.getStringExtra(Contacts.PHONE);
        mToken = intent.getStringExtra(Contacts.TOKEN);
        mUserId = intent.getStringExtra(Contacts.USERID);
        mPoint = intent.getIntExtra(Contacts.E_POINT, 0);
        if (mPhone == null)
            mPhone = "";
        if (mToken == null)
            mToken = "";
        if (mUserId == null)
            mUserId = "";
        SPUtil.put(this, Contacts.PHONE, mPhone);
        SPUtil.put(this, Contacts.TOKEN, mToken);
        SPUtil.put(this, Contacts.USERID, mUserId);
        SPUtil.put(this, Contacts.E_POINT, mPoint);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.a = 8;
        config.r = 8;
        config.g = 8;
        config.b = 8;
        androidDeviceCameraController = new AndroidDeviceCameraController(this);
        asset = new AssetManager();
        initialize(new MyGame(this, androidDeviceCameraController, asset), config);

        //设置日志等级
        Gdx.app.setLogLevel(LOG_NONE);
//        MobclickAgent.setCatchUncaughtExceptions(true);
//        MobclickAgent.setDebugMode(true);
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        graphics.getView().setKeepScreenOn(true);
        origWidth = graphics.getWidth();
        origHeight = graphics.getHeight();
    }

    public void post(Runnable r) {
        handler.post(r);
    }

    public void setFixedSize(int width, int height) {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(width, height);
        }
    }

    public void restoreFixedSize() {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(origWidth, origHeight);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
//        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (asset != null)
            asset.clear();
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stopPreviewAsync();
        }
        super.onDestroy();
    }

    public static Context getInstance() {
        return contexts.get();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void start() {
        startActivityForResult(new Intent(AndroidLauncher.this, ImageTargets.class), 101);
    }
}
