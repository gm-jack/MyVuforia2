package com.rtmap.game.actor;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.BeedItemOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.NativeFont;
import com.rtmap.game.text.NativeFontPaint;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedItemActor extends Actor {

    private Result result;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private List<TextureRegion> normal = new ArrayList<TextureRegion>();
    private float scale;
    private float realHeight;
    private boolean isUse = false;
    private Texture texture;
    private NativeFont lazyBitmapFont1;
    private NativeFont lazyBitmapFont2;
    private float radius = 0;
    private Pixmap picture;
    private boolean isNet = true;
    private int netCount = 0;

    public BeedItemActor(AssetManager assetManager, Result result) {
        super();
        this.result = result;
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        initResources();

        radius = width * 0.173f;


    }

    private void initResources() {
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_bg.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_nouse.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_use.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_line.png")));


        scale = (float) width / (float) normal.get(0).getRegionWidth();
        realHeight = normal.get(0).getRegionHeight() * scale;
    }

    public void setListener(final BeedItemOnClickListener beedItemOnClickListener, int item) {
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (beedItemOnClickListener != null) {
                    beedItemOnClickListener.onClick(BeedItemActor.this, result);
                }
            }
        };
        addListener(listener);
    }

    public void removeThisListener() {
        removeListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (normal.size() <= 0) return;

        batch.draw(normal.get(0), 0, getY() + realHeight * 0.04f, getOriginX(), getOriginY(), width, realHeight, getScaleX(), getScaleY(), getRotation());
//        if (isUse)
//            batch.draw(normal.get(2), width - normal.get(2).getRegionWidth() * 3 / 2, getY() + realHeight * 0.3f / 2 - normal.get(2).getRegionHeight() / 2 + realHeight * 0.06f, getOriginX(), getOriginY(), normal.get(2).getRegionWidth(), normal.get(2).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
//        else
//            batch.draw(normal.get(1), width - normal.get(1).getRegionWidth() * 3 / 2, getY() + realHeight * 0.3f / 2 - normal.get(1).getRegionHeight() / 2 + realHeight * 0.06f, getOriginX(), getOriginY(), normal.get(1).getRegionWidth(), normal.get(1).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(normal.get(3), 0, getY() + realHeight * 0.3f, getOriginX(), getOriginY(), width, normal.get(3).getRegionHeight(), getScaleX(), getScaleY(), getRotation());

//        try {
//            picture = NetUtil.getInstance().getLocalPicture(MD5Encoder.encode(result.getImgUrl()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (result != null && texture == null && !TextUtils.isEmpty(result.getImgUrl()) && isNet && netCount <= 4) {
            Gdx.app.error("http", "url   " + result.getImgUrl());
            isNet = false;
            NetUtil.getInstance().getPicture(result.getImgUrl(), new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    // 获取响应状态
                    HttpStatus httpStatus = httpResponse.getStatus();

                    if (httpStatus.getStatusCode() == 200) {

                        // 请求成功
                        Gdx.app.error("http", "图片请求成功");
                        final byte[] results = httpResponse.getResult();
//                        try {
//                            NetUtil.getInstance().getFileUtil().setFile(MD5Encoder.encode(result.getImgUrl()), results);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        // 还可以以流或字符串的方式获取
                        // httpResponse.getResultAsStream();
                        // httpResponse.getResultAsString();

                                            /*
                                             * 在响应回调中属于其他线程, 获取到响应结果后需要
                                             * 提交到 渲染线程（create 和 render 方法执行所在线程） 处理。
                                             */
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // 把字节数组加载为 Pixmap
                                picture = new Pixmap(results, 0, results.length);
//                                try {
//                                    NetUtil.getInstance().getMemoryUtil().setLru(MD5Encoder.encode(result.getImgUrl()), picture);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                // 把 pixmap 加载为纹理
                                int min = Math.min(picture.getWidth(), picture.getHeight());
//                                texture = new Texture(PixmapUtil.createRoundedPixmap(picture, min / 2, min, min));
                                texture = new Texture(picture);
                                // pixmap 不再需要使用到, 释放内存占用
                                picture.dispose();
                            }
                        });

                    } else {
                        netCount++;
                        isNet = true;
                        Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
                    }
                }

                @Override
                public void failed(Throwable t) {
                    isNet = true;
                    netCount++;
                    Gdx.app.error("http", t.getMessage());
                }

                @Override
                public void cancelled() {
                    isNet = true;
                    Gdx.app.error("http", "请求取消");
                }
            });
        }

        if (texture != null)
            batch.draw(texture, width * 0.13f, getY() + realHeight * 0.3f + width * 0.04f, radius, radius);

        if (lazyBitmapFont1 == null) {
            lazyBitmapFont1 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(12), Color.WHITE));
        }
//            lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(12), com.badlogic.gdx.graphics.Color.WHITE);
        lazyBitmapFont1.appendText("有效期限：" + result.getStartTime() + "-" + result.getEndTime());
        lazyBitmapFont1.draw(batch, "有效期限：" + result.getStartTime() + "-" + result.getEndTime(), width * 0.13f, getY() + realHeight * 0.3f / 2 + ScreenUtil.dp2px(12) / 2 + realHeight * 0.04f, width, Align.left, false);
//        lazyBitmapFont1.appendText("请到适用门店兑换");
//        lazyBitmapFont1.draw(batch, "请到适用门店兑换", width * 0.13f + radius + width * 0.04f, getY() + realHeight * 0.3f + width * 0.04f + radius / 4, width, Align.left, false);

        if (lazyBitmapFont2 == null) {
            lazyBitmapFont2 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(15), Color.WHITE));
        }
//            lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(15), com.badlogic.gdx.graphics.Color.WHITE);
        lazyBitmapFont2.appendText(result.getMain());
        lazyBitmapFont2.draw(batch, result.getMain(), width * 0.13f + radius + width * 0.04f, getY() + realHeight * 0.3f + width * 0.04f + radius * 3 / 4, width, Align.left, false);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (lazyBitmapFont1 != null)
            lazyBitmapFont1.dispose();
        if (lazyBitmapFont2 != null)
            lazyBitmapFont2.dispose();
        for (int i = 0; i < normal.size(); i++) {
            if (normal.get(i) != null)
                normal.get(i).getTexture().dispose();
        }
    }

    public float getRealHeight() {
        return realHeight;
    }
}
