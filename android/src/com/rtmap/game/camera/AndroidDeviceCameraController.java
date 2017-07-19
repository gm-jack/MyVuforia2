package com.rtmap.game.camera;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.interfaces.DeviceCameraControl;
import com.rtmap.game.util.CameraHelper;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

//import jp.co.cyberagent.android.gpuimage.util.AnimEndListener;


public class AndroidDeviceCameraController implements DeviceCameraControl,
        Camera.PictureCallback, Camera.AutoFocusCallback {

    private final CameraHelper mCameraHelper;
    private final CameraLoader mCamera;
    private AndroidLauncher androidLauncher;
    private GPUImage mGPUImage;
    private GLSurfaceView mGlSurfaceView;
    private FrameLayout mLayout;
    //    private CameraSurface mCameraSurface;
//    public static AndroidDeviceCameraController getInstance() {
//        return AndroidDeviceCameraControllerHolder.holder;
//    }
//
//    private static class AndroidDeviceCameraControllerHolder {
//        private static AndroidDeviceCameraController holder = new AndroidDeviceCameraController();
//    }

    public AndroidDeviceCameraController(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

        mCameraHelper = new CameraHelper(androidLauncher);
        mCamera = new CameraLoader();
    }

    public void setFilter() {
        if (mGPUImage != null)
            mGPUImage.setFilter(new GPUImageGrayscaleFilter());
    }

    @Override
    public synchronized void prepareCamera() {
        mLayout = new FrameLayout(androidLauncher);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayout.setLayoutParams(params);

//        mCameraSurface = new CameraSurface(androidLauncher);
        mGlSurfaceView = new GLSurfaceView(androidLauncher);

        mLayout.addView(mGlSurfaceView);
//        mLayout.addView(mCameraSurface);
        mGPUImage = new GPUImage(androidLauncher);
        mGPUImage.setGLSurfaceView(mGlSurfaceView);
        androidLauncher.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public synchronized void startPreview() {
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
//        if (mCameraSurface != null) {
//            mCameraSurface.getCamera().startPreview();
//        }

        if (mCamera != null)
            mCamera.onResume();
    }

    @Override
    public synchronized void stopPreview() {
        if (mGlSurfaceView != null) {
            ViewParent parentView = mGlSurfaceView.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(mGlSurfaceView);
//                viewGroup.removeView(mCameraSurface);
            }
//            if (mCameraSurface.getCamera() != null) {
//                mCameraSurface.getCamera().stopPreview();
//            }
            mCamera.onPause();
            androidLauncher.restoreFixedSize();
        }
    }

    @Override
    public synchronized void onAutoFocus(boolean success, Camera camera) {
        // Focus process finished, we now have focus (or not)
        if (success) {
            if (camera != null) {
                camera.stopPreview();
                // We now have focus take the actual picture
                camera.takePicture(null, null, null, this);
            }
        }
    }

    @Override
    public void prepareCameraAsync(final boolean show) {
        Runnable r = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public synchronized void startPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                startPreview();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public synchronized void stopPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public boolean isReady() {
        if (mGlSurfaceView != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
    }

//    public void setCat(final boolean b, final AnimEndListener listener) {
//        androidLauncher.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                mGlSurfaceView.setVisibility(View.VISIBLE);
//                Gdx.app.error("camera", "visible");
//            }
//        });
//        if (mGPUImage != null)
//            mGPUImage.setCat(b, listener);
//    }

    @Override
    public synchronized void stoPreviewAsync() {
        if (mCamera != null) {
            mCamera.releaseCamera();
//            mCameraSurface.getCamera().setPreviewCallback(null);
//            mCameraSurface.getCamera().stopPreview();
        }
    }

    private class CameraLoader {

        private int mCurrentCameraId = 0;
        private Camera mCameraInstance;

        public void onResume() {
            setUpCamera(mCurrentCameraId);
        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
            setUpCamera(mCurrentCameraId);
        }

        private void setUpCamera(final int id) {
//            mCameraInstance = getCameraInstance(mCurrentCameraId);
//            int orientation = mCameraHelper.getCameraDisplayOrientation(
//                    androidLauncher, mCurrentCameraId);
//            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
//            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
//            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
//            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
            mCameraInstance = getCameraInstance(id);
            Camera.Parameters parameters = mCameraInstance.getParameters();
            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCameraInstance.setParameters(parameters);

            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    androidLauncher, mCurrentCameraId);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        }

        /**
         * A safe way to get an instance of the Camera object.
         *
         * @param currentCameraId
         */
        private Camera getCameraInstance(int currentCameraId) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(currentCameraId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return c;
        }

        private void stopCamera() {
            mCameraInstance.stopPreview();
        }

        private void releaseCamera() {
            if (mCameraInstance != null) {
                mCameraInstance.setPreviewCallback(null);
                mCameraInstance.release();
                mCameraInstance = null;
            }
        }
    }
}