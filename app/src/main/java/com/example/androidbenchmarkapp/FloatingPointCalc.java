package com.example.androidbenchmarkapp;

import android.util.Log;

public class FloatingPointCalc {

    public int valueDeg;
    public int valueRad;
    public int A,N; //A sayı, N n.inci karekökü

    public double Degree_ToRadian()
    {
        double start = 0, finish = 0, elapsedTime = 0;
        int choice = (int)(Math.random() * 2); //0 ile 1 arası seçim yapacak
        double result;
        //0 çıkarsa negatif sayı üret, 1 çıkarsa pozitif sayı üret

        if (choice == 1)
        {
            valueDeg = (int) ((Math.random() * 100000) + 100000);

            start = System.nanoTime();
            result = Math.toRadians(valueDeg);
            finish = System.nanoTime();

            elapsedTime = (finish - start) / 1000000;
        }

        else if (choice == 0)
        {
            valueDeg = (int) ((Math.random() * -100000) - 100000);

            start = System.nanoTime();
            result = Math.toRadians(valueDeg);
            finish = System.nanoTime();

            elapsedTime = (finish - start) / 1000000;
        }

        //Log.d("!!!!!!!!!!!!!!!!!", Double.toString(elapsedTime));
        return elapsedTime;
    }

    public double Radian_ToDegree()
    {
        double start, finish, elapsedTime = 0;
        int choice = (int)(Math.random() * 2); //0 ile 1 arası seçim yapacak
        double result;
        //0 çıkarsa negatif sayı üret, 1 çıkarsa pozitif sayı üret

        if (choice == 1)
        {
            valueRad = (int) ((Math.random() * 100000) + 100000);

            start = System.nanoTime();
            result = Math.toDegrees(valueRad);
            finish = System.nanoTime();

            elapsedTime = (finish - start) / 1000000;
        }

        else if (choice == 0)
        {
            valueRad = (int) ((Math.random() * -100000) - 100000);

            start = System.nanoTime();
            result = Math.toDegrees(valueRad);
            finish = System.nanoTime();

            elapsedTime = (finish - start) / 1000000;
        }

        return elapsedTime;
    }

    // method returns Nth power of A
    public double nthRoot()
    {
        double start, finish, elapsedTime = 0;

        A = (int) ((Math.random() * 500000) + 500000);
        N = (int) ((Math.random() * 21));

        start = System.nanoTime();
        // intially guessing a random number between
        // 0 and 9
        double xPre = Math.random() % 10;

        // smaller eps, denotes more accuracy
        double eps = 0.001;

        // initializing difference between two
        // roots by INT_MAX
        double delX = 2147483647;

        // xK denotes current value of x
        double xK = 0.0;

        // loop untill we reach desired accuracy
        while (delX > eps)
        {
            // calculating current value from previous
            // value by newton's method
            xK = ((N - 1.0) * xPre +
                    (double)A / Math.pow(xPre, N - 1)) / (double)N;
            delX = Math.abs(xK - xPre);
            xPre = xK;
        }

        finish = System.nanoTime();
        elapsedTime = (finish - start) / 1000000; //milisaniye

        //return xK; //bu sonucu döndürüyor.
        return elapsedTime;
    }

}
