package com.rtmap.game.util;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by yxy on 2017/3/17.
 */
public class FileUtil {
    String filePath = "";
    private boolean isExternal = false;
    private boolean isLocal = false;


    public FileUtil() {
        if (Gdx.files.isExternalStorageAvailable()) {
            isExternal = true;
            filePath = Gdx.files.getExternalStoragePath();
        } else if (Gdx.files.isLocalStorageAvailable()) {
            isLocal = true;
            filePath = Gdx.files.getLocalStoragePath();
        }
    }

    public synchronized void setFile(String url, byte[] data) {
        FileHandle external = null;
        if (!TextUtils.isEmpty(filePath)) {
            if (isExternal) {
                external = Gdx.files.external(filePath + url);
            } else if (isLocal) {
                external = Gdx.files.local(filePath + url);
            }
            if (external != null) {
                external.writeBytes(data, false);
                Gdx.app.error("camera", "onPreviewFrame   " + external.path());
            }
        }
    }

    public synchronized byte[] getFile(String url) {
        FileHandle external = null;
        if (!TextUtils.isEmpty(filePath)) {
            if (isExternal) {
                external = Gdx.files.external(filePath + url);
            } else if (isLocal) {
                external = Gdx.files.local(filePath + url);
            }
            if (external != null)
                return external.readBytes();
        }
        return null;
    }
}
