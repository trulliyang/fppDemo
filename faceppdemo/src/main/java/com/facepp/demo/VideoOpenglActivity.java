package com.facepp.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facepp.demo.video.VideoGLSurfaceView;
import com.facepp.demo.video.recorder.GLSurfaceRecorder;
import com.facepp.demo.video.utils.MediaMuxerHelper;

import java.io.File;

/**
 * Create by SongChao on 2019/3/13
 */
public class VideoOpenglActivity extends Activity {

    private VideoGLSurfaceView mVideoGLSurfaceView;

    private Button mRecordButton;
    private ProgressDialog mProgressDialog;
    // todo : 确保你的手机里有 sdcard/demo_10.mp4 文件
    private File mOriginVideoFile = new File("/sdcard/12.mp4");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_video);
        mVideoGLSurfaceView = findViewById(R.id.gl_surface);
        mRecordButton = findViewById(R.id.record_btn);
        mVideoGLSurfaceView.setDataSource(Uri.fromFile(mOriginVideoFile));
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecord();
            }
        });

        findViewById(R.id.button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : click button 0
//                Toast.makeText(v.getContext(), "click button 0", Toast.LENGTH_SHORT).show();
                mVideoGLSurfaceView.setButton(0);
            }
        });

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : click button 1
//                Toast.makeText(v.getContext(), "click button 1", Toast.LENGTH_SHORT).show();
                mVideoGLSurfaceView.setButton(1);
            }
        });

        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : click button 2
//                Toast.makeText(v.getContext(), "click button 2", Toast.LENGTH_SHORT).show();
                mVideoGLSurfaceView.setButton(2);
            }
        });

        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : click button 3
//                Toast.makeText(v.getContext(), "click button 3", Toast.LENGTH_SHORT).show();
                mVideoGLSurfaceView.setButton(3);
            }
        });
    }

    private void clickRecord() {
        // 预览的
        mVideoGLSurfaceView.onPause();

        mProgressDialog = new ProgressDialog(VideoOpenglActivity.this);
        mProgressDialog.setMessage("Recording");
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressNumberFormat(" ");
        mProgressDialog.show();
        final File outFile = getCaptureFile();
        mVideoGLSurfaceView.record(outFile, 1.0f, new GLSurfaceRecorder.OnRecordListener() {

            @Override
            public void onRecordFinish(boolean success, int totalDuration) {
                onEncodeFinish(mOriginVideoFile, outFile);
            }

            @Override
            public void onRecordProgress(int recordedDuration, int totalDuration) {
                mProgressDialog.setProgress((int) (recordedDuration / (float) totalDuration * 100));
            }
        }, 0);
    }

    private void openFile(File outFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String type = "video/*";
        Uri uri = Uri.fromFile(outFile);
        intent.setDataAndType(uri, type);
        startActivity(intent);
    }

    /**
     * 录制结束之后将原始视频的音频与录制好的视频合并, 生成新的视频
     *
     * @param originFile
     * @param recordFile
     */
    private void onEncodeFinish(File originFile, File recordFile) {
        final File finalOutFile = getCaptureFile();
        Log.e("", "### final video file : " + finalOutFile.getAbsolutePath() +
                ", record video : " + recordFile.getAbsolutePath());

        try {
            boolean result = MediaMuxerHelper.mergeVideoAndAudio(recordFile.getAbsolutePath(),
                    originFile.getAbsolutePath(), finalOutFile.getAbsolutePath());
            Log.e("", "### merge video and audio " + result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mProgressDialog.dismiss();
        mVideoGLSurfaceView.onResume();
    }

    private File getCaptureFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), "movie-cache");
        dir.mkdirs();
        return new File(dir, getDateTimeString() + ".mp4");
    }

    private static final String getDateTimeString() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoGLSurfaceView != null) {
            mVideoGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoGLSurfaceView != null) {
            mVideoGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoGLSurfaceView != null) {
            mVideoGLSurfaceView.onDestroy();
        }
    }
}
