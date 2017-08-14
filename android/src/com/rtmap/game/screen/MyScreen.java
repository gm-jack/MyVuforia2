package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.rtmap.game.MyGame;
import com.rtmap.game.camera.MagicCamera;
import com.rtmap.game.interfaces.AnimationListener;
import com.rtmap.game.text.NativeFont;
import com.rtmap.game.text.NativeFontPaint;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.rtmap.game.actor.AimActor.number;

/**
 * Created by yxy on 2017/2/24.
 */
public abstract class MyScreen implements Screen {
    public static final int ZUO = 2;
    public static final int PAO = 0;
    public static final int HUANHU = 1;

    //    private CameraInputController camController;
    private MyGame game;
    public Array<GameObject> instances = new Array<GameObject>();
    private ModelBatch modelBatch;
    private Environment environment;
    private MagicCamera camera;
    private Vector3 position = new Vector3();
    //是否停止更新camera
    private boolean stopCamera = true;
    //是否加载模型
    public boolean isLoading = true;
    //是否停止绘制模型
    private boolean stopRerder = false;
    private AnimationController animationController1;
    private AnimationController animationController2;
    private AnimationController animationController3;

    //设置是否显示距离
    private boolean isLineShow = true;
    private int width;
    private int height;
    private SpriteBatch spriteBatch;
    private List<TextureRegion> texture = new ArrayList<TextureRegion>();
    private double angle;
    private double radius;
    private float nowAngle;
    //弧度制
    private double v;
    //字体
    private NativeFont lazyBitmapFont;
    private float x;
    private float y;
    //模型在球形背面
    private boolean isPositive = false;
    private int distance = 11;
    private int delNum = 0;
    private int drawNum = 100;
    //是否开启箭头动画
    private boolean isAnim = false;
    //动画监听
    private AnimationListener animationListener;
    //保存箭头是否显示
    private boolean oldIsLineShow = true;
    //判断是否正在平移动画
    private boolean isTranslate = false;
    private float time = 0;
    private Vector3 now;

    private int modelNumber = PAO;

    private float delTime = 0;
    private float rayDistance = -1;
    private Vector3 old;
    private float initAngle = -185f;
    private String message;
    private int mTao = 10;
    private boolean isRay = true;
    private boolean isPao = false;
    private boolean isUpdate = true;
    private float mTextWidthRadiu;
    private Vector3 translate = new Vector3();
    //世界坐标转换成的屏幕坐标
    private Vector3 mRealProject;
    private boolean isCanPlay = true;
    private int mQuadrant;
    private boolean isFind = true;
    private boolean isTrans = true;
    private float offsetX;
    private float mOffsetY;
    private boolean isY;
    private boolean mB;
    private int mMin = -7;
    private int mMax = 7;
    private boolean isX;
    private Vector3 mVector3;
    private float mOffsetX;
    private float mTranX;
    private float mTranY;
    private int count = 0;
    private double mChangeX;
    private double mChangeY;

    public MyScreen() {

    }

    public MyScreen(MyGame game) {
        this.game = game;
        if (spriteBatch == null)
            spriteBatch = new SpriteBatch();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        radius = height * 3 / 8;

//        if (modelBatch == null) {
//            DefaultShader.Config config = new DefaultShader.Config();
//            config.defaultCullFace = 0;
//            modelBatch = new ModelBatch(null, new DefaultShaderProvider(config), null);
//        }
        modelBatch = new ModelBatch();
        if (environment == null)
            environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.Diffuse, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -3f, -0.8f, -0.2f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        camera = new MagicCamera(67f, width, height);
        camera.translate(0, 0, 0);
        camera.lookAt(0, 0, 14);
        camera.far = 600.0f;
        camera.near = 1f;

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
    }

    private void doneLoading() {
        texture.add(new TextureRegion(game.asset.get("aim_line.png", Texture.class)));
        texture.add(new TextureRegion(game.asset.get("find_anim.png", Texture.class)));
        texture.add(new TextureRegion(game.asset.get("aim_success.png", Texture.class)));
        texture.add(new TextureRegion(game.asset.get("find_center.png", Texture.class)));
        mTextWidthRadiu = texture.get(2).getRegionWidth() / 2;

        GameObject shipInstanceHuanHu = new GameObject(game.asset.get("tiger/red2.g3dj", Model.class));
        GameObject shipInstancePao = new GameObject(game.asset.get("tiger/red1.g3dj", Model.class));
        GameObject shipInstanceZhuaQu = new GameObject(game.asset.get("tiger/red3.g3dj", Model.class));

        Material attribute1 = shipInstanceHuanHu.materials.get(0);
        attribute1.set(new ColorAttribute(ColorAttribute.Diffuse, 1f, 1f, 1f, 1f));
        Material attribute2 = shipInstancePao.materials.get(0);
        attribute2.set(new ColorAttribute(ColorAttribute.Diffuse, 1f, 1f, 1f, 1f));
        Material attribute3 = shipInstanceZhuaQu.materials.get(0);
        attribute3.set(new ColorAttribute(ColorAttribute.Diffuse, 1f, 1f, 1f, 1f));
//        int i = 9;
//        int j = 9;
//        boolean rd = Math.random() > 0.5;
//        if (rd) {
//            i = -i;
//        }
//        boolean rds = Math.random() > 0.5;
//        if (rds) {
//            j = -j;
//        }
        float x = (float) (-12 * Math.random() + 6);
        float y = (float) (-12 * Math.random() + 6);
        shipInstancePao.transform.setToTranslation(x, y, 14);
//        mQuadrant = getQuadrant(i, j);

        //        Take 001
        animationController2 = new AnimationController(shipInstanceHuanHu);
        animationController2.setAnimation("Take 001", -1);
        animationController1 = new AnimationController(shipInstancePao);
        animationController1.setAnimation("Take 001", -1);
        animationController3 = new AnimationController(shipInstanceZhuaQu);
        animationController3.setAnimation("Take 001", -1);
        instances.add(shipInstancePao);
        instances.add(shipInstanceHuanHu);
        instances.add(shipInstanceZhuaQu);

        getModelAngle();

        isLoading = false;
    }

    public void setRay(boolean ray) {
        isRay = ray;
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(int x, int y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }

    }

    /**
     * 设置模型正对相机角度
     */
    public void getModelAngle() {
        Gdx.app.error("Quadrant", "mQuadrant   =   " + mQuadrant);
//        switch (mQuadrant) {
//            case 1:
//                initAngle = 135f;
//                break;
//            case 2:
//                initAngle = 135f;
//                break;
//            case 3:
//                initAngle = 135f - 90;
//                break;
//            case 4:
//                initAngle = 135f + 180;
//                break;
//        }
        for (int i = 0; i < instances.size; i++) {
            if (i == 2)
                instances.get(i).transform.rotate(0, 1, 0, -175f);
            if (i != 2)
                instances.get(i).transform.rotate(0, 1, 0, initAngle);
        }
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
        for (int i = 0; i < texture.size(); i++) {
            texture.get(i).getTexture().dispose();
        }

        if (lazyBitmapFont != null)
            lazyBitmapFont.dispose();
    }

    public void setFind(boolean find) {
        isFind = find;
    }

    public void setTranslate(boolean translate) {
        this.isTranslate = translate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public void setCanPlay(boolean canPlay) {
        isCanPlay = canPlay;
    }

    @Override
    public void render(float delta) {
        if (spriteBatch == null) return;
        if (game.asset.update() && isLoading) {
            doneLoading();
        }
        if (!stopCamera && camera != null && isCanPlay) {
            if (isUpdate)
                camera.updateCamera();

            isPositive();

            if (texture.size() > 0) {
                spriteBatch.begin();

                mRealProject = camera.project(translate);
                Gdx.app.error("ray", " mProject X " + mRealProject.x + "  mProject   Y " + mRealProject.y + " mUnproject  Z " + mRealProject.z);
                if (mRealProject.y <= (height / 2 - mTextWidthRadiu * 3 / 4)) {
                    rayDistance = (float) Math.abs(Math.sqrt((mRealProject.x - width / 2) * (mRealProject.x - width / 2) + ((mRealProject.y + mTextWidthRadiu) - height / 2) * ((mRealProject.y + mTextWidthRadiu) - height / 2)));
                } else
                    rayDistance = (float) Math.abs(Math.sqrt((mRealProject.x - width / 2) * (mRealProject.x - width / 2) + (mRealProject.y - height / 2) * (mRealProject.y - height / 2)));
                distance = (int) rayDistance / 120;

                if (oldIsLineShow != isLineShow) {
                    oldIsLineShow = isLineShow;
                }

                if (delTime <= Gdx.graphics.getDeltaTime() * 30)
                    delTime += Gdx.graphics.getDeltaTime();

                if (delTime > Gdx.graphics.getDeltaTime() * 30) {
                    if (mRealProject.x > -20 && mRealProject.x < width + 20 && mRealProject.y > -20 && mRealProject.y < height + 20 && !isPositive) {
                        isLineShow = false;
                    } else
                        isLineShow = true;
                    if (animationListener != null) {
                        animationListener.startAnim(isLineShow);
                    }
                }

                if (isLineShow && isFind) {
                    //角度计算
                    getAngle(camera.project(position));

                    double abs = Math.abs(Math.sqrt(radius * radius - width * width / 4));
                    double minRightAngle = Math.toDegrees(Math.atan(Math.abs(width / 2 / abs)));
                    double maxRightAngle = 90 + Math.toDegrees(Math.atan(Math.abs(abs / width * 2)));
                    double minLeftAngle = 180 + Math.toDegrees(Math.atan(Math.abs(width / 2 / abs)));
                    double maxLeftAngle = 270 + Math.toDegrees(Math.atan(Math.abs(abs / width * 2)));


                    if (angle >= minRightAngle && angle <= 90) {
                        v = (90 - angle) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = (float) (nowAngle * Math.sin(v));
                        x = (float) (nowAngle * Math.cos(v));
                    } else if (angle > 90 && angle <= maxRightAngle) {
                        v = (angle - 90) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = -(float) (nowAngle * Math.sin(v));
                        x = (float) (nowAngle * Math.cos(v));
                    } else if (angle >= minLeftAngle && angle < 270) {
                        v = (270 - angle) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = -(float) (nowAngle * Math.sin(v));
                        x = -(float) (nowAngle * Math.cos(v));
                    } else if (angle >= 270 && angle <= maxLeftAngle) {
                        v = (angle - 270) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = (float) (nowAngle * Math.sin(v));
                        x = -(float) (nowAngle * Math.cos(v));
                    } else {
                        v = angle * Math.PI / 180;
                        nowAngle = (float) radius;
                        x = (float) (nowAngle * Math.sin(v));
                        y = (float) (nowAngle * Math.cos(v));
                    }

                    float fontX = width / 2 + texture.get(0).getRegionWidth() / 2 + x;
                    if (fontX < 0) {
                        fontX = width * 0.01f;
                    } else if (fontX + width * 0.09f > width) {
                        fontX = width * 0.91f;
                    }


                    //绘制指示器
                    spriteBatch.draw(texture.get(0), width / 2 - texture.get(0).getRegionWidth() / 2, height / 2, texture.get(0).getRegionWidth() / 2, 0, texture.get(0).getRegionWidth(), Math.abs(nowAngle), 1, 1, (float) (-angle));

                    if (lazyBitmapFont == null) {
                        lazyBitmapFont = new NativeFont(new NativeFontPaint(ScreenUtil.dp2px(10), Color.valueOf("#ffb54b")));
                    }
//                    if (lazyBitmapFont == null)
//                        lazyBitmapFont = new LazyBitmapFont(ScreenUtil.dp2px(10), Color.valueOf("#ffb54b"));

                    if (distance > 100 || isPositive) {
                        message = "距离\n>100";
                    } else {
                        message = "距离\n" + distance;
                    }
                    lazyBitmapFont.appendText(message);
//                    lazyBitmapFont.getCache().
                    lazyBitmapFont.draw(spriteBatch, message, fontX, height / 2 + y, width * 0.09f, Align.left, true);

                    if (isAnim) {
                        if (delNum <= drawNum) {
                            spriteBatch.draw(texture.get(1), width / 2 + x * 0.75f / drawNum * delNum + x * 0.186f - texture.get(1).getRegionWidth() / 2, height / 2 + y * 0.75f / drawNum * delNum + y * 0.186f, texture.get(1).getRegionWidth() / 2, 0, texture.get(1).getRegionWidth(), texture.get(1).getRegionHeight(), 2, 2, (float) (-angle));
                            delNum++;
                            Gdx.app.error("animations", isAnim + "   delNum    " + delNum);
                        } else {
                            if (animationListener != null) {
                                animationListener.endAnim();
                            }
                            delNum = 0;
                        }
                    }
                }

                spriteBatch.end();
            }
        }

        if (instances.size > 0 && !stopRerder) {
            GameObject object = instances.get(modelNumber);
            modelBatch.begin(camera);
            if (isVisible(camera, object)) {
                if (getScreen() instanceof AimScreen) {
                    isRay = true;
                } else {
                    isRay = false;
                }
                Gdx.app.error("add", "" + isRay);
//                mUnproject = camera.unproject(translate);
                modelBatch.render(object, environment);
            }
            modelBatch.end();

            instances.get(modelNumber).transform.getTranslation(translate);
            //线性移动
            if (isTranslate && modelNumber != ZUO) {
                setDuration();
                if (!isTrans) {
                    if (time >= 0.01) {
                        if (mTranX <= mMin) {
                            isTrans = true;
                        }
                        if (mTranX >= mMax) {
                            isTrans = true;
                        }
                        if (mTranY <= mMin) {
                            isTrans = true;
                        }
                        if (mTranY >= mMax) {
                            isTrans = true;
                        }
                        mTranX += (float) mChangeX;
                        mTranY += (float) mChangeY;
                        //                    if (isX) {
                        //                        offsetX = old.x;
                        //                    } else {
                        //                        if (isTrans) {
                        //                            if (offsetX <= mMin) {
                        //                                isTrans = false;
                        //                                isX = Math.random() >= 0.5;
                        ////                            isX = isTrans;
                        ////                            isTrans = Math.random() >= 0.5;
                        ////                            if (isTrans == isX) {
                        ////                                isTrans = !isX;
                        ////                            }
                        //                            }
                        //                            offsetX = old.x - 0.08f;
                        //                        } else {
                        //                            if (offsetX >= mMax) {
                        //                                isTrans = true;
                        //                                if (isY) {
                        //                                    isY = false;
                        //                                }
                        ////                            isX = isTrans;
                        ////                            isTrans = Math.random() >= 0.5;
                        ////                            if (isTrans == isX) {
                        ////                                isTrans = !isX;
                        ////                            }
                        //                            }
                        //                            offsetX = old.x + 0.08f;
                        //                        }
                        //                    }
                        //                    if (isY) {
                        //                        mOffsetY = old.y;
                        //                    } else {
                        //                        if (mB) {
                        //                            if (mOffsetY >= mMax) {
                        //                                mB = false;
                        ////                            isY = mB;
                        ////                            mB = Math.random() >= 0.5;
                        ////                            if (mB == isY) {
                        ////                                mB = !isY;
                        ////                            }
                        //                                isY = Math.random() >= 0.5;
                        //                            }
                        //
                        //                            mOffsetY = old.y + 0.08f;
                        //                        } else {
                        //                            if (mOffsetY <= mMin) {
                        //                                mB = true;
                        ////                            isY = mB;
                        //                                if (isX) {
                        //                                    isX = false;
                        //                                }
                        ////                            if (mB == isY) {
                        ////                                mB = !isY;
                        ////                            }
                        //                            }
                        //                            mOffsetY = old.y - 0.08f;
                        //                        }
                        //                    }
                        Gdx.app.error("random", "offsetX   " + mTranX + "   mOffsetY   " + mTranY);
                        float offsetZ = old.z;
                        if (mTranX != old.x || mTranY != old.y) {
                            this.now = new Vector3(mTranX, mTranY, offsetZ);
                            instances.get(modelNumber).transform.setToTranslation(now);
                            getModelAngle();
                        }
                        Gdx.app.error("translate", "当前坐标  translate.x  " + translate.x + "  translate.y  " + translate.y + "  translate.z  " + translate.z);
                        time = 0;
                    } else {
                        time += Gdx.graphics.getDeltaTime();
                    }
                }
            }

            switchAnimationModel();

            /**
             * 添加射线
             */

            Gdx.app.error("TAG", "isPao    " + isPao + "    mTao    " + mTao);
            if (!isPao && isRay && number <= 12 && rayDistance != -1 && !isAnimation()) {
                if (rayDistance < (mTextWidthRadiu * 3 / 4) && !isPositive) {
                    setTranslate(false);
                    setModelNumber(HUANHU);
                    if (modelNumber == HUANHU) {
                        addNumber();
                    } else {
                        subNumber();
                    }
                } else {
                    subNumber();
                    setTranslate(true);
                    setModelNumber(PAO);
                }
            }
        }
    }

    private void setDuration() {
        if (isTrans) {
            old = translate;

            double x = -12 * Math.random() + 6;
            double y = -12 * Math.random() + 6;
//            if (x > 0) {
//                x = -old.x;
//            } else if (y < 0) {
//                y = old.y;
//            }
            mVector3 = new Vector3((float) x, (float) y, translate.z);

            mOffsetX = mVector3.x - old.x;
            mOffsetY = mVector3.y - old.y;

            mChangeX = mOffsetX / 1 * 0.01;
            mChangeY = mOffsetY / 1 * 0.01;

            mTranX = old.x;
            mTranY = old.y;
            count = 0;

            isTrans = false;
        }
    }

    public MyScreen getScreen() {
        return this;
    }

    public boolean isAnimation() {
        return true;
    }

    //设置显示模型
    public void setModelNumber(int number) {
        if (modelNumber == number) {
            return;
        }

        this.modelNumber = number;
        if (instances.size > 0) {
            if (number == ZUO) {
                camera.lookAt(0, 0, 14);
                camera.update();
                instances.get(number).transform.setToTranslation(0, -1f, 14);
            } else if (number == HUANHU) {
                camera.lookAt(translate.x, translate.y, 14);
                camera.update();
                instances.get(number).transform.setToTranslation(translate.x, translate.y, translate.z);
            } else {
                instances.get(number).transform.setToTranslation(translate.x, translate.y, translate.z);
            }
//            else {
//                instances.get(number).transform.setToTranslation(translate.x, translate.y, translate.z + 5);
//            }
            getModelAngle();
        }
    }

    public void switchAnimationModel() {
        switch (modelNumber) {
            case PAO:
                animationController1.update(Gdx.graphics.getDeltaTime());
                break;
            case HUANHU:
                animationController2.update(Gdx.graphics.getDeltaTime());
                break;
            case ZUO:
                animationController3.update(Gdx.graphics.getDeltaTime());
                break;
            case 3:
//                animationController4.update(Gdx.graphics.getDeltaTime());
                break;
        }
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setIsAnim(boolean isAnim) {
        this.isAnim = isAnim;
//        delNum = 0;
    }

    public void setIsLineShow(boolean isLineShow) {
        this.isLineShow = isLineShow;
        delTime = 0;
    }

    public boolean isLineShow() {
        return isLineShow;
    }

    /**
     * 判断模型在摄像机正面还是背面
     * true:背面
     * false:正面
     */
    public void isPositive() {
        Vector3 direction = camera.direction;
        Vector3 backDirection = new Vector3(-direction.x, -direction.y, -direction.z);
        double abs1 = Math.abs(Math.sqrt((direction.x - translate.x) * (direction.x - translate.x) + (direction.y - translate.y) * (direction.y - translate.y) + (direction.z - translate.z) * (direction.z - translate.z)));
        double abs2 = Math.abs(Math.sqrt((backDirection.x - translate.x) * (backDirection.x - translate.x) + (backDirection.y - translate.y) * (backDirection.y - translate.y) + (backDirection.z - translate.z) * (backDirection.z - position.z)));
        if (abs1 - abs2 > 0) {
            isPositive = true;
        } else {
            isPositive = false;
        }
    }

    /**
     * 计算角度
     *
     * @param project
     * @return
     */
    private void getAngle(Vector3 project) {
        double tan;
        if (project.x == width / 2) {
            if (project.y > height / 2) {
                angle = 0;
            } else if (project.y < height / 2) {
                angle = 180;
            }
        } else if (project.y == height / 2) {
            if (project.x > width / 2) {
                angle = 90;
            } else if (project.x < width / 2) {
                angle = 270;
            }
        } else {
            if (project.x > width / 2 && project.y > height / 2) {
                //第一象限
                if (isPositive) {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan))) - 180;
                } else {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x > width / 2 && project.y < height / 2) {
                //第四象限
                if (isPositive) {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan))) - 90;
                } else {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 90 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x < width / 2 && project.y < height / 2) {
                //第三象限
                if (isPositive) {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan)));
                } else {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = 180 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x < width / 2 && project.y > height / 2) {
                //第二象限
                if (isPositive) {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 90 + Math.toDegrees(Math.atan(Math.abs(tan)));
                } else {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 270 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            }
        }
    }


    //子类实现
    public void addNumber() {
    }

    //子类实现
    public void subNumber() {

    }

    public void setStopCamera(boolean stopCamera) {
        this.stopCamera = stopCamera;
    }

    public void setStopRerder(boolean stopRerder) {
        this.stopRerder = stopRerder;
    }

    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }

    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject(Model mode) {
            super(mode);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }
}
