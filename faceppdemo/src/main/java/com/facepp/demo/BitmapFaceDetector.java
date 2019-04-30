package com.facepp.demo;

import android.content.Context;
import android.util.Log;

import com.facepp.demo.util.ConUtil;
import com.megvii.facepp.sdk.Facepp;

/**
 * Created by mrsimple on 13/3/2019
 */
public class BitmapFaceDetector {
    private Facepp facepp ;
    private int videoWidth ;
    private int videoHeight ;

    public BitmapFaceDetector(Context appContext, int videoWidth, int videoHeight) {
        facepp = new Facepp() ;
        String errorCode = facepp.init(appContext, ConUtil.getFileContent(appContext, R.raw.megviifacepp_0_5_2_model), 1);
        //sdk内部其他api已经处理好，可以不判断
        if (errorCode != null) {
            Log.e("", "### initFacepp error : " + errorCode) ;
            return;
        }
        setSizeChange(videoWidth, videoHeight);
    }

    public void setSizeChange(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth ;
        this.videoHeight = videoHeight ;

        Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
        faceppConfig.interval = 25;
        faceppConfig.minFaceSize = 200;
        faceppConfig.roi_left = 0;
        faceppConfig.roi_top = 0;
        faceppConfig.roi_right = videoWidth;
        faceppConfig.roi_bottom = videoHeight;
        faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_FAST;

        facepp.setFaceppConfig(faceppConfig);
    }

    /**
     * 根据 Bitmap 检测人脸关键点
     * @param bytes
     * @return
     */
    public Facepp.Face[] detectFacesWithGrayMode(byte[] bytes) {
        if ( bytes == null || videoWidth <= 0 || videoHeight <= 0 || facepp == null ) {
            return new Facepp.Face[0] ;
        }
        // raw bitmap byte convert to gray bytes
        byte[] grayBytes = new byte[videoWidth * videoHeight];
        for (int i = 0; i < videoHeight; i++) {
            for (int j = 0; j < videoWidth; j++) {
                int idx = i * videoWidth + j;
                grayBytes[idx] = bytes[4 * idx + 2];
            }
        }
        return detectFaces(grayBytes, Facepp.IMAGEMODE_GRAY) ;
    }


    /**
     * 根据 Bitmap 检测人脸关键点
     * @param bytes
     * @return
     */
    public Facepp.Face[] detectFaces(byte[] bytes, int imageMode) {
        if ( bytes == null || videoWidth <= 0 || videoHeight <= 0 || facepp == null ) {
            return new Facepp.Face[0] ;
        }
//        Log.e("", "### detect faces ,videoWidth : " + videoWidth  + ", height : " + videoHeight + ", mode : " + imageMode) ;
        long time0 = System.currentTimeMillis();
        final Facepp.Face[] faces = facepp.detect(bytes, videoWidth, videoHeight, imageMode);
        long time1 = System.currentTimeMillis();
        long dt = time1 - time0;
        Log.e("shiyang", "shiyang detect cost = " + dt + "ms") ;

        if (null != faces) {
            if (faces.length > 0) {
//                Log.e("", "### ------> detectFaces  : yaw = " + faces[0].yaw + ", rect = " + faces[0].rect) ;

                for (int faceIndex = 0; faceIndex < faces.length; faceIndex++) {
                    facepp.getLandmarkRaw(faces[faceIndex], Facepp.FPP_GET_LANDMARK106);
//                    for (int i = 0; i < faces[faceIndex].points.length; i++) {
//                        float x = (faces[faceIndex].points[i].x / videoWidth) * 2 - 1;
//                        float y = (faces[faceIndex].points[i].y / videoHeight) * 2 - 1;
//                        Log.e("shiyang", "shiyang po[" + i + "](x,y)=(" + faces[faceIndex].points[i].x + "," + faces[faceIndex].points[i].y + ")");
//                        Log.e("shiyang", "shiyang pm[" + i + "](x,y)=(" + x + "," + y + ")");
//                    }
                }
            } else {
                Log.e("shiyang", "### shiyang face length == 0");
            }
        } else {
            Log.e("shiyang", "### shiyang face == null");
        }
        return faces;
    }

    public void release() {
        if ( facepp != null ) {
            facepp.release();
        }
    }
}
