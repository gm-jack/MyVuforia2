package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.BackOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class BackActor extends Actor {
    private boolean isAnimation;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private TextureRegion press;
    private boolean isDown = false;
    private float changeRadius;
    private float regionWidth;
    private float regionHeight;
    private float nums = 30;
    private boolean isFirst = true;
    private TextureRegion mScaleTex;
    private float scale;
    private boolean isFirstTouch = true;

    public BackActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public BackActor(AssetManager asset, boolean isAnimation) {
        this.assetManager = asset;
        this.isAnimation = isAnimation;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    private void initResources() {
        mScaleTex = new TextureRegion((Texture) assetManager.get("anim_bg_top.png"));
        scale = (float) width / mScaleTex.getRegionWidth();

        normal = new TextureRegion((Texture) assetManager.get("find_left_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("find_left_press.png"));
        regionWidth = normal.getRegionWidth() * scale;
        changeRadius = regionWidth;
        regionHeight = normal.getRegionHeight() * scale;
        setPosition(0, 0);
        setSize(regionWidth, regionHeight);

    }

    public void setListener(final BackOnClickListener backOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                if (backOnClickListener != null && isFirstTouch) {
                    isFirstTouch = false;
                    backOnClickListener.onClick();
                }
            }

        };
        addListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (assetManager.update() && isFirst) {
            initResources();
            isFirst = false;
        }
        if (press == null || normal == null) {
            return;
        }
        if (isAnimation) {
            batch.draw(normal, 0 - changeRadius, 0, regionWidth, regionHeight);
            if (changeRadius > 0) {
                changeRadius -= regionWidth / nums;
            }
        } else {
            if (!isDown)
                batch.draw(press, 0, 0, press.getRegionWidth() * scale, press.getRegionHeight() * scale);
            else
                batch.draw(normal, 0, 0, normal.getRegionWidth() * scale, normal.getRegionHeight() * scale);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (normal != null)
            normal.getTexture().dispose();
        if (press != null)
            press.getTexture().dispose();
    }
}
