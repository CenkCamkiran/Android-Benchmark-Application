package com.example.androidbenchmarkapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class QRCodeScan {

    public double start, finish, elapsedTime;

    Context mContext;

    public QRCodeScan(Context context) {
        mContext = context;
    }

    protected double Scan()
    {
        try
        {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("Sample_QR_Code.jpg");

            //InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null)
            {
                Log.e("HATA!!!", "file is not a bitmap,");
            }

            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            start = System.nanoTime();
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try
            {
                Result result = reader.decode(bBitmap);
                finish = System.nanoTime();
                elapsedTime = (finish - start) / 1000000;
                Log.e("Başarılı!!!", String.valueOf(result));
                //Toast.makeText(mContext, "BAŞARILI", Toast.LENGTH_LONG).show();
            }
            catch (NotFoundException e)
            {
                Log.e("HATA!!!", "decode exception", e);
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e("HATA!!!", "can not open file");;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return elapsedTime;
    }
}
