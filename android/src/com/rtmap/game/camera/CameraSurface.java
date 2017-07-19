/*
 * Copyright 2012 Johnny Lish (johnnyoneeyed@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.rtmap.game.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera camera;
    private int size;
    private byte[] bytes;
    private int i = 0;

    public CameraSurface(Context context) {
        super(context);
        // We're implementing the Callback interface and want to get notified
        // about certain surface events.
        getHolder().addCallback(this);
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the camera, in this case.
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Once the surface is created, simply open a handle to the camera hardware.
        camera = Camera.open();
        camera.setDisplayOrientation(90);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // This method is called when the surface changes, e.g. when it's size is set.
        // We use the opportunity to initialize the camera preview display dimensions.
        Camera.Parameters p = camera.getParameters();
//        p.setPreviewSize( width, height );
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        camera.setParameters(p);
// We also assign the preview display to this surface...
        size = p.getPreviewSize().height * p.getPreviewSize().width * ImageFormat.getBitsPerPixel(p.getPreviewFormat()) / 8;
        bytes = new byte[size];
        Gdx.app.error("camera", "" + bytes.length);
        camera.addCallbackBuffer(bytes);
        camera.setPreviewCallbackWithBuffer(this);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Once the surface gets destroyed, we stop the preview mode and release
        // the whole camera since we no lon
        // ger need it.
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera cam) {
//        Gdx.app.error("camera", "onPreviewFrame   " + Gdx.files.getExternalStoragePath());
//        if (bytes == null)

        byte[] bytess = new byte[size];
//        for (int i = 0; i < data.length * 2 / 3; i++) {
//            if (i < data.length * 2 / 3)
//                bytess[i] = data[i];
//            else
//                bytess[i] = 0;
//        }
        System.arraycopy(data, 0, bytess, 0, data.length * 2 / 3);
//        i++;
//        fileUtil.setFile("Libgdx" + i, bytes);
        camera.addCallbackBuffer(bytess);
//        this.invalidate();
    }

     static interface DataCallBack{
         void cameraData(byte[] data);
     }
}