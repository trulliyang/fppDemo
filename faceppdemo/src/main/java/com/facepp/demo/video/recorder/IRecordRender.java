package com.facepp.demo.video.recorder;

import android.opengl.GLSurfaceView;

/**
 * Create by SongChao on 2019/2/15
 */
public interface IRecordRender extends GLSurfaceView.Renderer {
    /**
     * 开始渲染
     */
    void start();

    void onPause();

    void onResume();

    /**
     * 释放资源
     */
    void release();
}
