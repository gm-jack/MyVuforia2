package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.SPUtil;

/**
 * Created by yxy on 2017/2/21.
 */
public class AgainActor extends Actor {
    private boolean firstCatch;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private boolean isShow = false;
    private TextureRegion mRegion;
    private float normalWidth;
    private float normalHeight;
    private boolean isFirst = true;

    public AgainActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        firstCatch = (boolean) SPUtil.get(Contacts.FIRST, true);
    }

    private void initResources() {
        mRegion = new TextureRegion((Texture) assetManager.get("catch_bg.png"));
        normal = new TextureRegion((Texture) assetManager.get("open_again.png"));
        float scale = (float) width / mRegion.getRegionWidth();
        normalWidth = normal.getRegionWidth() * scale;
        normalHeight = normal.getRegionHeight() * scale;
        setPosition(width / 2 - normalWidth / 2, height * 0.33f);
        setSize(normalWidth, normalHeight);
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public void setListener(final AgainOnClickListener againOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (againOnClickListener != null && isFirst) {
                    isFirst = false;
                    againOnClickListener.againClick();
//                    Gdx.app.exit();
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
        if (assetManager.update()) {
            initResources();
        }
        if (isShow)
            if (normal != null)
                batch.draw(normal, width / 2 - normalWidth / 2, height * 0.33f, normalWidth, normalHeight);
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
    }

    public interface AgainOnClickListener {
        void againClick();
    }
}
