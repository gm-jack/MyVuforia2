package com.rtmap.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.stage.GameStage;
import com.rtmap.game.stage.LoadingStage;

import java.util.Timer;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingScreen extends MyScreen {

    private AndroidDeviceCameraController cameraController;
    private MyGame mGame;
    private GameStage loadingStage;
    private LoadingActor loadingActor;
    private AssetManager assetManager;
    private boolean isFirst = true;
    private Timer timer;

    public LoadingScreen(MyGame game, ScreenViewport viewport, AndroidDeviceCameraController androidDeviceCameraController) {
        super(game);
        this.mGame = game;
        this.cameraController = androidDeviceCameraController;
        assetManager = new AssetManager();

        initLoadingAsset();
        initAssets();
        timer = new Timer();
        loadingStage = new LoadingStage(viewport);

        loadingActor = new LoadingActor(assetManager, cameraController,mGame);
        loadingActor.setPosition(0, 0);
        loadingActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        loadingStage.addActor(loadingActor);
    }

    private void initLoadingAsset() {
        assetManager.load("main_bg.png", Texture.class);
        assetManager.load("loading_center.png", Texture.class);
        assetManager.load("loading_in.png", Texture.class);
        assetManager.load("loading_out.png", Texture.class);
        assetManager.load("loading_rotate.png", Texture.class);
        assetManager.load("loading_tip.png", Texture.class);
        assetManager.load("loading_wait.png", Texture.class);
    }

    public void initAssets() {
        mGame.asset.load("beed_open_bg.png", Texture.class);
        mGame.asset.load("find_bg.png", Texture.class);
        mGame.asset.load("aim_fail.png", Texture.class);
        mGame.asset.load("aim_success.png", Texture.class);
        mGame.asset.load("aim_red.png", Texture.class);
        mGame.asset.load("aim_blue.png", Texture.class);
        mGame.asset.load("aim_line.png", Texture.class);
        mGame.asset.load("anim_bg_top.png", Texture.class);
        mGame.asset.load("anim_bg_left.png", Texture.class);
        mGame.asset.load("anim_bg_right.png", Texture.class);
        mGame.asset.load("anim_bg_bottom.png", Texture.class);
        mGame.asset.load("find_center.png", Texture.class);
        mGame.asset.load("find_tip.png", Texture.class);
        mGame.asset.load("find_text.png", Texture.class);
        mGame.asset.load("find_left_normal.png", Texture.class);
        mGame.asset.load("find_left_press.png", Texture.class);
        mGame.asset.load("find_right_normal.png", Texture.class);
        mGame.asset.load("find_right_press.png", Texture.class);
        mGame.asset.load("find_circle.png", Texture.class);
        mGame.asset.load("find_anim.png", Texture.class);
        mGame.asset.load("catch_bg.png", Texture.class);
        mGame.asset.load("catch_center.png", Texture.class);
        mGame.asset.load("catch_circle.png", Texture.class);
        mGame.asset.load("catch_good.png", Texture.class);
        mGame.asset.load("catch_miss.png", Texture.class);
        mGame.asset.load("catch_tip.png", Texture.class);
        mGame.asset.load("catch_tip_text.png", Texture.class);
        mGame.asset.load("success_title.png", Texture.class);
        mGame.asset.load("success_center.png", Texture.class);
        mGame.asset.load("open_bg.png", Texture.class);
        mGame.asset.load("open_title.png", Texture.class);
        mGame.asset.load("open_line.png", Texture.class);
        mGame.asset.load("open_fail.png", Texture.class);
        mGame.asset.load("catch_cover.png", Texture.class);
        mGame.asset.load("catch_catch.png", Texture.class);
        mGame.asset.load("catch_button_normal.png", Texture.class);
        mGame.asset.load("catch_button_press.png", Texture.class);
        mGame.asset.load("success_open_normal.png", Texture.class);
        mGame.asset.load("success_open_press.png", Texture.class);
        mGame.asset.load("open_close.png", Texture.class);
        mGame.asset.load("open_again.png", Texture.class);
        mGame.asset.load("cover.png", Texture.class);
        mGame.asset.load("tiger/laohu-zhuaqu.g3dj", Model.class);
        mGame.asset.load("tiger/laohu-huanhu.g3dj", Model.class);
        mGame.asset.load("tiger/laohu-pao.g3dj", Model.class);
//        mGame.asset.load("feng/fengguang.g3dj", Model.class);
//        mGame.asset.load("tiger/tiger.g3dj", Model.class);
//        mGame.asset.load("tiger/knight.g3dj", Model.class);
    }

    @Override
    public void show() {

    }

    private float percent;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        percent = Interpolation.linear.apply(percent, mGame.asset.getProgress(), 0.1f);
        Gdx.app.log("percent", "percent---->" + percent);
        if (mGame.asset.update() && loadingActor != null && !loadingActor.isAnimation() && isFirst) {
            loadingActor.setIsEndAnimation(true);
            isFirst = false;
            return;
        }

        // 更新舞台逻辑
        loadingStage.act();
        loadingStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        setStopRerder(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (loadingStage != null) {
            loadingStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
        unLoadAsset();
    }

    private void unLoadAsset() {
        assetManager.unload("main_bg.png");
        assetManager.unload("loading_center.png");
        assetManager.unload("loading_in.png");
        assetManager.unload("loading_out.png");
        assetManager.unload("loading_rotate.png");
        assetManager.unload("loading_tip.png");
        assetManager.unload("loading_wait.png");
        mGame.asset.unload("beed_open_bg.png");
        mGame.asset.unload("find_bg.png");
        mGame.asset.unload("aim_fail.png");
        mGame.asset.unload("aim_success.png");
        mGame.asset.unload("aim_red.png");
        mGame.asset.unload("aim_blue.png");
        mGame.asset.unload("aim_line.png");
        mGame.asset.unload("anim_bg_top.png");
        mGame.asset.unload("anim_bg_left.png");
        mGame.asset.unload("anim_bg_right.png");
        mGame.asset.unload("anim_bg_bottom.png");
        mGame.asset.unload("find_center.png");
        mGame.asset.unload("find_tip.png");
        mGame.asset.unload("find_text.png");
        mGame.asset.unload("find_left_normal.png");
        mGame.asset.unload("find_left_press.png");
        mGame.asset.unload("find_right_normal.png");
        mGame.asset.unload("find_right_press.png");
        mGame.asset.unload("find_circle.png");
        mGame.asset.unload("find_anim.png");
        mGame.asset.unload("catch_bg.png");
        mGame.asset.unload("catch_center.png");
        mGame.asset.unload("catch_circle.png");
        mGame.asset.unload("catch_good.png");
        mGame.asset.unload("catch_miss.png");
        mGame.asset.unload("catch_tip.png");
        mGame.asset.unload("catch_tip_text.png");
        mGame.asset.unload("success_title.png");
        mGame.asset.unload("success_center.png");
        mGame.asset.unload("open_bg.png");
        mGame.asset.unload("open_title.png");
        mGame.asset.unload("open_line.png");
        mGame.asset.unload("open_fail.png");
        mGame.asset.unload("catch_cover.png");
        mGame.asset.unload("catch_catch.png");
        mGame.asset.unload("catch_button_normal.png");
        mGame.asset.unload("catch_button_press.png");
        mGame.asset.unload("success_open_normal.png");
        mGame.asset.unload("success_open_press.png");
        mGame.asset.unload("open_close.png");
        mGame.asset.unload("open_again.png");
        mGame.asset.unload("cover.png");
        mGame.asset.unload("tiger/laohu-zhuaqu.g3dj");
        mGame.asset.unload("tiger/laohu-huanhu.g3dj");
        mGame.asset.unload("tiger/laohu-pao.g3dj");
//        mGame.asset.unload("feng/fengguang.g3dj");
//        mGame.asset.unload("tiger/knight.g3dj");
//        mGame.asset.unload("tiger/tiger.g3dj");
    }
}
