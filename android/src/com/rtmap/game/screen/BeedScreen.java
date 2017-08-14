package com.rtmap.game.screen;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.BeedBackActor;
import com.rtmap.game.actor.BeedItemActor;
import com.rtmap.game.actor.CloseActor;
import com.rtmap.game.actor.DetailActor;
import com.rtmap.game.actor.GameBeedActor;
import com.rtmap.game.actor.UseRuleActor;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedItemOnClickListener;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.scrollpane.BeedScrollPane;
import com.rtmap.game.stage.BeedStage;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.CustomDialog;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class BeedScreen extends MyScreen {
    private AndroidLauncher androidLauncher;
    private GameBeedActor gameBeedActor;
    private BeedStage beedStage;
    private MyGame mGame;
    private BeedBackActor beedBackActor;
    private Group group;
    private Array<BeedItemActor> itemActors = new Array<BeedItemActor>();
    private BeedScrollPane beedScrollPane;
    private Table table;
    public List<Result> list = new ArrayList<Result>();
    private AssetManager assetManager;
    private CloseActor closeActor;
    private DetailActor detailActor;
    private EventListener beedBackClickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            mGame.showOldScreen();
            BeedScreen.this.dispose();
            super.clicked(event, x, y);
        }
    };
    private UseRuleActor mUseRuleActor;
    private CustomDialog mDialog;
    private boolean isShow = false;
    //    private ToastActor mActor;

    public BeedScreen(MyGame game, AndroidLauncher androidLauncher, ScreenViewport viewport) {
        this.mGame = game;
        //瞄准怪兽舞台
        this.androidLauncher = androidLauncher;
        SpriteBatch batch = new SpriteBatch();
//        batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
        batch.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE);
        beedStage = new BeedStage(viewport, batch);
        assetManager = new AssetManager();
        initResouce();

        group = new Group();
        gameBeedActor = new GameBeedActor(assetManager);
        gameBeedActor.setPosition(0, 0);
        gameBeedActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(gameBeedActor);

        beedBackActor = new BeedBackActor(assetManager, gameBeedActor);
        group.addActor(beedBackActor);

        table = new Table();
        table.align(Align.top);

        beedScrollPane = new BeedScrollPane(table);
        beedScrollPane.setScrollingDisabled(true, false);//设置是否可上下、左右移动..这里设置了横向可移动、纵向不可移动
        beedScrollPane.setSmoothScrolling(true);
//        beedScrollPane.setFlickScroll(false);
        beedScrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - gameBeedActor.getTitleHeight());
//        beedScrollPane.setPosition(Utils.xAxisCenter(getWidth()), Utils.yAxisCenter(getHeight()) - 20);
        beedScrollPane.layout();
        group.addActor(beedScrollPane);

        beedStage.addActor(group);

    }

    private void initResouce() {
        assetManager.load("beed_bg.png", Texture.class);
        assetManager.load("m_bg.png", Texture.class);
        assetManager.load("use.png", Texture.class);
        assetManager.load("main_beed.png", Texture.class);
        assetManager.load("beed_title.png", Texture.class);
        assetManager.load("beed_back.png", Texture.class);
        assetManager.load("beed_item_bg.png", Texture.class);
        assetManager.load("beed_item_nouse.png", Texture.class);
        assetManager.load("beed_item_use.png", Texture.class);
        assetManager.load("beed_item_line.png", Texture.class);
        assetManager.load("beed_open_bg.png", Texture.class);
        assetManager.load("open_line.png", Texture.class);
        assetManager.load("open_close.png", Texture.class);
        assetManager.load("cover.png", Texture.class);
        assetManager.load("catch_bg.png", Texture.class);
        assetManager.finishLoading();
    }

    private void initData(List<Result> lists) {
        if (lists != null && lists.size() > 0) {
            itemActors.clear();

            for (int i = 0; i < lists.size(); i++) {
                itemActors.add(new BeedItemActor(assetManager, lists.get(i)));
            }
            addListeners();
            for (int i = 0; i < itemActors.size; i++) {
                BeedItemActor beedItemActor = itemActors.get(i);
                table.add(beedItemActor).width(Gdx.graphics.getWidth()).height(beedItemActor.getRealHeight());
                table.row();
            }
        }
    }

    private void addListeners() {
        for (int i = 0; i < itemActors.size; i++) {
            BeedItemActor beedItemActor = itemActors.get(i);
            beedItemActor.setListener(new BeedItemOnClickListener() {
                @Override
                public void onClick(BeedItemActor actor, Result item) {
                    beedScrollPane.setScrollingDisabled(true, true);
                    beedScrollPane.setForceScroll(false, false);
                    beedScrollPane.setFlickScroll(false);
                    removeListeners();

                    //绘制优惠券详情
                    detailActor = new DetailActor(assetManager);
                    group.addActor(detailActor);

                    //使用规则
                    mUseRuleActor = new UseRuleActor(assetManager);
                    mUseRuleActor.setListener(new StartOnClickListener() {
                        @Override
                        public void onClick() {
                            androidLauncher.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showANewSingleButtonDialog();
                                }
                            });
                        }
                    });
                    group.addActor(mUseRuleActor);

                    //绘制关闭按钮
                    closeActor = new CloseActor(assetManager);

                    closeActor.setListener(new BackOnClickListener() {
                        @Override
                        public void onClick() {
                            if (mUseRuleActor != null)
                                group.removeActor(mUseRuleActor);
                            if (detailActor != null)
                                detailActor.setIsOpen(false);
                            closeActor.setIsShow(false);
                            beedScrollPane.setScrollingDisabled(true, false);
                            beedScrollPane.setForceScroll(false, true);
                            beedScrollPane.setFlickScroll(true);
                            addListeners();
                            if (beedBackActor != null && beedBackClickListener != null)
                                beedBackActor.addListener(beedBackClickListener);
                        }
                    });
                    group.addActor(closeActor);

                    detailActor.setResult(item);
                    detailActor.setIsOpen(true);
                    closeActor.setIsShow(true);
                    if (beedBackActor != null && beedBackClickListener != null)
                        beedBackActor.removeListener(beedBackClickListener);
                    Gdx.app.error("list", "onClick   item ==" + item);
                }
            }, i);
        }
    }

    private void removeListeners() {
        if (itemActors.size > 0)
            for (int i = 0; i < itemActors.size; i++) {
                BeedItemActor beedItemActor = itemActors.get(i);
                beedItemActor.removeThisListener();
            }
    }

    @Override
    public void show() {
        if (mGame != null)
            mGame.stopCamera(true);
    }

    private void initListener() {
        Gdx.input.setInputProcessor(beedStage);
        if (beedBackActor != null)
            beedBackActor.addListener(beedBackClickListener);
        if (beedScrollPane != null) {
            beedScrollPane.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.error("list", "touchDown");
                    event.stop();
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvOk;

    private void showANewSingleButtonDialog() {
        final CustomDialog dialog = new CustomDialog(androidLauncher);
        dialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gdx.app.error("app", "setOnClickListener   " + dialog.isShowing());
            }
        });
        dialog.show();

//        GDXButtonDialog dialogs = mDManager.newDialog(GDXButtonDialog.class);
//        dialogs.setTitle("使用规则");
//        dialogs.addButton("确定");
//        dialogs.setClickListener(new ButtonClickListener() {
//            @Override
//            public void click(int button) {
//
//            }
//        });

//        final AlertDialog.Builder builder = new AlertDialog.Builder(androidLauncher, R.style.dialog);
//        View inflate = View.inflate(androidLauncher, R.layout.dialog_rule, null);
//        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
//        tvContent = (TextView) inflate.findViewById(R.id.tv_content);
//        tvOk = (TextView) inflate.findViewById(R.id.tv_ok);
//        Gdx.app.error("app", "requestFocus   " + inflate.requestFocus());
//
//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                androidLauncher.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isBuild) {
//                            isBuild = false;
//                            WindowManager manager = androidLauncher.getWindowManager();
//                            manager.removeViewImmediate(mAlertDialog.getWindow().getDecorView());
//                            addListeners();
//                            Gdx.app.error("app", "setOnClickListener   " + mAlertDialog.isShowing());
//                        }
//                    }
//                });
//
//            }
//        });
//        builder.setView(inflate);
//        builder.setCancelable(false);
//        if (!isBuild) {
//            mAlertDialog = builder.create();
//            mAlertDialog.setView(inflate, 0, 0, 0, 0);
//
//            mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape);
////            androidLauncher.runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
//            mAlertDialog.show();
//            isBuild = true;
////                }
////            });
//        }


    }

    @Override
    public void render(float delta) {
        if (beedStage == null)
            return;

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // 更新舞台逻辑
        beedStage.act();
        // 绘制舞台
        beedStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.error("list", "resize");
        getData();
        initListener();
    }

    private void getData() {
        String phoneNumber = (String) SPUtil.get(Contacts.PHONE, "");
        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        Gdx.app.error("net", Contacts.LIST_NET + phoneNumber);
        if (!NetUtil.getInstance().checkConnection(androidLauncher) && !isShow) {
            androidLauncher.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isShow = true;
                    showNoInternet();
                }
            });

            return;
        }
        Gdx.app.error("net", Contacts.LIST_NET + phoneNumber);
        NetUtil.getInstance().get(Contacts.LIST_NET + phoneNumber, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.error("http", "handleHttpResponse()");
                String resultAsString = httpResponse.getResultAsString();
//                showToast(resultAsString);
                List<Result> lists = new ArrayList<Result>();
                try {
                    JSONArray array = new JSONArray(resultAsString);
                    for (int i = 0; i < array.length(); i++) {
                        Result result = new Result();

                        JSONObject object = array.getJSONObject(i);
                        result.setBuildId(object.optString("buildId"));
                        result.setCode(object.optString("code"));
                        result.setMarketName(object.optString("marketName"));
                        result.setShopName(object.optString("shopName"));
                        result.setLevel(object.optString("level"));
                        result.setMain(object.optString("main"));
                        result.setLogoUrl(object.optString("logoUrl"));
                        result.setExtend(object.optString("extend"));
                        result.setStartTime(object.optString("startTime"));
                        result.setEndTime(object.optString("endTime"));
                        result.setPosition(object.optString("position"));
                        result.setImgUrl(object.optString("imgUrl"));
                        result.setQr(object.optString("qr"));
                        result.setStatus(object.optString("status"));
                        result.setTemplate(object.optString("template"));
                        result.setTt(object.optString("tt"));
                        result.setPid(object.optString("pid"));
                        result.setNum(object.optString("num"));
                        result.setIssue(object.optString("issue"));
                        result.setCoupon(object.optString("coupon"));
                        result.setOpenId(object.optString("openId"));
                        result.setNickname(object.optString("nickname"));
                        result.setHead(object.optString("head"));
                        result.setDesc(object.optString("desc"));
                        result.setRefund(object.optString("refund"));
                        result.setWxsync(object.optString("wxsync"));
                        result.setCardId(object.optString("cardId"));
                        result.setShare(object.optString("share"));
                        result.setFollow(object.optString("follow"));
                        result.setPrice(object.optString("price"));
                        result.setNotifyType(object.optString("notifyType"));
                        result.setNotifyMessage(object.optString("notifyMessage"));

                        lists.add(result);
                    }

                    initData(lists);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t) {
//                showToast("加载失败");
                Gdx.app.error("http", t.getMessage());
            }

            @Override
            public void cancelled() {
                Gdx.app.error("http", "cancelled()");
            }
        });
    }

    private void showNoInternet() {
        if (mDialog == null)
            mDialog = new CustomDialog(androidLauncher);
        mDialog.setContentVisiable(true);
        mDialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = false;
                Gdx.app.error("app", "setOnClickListener   " + mDialog.isShowing());
            }
        });
        mDialog.setTitleText("提示");
        mDialog.setContentText("无法连接网络，请检查网络设置");
        mDialog.setButtonText("确定");
        mDialog.show();
    }
//    private void showToast(String message) {
//        Gdx.app.error("toast", "show");
//        if (mActor == null)
//            mActor = new ToastActor();
//        group.addActor(mActor);
//        mActor.setIsShow(true, message);
//    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        if (beedStage != null) {
            beedStage.dispose();
        }
    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (beedStage != null) {
            beedStage.dispose();
        }

    }
}
