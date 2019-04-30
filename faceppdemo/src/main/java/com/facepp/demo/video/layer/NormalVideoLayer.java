package com.facepp.demo.video.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.facepp.demo.BitmapFaceDetector;
import com.facepp.demo.FaceApplication;
import com.facepp.demo.R;
import com.facepp.demo.util.EdgeNode;
import com.facepp.demo.util.GlUtil;
import com.facepp.demo.util.SkinColorNode;
import com.facepp.demo.util.SkinInfo;
import com.facepp.demo.util.SwapMatrix;
import com.facepp.demo.util.TextResourceReader;
import com.facepp.demo.util.Uv21Helper;
import com.facepp.demo.util.WarningNode;
import com.megvii.facepp.sdk.Facepp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.facepp.demo.util.ColorUtil.argb2lab;
import static com.facepp.demo.util.ColorUtil.getSkinInfo;

/**
 * Create by SongChao on 2019/2/13
 */
public class NormalVideoLayer implements IDrawLayer, SurfaceTexture.OnFrameAvailableListener {

    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private Uri videoUrl;
    private Context mContext;

    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private FloatBuffer mUVTexVertexBuffer;
    private int mProgram = 0;
    private int mPositionHandle = 0;
    private int mTextureCoordinatorHandle = 0;
    private int mMVPMatrixHandle = 0;
    private int mTextureHandle = 0;

    private int mFrameBufferObject0Id = -999;
    private int mFrameBufferObjectTexture0Id = -999;

    private int mFrameBufferObject1Id = -999;
    private int mFrameBufferObjectTexture1Id = -999;

    private int mTexture3Id = -999;
    
    
    private SwapMatrix mSwapMatrix;
    private SkinColorNode mSkinNode;
    private EdgeNode mEdgeNode;
    private WarningNode mWarningNode;

    private boolean mHasFace;

    private boolean mNeedVideoSamples;
    private boolean mNeedUsersSamples;

    private float mVertex[] = {
            1f, 1f, 0f,    // top right
            -1f, 1f, 0f, // top left
            -1f, -1f, 0f, // bottom left
            1f, -1f, 0f // bottom right
    };

    private float mUVTexVertex[] = {1f, 0f, 0f, 0f, 0f, 1f, 1f, 1f};

    private short DRAW_ORDER[] = {0, 1, 2, 2, 0, 3};

    private float mMVP[] = new float[16];

    private BitmapFaceDetector faceDetector;
    private int mWidth;
    private int mHeight;
    private int mOriginVideoWidth;
    private int mOriginVideoHeight;
    private SkinInfo mSkinVideoInfo;
    private int mButtonNumber = 0;

    public NormalVideoLayer(Context context, Uri videoUrl) {
        this.mContext = context;
        this.videoUrl = videoUrl;
        this.mHasFace = false;
        this.mNeedVideoSamples = true;
        this.mNeedUsersSamples = true;
    }

    private void initFbo() {
        if (this.mFrameBufferObject0Id < 0 && this.mFrameBufferObjectTexture0Id < 0)  {
            this.mFrameBufferObjectTexture0Id = GlUtil.createTextures(mWidth, mHeight);
            this.mFrameBufferObject0Id = GlUtil.createFramebuffers(this.mFrameBufferObjectTexture0Id);
        }
        if (this.mFrameBufferObject1Id < 0 && this.mFrameBufferObjectTexture1Id < 0)  {
            this.mFrameBufferObjectTexture1Id = GlUtil.createTextures(mWidth, mHeight);
            this.mFrameBufferObject1Id = GlUtil.createFramebuffers(this.mFrameBufferObjectTexture1Id);
        }
    }

    @Override
    public void onButton(int i) {
        Log.e("shiyang", "shiyang button="+i);
        mButtonNumber = i;
    }

    @Override
    public void onSurfaceCreated(boolean isRecord) {
        prepare(isRecord);
        start();
    }


    @Override
    public void prepare(boolean isRecord) {
        try {
            mMediaPlayer.setDataSource(mContext, videoUrl);
            mMediaPlayer.setLooping(true);
            // mute when record
            if (isRecord) {
                mMediaPlayer.setVolume(0, 0);
            }
            mMediaPlayer.prepare();
            mOriginVideoWidth = mMediaPlayer.getVideoWidth();
            mOriginVideoHeight = mMediaPlayer.getVideoHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int textures[] = GlUtil.createTextureID(1);
        if (textures.length > 0) {
            mTextureId = textures[0];
            mSurfaceTexture = new SurfaceTexture(mTextureId);
            mSurfaceTexture.setOnFrameAvailableListener(this);
        }
        initShader();
        mSwapMatrix = new SwapMatrix(mContext);

//        initFbo();
        mSkinNode = new SkinColorNode(mContext);
        mEdgeNode = new EdgeNode(mContext);
        mWarningNode = new WarningNode(mContext);
    }

    private void initShader() {
        String vertexShader = TextResourceReader.readTextFileFromResource(mContext, R.raw.video_vertex_shader);
        String fragmentShader = TextResourceReader.readTextFileFromResource(mContext, R.raw.video_normal_fragment_shader);

        mProgram = GlUtil.createProgram(vertexShader, fragmentShader); // create vertex's shader and fragment's
        // shader, add to shader for build
        if (mProgram == 0) {
            return;
        }
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GlUtil.checkLocation(mPositionHandle, "vPosition");

        mTextureCoordinatorHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GlUtil.checkLocation(mTextureCoordinatorHandle, "inputTextureCoordinate");

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GlUtil.checkLocation(mMVPMatrixHandle, "uMVPMatrix");

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        GlUtil.checkLocation(mTextureHandle, "s_texture");

        mDrawListBuffer = ByteBuffer.allocateDirect(DRAW_ORDER.length * 2).order(ByteOrder.nativeOrder())
                .asShortBuffer().put(DRAW_ORDER);
        mVertexBuffer = ByteBuffer.allocateDirect(mVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(mVertex);
        mUVTexVertexBuffer = ByteBuffer.allocateDirect(mUVTexVertex.length * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(mUVTexVertex);

        mUVTexVertexBuffer.position(0);
        mDrawListBuffer.position(0);
        mVertexBuffer.position(0);
        Matrix.setIdentityM(mMVP, 0);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mOriginVideoWidth = mMediaPlayer.getVideoWidth();
        mOriginVideoHeight = mMediaPlayer.getVideoHeight();

        float wr = width*1.0f/mOriginVideoWidth;
        float hr = height*1.0f/mOriginVideoHeight;
        int x=0;
        int y=0;
//
        if (wr < hr) {
            this.mWidth = width;
            this.mHeight = (int) (height*wr/hr);
//            y = (height - this.mHeight)/2;
        } else {
            this.mWidth = (int) (width*hr/wr);
            this.mHeight = height;
//            x = (width-this.mWidth)/2;
        }

//        this.mWidth = mOriginVideoWidth;
//        this.mHeight = mOriginVideoHeight;
        GLES20.glViewport(x, y, this.mWidth, this.mHeight);

        Log.e("shiyang", "shiyang onSurfaceChanged (w,h)="+"("+width+","+height+")");
        if (faceDetector != null) {
            faceDetector.setSizeChange(this.mWidth, this.mHeight);
        }

        initFbo();
        mTexture3Id = GlUtil.createTextures(this.mWidth, this.mHeight);
    }

    private void drawCamera() {
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinatorHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinatorHandle, 2, GLES20.GL_FLOAT, false, 0, mUVTexVertexBuffer);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVP, 0);
        GLES20.glUniform1i(mTextureHandle, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, DRAW_ORDER.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinatorHandle);
        GLES20.glDisableVertexAttribArray(mMVPMatrixHandle);
        GLES20.glDisableVertexAttribArray(mTextureHandle);

    }


    public static String saveMyBitmap(String bitName,Bitmap mBitmap) {
        File f = new File("/sdcard/" + bitName + ".png");

        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("在保存图片时出错：" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception e) {
            return "create_bitmap_error";
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/sdcard/" + bitName + ".png";

    }

    private  boolean mNeedSave = false;

    private void drawNewFace() {
        if (mHasFace) {
//            GLES20.glClearColor(0,0,0,0);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            int texid = -1;

//            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBufferObject0Id);
//            GLES20.glClearColor(0,0,0,0);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            mSwapMatrix.draw(null, 0, mWidth, mHeight);
//            if (mNeedSave) {
//                Bitmap myFace = Uv21Helper.readGlFrameBitmap(mWidth, mHeight);
//                saveMyBitmap("lalala000", myFace);
//                mNeedSave=false;
//            }
//            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
//            texid = mFrameBufferObjectTexture0Id;
//
//            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBufferObject1Id);
//            SkinInfo userInfo = mSwapMatrix.getSwapSkinInfo();
//            mSkinNode.setSkinInfos(mSkinVideoInfo, userInfo);
//            mSkinNode.setTransferMode(mButtonNumber);
//            GLES20.glClearColor(0,0,0,0);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//            mSkinNode.draw(texid, mTexture3Id, mWidth, mHeight);
//            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
//            texid = mFrameBufferObjectTexture1Id;
//
//            mEdgeNode.draw(texid, mTexture3Id, mWidth, mHeight);

        } else {
//            mWarningNode.draw();
        }
    }

    @Override
    public void drawFrame() {
        mSurfaceTexture.updateTexImage();

        drawCamera();

        detectFace();

        drawNewFace();
    }

//    private SkinInfo getSkinInfo(Bitmap bmp, PointF[] pts) {
//        SkinInfo skInfo = new SkinInfo();
////        int w = bmp.getWidth();
////        int h = bmp.getHeight();
//        float lSum = 0.0f;
//        float aSum = 0.0f;
//        float bSum = 0.0f;
//        float nSum = 0.0f;
//
//
//        float lDevSum = 0.0f;
//        float aDevSum = 0.0f;
//        float bDevSum = 0.0f;
//
//
//        int[] samplingArray = new int[] {//16*3
//                0,32,60,
//                1,31,56,
//                2,30,52,
//                3,29,48,
//                4,28,45,
//                5,27,42,
//                6,26,38,
//                7,25,34,
//                8,24,30,
//                9,23,26,
//                10,22,22,
//                11,21,18,
//                12,20,15,
//                13,19,12,
//                14,18,8,
//                15,17,4
//        };
//
//        for (int i=0; i<16; i++) {
//            int jmax = samplingArray[3*i+2];
//            nSum += jmax;
//            for (int j=0; j<jmax; j++) {
//                float ptax = pts[samplingArray[3*i]].x;
//                float ptay = pts[samplingArray[3*i]].y;
//                float ptbx = pts[samplingArray[3*i+1]].x;
//                float ptby = pts[samplingArray[3*i+1]].y;
//
//                float alpha = j*1.0f/(jmax*1.0f-1.0f);
//                int x = (int) ((1.0f-alpha)*ptax + alpha*ptbx);
//                int y = (int) ((1.0f-alpha)*ptay + alpha*ptby);
//                int argb = bmp.getPixel(x, y);
//                float[] lab = argb2lab(argb);
//                lSum += lab[0];
//                aSum += lab[1];
//                bSum += lab[2];
//                lDevSum += lab[0]*lab[0];
//                aDevSum += lab[1]*lab[1];
//                bDevSum += lab[2]*lab[2];
//            }
//        }
//
//        float lMean = lSum/nSum;
//        float aMean = aSum/nSum;
//        float bMean = bSum/nSum;
//        skInfo.setLabMean(lMean, aMean, bMean);
//
//        float lStdDev = (float) Math.sqrt(lDevSum/nSum - lMean*lMean);
//        float aStdDev = (float) Math.sqrt(aDevSum/nSum - aMean*aMean);
//        float bStdDev = (float) Math.sqrt(bDevSum/nSum - bMean*bMean);
//        skInfo.setLabStandardDeviation(lStdDev, aStdDev, bStdDev);
//
//        mNeedVideoSamples = false;
//        return skInfo;
//    }

    /**
     * 检测人脸关键点和替换人脸
     */
    private void detectFace() {
        mHasFace = false;
        Bitmap frameBitmap = null;
        try {
            if (faceDetector == null) {
                faceDetector = new BitmapFaceDetector(FaceApplication.sContext, this.mWidth, this.mHeight);
            }

            frameBitmap = Uv21Helper.readGlFrameBitmap(mWidth, mHeight);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture3Id);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, frameBitmap, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            byte[] uv21Data = Uv21Helper.convertToNV21(frameBitmap, mWidth, mHeight);
            Facepp.Face[] faces = faceDetector.detectFaces(uv21Data, Facepp.IMAGEMODE_NV21);

            if (null == faces) {
                Log.e("shiyang", "shiyang null face ");
            } else {
                if (0 == faces.length) {
                    Log.e("shiyang", "shiyang 0 face ");
                } else {
                    mSwapMatrix.setTrackerData(faces[0].points);
                    mHasFace = true;
                    // do sampling
                    if (mNeedVideoSamples) {
                        mSkinVideoInfo = getSkinInfo(frameBitmap, faces[0].points);
                        float[] labMean = mSkinVideoInfo.getLabMean();
                        float[] labStdDev = mSkinVideoInfo.getLabStandardDeviation();
                        Log.e("shiyang", "shiyang lab mean video =("+labMean[0]+","+labMean[1]+","+labMean[2]+")");
                        Log.e("shiyang", "shiyang lab stddev video =("+labStdDev[0]+","+labStdDev[1]+","+labStdDev[2]+")");
//                        mNeedVideoSamples = false;
                    }

//                    PointF p000 = faces[0].points[0];
//                    PointF p016 = faces[0].points[16];
//                    PointF p032 = faces[0].points[32];
//                    PointF p043 = faces[0].points[43];
//                    Log.e("shiyang", "shiyang p000="+p000.toString());
//                    Log.e("shiyang", "shiyang p016="+p016.toString());
//                    Log.e("shiyang", "shiyang p032="+p032.toString());
//                    Log.e("shiyang", "shiyang p043="+p043.toString());
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", "### createBitmapFromGLSurface : " + e);
        } finally {
            if (frameBitmap != null) {
                frameBitmap.recycle();
            }
        }
    }


    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
            mMediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.pause();
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (faceDetector != null) {
            faceDetector.release();
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }
}
