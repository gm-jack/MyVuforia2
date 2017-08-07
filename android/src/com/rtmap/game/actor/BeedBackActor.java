package com.rtmap.game.actor;

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
public class BeedBackActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private List<TextureRegion> beedList = new ArrayList<TextureRegion>();
    private float regionHeight;
    private float scale = 1;
    private float realWidth;
    private float realHeight;

    public BeedBackActor(AssetManager assetManager, GameBeedActor gameBeedActor) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        regionHeight = gameBeedActor.getTitleHeight();
        scale = gameBeedActor.getScale();
        initResources();
    }

    private void initResources() {
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_back.png")));

        realWidth = beedList.get(0).getRegionWidth() * scale;
        realHeight = beedList.get(0).getRegionHeight() * scale;
        setPosition(30, height - regionHeight / 2 - realHeight / 2);
        setSize(realWidth, realHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (beedList.size() > 0) {
            batch.draw(beedList.get(0), 30, height - regionHeight / 2 - realHeight / 2, realWidth, realHeight)
            ;
        }
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
