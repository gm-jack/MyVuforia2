package com.rtmap.game.screen;

import android.text.TextUtils;
import android.view.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.AnimationListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.model.SurplusBean;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.CustomDialog;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimScreen extends MyScreen {
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
    private CustomDialog mCustomDialog;
    private int mSum = 0;

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
            aimActor.setFindListener(new CatchListener() {
                @Override
                public void onFirst() {

                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail() {

                }

                @Override
                public void onNumberFail(int number) {

                }

                @Override
                public void onTouched(int num) {
                    aimActor.setIsTip(false);
                    setCanPlay(true);
                }
            });
            setAnimationListener(new AnimationListener() {
                @Override
                public void startAnim(boolean isDistance) {
                    if (!aimActor.isAnimation()) {
                        if (isDistance) {
//                            aimActor.setIsStartAnimation(true);
                        } else {
                            isAim = (Boolean) SPUtil.get("first_find", true);
                            if (isAim) {
                                aimActor.setIsTip(true);
                                setCanPlay(false);
                                SPUtil.put("first_find", false);
                            }
//                            isAim = (Boolean) SPUtil.get(Contacts.FIRST, true);
//                            if (isAim) {
//                                aimActor.setIsTip(true);
//                                setCanPlay(false);
//                            }
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
        Gdx.app.error("dialog", "resize() ");
//        if ((num + 1) >= 6) {
//            setCanPlay(false);
//            showANewDoubleButtonDialog();
//        } else {
//            setCanPlay(true);
//        }
        Boolean o = (Boolean) SPUtil.get(Contacts.FIRST, true);
        Gdx.app.error("dialog", "resize() " + o);
        if (!o)
            getActivityOn();

        setIsLineShow(true);
        setStopCamera(false);
        setStopRerder(false);
        setRay(false);
        initListener();
//        isAnimation = (boolean) SPUtil.get(Contacts.ANIM_IS_ANIMATION, true);
        super.resize(width, height);
    }

    private void getActivityOn() {
        Gdx.app.error("dialog", Contacts.ACTIVY_ON);
        if (mCustomDialog != null)
            mCustomDialog.dismiss();
        NetUtil.getInstance().get(Contacts.ACTIVY_ON, new Net.HttpResponseListener() {


            //NetUtil.getInstance().get(Contacts.HOST + "1ff0788f5f9e965c51fd77f89666e214" + "&user_id=" + "" + "&type=10005", new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String result = httpResponse.getResultAsString();
                Gdx.app.error("dialog", "handleHttpResponse   " + result);

                if (TextUtils.isEmpty(result)) {
                    return;
                }

                if (!"1".equals(result)) {
                    androidLauncher.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showANewSingleButtonDialog();
                        }
                    });
//
                } else {
                    mSum = 0;
                    NetUtil.getInstance().get(Contacts.SURPLUS_COUNT, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            String result = httpResponse.getResultAsString();
                            Gdx.app.error("dialog", "handleHttpResponse   " + result);
                            if (TextUtils.isEmpty(result)) {
                                return;
                            }
                            try {
                                JSONObject object = new JSONObject(result);
                                SurplusBean surplusBean = new SurplusBean();

                                surplusBean.setStatus(object.optInt("status"));
                                surplusBean.setMessage(object.optString("message"));

                                JSONArray data = object.optJSONArray("data");
                                List<SurplusBean.DataBean> dataList = new ArrayList<SurplusBean.DataBean>();

                                for (int i = 0; i < data.length(); i++) {
                                    SurplusBean.DataBean bean = new SurplusBean.DataBean();
                                    JSONObject dataJSONObject = data.getJSONObject(i);
                                    bean.setId(dataJSONObject.optInt("id"));
                                    bean.setIssue(dataJSONObject.optInt("issue"));
                                    bean.setNum(dataJSONObject.optInt("num"));
                                    int count = dataJSONObject.optInt("surplus_count");
                                    if (count != 0) {
                                        mSum++;
                                    }
                                    bean.setSurplusCount(count);
                                    dataList.add(bean);
                                }
                                surplusBean.setData(dataList);

                                if (mSum == 0) {
                                    androidLauncher.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showANewSingleButtonDialog();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Gdx.app.error("dialog", e.getMessage());
                            }
                        }

                        @Override
                        public void failed(Throwable t) {
                            Gdx.app.error("dialog", t.getMessage());
                        }


                        @Override
                        public void cancelled() {
                            Gdx.app.error("dialog", "cancelled()");
                        }
                    });
                }

            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("dialog", t.getMessage());
            }


            @Override
            public void cancelled() {
                Gdx.app.error("dialog", "cancelled()");
            }
        });
    }

    private void showANewSingleButtonDialog() {
        mCustomDialog = new CustomDialog(androidLauncher);
        mCustomDialog.setContentVisiable(false);
        mCustomDialog.setTitleVisiable(true);
        mCustomDialog.setImageContentVisiable(false);
        mCustomDialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCanPlay(true);
                Gdx.app.error("dialog", "setOnClickListener   " + mCustomDialog.isShowing());
            }
        });
        mCustomDialog.setTitleText("优惠券已抢光，下次再来吧!");
        mCustomDialog.setButtonText("知道了");
        mCustomDialog.show();
    }

    @Override
    public void setCanPlay(boolean canPlay) {
        super.setCanPlay(canPlay);
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
