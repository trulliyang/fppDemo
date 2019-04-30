package com.facepp.demo;

import android.graphics.Rect;

import com.facepp.demo.util.CameraMatrix;
import com.facepp.demo.util.OpenGLDrawRect;
import com.facepp.demo.util.PointsMatrix;
import com.facepp.demo.util.SwapMatrix;
import com.megvii.facepp.sdk.Facepp;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by mrsimple on 13/3/2019
 */
public class FaceKeyPointRecognizer {

    private final Facepp facepp;
    private int cameraAngle;
    float confidence;
    int rotation = cameraAngle;
    private boolean is106Points = true;
    private boolean is3DPose = false;
    private int mImageMode = Facepp.IMAGEMODE_NV21;

    private PointsMatrix mPointsMatrix;
    private SwapMatrix mSwapMatrix;
    private CameraMatrix mCameraMatrix;

    public FaceKeyPointRecognizer(Facepp facepp,   CameraMatrix
            mCameraMatrix, SwapMatrix mSwapMatrix, PointsMatrix mPointsMatrix) {
        this.facepp = facepp;
        this.mCameraMatrix = mCameraMatrix;
        this.mSwapMatrix = mSwapMatrix;
        this.mPointsMatrix = mPointsMatrix;
    }


    public FaceKeyPointRecognizer setCameraAngle(int cameraAngle) {
        this.cameraAngle = cameraAngle;
        return this;
    }

    public FaceKeyPointRecognizer setImageMode(int mImageMode) {
        this.mImageMode = mImageMode;
        return this;
    }

    /**
     *  检测操作放到主线程，防止贴点延迟
     * @param imgData
     * @param width
     * @param height
     */
    public Facepp.Face[] detectFaces(final byte[] imgData, int width, int height, int orientation) {
        if (orientation == 0) {
            rotation = cameraAngle;
        }
        else if (orientation == 1) {
            rotation = 0;
        }
        else if (orientation == 2) {
            rotation = 180;
        }
        else if (orientation == 3) {
            rotation = 360 - cameraAngle;
        }

        float pitch = 0, yaw = 0, roll = 0;

        setConfig(rotation);

        final Facepp.Face[] faces = facepp.detect(imgData, width, height, mImageMode);
        if (faces != null) {
            ArrayList<ArrayList> pointsOpengl = new ArrayList<ArrayList>();
            ArrayList<FloatBuffer> rectsOpengl = new ArrayList<FloatBuffer>();
            if (faces.length > 0) {

                for (int c = 0; c < faces.length; c++) {

                    if (is106Points)
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK106);
                    else
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK81);

                    if (is3DPose) {
                        facepp.get3DPose(faces[c]);
                    }

                    pitch = faces[c].pitch;
                    yaw = faces[c].yaw;
                    roll = faces[c].roll;
                    confidence = faces[c].confidence;
                    //                    if (0 == c) mSwapMatrix.setTrackerData(faces[c].points);
                    //0.4.7之前（包括）jni把所有角度的点算到竖直的坐标，所以外面画点需要再调整回来，才能与其他角度适配
                    //目前getLandmarkOrigin会获得原始的坐标，所以只需要横屏适配好其他的角度就不用适配了，因为texture和preview的角度关系是固定的
                    ArrayList<FloatBuffer> tricameraAngleVBList = new ArrayList<FloatBuffer>();
                    for (int i = 0; i < faces[c].points.length; i++) {
                        float x = (faces[c].points[i].x / width) * 2 - 1;
                        float y = (faces[c].points[i].y / height) * 2 - 1;
                        float[] pointf = new float[]{y, x, 0.0f};
                        FloatBuffer fb = mCameraMatrix.floatBufferUtil(pointf);
                        tricameraAngleVBList.add(fb);
                    }

                    pointsOpengl.add(tricameraAngleVBList);

                    if (mPointsMatrix.isShowFaceRect) {
                        facepp.getRect(faces[c]);
                        FloatBuffer buffer = calRectPostion(faces[c].rect, width, height);
                        rectsOpengl.add(buffer);
                    }

                }
            } else {
                pitch = 0.0f;
                yaw = 0.0f;
                roll = 0.0f;
            }

            synchronized (mSwapMatrix) {
                if (faces.length > 0)
                    mSwapMatrix.setTrackerData(faces[0].points);
            }

            synchronized (mPointsMatrix) {
                if (faces.length > 0 && is3DPose) {
                    mPointsMatrix.bottomVertexBuffer = OpenGLDrawRect.drawBottomShowRect(0.15f, 0, -0.7f, pitch, -yaw, roll, rotation);

                } else {
                    mPointsMatrix.bottomVertexBuffer = null;
                }
                mPointsMatrix.points = pointsOpengl;
                mPointsMatrix.faceRects = rectsOpengl;
            }
        }
        return faces;
    }


    private void setConfig(int rotation) {
        Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
        if (faceppConfig.rotation != rotation) {
            faceppConfig.rotation = rotation;
            facepp.setFaceppConfig(faceppConfig);
        }
    }


    private FloatBuffer calRectPostion(Rect rect, float width, float height) {
        float top = 1 - (rect.top * 1.0f / height) * 2;
        float left = (rect.left * 1.0f / width) * 2 - 1;
        float right = (rect.right * 1.0f / width) * 2 - 1;
        float bottom = 1 - (rect.bottom * 1.0f / height) * 2;

        // 左上角
        float x1 = -top;
        float y1 = left;

        // 右下角
        float x2 = -bottom;
        float y2 = right;

        float[] tempFace = {
                x1, y2, 0.0f,
                x1, y1, 0.0f,
                x2, y1, 0.0f,
                x2, y2, 0.0f,
        };

        FloatBuffer buffer = mCameraMatrix.floatBufferUtil(tempFace);
        return buffer;
    }
}
