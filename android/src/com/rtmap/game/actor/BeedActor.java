package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.BeedOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedActor extends Actor {
    private boolean isAnimation;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private TextureRegion press;
    private boolean isDown = false;
    private float changeRadius = 0;
    private boolean isFirst = true;
    private float regionWidth;
    private float regionHeight;
    private int nums = 30;
    private TextureRegion mScaleTex;
    private float scale;

    public BeedActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public BeedActor(AssetManager asset, boolean isAnimation) {
        super();
        this.assetManager = asset;
        this.isAnimation = isAnimation;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    private void initResources() {
        mScaleTex = new TextureRegion((Texture) assetManager.get("anim_bg_top.png"));
        scale = (float) width / mScaleTex.getRegionWidth();

        normal = new TextureRegion((Texture) assetManager.get("find_right_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("find_right_press.png"));

        regionWidth = normal.getRegionWidth() * scale;
        regionHeight = normal.getRegionHeight() * scale;

        setPosition(width - regionHeight, 0);
        setSize(regionWidth, regionHeight);
    }

    public void setListener(final BeedOnClickListener beedOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                if (beedOnClickListener != null && isVisible()) {
                    beedOnClickListener.onClick();
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
            batch.draw(normal, width - changeRadius, 0, regionWidth, regionHeight);
            if (changeRadius < regionWidth) {
                changeRadius += regionWidth / nums;
            }
        } else {
            if (!isDown)
                batch.draw(press, width - press.getRegionWidth() * scale, 0, press.getRegionWidth() * scale, press.getRegionHeight() * scale);
            else
                batch.draw(normal, width - normal.getRegionWidth() * scale, 0, normal.getRegionWidth() * scale, normal.getRegionHeight() * scale);
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
