package com.rtmap.game.scrollpane;

import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by yxy on 2017/3/7.
 */
public class GestureListener extends GestureDetector.GestureAdapter {
    private BeedScrollPane scrollPanel;
    public void setScrollPanel(BeedScrollPane scrollPanel) {
        this.scrollPanel = scrollPanel;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (scrollPanel != null && scrollPanel.isVisible()) {
            scrollPanel.setScrollY(scrollPanel.getScrollY() - deltaY);
            return true;
        } else
            return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return super.panStop(x, y, pointer, button);
    }
}
