package com.rtmap.game.actor;

import android.graphics.Paint;
import android.graphics.Rect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.util.ScreenUtil;

/**
 * Created by yxy on 2017/2/21.
 */
public class UseRuleActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private boolean isFirst = true;

    private TextureRegion mUse;
    private final Rect mRect;

    public UseRuleActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        String test = "使用规则";
        mRect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(ScreenUtil.dp2px(14));
        paint.getTextBounds(test, 0, test.length(), mRect);
    }

    public void setListener(final StartOnClickListener startOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.error("touch", "touchDown");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.error("touch", "touchUp");
                if (startOnClickListener != null) {
                    startOnClickListener.onClick();
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
            initResouces();
            isFirst = false;
        }

      if(mUse!=null)
        batch.draw(mUse, width / 2 - mRect.width() / 2, height * 0.11f + height * 0.8f * 0.14f, mRect.width(),  mRect.height());

    }

    private void initResouces() {
        mUse = new TextureRegion((Texture) assetManager.get("use.png"));
        setPosition(width / 2 - mRect.width() / 2, height * 0.11f + height * 0.8f * 0.14f);
        setSize(mRect.width(), mRect.height());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (mUse != null)
            mUse.getTexture().dispose();
    }
}
