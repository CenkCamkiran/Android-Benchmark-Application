package com.example.androidbenchmarkapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.opengl.EGLConfig;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem tabCPU, tabGPU, tabIO, tabRAM;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;


    //Storage Permission
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("İZİN","Permission is granted");
                return true;
            } else {

                Log.v("İZİN","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("İZİN","Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
        tabCPU = findViewById(R.id.tabCPU);
        tabGPU = findViewById(R.id.tabGPU);
        tabIO = findViewById(R.id.tabIO);
        tabRAM = findViewById(R.id.tabRAM);
        viewPager = findViewById(R.id.viewPager);

        isStoragePermissionGranted();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_red_light));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_red_light));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                android.R.color.holo_red_light));
                    }
                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_green_light));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_green_light));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                android.R.color.holo_green_light));
                    }
                } else if (tab.getPosition() == 3) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_blue_bright));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_blue_bright));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                android.R.color.holo_blue_bright));
                    }
                }

                else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_orange_light));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            android.R.color.holo_orange_light));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
                                android.R.color.holo_orange_light));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}

