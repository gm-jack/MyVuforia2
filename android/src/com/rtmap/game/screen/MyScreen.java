package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
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
    private List<TextureRegion> texture = new ArrayList<>();
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
    //动画持续时间，动画完成设置初始值0
    private float durations = 0;
    //判断是否正在平移动画
    private boolean isTranslate = true;
    private float time = 0;
    //是否启动线性移动，优先级高
    private boolean isStart = true;
    private Vector3 now;

    private int modelNumber = PAO;

    private float delTime = 0;
    private float rayDistance = -1;
    private Vector3 old;
    private float initAngle = 50f;
    private String message;
    private int mTao = 10;
    private boolean isRay = true;
    private boolean isPao = false;
    private boolean isUpdate = true;
    private float addDetal = 0;
    private float mTextWidthRadiu;
    private Vector3 translate = new Vector3();
    //    private AnimationController animationController4;
    private float meZ1 = 12;
    private float meZ2 = -7;
    //屏幕坐标转换成的世界坐标
    private Vector3 mUnproject;
    //世界坐标转换成的屏幕坐标
    private Vector3 mRealProject;
    private int count = 0;
    private boolean isCanPlay = true;
    private int mQuadrant;
    private boolean isFind = true;

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
        camera.lookAt(10, 0, 10);
        camera.far = 500.0f;
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

        GameObject shipInstanceHuanHu = new GameObject(game.asset.get("tiger/laohu-huanhu.g3dj", Model.class));
        GameObject shipInstancePao = new GameObject(game.asset.get("tiger/laohu-pao.g3dj", Model.class));
        GameObject shipInstanceZhuaQu = new GameObject(game.asset.get("tiger/laohu-zhuaqu.g3dj", Model.class));

        int i = 9;
        int j = 9;
        boolean rd = Math.random() > 0.5;
        if (rd) {
            i = -i;
        }
        boolean rds = Math.random() > 0.5;
        if (rds) {
            j = -j;
        }
        shipInstancePao.transform.setToTranslation(i, 0, j);
        mQuadrant = getQuadrant(i, j);

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
        switch (mQuadrant) {
            case 1:
                initAngle = 135f;
                break;
            case 2:
                initAngle = 135f;
                break;
            case 3:
                initAngle = 135f - 90;
                break;
            case 4:
                initAngle = 135f + 180;
                break;
        }
        for (int i = 0; i < instances.size; i++) {
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

    public void setDurations(int time) {
        if (isStart) {
            this.old = new Vector3(translate.x, translate.y, translate.z);
            float i = (float) (Math.random() * (1.5 - 2) + 2);
            float j = (float) (Math.random() * (1.5 - 2) + 2);
            boolean rd = Math.random() > 0.5;
            if (rd) {
                i = -i;
                j = -j;
            }
            float offsetX = old.x + i;
            float offsetY = old.y;
            float offsetZ = old.z + j;
            if (offsetX > 0) {
                if (offsetX <= -meZ2) {
                    offsetX = -meZ2;
                } else if (offsetX >= meZ1) {
                    offsetX = meZ1;
                }
            } else {
                if (offsetX >= meZ2) {
                    offsetX = meZ2;
                } else if (offsetX <= -meZ1) {
                    offsetX = -meZ1;
                }
            }
            if (offsetZ > 0) {
                if (offsetZ >= meZ1) {
                    offsetZ = meZ1;
                } else if (offsetZ <= -meZ2) {
                    offsetZ = -meZ2;
                }
            } else {
                if (offsetZ <= -meZ1) {
                    offsetZ = -meZ1;
                } else if (offsetZ >= meZ2) {
                    offsetZ = meZ2;
                }
            }

            Gdx.app.error("model", "当前坐标  old.x  " + old.x + "  old.y  " + old.y + "  old.z  " + old.z);
            Gdx.app.error("model", "目标坐标  offsetX  " + offsetX + "  offsetY  " + offsetY + "  offsetZ  " + offsetZ);
            this.time = 0;
            this.durations = time;
            this.now = new Vector3(offsetX, offsetY, offsetZ);


            getModelInstanceAngle();

            isStart = false;
        }
    }

    /**
     * 平移渐变动画
     */
    public void translateAnimation() {
        if (time > durations) {
            isPao = false;
            isStart = true;
            isTranslate = false;
            durations = 0;
        } else {
            if (!isStart) {
                float renderTime = 1 / durations / 100;

                float scollX = (int) (now.x - old.x) * renderTime;
                float scollZ = (int) (now.z - old.z) * renderTime;
                float scollY = (int) (now.y - old.y) * renderTime;

                if (translate.x > 0) {
                    if (translate.x <= -meZ2) {
                        translate.x = -meZ2;
                    } else if (translate.x >= meZ1) {
                        translate.x = meZ1;
                    }
                } else {
                    if (translate.x >= meZ2) {
                        translate.x = meZ2;
                    } else if (translate.x <= -meZ1) {
                        translate.x = -meZ1;
                    }
                }
                if (translate.z > 0) {
                    if (translate.z >= meZ1) {
                        translate.z = meZ1;
                    } else if (translate.z <= -meZ2) {
                        translate.z = -meZ2;
                    }
                } else {
                    if (translate.z <= -meZ1) {
                        translate.z = -meZ1;
                    } else if (translate.z >= meZ2) {
                        translate.z = meZ2;
                    }
                }
                if (translate.y >= 5) {
                    translate.y = 5;
                } else if (translate.y <= -5) {
                    translate.y = -5;
                }
                Gdx.app.error("model", "当前坐标  translate.x  " + translate.x + "  translate.y  " + translate.y + "  translate.z  " + translate.z);
//                Gdx.app.error("time", "(now.x - old.x)  " + (now.x - old.x) + "   (now.z - old.z)  " + (now.z - old.z) + "  scollX " + scollX + "  scollZ " + scollZ);
                instances.get(modelNumber).transform.translate(scollX, scollY, scollZ);
                this.time += renderTime;
            }
        }
    }

    private void getV() {
        //向量叉积
        if (now == null || old == null)
            return;
        float v = now.x * old.z - old.x * now.z;

        double nowL = Math.abs(Math.sqrt(now.x * now.x + now.z * now.z));
        double oldL = Math.abs(Math.sqrt(old.x * old.x + old.z * old.z));
        double noL = Math.abs(Math.sqrt((now.x - old.x) * (now.x - old.x) + (now.z - old.z) * (now.z - old.z)));
        float degrees = (float) Math.toDegrees(Math.abs(Math.acos((nowL * nowL + oldL * oldL - noL * noL) / (2 * nowL * oldL))));

        Quaternion mHuanQuaternion = new Quaternion();
        instances.get(modelNumber).transform.getRotation(mHuanQuaternion);

        Gdx.app.error("Quaternion", mHuanQuaternion.getAngle() + "");
        if (!Double.isNaN(degrees)) {
            if (v > 0) {
                //顺时针
                instances.get(modelNumber).transform.rotate(0, 1, 0, mHuanQuaternion.getAngle() + degrees + initAngle);
            } else if (v < 0) {
                //逆时针
                instances.get(modelNumber).transform.rotate(0, 1, 0, mHuanQuaternion.getAngle() - degrees + initAngle);
            } else {
                //一条直线
                if ((now.x > 0 && old.x > 0) || (now.x < 0 && old.x < 0)) {
                    //同一象限
                    instances.get(modelNumber).transform.rotate(0, 1, 0, mHuanQuaternion.getAngle() + 0);
                } else {
                    //不同象限
                    instances.get(modelNumber).transform.rotate(0, 1, 0, mHuanQuaternion.getAngle() + 180);
                }
            }
        }
    }

    private void getModelInstanceAngle() {
        //向量叉积
        float nowX = now.x - old.x;
        float nowZ = now.z - old.z;
        float v = nowX * old.z - old.x * nowZ;
        double nowL = Math.abs(Math.sqrt(nowX * nowX + nowZ * nowZ));
        double oldL = Math.abs(Math.sqrt(old.x * old.x + old.z * old.z));
        double noL = Math.abs(Math.sqrt((nowX - old.x) * (nowX - old.x) + (nowZ - old.z) * (nowZ - old.z)));
        float degrees = (float) Math.toDegrees(Math.abs(Math.acos((nowL * nowL + noL * noL - oldL * oldL) / (2 * nowL * noL))));
        Quaternion quaternion = new Quaternion();
        instances.get(modelNumber).transform.getRotation(quaternion);
//        Gdx.app.error("model", "r  = " + degrees + "  v = " + v + "  quaternion.getAngle()   " + quaternion.getAngle());
        if (!Double.isNaN(degrees)) {
            if (v > 0) {
                //顺时针
                instances.get(modelNumber).transform.rotate(0, 1, 0, quaternion.getAngle() - degrees + 180);
            } else if (v < 0) {
                //逆时针
                instances.get(modelNumber).transform.rotate(0, 1, 0, quaternion.getAngle() + degrees + 90);
            } else {
                //一条直线
                if ((now.x > 0 && old.x > 0) || (now.x < 0 && old.x < 0)) {
                    //同一象限
                    instances.get(modelNumber).transform.rotate(0, 1, 0, quaternion.getAngle() + 0);
                } else {
                    //不同象限
                    instances.get(modelNumber).transform.rotate(0, 1, 0, quaternion.getAngle() + 180);
                }
            }
        }
    }

    public void setFind(boolean find) {
        isFind = find;
    }

    public void setTranslate(boolean translate) {
        this.isTranslate = translate;
    }

    public void setStart(boolean start) {
        isStart = start;
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
                setDurations(2);
                translateAnimation();
            }

            switchAnimationModel();


            if (rayDistance != -1) {
                Gdx.app.error("ray", "rayDistance  = " + rayDistance + "  mTextWidthRadiu = " + mTextWidthRadiu);
            }
            /**
             * 添加射线
             */

            Gdx.app.error("TAG", "isPao    " + isPao + "    mTao    " + mTao);
            if (!isPao && isRay && number <= 12 && rayDistance != -1 && !isAnimation()) {
//                Ray ray = camera.getPickRay(width / 2, height / 2);
//                final GameObject instance = object;
//                instance.transform.getTranslation(position);
////                Vector3 unproject = camera.project(position);
////                Gdx.app.error("ray", "unproject  X " + unproject.x + "  unproject   Y " + unproject.y + " unproject  Z " + unproject.z);
//
//                position.add(new Vector3(instance.center.x + 2, instance.center.y, instance.center.z));
//                final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
//                float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
////                double abs = Math.abs(Math.sqrt((unproject.x - width / 2) * (unproject.x - width / 2) + (unproject.y - width / 2) * (unproject.y - width / 2)));
//                Gdx.app.error("ray", "dist2 " + dist2 + "   ray.origin.x   " + ray.origin.x);

                if (rayDistance < (mTextWidthRadiu * 3 / 4) && !isPositive) {
//                    addDetal += Gdx.graphics.getDeltaTime();
//                    if (addDetal > 1f && count == 0) {
//                        mTao = new Random().nextInt(10);
//                        addDetal = 0;
//                    } else
//                        mTao = 10;
//
//                    if (mTao < 5) {
//                        count++;
//                        setTranslate(true);
//                        setModelNumber(PAO);
//
//                        isPao = true;
//                        boolean rd = translate.y <= 1;
//                        if (rd) {
//                            this.now = new Vector3(translate.x, translate.y + 5, translate.z);
//                        } else {
//                            this.now = new Vector3(translate.x, translate.y - 5, translate.z);
//                        }
//                        time = 0;
//                        durations = 1f;
//                        isStart = false;
//                    } else {
                    setTranslate(false);
                    setModelNumber(HUANHU);
//                    }
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
                camera.lookAt(-10, 0, 10);
                camera.update();
//                Vector3 zuoVector3 = camera.unproject(new Vector3(width / 2, height / 2, 0));
//                Gdx.app.error("zuo", "zuoVector3.x  " + zuoVector3.x + "   zuoVector3.y  " + zuoVector3.y + "  zuoVector3.z " + zuoVector3.z);
                instances.get(number).transform.setToTranslation(-10, -0.5f, 10);
//                instances.get(number).transform.rotate(mHuanQuaternion);
            } else {
                instances.get(number).transform.setToTranslation(translate.x, translate.y, translate.z);
            }

            if (number == HUANHU) {
                getV();
            } else {
                getModelAngle();
            }
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
