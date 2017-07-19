package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.text.NativeFont;
import com.rtmap.game.text.NativeFontPaint;
import com.rtmap.game.util.ScreenUtil;

/**
 * Created by yxy on 2017/2/21.
 */
public class ToastActor extends Actor {
    private int width;
    private int height;
    private boolean isShow = false;
    private Texture mTexture;
    private NativeFont mFont;
    private String mMessage = "";

    public ToastActor() {
        super();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }


    public void setIsShow(boolean isShow, String message) {
        this.mMessage = message;
        this.isShow = isShow;
        if (isShow) {
            MoveByAction action = Actions.moveBy(0, 150, 1000);
            MoveByAction actions = Actions.moveBy(0, -150, 1000);
            SequenceAction acltionAll = Actions.sequence(action, actions);
            acltionAll.setActor(this);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible() && !isShow) {
            return;
        }
        if (mFont == null)
            mFont = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(12), Color.WHITE));
        mFont.appendText(mMessage);
        mFont.draw(batch, mMessage, 0, height, width, Align.center, true);
        batch.draw(createBackgroundTexture(), 0, height, width, 150);
    }

    /**
     * 创建文本框的背景纹理
     */
    private Texture createBackgroundTexture() {
        Pixmap pixmap = new Pixmap(width, 150, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 1, 1);
        pixmap.drawRectangle(0, height, pixmap.getWidth(), pixmap.getHeight());
        mTexture = new Texture(pixmap);
        pixmap.dispose();
        return mTexture;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (mTexture != null) {
            mTexture.dispose();
        }
        if (mFont != null) {
            mFont.dispose();
        }
    }
}
