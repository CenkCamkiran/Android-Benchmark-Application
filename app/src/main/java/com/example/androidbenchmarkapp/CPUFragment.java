package com.example.androidbenchmarkapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static java.lang.Thread.sleep;

public class CPUFragment extends Fragment implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        //System.loadLibrary("dhry21");
        System.loadLibrary("linpack");
    }

    public native String stringFromJNI();
    //public native String stringFromJNI2();

    private Context mContext;

    private TextView PrimeResult_txtView, DegToRad_txtView,
            RadToDeg_txtView, matrixMultip_txtView, NthRoot_txtView;

    private TextView QuickSort_txtView, Dijkstra_txtView, FFT_textView;
    private TextView matrixInverse_txtView, matrixEqua_txtView, matrixRRF_txtView;
    private TextView matrixComplex_txtView, htmlParser_txtView, AES_txtView;
    private TextView CPUInfo_txtView, QRCode_txtView, Canny_txtView;
    private TextView Gaussian_Blur;

    private ProgressDialog progressDialog;

    private Button startCPUBench_btn;
    private String CPU_Info = null;

    CPU_ProgressDialog_Message message = new CPU_ProgressDialog_Message();

    private int index = 0;
    private double speed;
    double start, finish, elapsedTime;

    Map<String,Double> results_map = new HashMap<>();

    //**************************************************************

    private String version2 = " Dhry2 NoOpt Benchmark 1.2 ";
    private String[] xout2 = new String[20];
    //private TextView mDisplayDetails2;
    private String x1;

    private String version = " Android Linpack Benchmark ";
    private Button mStartButton;
    private Handler _myHandler;
    private Handler _myHandler2;
    private TextView mDisplayDetails;
    private Runnable _myTask;
    private Runnable _myTask2;
    private String[] xout = new String[20];
    private String x0;
    private String date$;

    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int testDone = 0;
    private int part;

    private Typeface tf = Typeface.create("monospace", Typeface.NORMAL);

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    //**************************************************************

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
    }

    public CPUFragment()
    {

    }

    public String CPUClock_Speed()
    {
        String cpuMaxFreq = "";
        RandomAccessFile reader = null;
        try {
            reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            cpuMaxFreq = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cpuMaxFreq;
    }

    /**
     * parse the CPU info to get the model name.
     *
     * @return the model name value as a String
     */
    public String getModelNameFromCpuInfo(){

        String Hardware = null;
        String cpuInfo = readCPUinfo();
        String[] cpuInfoArray =cpuInfo.split(":");

        for( int i = 0 ; i< cpuInfoArray.length;i++){

            if (cpuInfoArray[i].contains("Hardware"))
            {
                Hardware = cpuInfoArray[i+1];
                break;
            }
        }

        if(Hardware != null) Hardware = Hardware.trim();

        return Hardware;
    }

    public String readCPUinfo()
    {
        ProcessBuilder cmd;
        String result="";
        InputStream in = null;

        try{

            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};

            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            in = process.getInputStream();
            byte[] re = new byte[1024];

            while(in.read(re) != -1){

                System.out.println(new String(re));
                result = result + new String(re);

            }

        } catch(IOException ex){
            ex.printStackTrace();

        } finally {
            try {

                if(in !=null)
                    in.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        //return inflater.inflate(R.layout.fragment_cpu, container, false);

        View rootView = inflater.inflate(R.layout.fragment_cpu, container, false);

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d("OpenCV", "  OpenCVLoader.initDebug(), working.");
        }

        message.addMessage();

        matrixMultip_txtView = rootView.findViewById(R.id.matrixMultip_txtView);
        matrixInverse_txtView = rootView.findViewById(R.id.matrixInverse_txtView);
        matrixEqua_txtView = rootView.findViewById(R.id.matrixEqua_txtView);
        matrixRRF_txtView = rootView.findViewById(R.id.matrixRRF_txtView);
        matrixComplex_txtView = rootView.findViewById(R.id.matrixComplex_txtView);
        PrimeResult_txtView = rootView.findViewById(R.id.PrimeResult_txtField);
        DegToRad_txtView = rootView.findViewById(R.id.DegToRad_txtView);
        RadToDeg_txtView = rootView.findViewById(R.id.RadToDeg_txtView);
        NthRoot_txtView = rootView.findViewById(R.id.NthRoot_txtView);
        QuickSort_txtView = rootView.findViewById(R.id.quickSort_txtView);
        Dijkstra_txtView = rootView.findViewById(R.id.Dijkstra_txtView);
        FFT_textView = rootView.findViewById(R.id.FFT_textView);
        htmlParser_txtView = rootView.findViewById(R.id.htmlParser_txtView);
        AES_txtView = rootView.findViewById(R.id.AES_txtView);
        CPUInfo_txtView = rootView.findViewById(R.id.CPUInfo_txtView);
        QRCode_txtView = rootView.findViewById(R.id.QRCode_txtView);
        Canny_txtView = rootView.findViewById(R.id.Canny_txtView);
        Gaussian_Blur = rootView.findViewById(R.id.Gaussian_txtView);

        CPU_Info = getModelNameFromCpuInfo();
        CPUInfo_txtView.setText(getModelNameFromCpuInfo());
        CPUInfo_txtView.append("\n" + "MODEL: " + Build.MODEL + " \n" + "MANUFACTURER: " + Build.MANUFACTURER);
        speed = Double.parseDouble(CPUClock_Speed());
        speed = speed * Math.pow(10, -6);
        CPUInfo_txtView.append("\n" + "CPU Clock Speed: " + Double.toString(speed) + " GHZ");

        startCPUBench_btn = rootView.findViewById(R.id.startCPUBench_btn);
        startCPUBench_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = 0;
                ProgressRunner progressRunner = new ProgressRunner();
                progressRunner.execute();
            }
        });

        //**********************************************************************

        mStartButton = rootView.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(this);

        clear();
        x0 = " Not run yet\n";
        testDone = 0;

        mDisplayDetails = rootView.findViewById(R.id.displayDetails);
        //mDisplayDetails2 = rootView.findViewById(R.id.displayDetails2);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayDetails.setTypeface(tf);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);

        mDisplayDetails.setTextColor(Color.rgb(0, 0, 255));
        //mDisplayDetails2.setTextColor(Color.rgb(0, 0, 255));

        xout[0] = version + date$ + "\n";
        xout[1] = "\n";

        clear();

        //eski telefona ait kod
        int textSizeInSp = (int) getResources().getDimension(R.dimen.CPU);
        mDisplayDetails.setTextSize(convertSpToPixels(textSizeInSp , mContext));

        //note 4 e ait kod
        //mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);

        //annemin teli
        //mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

        displayAll();

        return rootView;
    }

    class ProgressRunner extends AsyncTask<URL, Integer, Long>
    {
        protected void onPreExecute()
        {
            try
            {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("CPU Benchmark");
                progressDialog.setMessage("Başlıyor...");
                progressDialog.setCancelable(false);
                progressDialog.setProgress(0);
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                sleep(1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }


        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            progressDialog.dismiss();
        }

        @Override
        protected Long doInBackground(URL... params)
        {

            try{

                //Integer Calculation Kodu
                Integer_Calc ınteger_calc = new Integer_Calc();
                int primeNumber = ınteger_calc.InputPrime("10000");
                results_map.put("PrimeNumber", (double) primeNumber);
                results_map.put("IntegerCalc", ınteger_calc.elapsedTime);
                publishProgress(9);
                sleep(1500);


                //Matrix İşlemleri Kodu
                MatrixCalculation matrix = new MatrixCalculation();
                matrix.Main();
                results_map.put("MatrixMultip", matrix.elapsedTime_Multip);
                results_map.put("MatrixInverse", matrix.elapsedTime_Inverse);
                results_map.put("MatrixEquations", matrix.elapsedTime_Equations);
                results_map.put("MatrixRRF", matrix.elapsedTime_RRF);
                results_map.put("MatrixComplex", matrix.elapsedTime_Complex);
                publishProgress(18);
                sleep(1500);


                //Floating Point Calculation Kodu
                FloatingPointCalc floatingPointCalc = new FloatingPointCalc();

                elapsedTime = floatingPointCalc.Degree_ToRadian();
                results_map.put("DegtoRad", elapsedTime);
                results_map.put("ValueDeg", (double) floatingPointCalc.valueDeg);

                elapsedTime = floatingPointCalc.Radian_ToDegree();
                results_map.put("RadtoDeg", elapsedTime);
                results_map.put("ValueRad", (double) floatingPointCalc.valueRad);

                elapsedTime = floatingPointCalc.nthRoot();
                results_map.put("NthRoot", elapsedTime);
                results_map.put("ValueBase", (double) floatingPointCalc.A);
                results_map.put("ValueRoot", (double) floatingPointCalc.N);
                publishProgress(27);
                sleep(1500);


                //QuickSort Kodu
                int arr[] = {10, 9, 6, 77, 5, 80, 91, 11, 53, 1, 71, 78, 14, 2, 63, 29, 38, 3, 9, 49, 60, 7, 75,
                        83, 96, 0, 40, 31, 62, 13, 21, 17, 34, 24, 23, 3, 0, 47, 66, 99, 44, 33, 86, 70, 12, 13,
                        14, 15, 10, 56, 53, 43, 100, 52, 63, 83, 71, 47, 5, 39, 19, 52, 18, 37, 47, 41, 13, 0, 9};

                int n = arr.length;
                QuickSort ob = new QuickSort();

                start = System.nanoTime();
                ob.sort(arr, 0, n-1);
                finish = System.nanoTime();

                elapsedTime = (finish - start) / 1000000;
                results_map.put("QuickSort", elapsedTime);
                publishProgress(36);
                sleep(1500);


                //Dijkstra Kodu
                Dijkstra dijkstra = new Dijkstra();
                elapsedTime = dijkstra.Calc_Dijkstra();
                results_map.put("Dijkstra", elapsedTime);
                publishProgress(45);
                sleep(1500);


                //FFT (Fast Fourier Transform) Kodu
                FFT fft = new FFT();
                fft.Main();
                results_map.put("FFT", fft.elapsedTime/fft.iteration);
                publishProgress(54);
                sleep(1500);


                //HTML Parser Kodu
                int iterations = 100; //100 iterasyon yeterli!!!
                StringBuilder sb = new StringBuilder();

                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("google_page.html")));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(reader.readLine()).append("\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                String html = sb.toString();
                start = System.nanoTime();
                for (int i = 0; i < iterations; i++) {
                    Document document = Jsoup.parse(html);
                }

                finish = System.nanoTime();
                elapsedTime = (finish - start) / 1000000;


                results_map.put("HTMLParser", elapsedTime);
                publishProgress(63);
                sleep(1500);


                //AES (encrypt decrypt) Kodu
                AES aes = new AES();
                try {
                    results_map.put("AES_Encrypt", aes.encrypt());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    results_map.put("AES_Decrypt", aes.decrypt());
                    //AES_txtView.append("Decrypt: " + Double.toString(aes.decrypt()) + " milisaniye");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                publishProgress(72);
                sleep(1500);


            }catch (Exception e)
            {
                Log.d("HATA!!!", e.toString());
            }

            QRCodeScan qrCodeScan = new QRCodeScan(mContext);
            results_map.put("QRCode", qrCodeScan.Scan());
            publishProgress(81);
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Canny canny = new Canny(mContext);
            results_map.put("Canny", canny.EdgeDetection());
            publishProgress(90);
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Gaussian_Blur gaussian_blur = new Gaussian_Blur(mContext);
            results_map.put("Gaussian", gaussian_blur.Blur());
            publishProgress(99);
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress)
        {
            progressDialog.setMessage(message.message_list.get(index));
            progressDialog.setProgress(progress[0]);
            index++;

        }

        protected void onPostExecute(Long result)
        {
            try
            {
                PrimeResult_txtView.setText("1000." + " asal sayı : " + results_map.get("PrimeNumber"));
                PrimeResult_txtView.append(" Çalışma Zamanı: " + Double.toString(results_map.get("IntegerCalc")) + " milisaniye");

                matrixMultip_txtView.setText("Matrix Multiplication Çalışma Zamanı: " + Double.toString(results_map.get("MatrixMultip")) + " milisaniye");
                matrixInverse_txtView.setText("Matrix Inverse Çalışma Zamanı: " + Double.toString(results_map.get("MatrixInverse")) + " milisaniye");
                matrixEqua_txtView.setText("Matrix Equations Çalışma Zamanı: " + Double.toString(results_map.get("MatrixEquations")) + " milisaniye");
                matrixRRF_txtView.setText("Matrix RRF Çalışma Zamanı: " + Double.toString(results_map.get("MatrixRRF")) + " milisaniye");
                matrixComplex_txtView.setText("Matrix Complex Çalışma Zamanı: " + Double.toString(results_map.get("MatrixComplex")) + " milisaniye");

                matrixComplex_txtView.setText("Matrix Inverse Çalışma Zamanı: " + Double.toString(results_map.get("MatrixComplex")) + " milisaniye");

                DegToRad_txtView.setText(Double.toString(results_map.get("ValueDeg")) + " derecenin Radyana çevrilmesi: " + Double.toString(results_map.get("DegtoRad")) + " milisaniye");
                RadToDeg_txtView.setText(Double.toString(results_map.get("ValueRad")) + " radyanın Dereceye çevrilmesi: " + Double.toString(results_map.get("RadtoDeg")) + " milisaniye");
                NthRoot_txtView.setText(Double.toString(results_map.get("ValueBase")) + " sayısının " + Double.toString(results_map.get("ValueRoot")) + " . karekökü : " + Double.toString(results_map.get("NthRoot")) + " milisaniye");

                QuickSort_txtView.setText("QuickSort: " + Double.toString(results_map.get("QuickSort")) + " milisaniye");

                Dijkstra_txtView.setText("Dijkstra: " + Double.toString(results_map.get("Dijkstra")) + " milisaniye");

                FFT_textView.setText("Execution Time" + " Averaged: " + Double.toString(results_map.get("FFT")) + " ms per iteration");

                htmlParser_txtView.setText(Double.toString(results_map.get("HTMLParser")) + " milisaniye" );

                AES_txtView.setText("Encrypt: " + Double.toString(results_map.get("AES_Encrypt")) + " milisaniye");

                AES_txtView.append(" \n Decrypt: " + Double.toString(results_map.get("AES_Decrypt")) + " milisaniye");

                QRCode_txtView.setText("Execution Time: " + Double.toString(results_map.get("QRCode")) + " milisaniye");

                Canny_txtView.setText("Canny Edge Detection: " + Double.toString(results_map.get("Canny")) + " milisaniye");

                Gaussian_Blur.setText("Gaussian Blur: " + Double.toString(results_map.get("Gaussian")) + " milisaniye");

                progressDialog.dismiss();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /*Map fonksiyonu için keyler => PrimeNumber, IntegerCalc,
    MatrixMultip, MatrixInverse, MatrixEquations, MatrixRRF,
    MatrixComplex, DegtoRad, RadtoDeg, NthRoot, QuickSort,
    Dijkstra, FFT, HTMLParser, AES_Encrypt, AES_Decrypt, ValueBase
    ValueRoot, ValueRad, ValueDeg, QRCode, Canny, Gaussian, FaceDetection
    */

    public void onClick(View v)
    {
        if(v.getId() == R.id.startButton)
        {
            clear();
            _myTask = new myLinpTask();
            _myHandler = new Handler();
            xout[15] = " Çalışma süresi 5 ila 10 saniye\n";
            xout[16] = "\n";
            //xout2[8] = " Running Time less than 2 seconds\n \n";
            displayAll();
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            _myHandler.postDelayed(_myTask, 100);

        }
    }

    private void displayAll()
    {
        if (testDone == 0)
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                    xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                    xout[8]  + xout[9]  + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] +
                    xout[16]);

            /*mDisplayDetails2.setText(xout2[0]  + xout2[1]  + xout2[2]  + xout2[3]  +
                    xout2[4]  + xout2[5]  + xout2[6]  + xout2[7]  +
                    xout2[8]  + xout2[9]  + xout2[10] + xout2[11] +
                    xout2[12]  + xout2[13] + xout2[14]);*/
        }
        else
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + x0 + xout[10] + xout[11] +
                    xout[12] + xout[13] + xout[14] + xout[15] + xout[16]);

            /*mDisplayDetails2.setText(xout2[0]  + xout2[1]  + x1 + xout2[5] +
                    xout2[6] + xout2[7] + xout2[8] +
                    xout2[9] + xout2[10] + xout2[11] +
                    xout2[12]  + xout2[13] + xout2[14]);*/
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

    private class myLinpTask implements Runnable
    {

        public void run()
        {

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = version + date$ + "\n";
            xout2[0]  = version + date$ + "\n";

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;

            x0 = stringFromJNI();

            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout[15] = " Toplam geçen süre  " + String.format("%5.1f", testTime)
                    + " saniye\n";
            xout[14] = "\n";
            xout[16] = "\n";

            //**************************************************************************

            /*startTest = System.currentTimeMillis();
            TimeUsed = 0.0;

            x1 = stringFromJNI2();

            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout2[8] = " Total Elapsed Time  " + String.format("%5.1f", testTime)
                    + " seconds\n";*/

            testDone = 1;
            displayAll();
            resetTest();
        }
    }


    public void clear()
    {
        xout[0]  = version + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = " MFLOPS'ta hızı ölçer ve sonuçları ekrana yazdırır:\n";
        xout[3]  = "\n";
        xout[4]  = " norm. resid\n";
        xout[5]  = " resid\n";
        xout[6]  = " machep\n";
        xout[7]  = " x[0]-1\n";
        xout[8]  = " x[n-1]-1\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";
        xout[13] = "\n";
        xout[14] = "\n";
        xout[15] = "\n";
        xout[16] = "\n";

        /*xout2[0]  = version2 + date$ + "\n";
        xout2[1]  = "\n";
        xout2[2]  = " Nanoseconds one Dhrystone run\n";
        xout2[3]  = " Dhrystones per Second\n";
        xout2[4]  = " VAX MIPS rating\n";
        xout2[5]  = "\n";
        xout2[6]  = "\n";
        xout2[7]  = "\n";
        xout2[8]  = "\n";
        xout2[9]  = "\n";
        xout2[10] = "\n";
        xout2[11] = "\n";
        xout2[12] = "\n";
        xout2[13] = "\n";
        xout2[14] = "\n";
        xout2[15] = "\n";
        xout2[16] = "\n";
        testDone = 0;*/
    }

}
