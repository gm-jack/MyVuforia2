package com.rtmap.game.scrollpane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * Created by yxy on 2017/3/7.
 */
public class BeedScrollPane extends ScrollPane {

    public BeedScrollPane(Actor widget) {
        super(widget);
    }

    public BeedScrollPane(Actor widget, Skin skin) {
        super(widget, skin);
    }

    public BeedScrollPane(Actor widget, ScrollPaneStyle style) {
        super(widget, style);
    }



}
