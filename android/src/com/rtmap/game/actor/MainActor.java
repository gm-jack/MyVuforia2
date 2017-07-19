package com.rtmap.game.actor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class MainActor extends Actor {
    private AssetManager asset;
    private List<TextureRegion> texReArray = new ArrayList();
    private int width;
    private int height;
    //    private LazyBitmapFont lazyBitmapFont1;
    private float scale = 1;


    public MainActor(AssetManager assetManager) {
        super();
        this.asset = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (asset.update()) {
            initResource();
        }
        // 如果 region 为 null 或者 演员不可见, 则直接不绘制
        if (!isVisible()) {
            return;
        }
        if (texReArray.size() <= 0) return;
        batch.draw(texReArray.get(0), 0, 0, width, height);
        batch.draw(texReArray.get(1), width * 0.103f, height * 0.09f, width * 0.802f, height * 0.587f);
    }

    private void initResource() {
        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) asset.get("m_bg.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("m_rule.png")));

        scale = width / texReArray.get(0).getRegionWidth();
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void clear() {
//        if (lazyBitmapFont1 != null)
//            lazyBitmapFont1.dispose();
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
    }
}
