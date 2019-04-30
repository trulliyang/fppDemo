package com.facepp.demo.util;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

public class ColorUtil {

    private static float gamma(float x) {
//        return (float) (x>0.04045 ? Math.pow((x+0.055f)/1.055f,2.4f) : x/12.92);
        return x;
    }

    private static float clamp(float x, float min, float max) {
        if (x < min) x= min;
        else if (x > max) x= max;
        return x;
    }

    public static float[] argb2lab(int argb) {
        float[] lab = new float[]{0f,0f,0f};
        int ai = (argb>>24)&(0x000000ff);
        int ri = (argb>>16)&(0x000000ff);
        int gi = (argb>> 8)&(0x000000ff);
        int bi = (argb>> 0)&(0x000000ff);
        float A = gamma(ai*1.0f/255.0f);
        float R = gamma(ri*1.0f/255.0f);
        float G = gamma(gi*1.0f/255.0f);
        float B = gamma(bi*1.0f/255.0f);

//        Log.e("shiyang", "shiyang argb="+A+","+R+","+G+","+B);

        float X= (float) (0.412453*R+0.357580*G+0.180423*B);
        float Y= (float) (0.212671*R+0.715160*G+0.072169*B);
        float Z= (float) (0.019334*R+0.119193*G+0.950227*B);

        float fX,fY,fZ;

        if (Y > 0.008856f)
            fY = (float)Math.pow(Y, 1.0f/3.0f);
        else
            fY = 7.787f * Y + 16.0f/116.0f;

        if (X > 0.008856f)
            fX = (float)Math.pow(X, 1.0f/3.0f);
        else
            fX = 7.787f * X + 16.0f/116.0f;

        if (Z > 0.008856)
            fZ = (float)Math.pow(Z, 1.0f/3.0f);
        else
            fZ = 7.787f * Z + 16.0f/116.0f;

        lab[0] = 116.0f * fY -16.0f;
        lab[1] = 500.f * (fX - fY);
        lab[2] = 200.f * (fY - fZ);

        lab[0] = clamp(lab[0], 0.0f, 100.0f);
        lab[1] = clamp(lab[1], -128.0f, 127.0f);
        lab[2] = clamp(lab[2], -128.0f, 127.0f);


        return  lab;
    }

    public static SkinInfo getSkinInfo(Bitmap bmp, PointF[] pts) {
        SkinInfo skInfo = new SkinInfo();
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
        float lSum = 0.0f;
        float aSum = 0.0f;
        float bSum = 0.0f;
        float nSum = 0.0f;


        float lDevSum = 0.0f;
        float aDevSum = 0.0f;
        float bDevSum = 0.0f;


        int[] samplingArray = new int[] {//16*3
                0,32,60,
                1,31,56,
                2,30,52,
                3,29,48,
                4,28,45,
                5,27,42,
                6,26,38,
                7,25,34,
                8,24,30,
                9,23,26,
                10,22,22,
                11,21,18,
                12,20,15,
                13,19,12,
                14,18,8,
                15,17,4
        };

        for (int i=0; i<16; i++) {
            int jmax = samplingArray[3*i+2];
            nSum += jmax;
            for (int j=0; j<jmax; j++) {
                float ptax = pts[samplingArray[3*i]].x;
                float ptay = pts[samplingArray[3*i]].y;
                float ptbx = pts[samplingArray[3*i+1]].x;
                float ptby = pts[samplingArray[3*i+1]].y;

                float alpha = j*1.0f/(jmax*1.0f-1.0f);
                int x = (int) ((1.0f-alpha)*ptax + alpha*ptbx);
                int y = (int) ((1.0f-alpha)*ptay + alpha*ptby);
                int argb = bmp.getPixel(x, y);
                float[] lab = argb2lab(argb);
                lSum += lab[0];
                aSum += lab[1];
                bSum += lab[2];
                lDevSum += lab[0]*lab[0];
                aDevSum += lab[1]*lab[1];
                bDevSum += lab[2]*lab[2];
            }
        }

        float lMean = lSum/nSum;
        float aMean = aSum/nSum;
        float bMean = bSum/nSum;
        skInfo.setLabMean(lMean, aMean, bMean);

        float lStdDev = (float) Math.sqrt(lDevSum/nSum - lMean*lMean);
        float aStdDev = (float) Math.sqrt(aDevSum/nSum - aMean*aMean);
        float bStdDev = (float) Math.sqrt(bDevSum/nSum - bMean*bMean);
        skInfo.setLabStandardDeviation(lStdDev, aStdDev, bStdDev);

//        mNeedVideoSamples = false;
        return skInfo;
    }
}
//        inline float gamma(float x)
//        {return x>0.04045?pow((x+0.055f)/1.055f,2.4f):x/12.92;};
//
//        void RGBToLab(unsigned char*rgbImg,float*labImg)
//        {
//        float B=gamma(rgbImg[0]/255.0f);
//        float G=gamma(rgbImg[1]/255.0f);
//        float R=gamma(rgbImg[2]/255.0f);
//        float X=0.412453*R+0.357580*G+0.180423*B;
//        float Y=0.212671*R+0.715160*G+0.072169*B;
//        float Z=0.019334*R+0.119193*G+0.950227*B;
//
//        　　float X/=0.95047;
//        　　float Y/=1.0;
//        　　float Z/=1.08883;
//
//        float FX = X > 0.008856f ? pow(X,1.0f/3.0f) : (7.787f * X +0.137931f);
//        float FY = Y > 0.008856f ? pow(Y,1.0f/3.0f) : (7.787f * Y +0.137931f);
//        float FZ = Z > 0.008856f ? pow(Z,1.0f/3.0f) : (7.787f * Z +0.137931f);
//        labImg[0] = Y > 0.008856f ? (116.0f * FY - 16.0f) : (903.3f * Y);
//        labImg[1] = 500.f * (FX - FY);
//        labImg[2] = 200.f * (FY - FZ);
//        }