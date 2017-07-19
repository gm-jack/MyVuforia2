package com.rtmap.game.screen;

import android.content.Context;
import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AgainActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.CloseActor;
import com.rtmap.game.actor.CoverActor;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.interfaces.CatchOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.stage.CatchStage;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.SPUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchScreen extends MyScreen {

    private Context context;
    private CatActor catActor;
    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;

    private BackActor backActor;
    private BeedActor beedActor;
    private Group group3;
    private Timer timer;

    private CatchStage catchStage;
    private CatchActor catchActor;

    private boolean stop = true;
    private CoverActor coverActor;
    private boolean isFirst = true;
    private boolean firstCatch;
    private CloseActor closeActor;
    private AgainActor againActor;
    private boolean isWin = false;
    private boolean isInit = true;
    private boolean isFirstNumber = true;

    public CatchScreen(MyGame game, AndroidLauncher androidLauncher, ScreenViewport viewport) {
        super(game);
        setUpdate(false);
        this.mGame = game;
        this.context = androidLauncher;
        //捕捉怪兽舞台
        catchStage = new CatchStage(viewport);

        group3 = new Group();
        catchActor = new CatchActor(mGame.asset);
        catchActor.setPosition(0, 0);
        catchActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group3.addActor(catchActor);

        backActor = new BackActor(mGame.asset);
        group3.addActor(backActor);

        beedActor = new BeedActor(mGame.asset);
        group3.addActor(beedActor);

        coverActor = new CoverActor(mGame.asset);
        group3.addActor(coverActor);

        catActor = new CatActor(mGame.asset);

        //添加关闭按钮actor
        closeActor = new CloseActor(mGame.asset);
        group3.addActor(closeActor);

        //添加再来一次按钮actor
        againActor = new AgainActor(mGame.asset);
        group3.addActor(againActor);

        catchStage.addActor(group3);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(catchStage);
        if (isFirst && !firstCatch) {
            isFirst = false;
            setlistener();
        }
        if (catchActor != null)
            catchActor.setCatchListener(new CatchListener() {
                @Override
                public void onFirst() {
                    Gdx.app.error("dialog", "onFirst");
                    coverActor.setIsFirst(true);
                    catchActor.setIsStop(false);
                    setlistener();
                }

                @Override
                public void onSuccess() {
                    Gdx.app.error("dialog", "onSuccess");
                    setStopRerder(true);
                    addTimeNum();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            if (firstCatch) {
                                coverActor.setIsFirst(false);
                                catchActor.setIsFirst(false);
                            }
                            catActor.setIsCatch(false);
                            catchActor.setFail(true);
                            catchActor.setIsSuccess(true);

                        }
                    }, 500);
                }

                @Override
                public void onFail() {
                    Gdx.app.error("dialog", "onFail");
                    setStopRerder(true);
                    addTimeNum();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            catActor.setIsShow(false);
                            catchActor.setFail(true);
                            catchActor.setIsSuccess(false);
                        }
                    }, 500);
                }

                @Override
                public void onNumberFail(int number) {
                    Gdx.app.error("dialog", "onNumberFail");
                    setStopRerder(true);
                    addTimeNum();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            catchActor.setShowFail(false);
                            catActor.setIsShow(false);
                            catchActor.setFail(true);
                            catchActor.setIsSuccess(false);
                        }
                    }, 500);
                }

                @Override
                public void onTouched(int num) {
                    Gdx.app.error("num", "num" + num);
                    if (num == 0) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mGame != null)
                                            mGame.showAimScreen(true);
                                    }
                                });
                            }
                        }, 500);
                    } else if (num == 1) {
                        catchActor.setIsCatchTip(false);
                        catchActor.setIsStop(false);
                    }
                }
            });
    }

    private void addTimeNum() {
//        int num = GdxUtil.getNum();
//        Gdx.app.error("dialog", "num    " + num);
//        num += 1;
//        SPUtil.put(Contacts.TIME_NUM, new String(GdxUtil.getTime() + ":" + num + ":" + SPUtil.get(Contacts.PHONE, "")));
    }

    private void setlistener() {
        if (backActor != null)
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    if (mGame != null)
                        mGame.showAimScreen(true);
                }
            });
        if (beedActor != null)
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    mGame.showBeedScreen(CatchScreen.this);
                }
            });
        if (catActor != null)
            catActor.setListener(new CatchOnClickListener() {

                @Override
                public void onCatchClick() {
                    catchActor.setIsStop(true);
                    catActor.setIsOpenFirst(true);
                }

                @Override
                public void onSuccessClick() {
                    Gdx.app.error("gdx", "onSuccessClick()");
                    group3.removeActor(backActor);
                    group3.removeActor(beedActor);
                    setResult();
                }
            });
        if (closeActor != null)
            closeActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    if (mGame != null)
                        mGame.showAimScreen(true);
                }
            });
        if (againActor != null)
            againActor.setListener(new AgainActor.AgainOnClickListener() {
                @Override
                public void againClick() {
                    if (mGame != null)
                        mGame.showAimScreen(true);

                    SPUtil.put(Contacts.FIRST, false);
                }
            });
    }

    private void setResult() {
        catActor.removeListener();
        catActor.setIsShow(false);
        String phoneNumber = (String) SPUtil.get(Contacts.PHONE, "");
        if (TextUtils.isEmpty(phoneNumber)) {
            fail();
            return;
        }
        Gdx.app.error("net", Contacts.WIN_NET + phoneNumber);
        NetUtil.getInstance().getConnection(Contacts.WIN_NET + phoneNumber, new NetUtil.HttpResponse() {
            @Override
            public void responseString(String response) {
                Gdx.app.error("http", "responseString  ==  " + response);

                Result result = null;
                try {
                    result = new Gson().fromJson(response, Result.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result != null && "0".equals(result.getCode())) {
                    isWin = true;
                    catchActor.setIsWin(isWin);
                    catchActor.setData(result);
                } else {
                    isWin = false;
                    catchActor.setIsWin(isWin);
                }
                catchActor.setIsOpen(true);
                againActor.setIsShow(!isWin);
                closeActor.setIsShow(true);
            }

            @Override
            public void responseFail() {
                fail();
            }
        });
    }

    private void fail() {
        isWin = false;
        catchActor.setIsWin(isWin);
        catchActor.setIsOpen(true);
        againActor.setIsShow(!isWin);
        closeActor.setIsShow(true);
    }

    @Override
    public void render(float delta) {
        if (catchStage == null)
            return;
        super.render(delta);
        if (isFirstNumber && !isLoading) {
            Gdx.app.error("gdx", "CatchScreen render");
            isFirstNumber = false;
            setModelNumber(ZUO);
        }
        // 更新舞台逻辑
        catchStage.act();
        // 绘制舞台
        catchStage.draw();
    }

    @Override
    public MyScreen getScreen() {
        return this;
    }

    @Override
    public void resize(int width, int height) {
        setStopCamera(true);
        setRay(false);
        setTranslate(false);

        if (isInit) {
            Gdx.app.error("gdx", "CatchScreen resize");
            isInit = false;
            catchActor.setIsStop(true);
            firstCatch = (boolean) SPUtil.get(Contacts.FIRST, true);
            catchActor.setIsFirst(firstCatch);
            if (timer == null)
                timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    catchActor.setCatch(false);
                    catchActor.setIsCatchTip(firstCatch);
                    if (!firstCatch) {
                        catchActor.setIsStop(false);
                    }
                    group3.addActor(catActor);
                    initListener();
                }
            }, 1000);
        } else {
            initListener();
        }
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (catchStage != null) {
            catchStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
