package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by yxy on 2017/2/21.
 */
public class CoverActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private TextureRegion normal;
    private Batch batch;
    private boolean isFirst = false;
    private TextureRegion tip;
    private TextureRegion catchs;
    private float tipWidth;
    private float tipHeight;
    private float catchsWidth;
    private float catchsHeight;

    public CoverActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    private void initResources() {

        normal = new TextureRegion((Texture) assetManager.get("catch_cover.png"));
        tip = new TextureRegion((Texture) assetManager.get("find_tip1.png"));
        catchs = new TextureRegion((Texture) assetManager.get("catch_catch.png"));

        float scale = (float) width / (float) normal.getRegionWidth();
        tipWidth = tip.getRegionWidth() * scale;
        tipHeight = tip.getRegionHeight() * scale;
        catchsWidth = catchs.getRegionWidth() * scale;
        catchsHeight = catchs.getRegionHeight() * scale;
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
        if (isFirst && normal != null && tip != null && catchs != null) {
            batch.draw(normal, 0, 0, width, height);
            batch.draw(tip, width / 2 - tipWidth / 2, height / 2 - tipHeight / 2, tipWidth, tipHeight);
            batch.draw(catchs, width / 2 - catchsWidth / 2, height / 2 - tipHeight / 2 + tipHeight / 5, catchsWidth, catchsHeight);
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
        if (tip != null)
            tip.getTexture().dispose();
        if (catchs != null)
            catchs.getTexture().dispose();
    }
}
