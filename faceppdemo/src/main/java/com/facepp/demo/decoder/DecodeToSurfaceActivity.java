package com.facepp.demo.decoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facepp.demo.R;

import java.io.File;
import java.io.IOException;

/**
 * 从mp4文件中解码指定帧数的图片,并且写入到文件中
 *
 * Created by mrsimple on 13/3/2019
 */
public class DecodeToSurfaceActivity extends Activity {


    public static void start(Context context) {
        Intent starter = new Intent(context, DecodeToSurfaceActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_to_surface);

        new Thread() {
            @Override
            public void run() {
                try {
                    // todo : 确保你的设备中有这个视频将要播放的视频
                    new TestDecoder().extractVideoFrames(new File("/sdcard/10.mp4"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
