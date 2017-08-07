package com.rtmap.game.actor;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.LoadCompleteListener;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.NativeFont;
import com.rtmap.game.text.NativeFontPaint;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.MD5Encoder;
import com.rtmap.game.util.ScreenUtil;
import com.rtmap.game.util.ZXingUtil;

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
    private NativeFont lazyBitmapFont2;
    private NativeFont lazyBitmapFont1;
    private int qrWidth = 0;
    private List<TextureRegion> beedList = new ArrayList<TextureRegion>();
    private float bgH;
    private float bgW;
    private float headWidth = 0;
    private boolean isShow = false;
    private NativeFont lazyBitmapFont4;
    private String mQr;

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

        qrWidth = (int) (bgH * 0.275f);
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

    String content = "";

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
            float fontWidth1 = ScreenUtil.getLength(ScreenUtil.dp2px(20), result.getMain());
            if (lazyBitmapFont1 == null) {
                lazyBitmapFont1 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(20), Color.WHITE));
            }
//            if (lazyBitmapFont1 == null)
//                lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(20), Color.WHITE);
            lazyBitmapFont1.appendText(result.getMain());
            lazyBitmapFont1.draw(batch, result.getMain(), width / 2 - fontWidth1 / 2, oriY + bgH * 0.74f, width * 0.707f, Align.left, true);

            batch.draw(beedList.get(1), width / 2 - beedList.get(1).getRegionWidth() / 2, oriY + bgH * 0.690f, beedList.get(1).getRegionWidth(), beedList.get(1).getRegionHeight());


            if (lazyBitmapFont2 == null) {
                lazyBitmapFont2 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(14), Color.WHITE));
            }
//            float fontWidth2 = ScreenUtil.getLength(ScreenUtil.dp2px(14), "店内部分商品参加活动");
//            lazyBitmapFont2.appendText("店内部分商品参加活动");
//            lazyBitmapFont2.draw(batch, "店内部分商品参加活动", width / 2 - fontWidth2 / 2, oriY + bgH * 0.690f - ScreenUtil.dp2px(14) / 2, width * 0.707f, Align.left, true);
            float fontWidth3 = ScreenUtil.getLength(ScreenUtil.dp2px(14), mQr);

            lazyBitmapFont2.appendText(mQr);
            lazyBitmapFont2.draw(batch, mQr, width / 2 - qrWidth / 2, oriY + bgH * 0.348f - ScreenUtil.dp2px(14) / 2, width * 0.707f, Align.left, true);

//            if (lazyBitmapFont3 == null)
//                lazyBitmapFont3 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);
//            lazyBitmapFont3.draw(batch, "11111111111111111", width / 2 - qrWidth / 2, height * 0.377f - 20, width, Align.left, true);
            float length = ScreenUtil.getLength(ScreenUtil.dp2px(14), "广发日");
            lazyBitmapFont2.appendText("广发日");
            lazyBitmapFont2.draw(batch, "广发日", width / 2 - length / 2, oriY + bgH * 0.831f - 15, width * 0.707f, Align.left, true);
            if (lazyBitmapFont4 == null) {
                lazyBitmapFont4 = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(12), Color.WHITE));
            }
            float length3 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "有效期限：" + result.getStartTime() + "-" + result.getEndTime());
            lazyBitmapFont4.appendText("有效期限： " + result.getStartTime() + "-" + result.getEndTime());
            lazyBitmapFont4.draw(batch, "有效期限： " + result.getStartTime() + "-" + result.getEndTime(), width / 2 - length3 / 2, oriY + bgH * 0.24f - ScreenUtil.dp2px(5), width * 0.63f, Align.left, true);
            if (TextUtils.isEmpty(content)) {
                float length2 = 0;
                float lengthMore = ScreenUtil.getLength(ScreenUtil.dp2px(12), "...");
                String location = "兑换地址：" + result.getPosition();
                for (int i = 1; i < location.length(); i++) {
                    length2 = ScreenUtil.getLength(ScreenUtil.dp2px(12), location.substring(0, i));
                    if ((length2 + lengthMore) >= (width * 0.8f - (width / 2 - length3 / 2))) {
                        content = location.substring(0, i - 1);
                        Gdx.app.error("content", content);
                        break;
                    }
                }
            }

            lazyBitmapFont4.appendText(content + "...");
            lazyBitmapFont4.draw(batch, content + "...", width / 2 - length3 / 2, oriY + bgH * 0.27f, width * 0.8f - (width / 2 - length3 / 2), Align.left, true);
//            batch.draw(beedList.get(1), width / 2 - beedList.get(1).getRegionWidth() / 2, oriY + bgH * 0.14f, beedList.get(1).getRegionWidth(), beedList.get(1).getRegionHeight());


//            if (null != result && !TextUtils.isEmpty(result.getImgUrl()) && texture1 == null && isNet && netCount <= 4) {
//                isNet = false;
//                NetUtil.getInstance().getPicture(result.getLogoUrl(), new Net.HttpResponseListener() {
//                    @Override
//                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
//                        // 获取响应状态
//                        HttpStatus httpStatus = httpResponse.getStatus();
//
//                        if (httpStatus.getStatusCode() == 200) {
//                            // 请求成功
//                            Gdx.app.error("http", "图片请求成功");
//
//                            // 以字节数组的方式获取响应内容
//                            final byte[] result = httpResponse.getResult();
//
//                            // 还可以以流或字符串的方式获取
//                            // httpResponse.getResultAsStream();
//                            // httpResponse.getResultAsString();
//
//                                            /*
//                                             * 在响应回调中属于其他线程, 获取到响应结果后需要
//                                             * 提交到 渲染线程（create 和 render 方法执行所在线程） 处理。
//                                             */
//                            Gdx.app.postRunnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // 把字节数组加载为 Pixmap
//                                    Pixmap pixmap = new Pixmap(result, 0, result.length);
//                                    // 把 pixmap 加载为纹理
//
//                                    texture1 = new Texture(pixmap);
//                                    // pixmap 不再需要使用到, 释放内存占用
//                                    pixmap.dispose();
//                                }
//                            });
//                        } else {
//                            netCount++;
//                            isNet = true;
//                            Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
//                        }
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//                        isNet = true;
//                        netCount++;
//                        Gdx.app.error("http", t.getMessage());
//                    }
//
//                    @Override
//                    public void cancelled() {
//                        isNet = true;
//                        Gdx.app.error("http", "请求取消");
//                    }
//                });
//            }
            if (texture1 != null)
                batch.draw(texture1, width / 2 - qrWidth / 2, oriY + bgH * 0.348f, qrWidth, qrWidth);

//            if (null != result && null != result.getImgUrl() && texture == null) {
//                NetUtil.getInstance().getPicture(result.getImgUrl(), new Net.HttpResponseListener() {
//                    @Override
//                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
//                        // 获取响应状态
//                        HttpStatus httpStatus = httpResponse.getStatus();
//
//                        if (httpStatus.getStatusCode() == 200) {
//                            // 请求成功
//                            Gdx.app.error("http", "请求成功");
//
//                            // 以字节数组的方式获取响应内容
//                            final byte[] result = httpResponse.getResult();
//
//                            // 还可以以流或字符串的方式获取
//                            // httpResponse.getResultAsStream();
//                            // httpResponse.getResultAsString();
//
//                                            /*
//                                             * 在响应回调中属于其他线程, 获取到响应结果后需要
//                                             * 提交到 渲染线程（create 和 render 方法执行所在线程） 处理。
//                                             */
//                            Gdx.app.postRunnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // 把字节数组加载为 Pixmap
//                                    Pixmap pixmap = new Pixmap(result, 0, result.length);
//                                    // 把 pixmap 加载为纹理
//                                    texture = new Texture(PixmapUtil.createRoundedPixmap(pixmap, (int) (headWidth / 2), (int) headWidth, (int) headWidth));
//                                    // pixmap 不再需要使用到, 释放内存占用
//                                    pixmap.dispose();
//                                }
//                            });
//                        } else {
//                            Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
//                        }
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//                        Gdx.app.error("http", t.getMessage());
//                    }
//
//                    @Override
//                    public void cancelled() {
//                        Gdx.app.error("http", "请求取消");
//                    }
//                });
//            } else {
//                if (texture != null)
//                    batch.draw(texture, width / 2 - headWidth / 2, oriY + bgH * 0.834f, headWidth, headWidth);
//            }
        }
    }

    private String formatformatQr(String qr) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < qr.length(); i++) {
            if(i%4==0&&i!=0){
                buffer.append(" ");
            }
            buffer.append(qr.charAt(i));
        }
        return buffer.toString();
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
        if (!TextUtils.isEmpty(result.getQr())) {
            mQr = formatformatQr(result.getQr());
            Gdx.app.error("file", Gdx.files.getExternalStoragePath() + Contacts.BITMAP_SD + result.getQr() + ".png");
            try {
                texture1 = new Texture(Gdx.files.absolute(Gdx.files.getExternalStoragePath() + Contacts.BITMAP_SD + MD5Encoder.encode(result.getQr() + ".png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (texture1 == null)
                ZXingUtil.createQr(result.getQr(), qrWidth, qrWidth, new LoadCompleteListener() {
                    @Override
                    public void load(final String path) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                texture1 = new Texture(Gdx.files.absolute(path));
                            }
                        });
                    }

                    @Override
                    public void loadFail(String message) {
                        Gdx.app.error("qrcode", message);
                    }
                });

        }
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
        if (texture1 != null) {
            texture1.dispose();
        }
    }
}
