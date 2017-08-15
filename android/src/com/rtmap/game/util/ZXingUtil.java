package com.rtmap.game.util;

import android.graphics.Bitmap;

import com.badlogic.gdx.Gdx;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rtmap.game.interfaces.LoadCompleteListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by yxy
 * on 2017/8/4.
 */

public class ZXingUtil {
    public static void createQr(final String code, final int width, final int height, final LoadCompleteListener listener) {
        new Thread() {
            @Override
            public void run() {
                Hashtable<EncodeHintType, String> hashtable = new Hashtable<>();
                hashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                try {
                    BitMatrix encode = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, width, height, hashtable);
                    int[] pixels = new int[width * height];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (encode.get(x, y)) {
                                pixels[y * width + x] = 0xff000000;
                            } else {
                                pixels[y * width + x] = 0xffffffff;
                            }
                        }
                    }
                    Bitmap bitmap = Bitmap.createBitmap(width, height,
                            Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
                    String path = saveBitmapToSD(bitmap, code,listener);
                    listener.load(path);
                } catch (WriterException e) {
                    listener.loadFail(e.getMessage());
                }
            }
        }.start();
    }
//将Bitmap图片保存到sd卡

    protected static String saveBitmapToSD(Bitmap bt, String code, LoadCompleteListener listener) {
        String path;
        boolean available = Gdx.files.isExternalStorageAvailable();
        if (available) {
            path = Gdx.files.getExternalStoragePath();
        } else {
            path = Gdx.files.getLocalStoragePath();
        }
        FileOutputStream out = null;

        File file1 = new File(path + Contacts.BITMAP_SD);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file = null;
        try {
            file = new File(file1, MD5Encoder.encode(code + ".png"));
        } catch (Exception e) {
            listener.loadFail(e.getMessage());
        }

        try {
            out = new FileOutputStream(file);
            bt.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (FileNotFoundException e) {
            listener.loadFail(e.getMessage());
        }finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                listener.loadFail(e.getMessage());
            }
        }
        return file.getAbsolutePath();
    }

    public static void createQr(final String code, final int width, final int height, final Bitmap bitmap) {

        new Thread() {
            @Override
            public void run() {
                Hashtable<EncodeHintType, String> hashtable = new Hashtable<>();
                hashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                try {
                    BitMatrix encode = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, width, height, hashtable);
                    int[] pixels = new int[width * height];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (encode.get(x, y)) {
                                pixels[y * width + x] = 0xff000000;
                            } else {
                                pixels[y * width + x] = 0xffffffff;
                            }
                        }
                    }
                    bitmap.setPixels(pixels, 0, 300, 0, 0, 300, 300);
                } catch (WriterException e) {
                }
            }
        }.start();
    }

    private static byte[] intArray2String(int[] pixels) {
        if (pixels == null || pixels.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pixels.length; i++) {
            sb.append(pixels[i]);
        }
        return sb.toString().getBytes();
    }
}
