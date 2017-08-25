package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.screen.AimScreen;
import com.rtmap.game.screen.BeedScreen;
import com.rtmap.game.screen.CatchScreen;
import com.rtmap.game.screen.LoadingScreen;
import com.rtmap.game.screen.MainScreen;
import com.rtmap.game.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class MyGame extends Game {

    private AndroidDeviceCameraController androidDeviceCameraController;
    private AndroidLauncher androidLauncher;
    private List<Screen> screenList;
    private LoadingScreen loadingScreen;
    private Screen oldScreen;
    private CatchScreen catchScreen;

    /**
     * 设置相机模式
     */
    public static int normal_Mode = 0;
    public static int prepare_Mode = 1;
    public static int preview_Mode = 2;

    private static int mode = normal_Mode;
    public AssetManager asset;
    private MainScreen mainScreen;
    private AimScreen aimScreen;
    private ScreenViewport mViewport;
    private boolean cameraShow = false;
    private boolean mToggle;
    private BeedScreen mBeedScreen;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController, AssetManager asset) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;
        this.asset = asset;
        screenList = new ArrayList<Screen>();
        mToggle = (Boolean) SPUtil.get("ar_toggle", true);
    }

    @Override
    public void create() {
        mViewport = new ScreenViewport();
        if (mToggle)
            showMainScreen();
        else
            showLoadingScreen();
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        if (!(getScreen() instanceof MainScreen || getScreen() instanceof BeedScreen)) {
            if (mode == normal_Mode) {
                if (androidDeviceCameraController != null) {
                    Gdx.app.error("gdx", "normal_Mode");
                    androidDeviceCameraController.prepareCameraAsync(cameraShow);
                    mode = prepare_Mode;
                }
            }
            if (mode == prepare_Mode) {
                if (androidDeviceCameraController != null)
                    if (androidDeviceCameraController.isReady()) {
                        Gdx.app.error("gdx", "prepare_Mode");
                        androidDeviceCameraController.startPreviewAsync();
                        mode = preview_Mode;
                    }
            }
        }
        super.render();
    }

    @Override
    public void resume() {
        super.resume();
        if (mode != normal_Mode)
            mode = normal_Mode;
    }

    @Override
    public void pause() {
        super.pause();
        stopCamera();
    }

    public void stopCamera(boolean cameraShow) {
        this.cameraShow = cameraShow;
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stoPreviewAsync();
            mode = preview_Mode;
        }
    }

    public void stopCamera() {
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stopPreviewAsync();
            mode = preview_Mode;
        }
    }

    @Override
    public void dispose() {
        stopCamera();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);
            if (screen != null) {
                screen.dispose();
                screenList.remove(i);
            }
        }
    }

    public void showCatchScreen() {
        catchScreen = new CatchScreen(this, androidLauncher, mViewport);
        setScreen(catchScreen);
    }

    public void showMainScreen() {
        stopCamera();
        if (mode != normal_Mode)
            mode = normal_Mode;
//        mBeedScreen = new BeedScreen(this, androidLauncher, mViewport);
        mainScreen = new MainScreen(this, androidLauncher, mViewport);
        setScreen(mainScreen);

    }

    public void showOldScreen() {
        if (mode != normal_Mode)
            mode = normal_Mode;
        if (oldScreen == null)
            return;
        setScreen(oldScreen);
    }

    public void showBeedScreen(Screen oldScreen) {
        mode = preview_Mode;
        this.oldScreen = oldScreen;
        setScreen(new BeedScreen(this, androidLauncher, mViewport));
    }

    public void showLoadingScreen() {
        if (mode != normal_Mode)
            mode = normal_Mode;
        loadingScreen = new LoadingScreen(this, mViewport, androidDeviceCameraController);
        setScreen(loadingScreen);
    }


    public void showAimScreen(boolean fail, boolean isCatch) {
        if (isCatch && mode != normal_Mode) {
            mode = normal_Mode;
        }
        aimScreen = new AimScreen(this, androidLauncher, mViewport);
        setScreen(aimScreen);
        aimScreen.setIsFail(fail);
    }
}
