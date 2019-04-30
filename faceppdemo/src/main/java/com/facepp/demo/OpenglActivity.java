package com.facepp.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.demo.bean.FaceActionInfo;
import com.facepp.demo.bean.FeatureInfo;
import com.facepp.demo.facecompare.FaceCompareManager;
import com.facepp.demo.mediacodec.MediaHelper;
import com.facepp.demo.util.CameraMatrix;
import com.facepp.demo.util.ConUtil;
import com.facepp.demo.util.DialogUtil;
import com.facepp.demo.util.ICamera;
import com.facepp.demo.util.MediaRecorderUtil;
import com.facepp.demo.util.OpenGLDrawRect;
import com.facepp.demo.util.OpenGLUtil;
import com.facepp.demo.util.PointsMatrix;
import com.facepp.demo.util.Screen;
import com.facepp.demo.util.SensorEventUtil;
import com.facepp.demo.util.SwapMatrix;
import com.facepp.demo.util.Uv21Helper;
import com.megvii.facepp.sdk.Facepp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenglActivity extends Activity implements PreviewCallback, Renderer, SurfaceTexture
        .OnFrameAvailableListener {

    private boolean isStartRecorder, is3DPose, isDebug, isROIDetect, is106Points, isBackCamera, isFaceProperty,
            isOneFaceTrackig, isFaceCompare, isShowFaceRect;
    private String trackModel;
    private int printTime = 31;
    private GLSurfaceView mGlSurfaceView;
    private ICamera mICamera;
    private Camera mCamera;
    private DialogUtil mDialogUtil;
    private TextView debugInfoText, debugPrinttext, AttriButetext;
    private TextView featureTargetText;
    private ImageButton btnAddFeature;
    private HandlerThread mHandlerThread = new HandlerThread("facepp");
    private Handler mHandler;
    private Facepp facepp;
    private MediaRecorderUtil mediaRecorderUtil;
    private int min_face_size = 200;
    private int detection_interval = 25;
    private HashMap<String, Integer> resolutionMap;
    private SensorEventUtil sensorUtil;
    private float roi_ratio = 0.8f;
    private byte[] carmeraImgData;


    private FaceActionInfo faceActionInfo;
    private boolean needInit;
    private MediaHelper mMediaHelper;
    private byte[] newestFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screen.initialize(this);
        setContentView(R.layout.activity_opengl);
        needInit = true;
        init();
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                startRecorder();
        //            }
        //        }, 2000);

        FaceCompareManager.instance().loadFeature(this);
        ConUtil.toggleHideyBar(this);
    }

    private void init() {
        if (android.os.Build.MODEL.equals("PLK-AL10"))
            printTime = 50;

        faceActionInfo = (FaceActionInfo) getIntent().getSerializableExtra("FaceAction");

        isStartRecorder = faceActionInfo.isStartRecorder;
        is3DPose = faceActionInfo.is3DPose;
        isDebug = faceActionInfo.isdebug;
        isROIDetect = faceActionInfo.isROIDetect;
        is106Points = faceActionInfo.is106Points;
        isBackCamera = faceActionInfo.isBackCamera;
        isFaceProperty = faceActionInfo.isFaceProperty;
        isOneFaceTrackig = faceActionInfo.isOneFaceTrackig;
        isFaceCompare = faceActionInfo.isFaceCompare;
        trackModel = faceActionInfo.trackModel;

        min_face_size = faceActionInfo.faceSize;
        detection_interval = faceActionInfo.interval;
        resolutionMap = faceActionInfo.resolutionMap;

        facepp = new Facepp();

        sensorUtil = new SensorEventUtil(this);

        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        mGlSurfaceView.setEGLContextClientVersion(2);// 创建一个OpenGL ES 2.0
        // context
        mGlSurfaceView.setRenderer(this);// 设置渲染器进入gl
        // RENDERMODE_CONTINUOUSLY不停渲染
        // RENDERMODE_WHEN_DIRTY懒惰渲染，需要手动调用 glSurfaceView.requestRender() 才会进行更新
        mGlSurfaceView.setRenderMode(mGlSurfaceView.RENDERMODE_WHEN_DIRTY);// 设置渲染器模式
        mGlSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });

        mICamera = new ICamera();
        mDialogUtil = new DialogUtil(this);
        debugInfoText = (TextView) findViewById(R.id.opengl_layout_debugInfotext);
        AttriButetext = (TextView) findViewById(R.id.opengl_layout_AttriButetext);
        debugPrinttext = (TextView) findViewById(R.id.opengl_layout_debugPrinttext);
        if (isDebug)
            debugInfoText.setVisibility(View.VISIBLE);
        else
            debugInfoText.setVisibility(View.INVISIBLE);

        btnAddFeature = (ImageButton) findViewById(R.id.opengl_layout_addFaceInfo);
        btnAddFeature.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 保存feature数据
                if (mICamera == null || mICamera.mCamera == null) {
                    return;
                }
                if (compareFaces == null || compareFaces.length <= 0 || carmeraImgData == null) {
                    Toast.makeText(OpenglActivity.this, "当前未检测到人脸", Toast.LENGTH_SHORT).show();
                    return;
                }

                //                Log.e("xie","xie rect"+compareFaces[0].rect.top+"bottom"+compareFaces[0].rect
                // .bottom+newestFeature);

                FaceCompareManager.instance().startActivity(OpenglActivity.this, compareFaces, mICamera,
                        carmeraImgData, isBackCamera, faceActionInfo);
            }
        });

        featureTargetText = (TextView) findViewById(R.id.opengl_layout_targetFaceName);
        if (isFaceCompare) {
            btnAddFeature.setVisibility(View.VISIBLE);
        } else {
            btnAddFeature.setVisibility(View.GONE);
        }

        //        doMyFaceDetection();
    }

    private void doMyFaceDetection() throws IOException {
        if (!needInit) {
            return;
        }
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.face002);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap.Config cfg = bitmap.getConfig();
        int[] ia = new int[w * h];
        bitmap.getPixels(ia, 0, w, 0, 0, w, h);

        ByteBuffer pixels = ByteBuffer.allocate(w * h * 4);
        //        pixels.order(ByteOrder.nativeOrder());
        pixels.asIntBuffer().put(ia);

        byte[] bytes = pixels.array();
        byte[] bytes2 = new byte[w * h];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int idx = i * w + j;
                bytes2[idx] = bytes[4 * idx + 2];
            }
        }
        //        int w = 1280;
        //        int h = 720;
        //        byte[] byteArray = new byte[w*h*4];
        //        is.read(byteArray);

        setConfig(0);
        final Facepp.Face[] faces = facepp.detect(bytes2, w, h, Facepp.IMAGEMODE_GRAY);
        if (null != faces) {
            if (faces.length > 0) {
                for (int c = 0; c < faces.length; c++) {

                    if (is106Points)
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK106);
                    else
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK81);

                    final Facepp.Face face = faces[c];
                    if (0 == c)
                        mSwapMatrix.setTrackerData(faces[c].points);
                    for (int i = 0; i < faces[c].points.length; i++) {
                        float x = (faces[c].points[i].x / w) * 2 - 1;
                        float y = (faces[c].points[i].y / h) * 2 - 1;
//                        Log.e("shiyang", "shiyang po[" + i + "](x,y)=(" + faces[c].points[i].x + "," + faces[c].points[i].y + ")");
//                        Log.e("shiyang", "shiyang pm[" + i + "](x,y)=(" + x + "," + y + ")");
                    }
                }
            } else {
                Log.e("shiyang", "shiyang face length == 0");
            }
        } else {
            Log.e("shiyang", "shiyang face == null");
        }

        needInit = false;
    }


    /**
     * 开始录制
     */
    private void startRecorder() {
        if (isStartRecorder) {
            int Angle = 360 - mICamera.Angle;
            if (isBackCamera)
                Angle = mICamera.Angle;
            mediaRecorderUtil = new MediaRecorderUtil(this, mCamera, mICamera.cameraWidth, mICamera.cameraHeight);
            isStartRecorder = mediaRecorderUtil.prepareVideoRecorder(Angle);
            if (isStartRecorder) {
                boolean isRecordSucess = mediaRecorderUtil.start();
                if (isRecordSucess)
                    mICamera.actionDetect(this);
                else
                    mDialogUtil.showDialog(getResources().getString(R.string.no_record));
            }
        }
    }

    private void autoFocus() {
        if (mCamera != null && isBackCamera) {
            mCamera.cancelAutoFocus();
            Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
        }
    }

    private int Angle;

    @Override
    protected void onResume() {
        super.onResume();
        ConUtil.acquireWakeLock(this);
        startTime = System.currentTimeMillis();
        mCamera = mICamera.openCamera(isBackCamera, this, resolutionMap);
        if (mCamera != null) {
            Angle = 360 - mICamera.Angle;
            if (isBackCamera)
                Angle = mICamera.Angle;

            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            mGlSurfaceView.setLayoutParams(layout_params);

            int width = mICamera.cameraWidth;
            int height = mICamera.cameraHeight;

            int left = 0;
            int top = 0;
            int right = width;
            int bottom = height;
            if (isROIDetect) {
                float line = height * roi_ratio;
                left = (int) ((width - line) / 2.0f);
                top = (int) ((height - line) / 2.0f);
                right = width - left;
                bottom = height - top;
            }

            String errorCode = facepp.init(this, ConUtil.getFileContent(this, R.raw.megviifacepp_0_5_2_model),
                    isOneFaceTrackig ? 1 : 0);

            //sdk内部其他api已经处理好，可以不判断
            if (errorCode != null) {
                Intent intent = new Intent();
                intent.putExtra("errorcode", errorCode);
                setResult(101, intent);
                finish();
                return;
            }

            Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
            faceppConfig.interval = detection_interval;
            faceppConfig.minFaceSize = min_face_size;
            faceppConfig.roi_left = left;
            faceppConfig.roi_top = top;
            faceppConfig.roi_right = right;
            faceppConfig.roi_bottom = bottom;
            String[] array = getResources().getStringArray(R.array.trackig_mode_array);
            if (trackModel.equals(array[0]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_FAST;
            else if (trackModel.equals(array[1]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_ROBUST;
            else if (trackModel.equals(array[2])) {
                faceppConfig.detectionMode = Facepp.FaceppConfig.MG_FPP_DETECTIONMODE_TRACK_RECT;
                isShowFaceRect = true;
            }


            facepp.setFaceppConfig(faceppConfig);

            String version = facepp.getVersion();
            Log.d("ceshi", "onResume:version:" + version);
        } else {
            mDialogUtil.showDialog(getResources().getString(R.string.camera_error));
        }
        mMediaHelper = new MediaHelper(mICamera.cameraWidth, mICamera.cameraHeight, true, mGlSurfaceView);
        //        newMethodCall();
        //        doMyFaceDetection();
    }

    private void setConfig(int rotation) {
        Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
        if (faceppConfig.rotation != rotation) {
            faceppConfig.rotation = rotation;
            facepp.setFaceppConfig(faceppConfig);
        }
    }


    /**
     * 画绿色框
     */
    private void drawShowRect() {
        mPointsMatrix.vertexBuffers = OpenGLDrawRect.drawCenterShowRect(isBackCamera, mICamera.cameraWidth, mICamera
                .cameraHeight, roi_ratio);
    }

    boolean isSuccess = false;
    float confidence;
    long startTime;
    long time_AgeGender_end = 0;
    String AttriButeStr = "";
    Facepp.Face[] compareFaces;
    long featureTime = 0;
    private ArrayList<TextView> tvFeatures = new ArrayList<>();

    long matrixTime;
    private int prefaceCount = 0;
    private FaceKeyPointRecognizer mFaceKeyPointRecognizer  ;

    @Override
    public void onPreviewFrame(final byte[] previewData, final Camera camera) {
        final int orientation = sensorUtil.orientation;
        long faceDetectTime_action = System.currentTimeMillis();

        if ( mFaceKeyPointRecognizer == null ) {
            mFaceKeyPointRecognizer = new FaceKeyPointRecognizer(facepp, mCameraMatrix, mSwapMatrix, mPointsMatrix) ;
        }

        mFaceKeyPointRecognizer.setCameraAngle(Angle);
        // detect faces from camera preview data
        final Facepp.Face[] faces = mFaceKeyPointRecognizer.detectFaces(previewData, mICamera.cameraWidth, mICamera.cameraHeight, orientation);

        final long algorithmTime = System.currentTimeMillis() - faceDetectTime_action;
        long actionMaticsTime = System.currentTimeMillis();
        matrixTime = System.currentTimeMillis() - actionMaticsTime;
        if (isSuccess) {
            return;
        }
        isSuccess = true;

        if ( faces != null && faces.length > 0 ) {
            Log.e("", "### onPreviewFrame defect : " + faces[0].yaw + ", points : " + faces[0].points) ;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (faces != null) {
                    confidence = 0.0f;
                    if (faces.length > 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tvFeatures.size() < faces.length) {
                                    int tvFeaturesSize = tvFeatures.size();
                                    for (int i = 0; i < faces.length - tvFeaturesSize; i++) {
                                        TextView textView = new TextView(OpenglActivity.this);
                                        textView.setTextColor(0xff1a1d20);
                                        tvFeatures.add(textView);
                                    }
                                }
                                for (int i = prefaceCount; i < faces.length; i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).addView(tvFeatures.get(i));
                                }
                                for (int i = faces.length; i < tvFeatures.size(); i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).removeView(tvFeatures.get(i));
                                }
                                prefaceCount = faces.length;
                            }
                        });

                        for (int c = 0; c < faces.length; c++) {

                            final Facepp.Face face = faces[c];
                            if (isFaceProperty) {
                                long time_AgeGender_action = System.currentTimeMillis();
                                facepp.getAgeGender(faces[c]);
                                time_AgeGender_end = System.currentTimeMillis() - time_AgeGender_action;
                                String gender = "man";
                                if (face.female > face.male)
                                    gender = "woman";
                                AttriButeStr = "\nage: " + (int) Math.max(face.age, 1) + "\ngender: " + gender;
                            }


                            // 添加人脸比对
                            if (isFaceCompare) {
                                if (c == 0) {
                                    featureTime = System.currentTimeMillis();
                                }
                                if (facepp.getExtractFeature(face)) {
                                    synchronized (OpenglActivity.this) {
                                        newestFeature = face.feature;
                                        carmeraImgData = previewData;
                                    }

                                    if (c == faces.length - 1) {
                                        compareFaces = faces;
                                    }

                                    final FeatureInfo featureInfo = FaceCompareManager.instance().compare(facepp,
                                            face.feature);

                                    final int index = c;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            featureTargetText = tvFeatures.get(index);
                                            if (featureInfo != null) {
                                                featureTargetText.setVisibility(View.VISIBLE);
                                                featureTargetText.setText(featureInfo.title);
                                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                                        featureTargetText.getLayoutParams();

                                                int txtWidth = featureTargetText.getWidth();
                                                int txtHeight = featureTargetText.getHeight();


                                                PointF noseP = null;
                                                PointF eyebrowP = null;
                                                if (is106Points) {
                                                    noseP = face.points[46];
                                                    eyebrowP = face.points[37];
                                                } else {
                                                    noseP = face.points[34];
                                                    eyebrowP = face.points[19];
                                                }
                                                boolean isVertical;
                                                if (orientation == 0 || orientation == 3) {
                                                    isVertical = true;
                                                } else {
                                                    isVertical = false;
                                                }
                                                int tops = (int) (((mICamera.cameraWidth - (isVertical ? eyebrowP.x :
                                                        noseP.x))) * (mGlSurfaceView.getHeight() * 1.0f / mICamera
                                                        .cameraWidth));
                                                int lefts = (int) ((mICamera.cameraHeight - (isVertical ? noseP.y :
                                                        eyebrowP.y)) * (mGlSurfaceView.getWidth() * 1.0f / mICamera
                                                        .cameraHeight));
                                                if (isBackCamera) {
                                                    tops = mGlSurfaceView.getHeight() - tops;
                                                }
                                                tops = tops - txtHeight / 2;
                                                lefts = lefts - txtWidth / 2;
                                                params.leftMargin = lefts;
                                                params.topMargin = tops;
                                                featureTargetText.setLayoutParams(params);

                                            } else {

                                                featureTargetText.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    });

                                }
                                if (c == faces.length - 1) {
                                    featureTime = System.currentTimeMillis() - featureTime;
                                }
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < tvFeatures.size(); i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).removeView(tvFeatures.get(i));
                                }
                                prefaceCount = 0;
                            }
                        });
                        mPointsMatrix.rect = null;
                        compareFaces = null;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String logStr = "\ncameraWidth: " + mICamera.cameraWidth + "\ncameraHeight: " + mICamera
                                    .cameraHeight + "\nalgorithmTime: " + algorithmTime + "ms" + "\nmatrixTime: " +
                                    matrixTime + "\nconfidence:" + confidence;
                            debugInfoText.setText(logStr);
                            if (faces.length > 0 && isFaceProperty && AttriButeStr != null && AttriButeStr.length() > 0)
                                AttriButetext.setText(AttriButeStr + "\nAgeGenderTime:" + time_AgeGender_end);
                            else
                                AttriButetext.setText("");
                        }
                    });
                } else {
                    compareFaces = null;
                }
                isSuccess = false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConUtil.releaseWakeLock();
        if (mediaRecorderUtil != null) {
            mediaRecorderUtil.releaseMediaRecorder();
        }
        mICamera.closeCamera();
        mCamera = null;


        finish();
    }

    @Override
    protected void onDestroy() {
        if (mMediaHelper != null)
            mMediaHelper.stopRecording();
        super.onDestroy();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                facepp.release();
            }
        });
    }

    private int mTextureID = -1;
    private SurfaceTexture mSurface;
    private CameraMatrix mCameraMatrix;
    private PointsMatrix mPointsMatrix;
    private SwapMatrix mSwapMatrix;

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //		Log.d("ceshi", "onFrameAvailable");
        mGlSurfaceView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 黑色背景
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        surfaceInit();
    }

    private void surfaceInit() {
        mTextureID = OpenGLUtil.createTextureID();

        mSurface = new SurfaceTexture(mTextureID);
        if (isStartRecorder) {
            mMediaHelper.startRecording(mTextureID);
        }
        // 这个接口就干了这么一件事，当有数据上来后会进到onFrameAvailable方法
        mSurface.setOnFrameAvailableListener(this);// 设置照相机有数据时进入
        mCameraMatrix = new CameraMatrix(mTextureID);
        mPointsMatrix = new PointsMatrix(isFaceCompare);
        mPointsMatrix.isShowFaceRect = isShowFaceRect;

        //        mFaceMatrix = new FaceMatrix(isFaceCompare);

        mSwapMatrix = new SwapMatrix(getApplicationContext());

        mICamera.startPreview(mSurface);// 设置预览容器
        mICamera.actionDetect(this);
        if (isROIDetect)
            drawShowRect();
    }

    private boolean flip = true;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置画面的大小
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        ratio = 1; // 这样OpenGL就可以按照屏幕框来画了，不是一个正方形了

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // Matrix.perspectiveM(mProjMatrix, 0, 0.382f, ratio, 3, 700);

    }

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        final long actionTime = System.currentTimeMillis();
        //		Log.w("ceshi", "onDrawFrame===");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);

        //        Log.e("shiyang", "mtx 0="+ mtx[0]+","+mtx[1]+","+mtx[2]+ ","+mtx[3]);
        //        Log.e("shiyang", "mtx 1="+ mtx[4]+","+mtx[5]+","+mtx[6]+ ","+mtx[7]);
        //        Log.e("shiyang", "mtx 2="+ mtx[8]+","+mtx[9]+","+mtx[10]+ ","+mtx[11]);
        //        Log.e("shiyang", "mtx 3="+ mtx[12]+","+mtx[13]+","+mtx[14]+ ","+mtx[15]);

        mCameraMatrix.draw(mtx);
        //        mFaceMatrix.draw(mtx);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        //        mPointsMatrix.draw(mtx);

        mSwapMatrix.draw(mtx);

        if (isDebug) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final long endTime = System.currentTimeMillis() - actionTime;
                    debugPrinttext.setText("printTime: " + endTime);
                }
            });
        }
        mSurface.updateTexImage();// 更新image，会调用onFrameAvailable方法
        if (isStartRecorder) {
            flip = !flip;
            if (flip) {    // ~30fps
                synchronized (this) {
                    //                    mMediaHelper.frameAvailable(mtx);
                    mMediaHelper.frameAvailable(mtx);
                }
            }
        }

    }
}
