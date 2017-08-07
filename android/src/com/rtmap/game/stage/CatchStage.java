package com.rtmap.game.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by yxy on 2017/2/21.
 */
public class CatchStage extends GameStage {
    public CatchStage() {
//        this(new StretchViewport());
    }

    public CatchStage(Viewport viewport) {
        super(viewport);
    }

    public CatchStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }
}
