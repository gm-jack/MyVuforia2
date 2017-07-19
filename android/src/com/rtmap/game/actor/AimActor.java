package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.AnimationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimActor extends Actor {
    /**
     * 状态
     */
    public static final int STATE_SUCCESS = 0;
    public static final int STATE_NORMAL = 1;
    public static final int STATE_FAIL = 2;
    public static int STATE = STATE_SUCCESS;
    private int changeX;
    private int changeY;

    private List<TextureRegion> texReArray = new ArrayList();
    private List<TextureRegion> findReArray = new ArrayList();
    private TextureRegion[] mKeyFrames = new TextureRegion[3];

    //绘制次数
    public static int number = 0;
    private int maxNumber = 12;
    private int width;
    private int height;
    private AssetManager assetManager;
    private int startAngle = 390;
    private float degree = startAngle;
    private float delta = 0;
    private int angle = 30;
    private AimListener aimListener;
    //是否发现模型，更改状态
    private boolean isFind = false;
    //模型进入视角内提示
    private boolean isTip = false;
    //初始控制
    private boolean isOne = true;
    //判断是否为失败返回场景
    private boolean isFail = false;
    private int num = 60;
    //蓝圈半径变化值
    private float changeRadiu = 0;
    private float findRadiu = 0;
    private float centerWidth;
    //是否开启蓝圈变化
    private boolean isStartAnimation = true;

    private AnimationListener animationListener;
    private float scale;
    private boolean isFirst = true;
    private float topHeight;
    private float leftWidth;
    private float leftHeight;
    private float rightWidth;
    private float rightHeight;
    private float bottomWidth;
    private float bottomHeight;
    //开场动画
    private boolean isAnimation = true;
    private int nums = 30;
    private float topAnim = 0;
    private float leftAnim = 0;
    private float rightAnim = 0;
    private float bottomAnim = 0;
    private float findAnim = 0;
    private boolean isBig = true;
    private int findNum = 10;
    private float aimWidth;
    private float progreeWidth;
    private float tipWidth;
    private float tipHeight;
    private float textWidth;
    private float textHeight;
    private boolean isCanPlay = false;
    private boolean isAnimationFirst = true;

    public AimActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        changeX = width / 2;
        changeY = height / 2;
    }

    public AimActor(AssetManager asset, boolean isAnimation) {
        this.assetManager = asset;
        this.isAnimation = isAnimation;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        changeX = width / 2;
        changeY = height / 2;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setAimListener(AimListener aimListener) {
        this.aimListener = aimListener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // 如果 region 为 null 或者 演员不可见, 则直接不绘制
        if (!isVisible()) {
            return;
        }
        if (assetManager.update() && isFirst) {
            initResources();
            isFirst = false;
        }
        if (texReArray.size() <= 0 || findReArray.size() <= 0 || mKeyFrames.length <= 0) {
            return;
        }

        if (isAnimation) {
            batch.draw(texReArray.get(2), 0, height - topAnim, width, topHeight);
            if (topAnim < topHeight) {
                topAnim += topHeight / nums;
            } else {
                isAnimation = false;
                setIsStartAnimation(true);
//                SPUtil.put(Contacts.ANIM_IS_ANIMATION, isAnimation);
            }
            batch.draw(texReArray.get(3), leftAnim - leftWidth, height / 2 - leftHeight / 2, leftWidth, leftHeight);
            if (leftAnim < leftWidth) {
                leftAnim += leftWidth / nums;
            }
            batch.draw(texReArray.get(4), width - rightAnim, height / 2 - rightHeight / 2, rightWidth, rightHeight);
            if (rightAnim < rightWidth) {
                rightAnim += rightWidth / nums;
            }
            batch.draw(texReArray.get(5), width / 2 - bottomWidth / 2, bottomAnim - bottomHeight, bottomWidth, bottomHeight);
            if (bottomAnim < bottomHeight) {
                bottomAnim += bottomHeight / nums;
            }
            batch.draw(findReArray.get(0), changeX - findRadiu, changeY - findRadiu, findRadiu * 2, findRadiu * 2);
            if (findRadiu <= centerWidth && isBig) {
                if (findRadiu >= centerWidth * 7 / 8) {
                    isBig = false;
                }
                findRadiu += centerWidth / 2 / findNum;
            } else if (findRadiu > centerWidth / 2) {
                findRadiu -= centerWidth / 2 / findNum;
            } else {
                batch.draw(findReArray.get(0), width / 2 - centerWidth / 2, height / 2 - centerWidth / 2, centerWidth, centerWidth);
            }
        } else {
            if (animationListener != null && isAnimationFirst) {
                isAnimationFirst = false;
//                animationListener.startAnim(isAnimationFirst);
            }
            batch.draw(texReArray.get(2), 0, height - topHeight, width, topHeight);
            batch.draw(texReArray.get(3), 0, height / 2 - leftHeight / 2, leftWidth, leftHeight);
            batch.draw(texReArray.get(4), width - rightWidth, height / 2 - rightHeight / 2, rightWidth, rightHeight);
            batch.draw(texReArray.get(5), width / 2 - bottomWidth / 2, 0, bottomWidth, bottomHeight);

            if (isFind) {
                float aimWidths = width / 2 - aimWidth / 2;
                float aimHeights = height / 2 - aimWidth / 2;
                if (STATE == STATE_SUCCESS) {
                    if (number >= maxNumber) {
                        if (aimListener != null) {
                            aimListener.aimSuccess();
                        }
                    }
                    batch.draw(texReArray.get(0), aimWidths, aimHeights, aimWidth, aimWidth);
                    for (int i = 0; i < number; i++) {
                        //                    if (delta < 0.5f && (number - 1) == i) {
                        //                        batch.draw(mKeyFrames[2], aimWidth, aimHeight, mKeyFrames[2].getRegionWidth() / 2, mKeyFrames[2].getRegionHeight() / 2, mKeyFrames[2].getRegionWidth(), mKeyFrames[2].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                        //                    } else {
                        batch.draw(mKeyFrames[0], aimWidths, aimHeights, progreeWidth / 2, progreeWidth / 2, progreeWidth, progreeWidth, getScaleX(), getScaleY(), degree - angle * i);
                        //                    }
                    }
                    if (delta >= 0.5f) {
                        delta = 0;
                    }
                } else if (STATE == STATE_FAIL) {
                    batch.draw(texReArray.get(1), aimWidths, aimHeights, aimWidth, aimWidth);
                    for (int i = 0; i < number; i++) {
                        batch.draw(mKeyFrames[1], aimWidths, aimHeights, progreeWidth / 2, progreeWidth / 2, progreeWidth, progreeWidth, getScaleX(), getScaleY(), degree - angle * i);
                    }
                    if (delta >= 0.5f) {
                        delta = 0;
                    }
                }
            } else {
                batch.draw(findReArray.get(0), width / 2 - centerWidth / 2, height / 2 - centerWidth / 2, centerWidth, centerWidth);

                if (isStartAnimation) {
                    batch.draw(findReArray.get(4), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
                    if (changeRadiu <= (centerWidth * 0.373f)) {
                        changeRadiu += centerWidth * 0.373f / num;
                        Gdx.app.error("animations", (centerWidth * 0.373f / num) + "   isStartAnimation   " + changeRadiu + "    " + (centerWidth * 0.373f));
                    } else {
                        if (animationListener != null) {
                            animationListener.endAnim();
                        }
                        changeRadiu = 0;
                    }
                }
                if (isTip) {
                    //绘制遮罩
                    batch.draw(findReArray.get(3), 0, 0, width, height);
                    batch.draw(findReArray.get(1), width / 2 - tipWidth / 2, height / 2 - tipHeight / 2, tipWidth, tipHeight);
                    batch.draw(findReArray.get(2), width / 2 - textWidth / 2, height / 2 - tipHeight / 2 + tipHeight / 5, textWidth, textHeight);
                }
            }
        }
    }


    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public void setIsStartAnimation(boolean isStartAnimation) {
        this.isStartAnimation = isStartAnimation;
//        changeRadiu = 0;
    }

    public void setIsFind(boolean isFind) {
        this.isFind = isFind;
    }

    public void setIsTip(boolean isTip) {
        this.isTip = isTip;
    }

    public void setIsFail(boolean isFail) {
        this.isFail = isFail;

//        if (!isFail) {
//            setIsFind(!isFail);
//            number = maxNumber - 1;
//            STATE = STATE_FAIL;
//        } else {
        number = 0;
        STATE = STATE_NORMAL;
//        }
    }

    public void addNumber() {
        STATE = STATE_SUCCESS;
        setIsFind(true);
        if (isOne) {
            delta = 0;
            isOne = false;
        }
        delta += Gdx.graphics.getDeltaTime();
        if (number <= maxNumber && delta > 0.5f)
            number++;
    }

    public void subNumber() {
        STATE = STATE_FAIL;
        if (number == 0) {
            if (aimListener != null) {
                aimListener.aimFail();
            }
            return;
        }

        delta += Gdx.graphics.getDeltaTime();
        if (number > 0 && delta > 0.5f)
            number--;
    }

    public void initResources() {
        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_success.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_fail.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("anim_bg_top.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("anim_bg_left.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("anim_bg_right.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("anim_bg_bottom.png")));

        scale = (float) width / texReArray.get(2).getRegionWidth();
        aimWidth = texReArray.get(0).getRegionHeight() * scale;
        topHeight = texReArray.get(2).getRegionHeight() * scale;
        leftWidth = texReArray.get(3).getRegionWidth() * scale;
        leftHeight = texReArray.get(3).getRegionHeight() * scale;
        rightWidth = texReArray.get(4).getRegionWidth() * scale;
        rightHeight = texReArray.get(4).getRegionHeight() * scale;
        bottomWidth = texReArray.get(5).getRegionWidth() * scale;
        bottomHeight = texReArray.get(5).getRegionHeight() * scale;


        findReArray = new ArrayList<>();
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_center.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_tip.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_text.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("cover.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_circle.png")));

        centerWidth = findReArray.get(0).getRegionWidth() * scale;
        tipWidth = findReArray.get(1).getRegionWidth() * scale;
        tipHeight = findReArray.get(1).getRegionHeight() * scale;
        textWidth = findReArray.get(2).getRegionWidth() * scale;
        textHeight = findReArray.get(2).getRegionHeight() * scale;

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("aim_blue.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("aim_red.png"));

        progreeWidth = mKeyFrames[0].getRegionWidth() * scale;
    }

    @Override
    public void clear() {
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
        for (int i = 0; i < findReArray.size(); i++) {
            findReArray.get(i).getTexture().dispose();
        }
        for (int i = 0; i < mKeyFrames.length; i++) {
            mKeyFrames[i].getTexture().dispose();
        }
    }
}
