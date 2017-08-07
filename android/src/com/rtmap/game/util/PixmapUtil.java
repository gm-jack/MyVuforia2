package com.rtmap.game.util;

import com.badlogic.gdx.graphics.Pixmap;

/**
 * Created by yxy on 2017/3/14.
 */
public class PixmapUtil {
    public static Pixmap createRoundedPixmap(Pixmap pixmap, int radius, int width, int height) {
        Pixmap mask = getPixmapRoundedRectangle(pixmap.getWidth(), pixmap.getHeight(), radius, android.graphics.Color.WHITE);//掩码
        Pixmap result = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);//保存结果
        pixmapMask(width, height, pixmap, mask, result, false);
        return result;
    }

    //创建round rectangle
    public static Pixmap getPixmapRoundedRectangle(int width, int height, int radius, int color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // 横着的矩形
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - 2 * radius);
        // 垂直矩形
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2 * radius, pixmap.getHeight());
        // 左上 circle
        pixmap.fillCircle(radius, radius, radius);
        // 左下 circle
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
        // 右上 circle
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);
        // 右下 circle
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        return pixmap;
    }

    //创建round
    public static Pixmap getPixmapRounded(int width, int height, int radius, int color) {

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // circle
        pixmap.fillCircle(radius, radius, radius);
        return pixmap;
    }

    public static void pixmapMask(int pixmapWidth, int pixmapHeight, Pixmap pixmap, Pixmap mask, Pixmap result, boolean invertMaskAlpha) {
        com.badlogic.gdx.graphics.Color pixelColor = new com.badlogic.gdx.graphics.Color();
        com.badlogic.gdx.graphics.Color maskPixelColor = new com.badlogic.gdx.graphics.Color();

        Pixmap.Blending blending = result.getBlending();
        result.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < pixmapWidth; x++) {
            for (int y = 0; y < pixmapHeight; y++) {
                com.badlogic.gdx.graphics.Color.rgba8888ToColor(pixelColor, pixmap.getPixel(x, y));                           // 获取原图像素颜色
                com.badlogic.gdx.graphics.Color.rgba8888ToColor(maskPixelColor, mask.getPixel(x, y));                         // 获取掩码像素颜色

                maskPixelColor.a = (invertMaskAlpha) ? 1.0f - maskPixelColor.a : maskPixelColor.a;    // 如果转换掩码
                pixelColor.a = pixelColor.a * maskPixelColor.a;                                     // 颜色吸相乘
                result.setColor(pixelColor);
                result.drawPixel(x, y);
            }
        }
        result.setBlending(blending);
    }
}
