package com.asiet.final_file_encrypt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Akash Bhaskaran on 3/5/2017.
 */

public class PhotoTakingService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        takePhoto(this);
    }

    @SuppressWarnings("deprecation")
    private static void takePhoto(final Context context) {

        System.out.println("Preparing to take photo");
        Camera camera = null;

        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            SystemClock.sleep(1000);

            Camera.getCameraInfo(camIdx, cameraInfo);

            try {
                camera = Camera.open(camIdx);
            } catch (RuntimeException e) {
                System.out.println("Camera not available: " + camIdx);
                camera = null;
                //e.printStackTrace();
            }
            try {
                if (null == camera) {
                    System.out.println("Could not get camera instance");
                } else {
                    System.out.println("Got the camera, creating the dummy surface texture");
                    //SurfaceTexture dummySurfaceTextureF = new SurfaceTexture(0);
                    try {
                        //camera.setPreviewTexture(dummySurfaceTextureF);
                        camera.setPreviewTexture(new SurfaceTexture(0));
                        camera.startPreview();
                    } catch (Exception e) {
                        System.out.println("Could not set the surface preview texture");
                        e.printStackTrace();
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                            String date = dateFormat.format(new Date());
                            String photoFile = "PictureFront_" + "_" + date + ".jpg";
                            String filename= Environment.getExternalStorageDirectory() + File.separator + photoFile;
                            File mainPicture = new File(filename);
                            //addImageFile(mainPicture);

                            try {
                                FileOutputStream fos = new FileOutputStream(mainPicture);
                                fos.write(data);
                                fos.close();
                                System.out.println("image saved");
                            } catch (Exception error) {
                                System.out.println("Image could not be saved");
                            }
                            camera.release();
                        }
                    });
                }
            } catch (Exception e) {
                camera.release();
            }

        }
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}