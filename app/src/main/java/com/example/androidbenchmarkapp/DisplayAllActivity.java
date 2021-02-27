/*
 * Based of some of the following:
 *
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *************************************************************************
 *
 * Copyright (c) 2010, Lauren Darcey and Shane Conder
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of
 *   conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list
 *   of conditions and the following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * <ORGANIZATION> = Mamlambo
 */

package com.example.androidbenchmarkapp;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Display;

import android.content.Intent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;


public class DisplayAllActivity extends Activity
{
    private long startTime;
    private static long endTime;
    private static double testTime;
    private static double maxTime = 10;
    private static double[] test_time = new double[100];
    private static double[] frames_ps = new double[100];
    private static int[] no_frames = new int[100];

    private String results;
    int frameCount = 0;
    int testCount = 0;
    int firstCount = 0;
    int maxTests = 12;
    int tunnel = 1;
    int wallPart;

    int rev = 0;
    int kk = 5;
    float gg = 0.2f;
    float gi = 0;
    float hh;

    float ww = 1.0f;
    float ll = 0.0625f; // 0.03125f; = 1/32

    private static int pixWide;
    private static int pixHigh;
    private boolean dowire = false;
    private boolean doTunnel = true;
    private boolean doTextures = true;

    Random randNum = new Random();

    CustomSurfaceView mAndroidSurface = null;

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mAndroidSurface != null)
        {
            mAndroidSurface.onPause();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mAndroidSurface != null)
        {
            mAndroidSurface.onResume();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        frameCount = 0;
        testCount = 0;
        firstCount = 0;
        gi = -4.0f;
        hh = 2f;

        WindowManager wm = getWindowManager();
        Display dp = wm.getDefaultDisplay();
        pixWide = dp.getWidth();
        pixHigh = dp.getHeight();

        if (pixWide < pixHigh) hh = 1f;

        randNum.setSeed(999);

        startTime = System.currentTimeMillis();

        mAndroidSurface = new CustomSurfaceView(this);
        setContentView(mAndroidSurface);
    }

    private class CustomSurfaceView extends GLSurfaceView
    {
        CustomRenderer mRenderer;

        public CustomSurfaceView(Context context) {
            super(context);
            mRenderer = new CustomRenderer(context);
            setEGLContextClientVersion(1);
            setRenderer(mRenderer);
        }
    }

    private class CustomRenderer implements GLSurfaceView.Renderer
    {
        @SuppressWarnings("unused")
        private static final String DEBUG_TAG = "ShowAndroidGLActivity$CustomRenderer";

        boolean fAnimPaused = false;
        float mRotateAngle = 0f;

        float r1;
        float r2;
        float r3;
        float re;
        float gr;
        float bl;
        float t1;
        float t2;
        float t3;
        float nf;
        float ng;

        private float fj[] =
                { 1f,  1f, -1f,  1f,
                        1f, -1f, -1f, -1f,
                };

        private Context context;

        CustomRenderer(Context context)
        {
            super();
            this.context = context;
        }

        public void onDrawFrame(GL10 gl)
        {
            float fps;
            int i, j, k;

            gl.glClearColor(0.7f, 1.0f, 1.0f ,1.0f);

            if (fAnimPaused)
            {
                return;
            }
            frameCount = frameCount + 1;

            if (testCount == 0 || testCount == 4  || testCount == 8)
            {
                dowire     = true;
                doTunnel   = false;
                doTextures = false;
            }
            if (frameCount == 1 && firstCount == 0)
            {
                // kick start and avoid timing initial overheads
                dowire     = false;
                doTunnel   = true;
                doTextures = true;
                firstCount = 1;
                frameCount = 0;
            }
            if (testCount == 1 || testCount == 5  || testCount == 9)
            {
                dowire     = false;
                doTunnel   = false;
                doTextures = false;
            }
            if (testCount == 2 || testCount == 6  || testCount == 10)
            {
                dowire     = false;
                doTunnel   = true;
                doTextures = false;
            }
            if (testCount == 3 || testCount == 7 || testCount == 11)
            {
                dowire     = false;
                doTunnel   = true;
                doTextures = true;
            }

            if (testCount == 0 || testCount == 1 || testCount == 2 || testCount == 3)
            {
                kk = 15;
            }
            if (testCount == 4 || testCount == 5 || testCount == 6 || testCount == 7)
            {
                kk = 30;
            }
            if (testCount == 8 || testCount == 9 || testCount == 10 || testCount == 11)
            {
                kk = 60;
            }
            if (rev == 0)
            {
                gi = gi + gg;
            }
            else
            {
                gi = gi - gg;
            }
            if (gi > 8.7f)
            {
                rev = 1;
            }
            if (gi < 0)
            {
                rev = 0;
            }

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_MODELVIEW);


            if (frameCount == 1)
            {
                r1 = randNum.nextFloat() * 1f;
                r2 = randNum.nextFloat() * 1f;
                r3 = randNum.nextFloat() * 1f;
            }

            j = 0;
            for (i=0; i<50; i++)
            {
                nf = fj[j];
                ng = fj[j+1];
                j = j + 2;
                if (j > 7) j = 0;

                re = randNum.nextFloat() * 1f;
                gr = randNum.nextFloat() * 1f;
                bl = randNum.nextFloat() * 1f;

                t1 = randNum.nextFloat() * 30f*nf;
                t2 = randNum.nextFloat() * 15f*ng;
                t3 = randNum.nextFloat() * 10f - 10f + gi;

                for (k=0; k<kk; k++)
                {

                    if (doTextures)
                    {
                        gl.glLoadIdentity();
                        gl.glTranslatef (t1, t2, t3);
                        gl.glRotatef(mRotateAngle, r1, r2, r3);
                        gl.glColor4f(re, gr, bl, 1f);
                        texturedCube1.draw(gl);
                    }
                    else
                    {
                        gl.glLoadIdentity();
                        gl.glTranslatef (t1, t2, t3);
                        gl.glRotatef(mRotateAngle, r1, r2, r3);
                        gl.glColor4f(re, gr, bl, 1f);
                        acube.draw(gl);
                    }
                }

            }

            int ii = 0;

            float q =  0.6f;
            float r = -0.6f*hh;
            float ic;
            float pass = 1f;

            for (i=1; i<13; i=i+4)
            {
                ic = (float)i/3;
                if (doTextures)
                {
                    gl.glLoadIdentity();
                    gl.glTranslatef(r, q, ic+gi);
                    gl.glRotatef(mRotateAngle*3, 0f, 0f, pass);
                    if (!dowire) gl.glColor4f(1.0f, 0.8f, 0.8f, 1.0f);
                    if ( dowire) gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    if (pass == 1f) texturedFlyer1.draw(gl);
                    if (pass ==-1f) texturedFlyer2.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(-r, q, ic+1.0f+gi);
                    gl.glRotatef(mRotateAngle*2, 0f, 0f, -pass);
                    if (pass == 1f) texturedFlyer2.draw(gl);
                    if (pass ==-1f) texturedFlyer1.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(r, -q, ic+2.0f+gi);
                    gl.glRotatef(mRotateAngle*5, 0f, 0f, pass);
                    if (pass == 1f) texturedFlyer2.draw(gl);
                    if (pass ==-1f) texturedFlyer1.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(-r, -q, ic+3.0f+gi);
                    gl.glRotatef(mRotateAngle*5, 0f, 0f, -pass);
                    if (pass == 1f) texturedFlyer1.draw(gl);
                    if (pass ==-1f) texturedFlyer2.draw(gl);
                }
                else
                {
                    gl.glEnable(GL10.GL_COLOR_MATERIAL);
                    gl.glLoadIdentity();
                    gl.glTranslatef(r, q, ic+gi);
                    gl.glRotatef(mRotateAngle*5, 0.0f, 0.0f, pass);
                    if ( dowire) gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    if (!dowire && pass == 1f) gl.glColor4f(1.0f, 0.4f, 0.4f, 1.0f);
                    if (!dowire && pass ==-1f) gl.glColor4f(0.2f, 0.6f, 0.2f, 1.0f);
                    aflyer.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(-r, q, ic+1.0f+gi);
                    gl.glRotatef(mRotateAngle*7, 0.0f, 0.0f, -pass);
                    if ( dowire) gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                    if (!dowire && pass == 1f) gl.glColor4f(0.2f, 0.6f, 0.2f, 1.0f);
                    if (!dowire && pass ==-1f) gl.glColor4f(1.0f, 0.4f, 0.4f, 1.0f);
                    aflyer.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(r, -q, ic+2.0f+gi);
                    gl.glRotatef(mRotateAngle*6, 0.0f, 0.0f, pass);
                    if ( dowire) gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
                    if (!dowire && pass == 1f) gl.glColor4f(0.3f, 0.3f, 1.0f, 1.0f);
                    if (!dowire && pass ==-1f) gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);
                    aflyer.draw(gl);

                    gl.glLoadIdentity();
                    gl.glTranslatef(-r, -q, ic+3.0f+gi);
                    gl.glRotatef(mRotateAngle*8, 0.0f, 0.0f, -pass);
                    if ( dowire) gl.glColor4f(0.5f, 0.0f, 0.5f, 1.0f);
                    if (!dowire && pass == 1f) gl.glColor4f(0.3f, 0.3f, 1.0f, 1.0f);
                    if (!dowire && pass ==-1f) gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);
                    aflyer.draw(gl);
                }
                if (pass == 1)
                {
                    pass = -1;
                }
                else
                {
                    pass = 1;
                }
            }
            if (doTunnel)
            {
                for (tunnel=1; tunnel<5; tunnel++)
                {
                    for (i=0; i<16; i++)
                    {
                        float dd = ((float) i) / 8;

                        gl.glEnable(GL10.GL_COLOR_MATERIAL);
                        gl.glLoadIdentity();
                        gl.glTranslatef(0f, 0f, gi+11+dd);
                        setColor (gl,tunnel, ii);
                        drawSquare(gl);

                        gl.glLoadIdentity();
                        gl.glTranslatef(0f, 0f, gi+9+dd);
                        setColor (gl, tunnel, ii);
                        drawSquare(gl);

                        gl.glLoadIdentity();
                        gl.glTranslatef(0f, 0f, gi+7+dd);
                        setColor (gl, tunnel, ii);
                        drawSquare(gl);

                        gl.glLoadIdentity();
                        gl.glTranslatef(0f, 0f, gi+5+dd);
                        setColor (gl, tunnel, ii);
                        drawSquare(gl);

                        gl.glLoadIdentity();
                        gl.glTranslatef(0f, 0f, gi+3+dd);
                        setColor (gl, tunnel, ii);
                        drawSquare(gl);

                        if (ii == 0)
                        {
                            ii = 1;
                        }
                        else
                        {
                            ii = 0;
                        }

                    }
                }
            }

            mRotateAngle += 2.0f;

            endTime = System.currentTimeMillis();
            testTime = (float)(endTime - startTime) / 1000.0;

            fps = (float)frameCount / (float)testTime;

            if (frameCount == 0)
            {
                testTime = 0;
                startTime = System.currentTimeMillis();
            }
            if (testTime > maxTime)
            {
                no_frames[testCount] = frameCount;
                test_time[testCount] = testTime;
                frames_ps[testCount] = fps;
                testCount = testCount + 1;
                startTime = System.currentTimeMillis();
                frameCount = 0;
                gi = 0;
                rev = 0;
                if (testCount == maxTests)
                {
                    results = String.format("           --------- Frames Per Second --------\n" +
                                    " Triangles WireFrame   Shaded  Shaded+ Textured\n" +
                                    " \n" +
                                    "   9000+    %7.2f  %7.2f  %7.2f  %7.2f\n" +
                                    "  18000+    %7.2f  %7.2f  %7.2f  %7.2f\n" +
                                    "  36000+    %7.2f  %7.2f  %7.2f  %7.2f\n\n" +
                                    "      Screen Pixels %d Wide %d High\n",
                            frames_ps[0], frames_ps[1], frames_ps[2], frames_ps[3],
                            frames_ps[4], frames_ps[5], frames_ps[6], frames_ps[7],
                            frames_ps[8], frames_ps[9], frames_ps[10], frames_ps[11],
                            pixWide, pixHigh);
                    Intent in = new Intent();
                    in.putExtra("OPENGL1", results);
                    setResult(1,in);
                    finish();
                }
            }
        }

        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            gl.glViewport(0, 0, width, height);

            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 50);
            GLU.gluLookAt(gl, 0, 0, 10f, 0, 0, 0, 0, 1, 0f);

            gl.glEnable(GL10.GL_LIGHTING);
            gl.glEnable(GL10.GL_LIGHT0);

            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[] { 0.1f, 0.1f, 0.1f, 1f }, 0);
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE,  new float[] { 1f, 1f, 1f, 1f }, 0);
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION,  new float[] { 0f, 0f, 10f, 1f }, 0);


            gl.glEnable(GL10.GL_COLOR_MATERIAL);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glDisable(GL10.GL_DITHER);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glEnable(GL10.GL_DEPTH_TEST);
//            gl.glEnable(GL10.GL_CULL_FACE);

            texturedFlyer1  = new texturedFlyer1(2f);
            texturedFlyer2  = new texturedFlyer2(2f);
            texturedSquare1 = new texturedSquare1(2f);
            texturedSquare2 = new texturedSquare2(2f);
            texturedSquare3 = new texturedSquare3(2f);
            texturedCube1   = new texturedCube1(2f);
            aflyer = new Aflyer(2f);
            aSquare = new ASquare(2f);
            acube = new Acube(2f);

            gl.glEnable(GL10.GL_TEXTURE_2D);

            gl.glActiveTexture(GL10.GL_TEXTURE0);

            int[] texture1 = new int[1];
            gl.glGenTextures(1, texture1, 0);
            texturedFlyer1.setTexture(gl, context, texture1[0], R.raw.opengl);

            int[] texture2 = new int[1];
            gl.glGenTextures(1, texture2, 0);
            texturedFlyer2.setTexture(gl, context, texture2[0], R.raw.android);

            int[] texture3 = new int[1];
            gl.glGenTextures(1, texture3, 0);
            texturedSquare1.setTexture(gl, context, texture3[0], R.raw.pillar);

            int[] texture4 = new int[1];
            gl.glGenTextures(1, texture4, 0);
            texturedSquare2.setTexture(gl, context, texture4[0], R.raw.ceiling);

            int[] texture5 = new int[1];
            gl.glGenTextures(1, texture5, 0);
            texturedSquare3.setTexture(gl, context, texture5[0], R.raw.floor);

            int[] texture6 = new int[1];
            gl.glGenTextures(1, texture6, 0);
            texturedCube1.setTexture(gl, context, texture6[0], R.raw.floor);
        }
        texturedFlyer1 texturedFlyer1;
        texturedFlyer2 texturedFlyer2;
        texturedSquare1 texturedSquare1;
        texturedSquare2 texturedSquare2;
        texturedSquare3 texturedSquare3;
        texturedCube1   texturedCube1;

        Aflyer aflyer;
        ASquare aSquare;
        Acube acube;

        public class Aflyer
        {
            private FloatBuffer mvertexFlyer;
            private FloatBuffer mNormalBuffer;
            private ByteBuffer mIndexBuffer;

            Aflyer(float size)
            {
                for (int vertex = 0; vertex < flyer.length; vertex++ )
                {
                    flyer[vertex] *= size;
                }

                mvertexFlyer = getFloatBufferFromFloatArray(flyer);
                mNormalBuffer = getFloatBufferFromFloatArray(normalsf);
                mIndexBuffer = getByteBufferFromByteArray(indicesf);

            }

            float ff = 0.15f;
            float flyer[] =
                    {
                            -ff,   ff,   ff,
                            ff,   ff,   ff,
                            ff,  -ff,   ff,
                            -ff,  -ff,   ff,
                    };

            byte indicesf[] = { 0, 1, 2, 2, 3, 0};


            float normalsf[] =
                    {
                            0, 0, 1,
                            0, 0, 1,
                            0, 0, 1,
                            0, 0, 1,
                    };


            void draw(GL10 gl)
            {
                gl.glFrontFace(GL10.GL_CW);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mvertexFlyer);
                gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
                if (dowire)
                {
                    gl.glDrawElements(GL10.GL_LINE_LOOP, indicesf.length,
                            GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
                }
                else
                {
                    gl.glDrawElements(GL10.GL_TRIANGLES, indicesf.length,
                            GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
                }

            }
        }


        public class texturedFlyer1 extends Aflyer
        {
            texturedFlyer1(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {

                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);
                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapf = Bitmap.createScaledBitmap(bitmap, 128, 128, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapf, 0);
                bitmap.recycle();
                bitmapf.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);

                if (!dowire && doTextures) textureEnabled = true;

            }

            public void draw(GL10 gl)
            {

                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }

            }
        }

        public class texturedFlyer2 extends Aflyer
        {

            texturedFlyer2(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {
                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);

                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapAndroid = Bitmap.createScaledBitmap(bitmap, 128, 128, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapAndroid, 0);
                bitmap.recycle();
                bitmapAndroid.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);

                if (!dowire && doTextures) textureEnabled = true;
            }

            public void draw(GL10 gl)
            {
                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }
            }

        }

        public class Acube
        {
            private FloatBuffer vertexBuffer;
            private ByteBuffer indexBuffer;
            private FloatBuffer normalBuffer;

            Acube(float size)
            {
                if (size != 1.0f)
                {
                    for (int vertex = 0; vertex < vertices.length; vertex++ )
                    {
                        vertices[vertex] *= size;
                    }
                }

                vertexBuffer = getFloatBufferFromFloatArray(vertices);
                indexBuffer   = getByteBufferFromByteArray(indices);
                normalBuffer = getFloatBufferFromFloatArray(normals);
            }

            private float vertices[] =
                    {
                            -0.5f, 0.5f, 0.5f,   0.5f, 0.5f, 0.5f,   0.5f,-0.5f, 0.5f,  -0.5f,-0.5f, 0.5f,
                            0.5f, 0.5f,-0.5f,  -0.5f, 0.5f,-0.5f,  -0.5f,-0.5f,-0.5f,   0.5f,-0.5f,-0.5f,
                            -0.5f, 0.5f,-0.5f,   0.5f, 0.5f,-0.5f,   0.5f, 0.5f, 0.5f,  -0.5f, 0.5f, 0.5f,
                            -0.5f,-0.5f, 0.5f,   0.5f,-0.5f, 0.5f,   0.5f,-0.5f,-0.5f,  -0.5f,-0.5f,-0.5f,
                            0.5f, 0.5f, 0.5f,   0.5f, 0.5f,-0.5f,   0.5f,-0.5f,-0.5f,   0.5f,-0.5f, 0.5f,
                            -0.5f, 0.5f,-0.5f,  -0.5f, 0.5f, 0.5f,  -0.5f,-0.5f, 0.5f,  -0.5f,-0.5f,-0.5f,
                    };

            private byte indices[] =
                    {
                            0, 1, 2, 2,  3, 0,
                            4, 5, 6, 6,  7, 4,
                            8, 9,10, 10,11, 8,
                            12,13,14, 14,15,12,
                            16,17,18, 18,19,16,
                            20,21,22, 22,23,20,
                    };

            float normals[] =
                    {
                            0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1,
                            0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,
                            0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0,
                            0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0,
                            1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0,
                            -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0,
                    };

            void draw(GL10 gl)
            {
                gl.glFrontFace(GL10.GL_CW);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
                gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
                if (dowire)
                {
                    gl.glDrawElements(GL10.GL_LINE_LOOP, indices.length,
                            GL10.GL_UNSIGNED_BYTE, indexBuffer);
                }
                else
                {
                    gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                            GL10.GL_UNSIGNED_BYTE, indexBuffer);
                }
            }
        }

        public class texturedCube1 extends Acube
        {
            texturedCube1(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                            1,0, 1,1, 0,1, 0,0,
                            1,0, 1,1, 0,1, 0,0,
                            1,0, 1,1, 0,1, 0,0,
                            1,0, 1,1, 0,1, 0,0,
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {

                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);
                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapf = Bitmap.createScaledBitmap(bitmap, 128, 128, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapf, 0);
                bitmap.recycle();
                bitmapf.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);

                if (!dowire && doTextures) textureEnabled = true;

            }

            public void draw(GL10 gl)
            {

                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }

            }
        }


        public class ASquare
        {
            private FloatBuffer vertexBsq1a;
            private FloatBuffer vertexBsq2a;
            private FloatBuffer vertexBsq3a;
            private FloatBuffer vertexBsq4a;
            private ByteBuffer  indexBsq1a;
            private FloatBuffer mNormalBuffsqa;

            ASquare(float size)
            {
                for (int vertex = 0; vertex < vertsq1a.length; vertex++ )
                {
                    vertsq1a[vertex] *= size;
                    vertsq2a[vertex] *= size;
                    vertsq3a[vertex] *= size;
                    vertsq4a[vertex] *= size;
                }
                vertexBsq1a = getFloatBufferFromFloatArray(vertsq1a);
                vertexBsq2a = getFloatBufferFromFloatArray(vertsq2a);
                vertexBsq3a = getFloatBufferFromFloatArray(vertsq3a);
                vertexBsq4a = getFloatBufferFromFloatArray(vertsq4a);

                indexBsq1a  = getByteBufferFromByteArray(indsq1a);
                mNormalBuffsqa = getFloatBufferFromFloatArray(normalsqa);
            }

            private float vertsq1a[] =
                    {
                            hh,   ww, - ll,  // 0, // Right Wall
                            hh, - ww, - ll,  // 1,
                            hh, - ww,   ll,  // 2,
                            hh,   ww,   ll,  // 3,
                    };

            private float vertsq2a[] =
                    {
                            -hh,   ww, - ll,  // Left Wall
                            -hh, - ww, - ll,
                            -hh, - ww,   ll,
                            -hh,   ww,   ll,
                    };

            private float vertsq3a[] =
                    {
                            hh,   ww, - ll,  // Top
                            -hh,   ww, - ll,
                            -hh,   ww,   ll,
                            hh,   ww,   ll,
                    };

            private float vertsq4a[] =
                    {
                            hh, - ww, - ll,  // Bottom
                            -hh, - ww, - ll,
                            -hh, - ww,   ll,
                            hh, - ww,   ll,
                    };

            private byte indsq1a[] = { 0, 1, 2, 0, 2, 3 };

            float normalsqa[] =
                    {
                            0, 0, 1,
                            0, 0, 1,
                            0, 0, 1,
                            0, 0, 1,
                    };


            void draw(GL10 gl)
            {
                if (tunnel == 1) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq1a);
                if (tunnel == 2) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq2a);
                if (tunnel == 3) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq3a);
                if (tunnel == 4) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq4a);

                gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffsqa);
                gl.glDrawElements(GL10.GL_TRIANGLES, indsq1a.length,
                        GL10.GL_UNSIGNED_BYTE, indexBsq1a);
            }

        }

        public class texturedSquare1  extends ASquare
        {

            texturedSquare1(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {
                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);

                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapAndroid = Bitmap.createScaledBitmap(bitmap, 40, 160, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapAndroid, 0);
                bitmap.recycle();
                bitmapAndroid.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);
                if (!dowire && doTextures) textureEnabled = true;
            }

            public void draw(GL10 gl)
            {
                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }
            }

        }

        public class texturedSquare2  extends ASquare
        {

            texturedSquare2(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {
                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);

                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapAndroid = Bitmap.createScaledBitmap(bitmap, 40, 160, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapAndroid, 0);
                bitmap.recycle();
                bitmapAndroid.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);
                if (!dowire && doTextures) textureEnabled = true;
            }

            public void draw(GL10 gl)
            {
                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }
            }

        }

        public class texturedSquare3  extends ASquare
        {

            texturedSquare3(float size) throws IllegalArgumentException
            {
                super(size);
            }

            FloatBuffer mCoordinateBuffer;
            float textureCoords[] =
                    {
                            1,0, 1,1, 0,1, 0,0,
                    };

            int mTextureID;
            boolean textureEnabled = false;

            public void setTexture (GL10 gl, Context c, int textureID, int drawableID)
            {
                mCoordinateBuffer = getFloatBufferFromFloatArray(textureCoords);

                mTextureID = textureID;

                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

                Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), drawableID);
                Bitmap bitmapAndroid = Bitmap.createScaledBitmap(bitmap, 40, 160, false);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapAndroid, 0);
                bitmap.recycle();
                bitmapAndroid.recycle();

                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                        GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER,
                        GL10.GL_LINEAR);
                if (!dowire && doTextures) textureEnabled = true;
            }

            public void draw(GL10 gl)
            {
                if (textureEnabled)
                {
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordinateBuffer);
                }

                super.draw(gl);

                if (textureEnabled)
                {
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glDisable(GL10.GL_TEXTURE_2D);
                }
            }
        }

        private void drawSquare(GL10 gl)
        {
            if (tunnel == 1) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq1);
            if (tunnel == 2) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq2);
            if (tunnel == 3) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq3);
            if (tunnel == 4) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBsq4);
            if (dowire)
            {
                gl.glDrawElements(GL10.GL_LINE_LOOP, indsq1.length,
                        GL10.GL_UNSIGNED_BYTE, indexBsq1);
            }
            else
            {
                gl.glDrawElements(GL10.GL_TRIANGLES, indsq1.length,
                        GL10.GL_UNSIGNED_BYTE, indexBsq1);
            }
        }
        private float vertsq1[] =
                {
                        hh,   ww, - ll/3,  // 0, // Right Wall
                        hh, - ww, - ll/3,  // 1,
                        hh, - ww,   ll/3,  // 2,
                        hh,   ww,   ll/3,  // 3,

                };

        private float vertsq2[] =
                {
                        -hh,   ww, - ll/3,  // Left Wall
                        -hh, - ww, - ll/3,
                        -hh, - ww,   ll/3,
                        -hh,   ww,   ll/3,

                };

        private float vertsq3[] =
                {
                        hh,   ww, - ll/3,  // Top
                        -hh,   ww, - ll/3,
                        -hh,   ww,   ll/3,
                        hh,   ww,   ll/3,

                };

        private float vertsq4[] =
                {
                        hh, - ww, - ll,  // Bottom
                        -hh, - ww, - ll,
                        -hh, - ww,   ll,
                        hh, - ww,   ll,

                };


        private byte indsq1[] = { 0, 1, 2, 0, 2, 3 };

        FloatBuffer vertexBsq1 = getFloatBufferFromFloatArray(vertsq1);
        FloatBuffer vertexBsq2 = getFloatBufferFromFloatArray(vertsq2);
        FloatBuffer vertexBsq3 = getFloatBufferFromFloatArray(vertsq3);
        FloatBuffer vertexBsq4 = getFloatBufferFromFloatArray(vertsq4);
        ByteBuffer indexBsq1   = getByteBufferFromByteArray(indsq1);
    }

    private void setColor (GL10 gl, int tunnel, int ii)
    {
        if (ii == 0)
        {
            if (tunnel == 1) gl.glColor4f(1.0f, 0.4f, 0.0f, 1f);
            if (tunnel == 2) gl.glColor4f(1.0f, 0.4f, 0.0f, 1f);
            if (tunnel == 3) gl.glColor4f(0.0f, 0.4f, 1.0f, 1f);
            if (tunnel == 4) gl.glColor4f(0.0f, 0.8f, 0.0f, 1f);
        }
        else
        {
            if (tunnel == 1) gl.glColor4f(1.0f, 0.7f, 0.0f, 1f);
            if (tunnel == 2) gl.glColor4f(1.0f, 0.7f, 0.0f, 1f);
            if (tunnel == 3) gl.glColor4f(0.0f, 0.6f, 1.0f, 1f);
            if (tunnel == 4) gl.glColor4f(0.0f, 0.6f, 0.0f, 1f);
        }
    }

    ByteBuffer getByteBufferFromByteArray(byte array[])
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(array.length);
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

    FloatBuffer getFloatBufferFromFloatArray(float array[])
    {
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(array.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = tempBuffer.asFloatBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

}
