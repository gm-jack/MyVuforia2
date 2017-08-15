package com.rtmap.game.interfaces;

/**
 * Created by yxy
 * on 2017/8/4.
 */

public interface LoadCompleteListener {
    void load(String path);

    void loadFail(String message);
}
