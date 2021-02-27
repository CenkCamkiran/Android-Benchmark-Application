package com.example.androidbenchmarkapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.graphics.Typeface;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.text.Editable;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GPUFragment extends Fragment implements View.OnClickListener{

    Context mContext;

    private Button mStartButton;

    private Handler _myHandler;
    private Handler _myHandler2;

    private TextView mDisplayDetails;
    private TextView mDisplayDetails2;

    private Runnable _myTask;
    private Runnable _myTask2;

    private String[] xout = new String[20];
    private String[] xout2 = new String[20];
    private String date$;

    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int testDone = 0;

    private Typeface tf = Typeface.create("monospace", Typeface.NORMAL);

    int requestCode;

    private TextView GPUInfo_txtView;

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
    }

    public GPUFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int pixels = 15;
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_gpu, container,false);

        //*********************************************************

        mStartButton = rootView.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(this);

        mDisplayDetails2 = rootView.findViewById(R.id.displayDetails2);
        mDisplayDetails = rootView.findViewById(R.id.displayDetails);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayDetails.setTypeface(tf);
        mDisplayDetails2.setTypeface(tf);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);

        mDisplayDetails.setTextColor(Color.rgb(0,0,255));
        mDisplayDetails2.setTextColor(Color.rgb(0,0,255));

        xout[0]   = " Android Java OpenGL Benchmark " + date$ + "\n";
        xout[1]   = "\n";
        xout[2]   = "\n";
        xout[3]   = "\n";
        xout[4]   = "\n";
        xout[5]   = "\n";
        xout[6]   = "\n";
        xout[7]   = "\n";
        xout[8]   = "\n";
        xout[9]   = "\n";
        xout[10]  = "\n";
        xout[11]  = "\n";
        xout[12]  = "\n";
        xout[13]  = "\n";
        xout[14]  = "\n";
        xout[15]  = "\n";
        xout[16]  = "\n";
        testDone = 0;

        xout2[0]   = " Android Java Drawing Benchmark " + date$ + "\n";
        xout2[1]   = "\n";
        xout2[2]   = "\n";
        xout2[3]   = "\n";
        xout2[4]   = "\n";
        xout2[5]   = "\n";
        xout2[6]   = "\n";
        xout2[7]   = "\n";
        xout2[8]   = "\n";
        xout2[9]   = "\n";
        xout2[10]  = "\n";
        xout2[11]  = "\n";
        xout2[12]  = "\n";
        xout2[13]  = "\n";
        xout2[14]  = "\n";
        xout2[15]  = "\n";
        xout2[16]  = "\n";
        testDone = 0;

        clear();

        //eski telefona ait kod
        int textSizeInSp = (int) getResources().getDimension(R.dimen.GPU);
        mDisplayDetails.setTextSize(convertSpToPixels(textSizeInSp , mContext));
        mDisplayDetails2.setTextSize(convertSpToPixels(textSizeInSp , mContext));

        //note 4 ün kodu
        /*mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);
        mDisplayDetails2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);*/

        //annemin teli
        //mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        //mDisplayDetails2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

        displayAll(4);

        //*********************************************************

        return rootView;
    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.startButton)
        {
            clear();
            _myTask = new myWhetTask();
            _myHandler = new Handler();
            xout[15] = " Running ------ 12 Tests of 10+ seconds\n";
            xout[16] = "\n";
            xout2[15] = " Running ------ 4 Tests of 10+ seconds\n";
            xout2[16] = "\n";
            displayAll(4);
            startTest = System.currentTimeMillis();
            _myHandler.postDelayed(_myTask, 100);
        }
    }

    private void displayAll(int resultCode)
    {

        if (resultCode == 1)
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);
        }

        else if (resultCode == 2)
        {
            mDisplayDetails2.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);
        }

        else if (resultCode == 3)
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);

            mDisplayDetails2.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);
        }

        else if (resultCode == 4)
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);

            mDisplayDetails2.setText(xout2[0]  + xout2[1]  + xout2[2]  + xout2[3]  +
                    xout2[4]  + xout2[5]  + xout2[6]  + xout2[7]  +
                    xout2[8]  + xout2[9]  + xout2[10] + xout2[11] +
                    xout2[12] + xout2[13] + xout2[14] + xout2[15] +
                    xout2[16]);
        }

    }

    private void resetTest()
    {
        if(_myHandler != null)
        {
            _myHandler.removeCallbacks(_myTask);
        }
        if(_myHandler2 != null)
        {
            _myHandler2.removeCallbacks(_myTask2);
        }
    }

    private class myWhetTask implements Runnable
    {

        public void run()
        {

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = " Android Java OpenGL Benchmark " + date$ + "\n";

            startTest = System.currentTimeMillis();
            testTime = 0.0;
            testDone = 0;
            runit();
        }
    }


    public void runit()
    {
        Intent intent = new Intent(mContext, DisplayAllActivity.class);
        startActivityForResult(intent, requestCode);

        Intent intent2 = new Intent(mContext, DisplayAllActivity2.class);
        startActivityForResult(intent2, requestCode);
    }


    public void finish()
    {
        getActivity().finish();
    }

    public void clear()
    {
        xout[0]  = " Android Java OpenGL Benchmark " + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = "           --------- Frames Per Second --------\n";
        xout[3]  = " Üçgenler WireFrame   Gölgeli  Gölgeli+ Dokulu\n";
        xout[4]  = "\n";
        xout[5]  = "   9000\n";
        xout[6]  = "  18000+\n";
        xout[7]  = "  36000+\n";
        xout[8]  = "\n";
        xout[9]  = " Dört test, kare başına yukarıdaki üçgenleri üreten tekrarlı  \n";
        xout[10] = " rastgele konumlar, renkler ve dönme ayarlarıyla 50 küp çiziyor.\n";
        xout[11] = " Wireframes, renk gölgeli ve dokulardır. \n";
        xout[12] = " Üçüncü ve dördüncü testler, oluklu kenarları ve tavanı olan\n";
        xout[13] = " bir tünele girip çıkar.    \n";
        xout[14] = "\n";
        xout[15] = " Beklenen çalışma süresi 12 testin her biri için 10+ saniyedir.\n";
        xout[16] = "\n";

        xout2[0]  = " Android Java Drawing Benchmark " + date$ + "\n";
        xout2[1]  = "\n";
        xout2[2]  = " Test                            Frames     FPS\n";
        xout2[3]  = "\n";
        xout2[4]  = " PNG Bitmap'i İki Kez Göster\n";
        xout2[5]  = " Artı İki SweepGradient Çemberleri\n";
        xout2[6]  = " Artı 200 tane Rastgele Küçük Çemberler\n";
        xout2[7]  = " Artı 320 tane Uzun Çizgi\n";
        xout2[8]  = " Artı 4000 tane Rastgele Küçük Çemberler\n";
        xout2[9]  = "\n";
        xout2[10] = " .....................................\n";
        xout2[11] = "\n";
        xout2[12] = " Beklenen çalışma süresi her test için 10 saniyedir.\n";
        xout2[13] = "\n";
        xout2[14] = "\n";
        xout2[15] = "\n";
        xout2[16] = "\n";

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String extraData = data.getStringExtra("OPENGL1");
        //Log.d("Cenk", String.valueOf(data.getExtras().keySet()));
        String extraData2 = data.getStringExtra("JavaDraw");

        if(resultCode == 1)
        {
            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout[2] = extraData;
            xout[3]  = "";
            xout[4]  = "";
            xout[5]  = "";
            xout[6]  = "";
            xout[7]  = "";
            xout[8]  = "";
            xout[9]  = "";
            xout[10] = "\n";
            xout[11] = "      Toplam geçen süre  " + String.format("%5.1f", testTime) + " saniye\n";
            xout[12] = "\n";
            xout[13] = "\n";
            xout[14] = "\n";
            xout[15] = "\n";
            xout[16] = "\n";
            testDone = 1;
            displayAll(1);
            Toast.makeText(mContext, "Başarılı", Toast.LENGTH_LONG).show();
        }

        else if(resultCode == 2)
        {
            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout[2] = extraData2;
            xout[3]  = "";
            xout[4]  = "";
            xout[5]  = "";
            xout[6]  = "";
            xout[7]  = "";
            xout[8]  = "";
            xout[9]  = "";
            xout[10] = "\n";
            xout[11] = "      Toplam geçen süre  " + String.format("%5.1f", testTime) + " saniye\n";
            xout[12] = "\n";
            xout[13] = "\n";
            xout[14] = "\n";
            xout[15] = "\n";
            xout[16] = "\n";
            testDone = 1;
            displayAll(2);
            Toast.makeText(mContext, "Başarılı", Toast.LENGTH_LONG).show();
        }

        else
        {
            xout[2]  = "\n";
            xout[3]  = "\n";
            xout[4]  = "\n";
            xout[5]  = "\n";
            xout[6]  = "\n";
            xout[7]  = "\n";
            xout[8]  = "\n";
            xout[9]  = "\n";
            xout[10] = "\n";
            xout[11] = "  Program başarısız\n";
            xout[12] = "\n";
            xout[13] = "\n";
            xout[14] = "\n";
            xout[15] = "\n";
            xout[16] = "\n";
            testDone = 1;
            displayAll(3);
            Toast.makeText(mContext, "Başarısız", Toast.LENGTH_LONG).show();
        }
    }
}
