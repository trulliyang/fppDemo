package com.facepp.demo.video;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.facepp.demo.video.recorder.GLSurfaceRecorder;

import java.io.File;

/**
 * Create by SongChao on 2019/3/13
 */
public class VideoGLSurfaceView extends GLSurfaceView {

    private static final int GL_CONTEXT_VERSION = 2;
    private GLSurfaceRenderer mGLSurfaceRenderer;
    private Uri mDateSource;
    private Handler mHandler = new Handler(Looper.getMainLooper()) ;

    public VideoGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public VideoGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(GL_CONTEXT_VERSION);
    }

    public void setDataSource(Uri dataSource) {
        mDateSource = dataSource;
        mGLSurfaceRenderer = new GLSurfaceRenderer();
        mGLSurfaceRenderer.setDataSource(getContext(), dataSource);
        setRenderer(mGLSurfaceRenderer);
    }

    public void setButton(int i) {
        mGLSurfaceRenderer.setButton(i);
    }

    /**
     * 重新创建一个 Render 进行录制
     *
     * @param outPutFile
     * @param scale
     * @param listener
     * @param duration
     */
    public void record(File outPutFile, float scale, final GLSurfaceRecorder.OnRecordListener listener, int duration) {
        int videoWidth = 0;
        int videoHeight = 0;
        float frameRate = 25;
        int bitRate = 2000000;
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(getContext(), mDateSource);
            duration = extractMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_DURATION);
            videoWidth = extractMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            videoHeight = extractMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            bitRate = extractMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_BITRATE);
            frameRate = Float.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        frameRate = frameRate == 0.0f ? 25 : frameRate;

        Log.e("", "### record to file " + outPutFile + ", total duration : " + duration + ", frame rate : " +
                frameRate + ", bit rate : " + bitRate);

        final GLSurfaceRecorder recorder = new GLSurfaceRecorder();
        int outWidth = (int) (videoWidth * scale);
        if (outWidth <= 0) {
            outWidth = 1;
        }
        int outHeight = (int) (videoHeight * scale);
        if (outHeight <= 0) {
            outHeight = 1;
        }
        final GLSurfaceRenderer newMovieRenderer = new GLSurfaceRenderer();
        newMovieRenderer.setRecord(true);
        newMovieRenderer.setDataSource(getContext(), mDateSource);
        recorder.setDataSource(newMovieRenderer);
        recorder.configOutput(outWidth, outHeight, bitRate, (int) frameRate, 1, outPutFile.getAbsolutePath(), duration);
        recorder.prepare();
        // 录制的时候延迟 500, 因为 prepare 会有一些延时
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recorder.startRecord(listener);
            }
        }, 500);

    }

    private int extractMetadata(MediaMetadataRetriever retriever, int metadataKey) {
        int value = 0 ;
        try {
            value = Integer.valueOf(retriever.extractMetadata(metadataKey)) ;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mGLSurfaceRenderer != null ) {
            mGLSurfaceRenderer.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ( mGLSurfaceRenderer != null ) {
            mGLSurfaceRenderer.onResume();
        }
    }

    public void onDestroy() {
        if ( mGLSurfaceRenderer != null ) {
            mGLSurfaceRenderer.onDestroy();
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}
