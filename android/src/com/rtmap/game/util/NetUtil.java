package com.rtmap.game.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by yxy on 2017/3/9.
 */
public class NetUtil {

    private static volatile NetUtil netUtil = null;
    private HttpRequestBuilder requestBuilder;
    private final FileUtil fileUtil;
    private final MemoryUtil memoryUtil;

    public static NetUtil getInstance() {
        if (netUtil == null) {
            synchronized (NetUtil.class) {
                if (netUtil == null) {
                    netUtil = new NetUtil();
                }
            }
        }
        return netUtil;
    }

    private NetUtil() {
        requestBuilder = new HttpRequestBuilder();
        fileUtil = new FileUtil();
        memoryUtil = new MemoryUtil();
    }

    public boolean checkConnection(Context context) {
        ConnectivityManager CManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        try {
            if (NInfo != null && NInfo.isConnectedOrConnecting()) {
                if (InetAddress.getByName("www.163.com").isReachable(1000)) {
                    Gdx.app.error("http", "reachable()");
                    // host reachable
                    return true;
                } else {
                    Gdx.app.error("http", "not reachable()");
                    return false;
                    // host not reachable
                }
            } else {
                Gdx.app.error("http", "fail()");
                return false;
            }
        } catch (Exception e) {
            Gdx.app.error("http", e.getMessage());
            return false;
        }
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public MemoryUtil getMemoryUtil() {
        return memoryUtil;
    }

    /**
     * 检测网络连接
     *
     * @return
     */
//    public boolean checkConnection() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) AndroidLauncher
//                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo != null) {
//            return networkInfo.isAvailable();
//        }
//        return false;
//    }

    /**
     * Get方式请求
     *
     * @param url
     * @return
     */
    public void getConnection(String url, HttpResponse httpResponse) {
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                if (httpResponse != null) {
                    httpResponse.responseString(convertStreamToString(is));
                }
            } else {
                if (httpResponse != null)
                    httpResponse.responseFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (httpResponse != null)
                httpResponse.responseFail();
        }
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @return
     * @throws Exception
     */
    public synchronized String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * get请求
     * "http://182.92.31.114/rest/act/17888/15210420307"
     *
     * @param url
     * @return
     */
    public void get(String url, Net.HttpResponseListener responseListener) {
        Net.HttpRequest httpRequest = requestBuilder.newRequest().header("Content-Type",
                "application/json;charset=UTF-8").method(Net.HttpMethods.GET).url(url).build();
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }
    public void getText(String url, Net.HttpResponseListener responseListener) {
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }

    public Pixmap getLocalPicture(String url) {
        Pixmap lru = memoryUtil.getLru(url);
        if (lru != null) {
            return lru;
        }
        byte[] file = fileUtil.getFile(url);
        if (file != null) {
            return new Pixmap(file, 0, file.length);
        }
        return null;
    }

    /**
     * 请求图片数据
     * "http://182.92.31.114/rest/act/17888/15210420307"
     *
     * @param url
     * @return
     */
    public void getPicture(String url, Net.HttpResponseListener responseListener) {
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }

    public interface HttpResponse {
        void responseString(String response);

        void responseFail();
    }
}
