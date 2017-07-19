package com.rtmap.game.actor;

import android.opengl.GLES20;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class GameBeedActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private List<TextureRegion> beedList = new ArrayList<>();
    private float scale = 1;


    public GameBeedActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_bg.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_title.png")));
        scale = (float) width / (float) beedList.get(1).getRegionWidth();
    }

    public float getTitleHeight() {
        float height = 0;
        if (beedList.size() > 0)
            height = beedList.get(1).getRegionHeight() * scale;
        return height;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        GLES20.glClearColor(0, 0, 0, 1f);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (beedList.size() <= 0) return;
        batch.draw(beedList.get(0), 0, 0, width, height);
        batch.draw(beedList.get(1), 0, height - beedList.get(1).getRegionHeight() * scale, width, beedList.get(1).getRegionHeight() * scale);
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        for (int i = 0; i < beedList.size(); i++) {
            beedList.get(i).getTexture().dispose();
        }

    }
}
