package com.example.androidbenchmarkapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class Canny {

    protected double start, finish, elapsedTime;

    protected Context mContext;

    public Canny(Context context) {
        mContext = context;
    }

    protected double EdgeDetection()
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

        Mat gray = new Mat(srcMat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(srcMat, gray, Imgproc.COLOR_RGB2GRAY);
        Mat edge = new Mat();
        Mat dst = new Mat();
        Imgproc.Canny(gray, edge, 80, 90);
        Imgproc.cvtColor(edge, dst, Imgproc.COLOR_GRAY2RGBA,4);
        Bitmap resultBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, resultBitmap);

        finish = System.nanoTime();
        elapsedTime = (finish - start) / 1000000;

        Log.d("Cenk", Double.toString(elapsedTime));

        //i.setImageBitmap(resultBitmap);

        return elapsedTime;
    }
}
