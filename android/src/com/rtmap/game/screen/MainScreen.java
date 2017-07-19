package com.rtmap.game.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidEventListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.CloseActor;
import com.rtmap.game.actor.MainActor;
import com.rtmap.game.actor.MyBeedActor;
import com.rtmap.game.actor.StartActor;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.stage.MainStage;

/**
 * Created by yxy on 2017/3/2.
 */
public class MainScreen extends MyScreen implements AndroidEventListener {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private MyBeedActor myBeedActor;
    private StartActor startActor;
    private MyGame mGame;
    private AndroidLauncher androidLauncher;
    private MainStage mainStage;
    private Group group;
    private MainActor mainActor;
    private AssetManager assetManager;
    private CloseActor mCloseActor;
    private boolean isFirst = true;


    public MainScreen(MyGame game, AndroidLauncher androidLauncher, ScreenViewport viewport) {
        this.mGame = game;
        this.androidLauncher = androidLauncher;

        androidLauncher.addAndroidEventListener(this);
        mainStage = new MainStage(viewport);
        assetManager = new AssetManager();

        initResources();

        group = new Group();
        mainActor = new MainActor(assetManager);
        mainActor.setPosition(0, 0);
        mainActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(mainActor);

        startActor = new StartActor(assetManager);
        group.addActor(startActor);

        mCloseActor = new CloseActor(assetManager);
        mCloseActor.setIsShow(true);
        group.addActor(mCloseActor);

        myBeedActor = new MyBeedActor(assetManager);
        group.addActor(myBeedActor);

        mainStage.addActor(group);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (androidLauncher.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                androidLauncher.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            }
        }
    }

    public void initResources() {
        assetManager.load("m_bg.png", Texture.class);
        assetManager.load("m_rule.png", Texture.class);
        assetManager.load("m_start.png", Texture.class);
        assetManager.load("open_close.png", Texture.class);
        assetManager.load("catch_bg.png", Texture.class);
        assetManager.load("main_beed.png", Texture.class);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(mainStage);
        if (startActor != null)
            startActor.setListener(new StartOnClickListener() {
                @Override
                public void onClick() {
//                    if (mGame != null) {
//                        mGame.showLoadingScreen();
//                    }
                    if (isFirst) {
                        isFirst = false;
                        androidLauncher.start();
                    }

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (androidLauncher.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        } else if (mGame != null) {
//                            group.removeActor(startActor);
//                            mGame.showLoadingScreen();
//                        }
//                    } else if (mGame != null) {
//                        group.removeActor(startActor);
//                        mGame.showLoadingScreen();
//                    }
                }
            });
        if (mCloseActor != null)
            mCloseActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    Gdx.app.exit();
                }
            });

        if (myBeedActor != null) {
            myBeedActor.setListener(new StartOnClickListener() {
                @Override
                public void onClick() {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            if (mGame != null)
                                mGame.showBeedScreen(MainScreen.this);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        if (mainStage == null)
            return;
        // 更新舞台逻辑
        mainStage.act();
        // 绘制舞台
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        initListener();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {


//        if (mGame != null && !isFirst) {
//            group.removeActor(startActor);
//            mGame.showLoadingScreen();
//        }
//        isFirst = false;
        Log.e("imagetarget", "resultCode    ");
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        androidLauncher.removeAndroidEventListener(this);
        assetManager.unload("m_bg.png");
        assetManager.unload("m_rule.png");
        assetManager.unload("m_start.png");
        assetManager.unload("open_close.png");
        assetManager.unload("catch_bg.png");
        assetManager.unload("main_beed.png");

        if (mainStage != null) {
            mainStage.dispose();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        isFirst = true;
        Log.e("android", "onActivityResult()   " + requestCode + "    " + resultCode);
        if (requestCode == 101) {
            if (data == null)
                return;
            boolean target = data.getBooleanExtra("target", false);
            if (target) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (mGame != null) {
                            mGame.showLoadingScreen();
                        }
                    }
                });
            }
        }

    }
}