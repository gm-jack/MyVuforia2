package com.rtmap.game.screen;

import android.text.SpannableString;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.AnimationListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.model.BaseBean;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.SPUtil;

import java.util.Timer;
import java.util.TimerTask;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimScreen extends MyScreen {
    private GDXDialogs dManager;
    private ScreenViewport mViewPort;
    private AndroidLauncher androidLauncher;
    private MyGame mGame;

    private BackActor backActor;
    private BeedActor beedActor;
    private boolean fail;
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    private boolean isFirst = true;
    private boolean isAim = false;
    private boolean isAnimation = true;
    private int nums = 0;
    private String mToken;
    private String mUserId;

    public AimScreen(MyGame game, AndroidLauncher androidLauncher, ScreenViewport viewport) {
        super(game);
        setUpdate(true);
        this.mGame = game;
        this.androidLauncher = androidLauncher;
        this.mViewPort = viewport;

        //瞄准怪兽舞台
        aimStage = new AimStage(viewport);

        group2 = new Group();
        aimActor = new AimActor(mGame.asset, isAnimation);
        aimActor.setPosition(0, 0);
        aimActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group2.addActor(aimActor);

        backActor = new BackActor(mGame.asset, isAnimation);
        group2.addActor(backActor);

        beedActor = new BeedActor(mGame.asset, isAnimation);
        group2.addActor(beedActor);

        aimStage.addActor(group2);
        dManager = GDXDialogsSystem.install();
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(aimStage);
        aimActor.setAimListener(new AimListener() {
            @Override
            public void aimSuccess() {
                setRay(false);
                setTranslate(false);
                setModelNumber(ZUO);
                if (mGame != null)
                    mGame.showCatchScreen();
            }

            @Override
            public void aimFail() {
                aimActor.setIsFind(false);
                setFind(true);
            }
        });
        if (isFirst) {
            isFirst = false;
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    if (mGame != null) {
                        mGame.asset.clear();
                        mGame.stopCamera();
                        mGame.showMainScreen();
                    }
                }
            });
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    if (mGame != null)
                        mGame.showBeedScreen(AimScreen.this);
                }
            });
            aimActor.setAnimationListener(new AnimationListener() {
                @Override
                public void startAnim(boolean isDistance) {

                }

                @Override
                public void endAnim() {
                    aimActor.setIsStartAnimation(false);
                    setIsAnim(true);
                }
            });
            setAnimationListener(new AnimationListener() {
                @Override
                public void startAnim(boolean isDistance) {
                    if (!aimActor.isAnimation()) {
                        if (isDistance) {
//                            aimActor.setIsStartAnimation(true);
                        } else {
                            isAim = (boolean) SPUtil.get("first_find", true);
                            if (isAim) {
                                aimActor.setIsTip(true);
                                SPUtil.put("first_find", false);
                                if (timer == null)
                                    timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        aimActor.setIsTip(false);
                                    }
                                }, 1000);
                            }
//                            aimActor.setIsStartAnimation(false);
                        }
                    }
                }

                @Override
                public void endAnim() {
                    setIsAnim(false);
                    aimActor.setIsStartAnimation(true);
                }
            });
        }
    }

    @Override
    public void addNumber() {
        super.addNumber();
        Gdx.app.error("add", "addNumber()");
        if (aimActor != null)
            aimActor.addNumber();
    }

    @Override
    public void subNumber() {
        super.subNumber();
        setFind(false);
        Gdx.app.error("add", "subNumber()");
        if (aimActor != null)
            aimActor.subNumber();
    }

    @Override
    public void render(float delta) {
        if (aimStage == null)
            return;
        super.render(delta);
        // 更新舞台逻辑
        aimStage.act();
        // 绘制舞台
        aimStage.draw();
    }

    @Override
    public MyScreen getScreen() {
        return this;
    }

    public void setIsFail(boolean fail) {
        this.fail = fail;
        if (aimActor != null)
            aimActor.setIsFail(fail);
    }

    @Override
    public void resize(int width, int height) {
//        int num = GdxUtil.getNum();
//        Gdx.app.error("dialog", "num   " + num);
//        if ((num + 1) >= 6) {
//            setCanPlay(false);
//            showANewDoubleButtonDialog();
//        } else {
//            setCanPlay(true);
//        }


        setIsLineShow(true);
        setStopCamera(false);
        setStopRerder(false);
        setRay(false);
        initListener();
//        isAnimation = (boolean) SPUtil.get(Contacts.ANIM_IS_ANIMATION, true);
        super.resize(width, height);
    }

    private void showANewDoubleButtonDialog() {
        GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);

        bDialog.setTitle("提示");
        int point = (int) SPUtil.get(Contacts.E_POINT, 0);
        SpannableString string = new SpannableString("优惠券已抢光，下次再来！");
//        string.setSpan(new ForegroundColorSpan(Color.parseColor("#82E0F2")), "您的今日挑战次数已达到5次，如果继续挑战需扣减3E点/次。\n(当前E点余额：".length(), new String("您的今日挑战次数已达到5次，如果继续挑战需扣减3E点/次。\n(当前E点余额：" + point).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bDialog.setMessage(string);

        bDialog.setClickListener(new ButtonClickListener() {

            @Override
            public void click(int button) {
                if (button == 0) {
                    //继续挑战
                    mToken = (String) SPUtil.get(Contacts.TOKEN, "");
                    mUserId = (String) SPUtil.get(Contacts.USERID, "");

                    NetUtil.getInstance().get(Contacts.HOST + mToken + "&user_id=" + mUserId + "&type=10005", new Net.HttpResponseListener() {
                        //NetUtil.getInstance().get(Contacts.HOST + "1ff0788f5f9e965c51fd77f89666e214" + "&user_id=" + "" + "&type=10005", new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            String string = httpResponse.getResultAsString();
                            Gdx.app.error("dialog", string);
                            BaseBean bean = new Gson().fromJson(string, BaseBean.class);
                            if (bean != null) {
                                Gdx.app.error("dialog", "debug   " + ("0".equals(bean.getCode())));
                                if ("0".equals(bean.getCode())) {
                                    setCanPlay(true);
                                    SPUtil.put(Contacts.E_POINT, bean.getData().getOtherPoint());
                                } else {
                                    setCanPlay(false);
                                    showANewSingleButtonDialog();
                                }
                            } else {
                                setCanPlay(false);
                                showANewSingleButtonDialog();
                            }
                        }

                        @Override
                        public void failed(Throwable t) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (mGame != null)
                                        mGame.showMainScreen();
                                }
                            });
                        }

                        @Override
                        public void cancelled() {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (mGame != null)
                                        mGame.showMainScreen();
                                }
                            });
                        }
                    });

                } else {
                    //先不玩了
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            if (mGame != null)
                                mGame.showMainScreen();
                        }
                    });
                }

            }
        });

        bDialog.addButton("继续挑战");
        bDialog.addButton("先不玩了");

        bDialog.build().show();

    }

    @Override
    public void setCanPlay(boolean canPlay) {
        super.setCanPlay(canPlay);
    }

    private void showANewSingleButtonDialog() {
        final GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);

        bDialog.setMessage("您的E点不足，明天再来吧。另外，签到也能赚E点哦!");

        bDialog.setClickListener(new ButtonClickListener() {

            @Override
            public void click(int button) {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (mGame != null)
                            mGame.showMainScreen();
                    }
                });
            }
        });

        bDialog.addButton("确定");

        bDialog.build().show();

    }

    @Override
    public boolean isAnimation() {
        if (aimActor != null)
            return aimActor.isAnimation();
        return true;
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
        if (aimStage != null) {
            aimStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }

    }
}
