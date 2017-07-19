package com.rtmap.game.util;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yxy
 * on 2017/6/9.
 */

public class GdxUtil {
    public static int getNum() {
        String timeNum = (String) SPUtil.get(Contacts.TIME_NUM, "");
        if (!TextUtils.isEmpty(timeNum)) {
            String[] split = timeNum.split(":");
            Gdx.app.error("dialog", "getTime()    " + getTime() + "   split[0]  " + split[0]);
            if (getTime().equals(split[0]) && split[2].equals(SPUtil.get(Contacts.PHONE, ""))) {
                int i = Integer.parseInt(split[1]);
                return i;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return format;
    }
}
