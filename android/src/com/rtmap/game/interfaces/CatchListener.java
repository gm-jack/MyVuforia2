package com.rtmap.game.interfaces;

/**
 * Created by yxy on 2017/2/21.
 */
public interface CatchListener {

    void onFirst();

    void onSuccess();

    void onFail();

    void onNumberFail(int number);

    void onTouched(int num);
}
