package com.rtmap.game.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class GameStage extends Stage {
    private List<Stage> stageList = new ArrayList<Stage>();

    public GameStage() {
        this(new ScreenViewport());
    }

    public GameStage(Viewport viewport) {
        super(viewport);
//        stageList.add(this);
    }

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
//        stageList.add(this);
    }

    public void removeStage(Stage stage) {
        for (int i = 0; i < stageList.size(); i++) {
            if (stageList.get(i).equals(stage)) {
                stageList.get(i).dispose();
                stageList.remove(stage);
            }
        }
    }

    @Override
    public void clear() {
//        for (int i = stageList.size() - 1; i >= 0; i--) {
//            stageList.get(i).dispose();
//            stageList.remove(i);
//        }
        super.clear();
    }

//    @Override
//    public boolean equals(Object obj) {
//        GameStage obj1 = (GameStage) obj;
//        return obj1.getActors()
//    }
}
