package com.facepp.demo.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mrsimple on 14/3/2019
 */
public class FaceppNetworkApi {

    private static final String FACE_DETECT_URL = "https://api-cn.faceplusplus.com/facepp/v3/detect" ;
    private static final String API_KEY = "xLhZzHCnoym6vgxHeT69YubAp_1Tr2LT";
    private static final String API_SECRET = "zlrSLjko__Y73XCdL3WVwSiUI4DTS6Hm";

    /**
     * detect faces from image file
     * @param file
     * @param listener
     */
    public void detectFacesFromImageFile(File file, final OnFaceJsonListener listener) {

        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("image_file", file.getName(), body);
        }

        requestBody.addFormDataPart("api_key", API_KEY);
        requestBody.addFormDataPart("api_secret", API_SECRET);
        requestBody.addFormDataPart("return_landmark", String.valueOf(2));

        Request request = new Request.Builder().url(FACE_DETECT_URL).post(requestBody.build()).build();
        client.newBuilder().readTimeout(15000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq" ,"### detectFacesFromImageFile onFailure");
                callback(listener, "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    callback(listener, str);
//                    Log.i("lfq", response.message() + " , ### body " + str);
                } else {
                    callback(listener, "");
                    Log.i("lfq" ,response.message() + " error : body " + response.body().string());
                }
            }
        });
    }

    private void callback(OnFaceJsonListener listener, String body) {
        if ( listener == null ) {
            return;
        }
        JSONObject jsonObject = null ;
        try {
            jsonObject = new JSONObject(body) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onFacesJson(jsonObject);
    }

    public static interface OnFaceJsonListener {
        void onFacesJson(JSONObject jsonObject);
    }

}
