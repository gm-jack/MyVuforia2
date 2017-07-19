package com.rtmap.game.actor;

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
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.NativeFont;
import com.rtmap.game.text.NativeFontPaint;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.PixmapUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class DetailActor extends Actor {
    private float oriX;
    private float oriY;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private boolean isOpen = false;
    private Texture texture1;
    private Result result;
    private Texture texture;
    private NativeFont lazyBitmapFont3;
    private NativeFont lazyBitmapFont2;
    private NativeFont lazyBitmapFont1;
    private float qrWidth = 0;
    private List<TextureRegion> beedList = new ArrayList<>();
    private float bgH;
    private float bgW;
    private float headWidth = 0;
    private boolean isShow = false;

    public DetailActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResouces();
        oriY = height * 0.11f;
        oriX = 0.07f * width;
        bgH = height * 0.8f;
        bgW = width * 0.86f;

        qrWidth = bgH * 0.275f;
        headWidth = bgH * 0.076f;
    }

    public void setListener(final StartOnClickListener startOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (startOnClickListener != null) {
                    startOnClickListener.onClick();
                }
            }
        };
        addListener(listener);
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible() && isShow) {
            return;
        }
        Gdx.gl20.glClearColor(0, 0, 0, 1f);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        if (beedList.size() < 0)
            return;
        if (isOpen) {
            batch.draw(beedList.get(2), 0, 0, width, height);
            if (result == null)
                return;
            //从顶部向下绘制
            batch.draw(beedList.get(0), oriX, oriY, bgW, bgH);
            float fontWidth1 = ScreenUtil.getLength(ScreenUtil.dp2px(20), "满100减40");
            if (lazyBitmapFont1 == null) {
                lazyBitmapFont1 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(20),Color.WHITE));
            }
//            if (lazyBitmapFont1 == null)
//                lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(20), Color.WHITE);
            lazyBitmapFont1.appendText("满100减40");
            lazyBitmapFont1.draw(batch, "满100减40", width / 2 - fontWidth1 / 2, oriY + bgH * 0.74f, width * 0.707f, Align.left, true);

            batch.draw(beedList.get(1), width / 2 - beedList.get(1).getRegionWidth() / 2, oriY + bgH * 0.690f, beedList.get(1).getRegionWidth(), beedList.get(1).getRegionHeight());

            float fontWidth2 = ScreenUtil.getLength(ScreenUtil.dp2px(14), "店内部分商品参加活动");
            float fontWidth3 = ScreenUtil.getLength(ScreenUtil.dp2px(14), "12627238383333333");
            if (lazyBitmapFont2 == null) {
                lazyBitmapFont2 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(14),Color.WHITE));
            }
//            if (lazyBitmapFont2 == null)
//                lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(14), Color.WHITE);
            lazyBitmapFont2.appendText("店内部分商品参加活动");
            lazyBitmapFont2.draw(batch, "店内部分商品参加活动", width / 2 - fontWidth2 / 2, oriY + bgH * 0.690f - ScreenUtil.dp2px(14) / 2, width * 0.707f, Align.left, true);
            lazyBitmapFont2.appendText("12627238383333333");
            lazyBitmapFont2.draw(batch, "12627238383333333", width / 2 - fontWidth3 / 2, oriY + bgH * 0.348f - ScreenUtil.dp2px(14) / 2, width * 0.707f, Align.left, true);

            if (lazyBitmapFont3 == null) {
                lazyBitmapFont3 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(12),Color.WHITE));
            }
//            if (lazyBitmapFont3 == null)
//                lazyBitmapFont3 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);
//            lazyBitmapFont3.draw(batch, "11111111111111111", width / 2 - qrWidth / 2, height * 0.377f - 20, width, Align.left, true);
            float length = ScreenUtil.getLength(ScreenUtil.dp2px(12), result.getMain());
            lazyBitmapFont3.appendText(result.getMain());
            lazyBitmapFont3.draw(batch, result.getMain(), width / 2 - length / 2, oriY + bgH * 0.831f - 15, width * 0.707f, Align.left, true);
            float length2 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "兑换地址：" + result.getShopName());
            lazyBitmapFont3.appendText( "兑换地址：" + result.getShopName());
            lazyBitmapFont3.draw(batch, "兑换地址：" + result.getShopName(), width / 2 - length2 / 2, oriY + bgH * 0.24f, width * 0.707f, Align.left, true);
            float length3 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "有效期限：" + result.getStartTime() + "-" + result.getEndTime());
            lazyBitmapFont3.appendText("有效期限： " + result.getStartTime() + "-" + result.getEndTime());
            lazyBitmapFont3.draw(batch, "有效期限： " + result.getStartTime() + "-" + result.getEndTime(), width / 2 - length3 / 2, oriY + bgH * 0.24f - ScreenUtil.dp2px(12), width * 0.707f, Align.left, true);

            if (null != result && null != result.getImgUrl() && texture1 == null) {
                NetUtil.getInstance().getPicture(result.getImgUrl(), new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        // 获取响应状态
                        HttpStatus httpStatus = httpResponse.getStatus();

                        if (httpStatus.getStatusCode() == 200) {
                            // 请求成功
                            Gdx.app.error("http", "请求成功");

                            // 以字节数组的方式获取响应内容
                            final byte[] result = httpResponse.getResult();

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
                                    Pixmap pixmap = new Pixmap(result, 0, result.length);
                                    // 把 pixmap 加载为纹理

                                    texture1 = new Texture(pixmap);
                                    // pixmap 不再需要使用到, 释放内存占用
                                    pixmap.dispose();
                                }
                            });
                        } else {
                            Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Gdx.app.error("http", t.getMessage());
                    }

                    @Override
                    public void cancelled() {
                        Gdx.app.error("http", "请求取消");
                    }
                });
            } else {
                if (texture1 != null)
                    batch.draw(texture1, width / 2 - qrWidth / 2, oriY + bgH * 0.348f, qrWidth, qrWidth);
            }

            if (null != result && null != result.getImgUrl() && texture == null) {
                NetUtil.getInstance().getPicture(result.getImgUrl(), new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        // 获取响应状态
                        HttpStatus httpStatus = httpResponse.getStatus();

                        if (httpStatus.getStatusCode() == 200) {
                            // 请求成功
                            Gdx.app.error("http", "请求成功");

                            // 以字节数组的方式获取响应内容
                            final byte[] result = httpResponse.getResult();

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
                                    Pixmap pixmap = new Pixmap(result, 0, result.length);
                                    // 把 pixmap 加载为纹理
                                    texture = new Texture(PixmapUtil.createRoundedPixmap(pixmap, (int) (headWidth / 2), (int) headWidth, (int) headWidth));
                                    // pixmap 不再需要使用到, 释放内存占用
                                    pixmap.dispose();
                                }
                            });
                        } else {
                            Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Gdx.app.error("http", t.getMessage());
                    }

                    @Override
                    public void cancelled() {
                        Gdx.app.error("http", "请求取消");
                    }
                });
            } else {
                if (texture != null)
                    batch.draw(texture, width / 2 - headWidth / 2, oriY + bgH * 0.834f, headWidth, headWidth);
            }
        }
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    private void initResouces() {
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_open_bg.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("open_line.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("cover.png")));
    }

    public void setResult(Result item) {
        this.result = item;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        for (int i = 0; i < beedList.size(); i++) {
            beedList.get(i).getTexture().dispose();
        }
        if (lazyBitmapFont1 != null) {
            lazyBitmapFont1.dispose();
        }
        if (lazyBitmapFont2 != null) {
            lazyBitmapFont2.dispose();
        }
        if (lazyBitmapFont3 != null) {
            lazyBitmapFont3.dispose();
        }
    }
}
