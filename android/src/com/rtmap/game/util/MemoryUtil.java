package com.rtmap.game.util;

import android.util.LruCache;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.nio.ByteBuffer;

/**
 * Created by yxy on 2017/3/17.
 */
public class MemoryUtil {
    private LruCache<String, Pixmap> lru;

    public MemoryUtil() {
        long size = Runtime.getRuntime().maxMemory() / 8;
        lru = new LruCache<String, Pixmap>((int) size) {
            @Override
            protected int sizeOf(String key, Pixmap value) {
                return value.getPixels().array().length;
            }
        };
    }

    public synchronized void setLru(String url, Pixmap pixmap) {
        lru.put(url, pixmap);
    }

    public synchronized Pixmap getLru(String url) {
        return lru.get(url);
    }
}
