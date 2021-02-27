package com.example.androidbenchmarkapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;

import android.view.View;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.Matrix;
import android.graphics.LinearGradient;
import android.graphics.SweepGradient;
import android.graphics.Shader;

import android.util.Log;
import android.util.DisplayMetrics;

import java.util.Random;

public class DisplayAllActivity2 extends Activity
{
    private String results;
    Paint circlePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private MyView mMyView;

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mMyView = new MyView(this);
        setContentView(mMyView);

    }

    class MyView extends SurfaceView implements SurfaceHolder.Callback
    {
        private Bitmap mbitmap;
        Context context;
        MyThread  mythread;
        private int frames = 0;
        private String msg;
        private double startTime;
        private double runTime = 0;
        private double fps;
        private int test = 1;
        private double[] frames_ps = new double[100];
        private int[] no_frames = new int[100];

        private int updown = 1;
        private int pos  = 20;
        private int pixWide;
        private int pixHigh;
        private int pixMin;
        private int ud2  = 3;
        private int circ = 120;
        private int grn = 0;
        private int gch = 3;
        Paint apaint = new Paint();

        DisplayMetrics metrics;

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint circlePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint circlePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint circlePaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint circlePaint4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        SweepGradient sweepGrad1;
        SweepGradient sweepGrad2;
        Typeface mType;


        boolean mRun;

        Random randNum = new Random();
        SurfaceHolder holder = getHolder();

        public MyView(Context ctx)
        {
            super(ctx);
            context = ctx;
            metrics = context.getResources().getDisplayMetrics();
            pixHigh = metrics.heightPixels;
            pixWide = metrics.widthPixels;
            pixMin  = pixWide;
            if (pixHigh < pixWide) pixMin  = pixHigh;
            mType = Typeface.create(Typeface.MONOSPACE,  Typeface.NORMAL);
            mPaint.setTextSize(24);
            mPaint.setColor(Color.WHITE);
            mPaint.setTypeface(mType);
            randNum.setSeed(999);
            startTime = (double)System.currentTimeMillis();
            holder.addCallback(this);
        }
        public void surfaceCreated(SurfaceHolder holder)
        {
            mythread = new MyThread(holder, context, this);
            mythread.setRunning(true);
            mythread.start();
        }

        public void surfaceDestroyed(SurfaceHolder holder)
        {
            mythread.setRunning(false);
            boolean retry = true;
            while(retry)
            {
                try
                {
                    mythread.join();
                    retry = false;
                }
                catch(Exception e)
                {
                    Log.v("Exception Occured", e.getMessage());
                }
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
        {

        }
        public class MyThread extends Thread
        {
            Canvas mcanvas;
            SurfaceHolder surfaceHolder;
            Context context;
            MyView mView;

            public MyThread(SurfaceHolder sholder, Context ctx, MyView spanel)
            {
                surfaceHolder = sholder;
                context = ctx;
                mRun = false;
                mView = spanel;
            }

            void setRunning(boolean bRun)
            {
                mRun = bRun;
            }

            @Override
            public void run()
            {
                super.run();
                while(mRun)
                {
                    mcanvas = surfaceHolder.lockCanvas();
                    if(mcanvas != null)
                    {
                        mView.doDraw(mcanvas);
                        surfaceHolder.unlockCanvasAndPost(mcanvas);
                    }
                }

            }
        }

        void doDraw(Canvas canvas)
        {
            int i;
            canvas.drawColor(Color.rgb(0, grn, 255));
            grn = grn + gch;
            if (grn > 254) gch = -3;
            if (grn < 1) gch =  3;

            frames = frames + 1;

            if (test > 2)
            {
                LinearGradient linGrad = new LinearGradient(0, 0, 5, 5,  Color.WHITE, Color.BLACK,  Shader.TileMode.MIRROR);
                int c2 = 5;
                circlePaint3.setShader(linGrad);
                for (i=0; i<50; i++)
                {
                    int r1 = (int)(randNum.nextFloat() * (float)pixMin / 4);
                    int r2 = (int)(randNum.nextFloat() * (float)pixMin / 4);
                    canvas.drawCircle(canvas.getWidth() / 2 + r1,  canvas.getHeight() / 2 + r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 - r1,  canvas.getHeight() / 2 - r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 + r1,  canvas.getHeight() / 2 - r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 - r1,  canvas.getHeight() / 2 + r2, c2, circlePaint3);
                }
            }

            if (test > 4)
            {
                LinearGradient linGrad2 = new LinearGradient(0, 0, 5, 5,  Color.WHITE, Color.BLACK,  Shader.TileMode.MIRROR);
                int c22 = 5;
                circlePaint4.setShader(linGrad2);
                for (i=0; i<1000; i++)
                {
                    int r12 = (int)(randNum.nextFloat() * (float)canvas.getWidth() / 2);
                    int r22 = (int)(randNum.nextFloat() * (float)canvas.getHeight() / 2);
                    canvas.drawCircle(canvas.getWidth() / 2 + r12,  canvas.getHeight() / 2 + r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 - r12,  canvas.getHeight() / 2 - r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 + r12,  canvas.getHeight() / 2 - r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 - r12,  canvas.getHeight() / 2 + r22, c22, circlePaint4);
                }
            }
            float ih;
            float iw;
            if (test > 3)
            {
                for (i=0; i<80; i++)
                {
                    ih = (Float)(canvas.getHeight() / 80 * (float)i);
                    iw = (Float)(canvas.getWidth() / 80 * (float)i);
                    apaint.setColor(Color.rgb(grn, 0, 0));
                    canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), (int)ih, apaint);
                    canvas.drawLine(canvas.getWidth() / 2, 0, (int)iw, canvas.getHeight(), apaint);
                    apaint.setColor(Color.rgb(255, grn, 0));
                    canvas.drawLine(canvas.getWidth(), canvas.getHeight() / 2, 0, (int)ih, apaint);
                    canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight(), (int)iw, 0, apaint);
                }
            }

            if (test > 0)
            {
                mbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bground);
                canvas.drawBitmap(mbitmap, pos/2, 50, null); // pos/4
                Matrix mirrorAndTilt30 = new Matrix();
                mirrorAndTilt30.preRotate(pos);          // pos/2
                mirrorAndTilt30.preScale(1, 1);
                Bitmap mirrorPic = Bitmap.createBitmap(mbitmap, 0, 0,  mbitmap.getWidth(),
                        mbitmap.getHeight(), mirrorAndTilt30, false);
                canvas.drawBitmap(mirrorPic, pixWide - mbitmap.getWidth() * 5 / 4 - pos/5, 50, null);
                mbitmap.recycle();
            }

            if (test > 1)
            {
                int c1 = pixMin / 6;
                sweepGrad1 = new  SweepGradient (canvas.getWidth()-circ,  canvas.getHeight()-circ,  new int[]
                        {
                                Color.RED, Color.YELLOW, Color.GREEN,  Color.BLUE, Color.MAGENTA
                        }, null);
                circlePaint1.setShader(sweepGrad1);
                canvas.drawCircle(canvas.getWidth()-circ,  canvas.getHeight()-circ, c1,  circlePaint1);


                sweepGrad2 = new  SweepGradient (circ,  canvas.getHeight()-circ,  new int[]
                        {
                                Color.GREEN, Color.RED, Color.BLUE,  Color.YELLOW, Color.MAGENTA
                        }, null);
                circlePaint2.setShader(sweepGrad2);
                canvas.drawCircle(circ, canvas.getHeight()-circ, c1,  circlePaint2);

                circ = circ + ud2;
                if (circ > c1 * 3) ud2 = -3;
                if (circ < c1) ud2 = 3;
            }

            runTime = ((double)System.currentTimeMillis() - startTime) / 1000;
            fps = (double)frames / runTime;
            msg = String.format(" %6.2f FPS, %d Frames", fps, frames);
            canvas.drawText(msg, 10, 30, mPaint);

            pos = pos + 1 * updown;
            if (pos > pixMin) updown = -1;
            if (pos < 20 ) updown = 1;

            if (runTime > 10)
            {
                no_frames[test] = frames;
                frames_ps[test] = fps;
                frames = 0;
                runTime = 0;
                startTime = (double)System.currentTimeMillis();
                test = test + 1;
                if (test == 6)
                {
                    test = 1;
                    mRun = false;

                    results = String.format(" Test                            Frames     FPS\n"+
                                    "\n"+
                                    " Display PNG Bitmap Twice       %6d  %7.2f\n"+
                                    " Plus 2 SweepGradient Circles   %6d  %7.2f\n"+
                                    " Plus 200 Random Small Circles  %6d  %7.2f\n"+
                                    " Plus 320 Long Lines            %6d  %7.2f\n"+
                                    " Plus 4000 Random Small Circles %6d  %7.2f\n"+
                                    "\n"+
                                    "      Screen pixels %d Wide %d High\n",
                            no_frames[1], frames_ps[1],
                            no_frames[2], frames_ps[2],
                            no_frames[3], frames_ps[3],
                            no_frames[4], frames_ps[4],
                            no_frames[5], frames_ps[5],
                            pixWide, pixHigh);
                    Intent in = new Intent();
                    in.putExtra("JavaDraw", results);
                    setResult(2,in);
                    finish();
                }
            }

        }
    }
}



