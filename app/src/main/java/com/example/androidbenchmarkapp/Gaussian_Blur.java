package com.example.androidbenchmarkapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class Gaussian_Blur {

    protected Context mContext;

    protected double start, finish, elapsedTime;

    public Gaussian_Blur(Context context) {
        mContext = context;
    }

    protected double Blur()
    {

        AssetManager assetManager = mContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("Sample_Image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        Mat srcMat = new Mat( bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC3);
        Bitmap myBitmap32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, srcMat);

        start = System.nanoTime();
        Mat dst = new Mat();
        Imgproc.GaussianBlur(srcMat, dst, new Size(45,45), 0);
        Bitmap resultBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, resultBitmap);
        finish = System.nanoTime();

        elapsedTime = (finish - start) / 1000000;

        //imageView.setImageBitmap(resultBitmap);

        return elapsedTime;
    }
}
