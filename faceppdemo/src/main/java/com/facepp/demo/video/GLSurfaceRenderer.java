package com.facepp.demo.video;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facepp.demo.video.layer.IDrawLayer;
import com.facepp.demo.video.layer.NormalVideoLayer;
import com.facepp.demo.video.recorder.IRecordRender;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Create by SongChao on 2019/3/13
 */
public class GLSurfaceRenderer implements IRecordRender {

    private IDrawLayer mVideoLayer;
    private boolean isRecord = false ;

    public GLSurfaceRenderer setRecord(boolean record) {
        isRecord = record;
        return this;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if ( mVideoLayer != null ) {
            mVideoLayer.onSurfaceCreated(isRecord);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if ( mVideoLayer != null ) {
            mVideoLayer.onSurfaceChanged(width, height);
        }
        Log.e("", "### onSurfaceChanged width : " + width + ", height : " + height) ;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if ( mVideoLayer != null ) {
            mVideoLayer.drawFrame();
        }
    }

    public void setDataSource(Context context, Uri dataSource) {
        mVideoLayer = new NormalVideoLayer(context, dataSource);
    }

    public void onDestroy() {
        if ( mVideoLayer != null ) {
            mVideoLayer.onDestroy();
        }
    }

    public void setButton(int i) {
        mVideoLayer.onButton(i);
    }


    @Override
    public void start() {
        if ( mVideoLayer != null ) {
            mVideoLayer.start();
        }
    }

    @Override
    public void onPause() {
        if ( mVideoLayer != null ) {
            mVideoLayer.onPause();
        }
    }

    @Override
    public void onResume() {
        if ( mVideoLayer != null ) {
            mVideoLayer.onResume();
        }
    }

    @Override
    public void release() {
        onDestroy();
    }
}
