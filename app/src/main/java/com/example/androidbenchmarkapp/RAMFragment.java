package com.example.androidbenchmarkapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class RAMFragment extends Fragment implements View.OnClickListener {

    static
    {
        System.loadLibrary("randmem");
        System.loadLibrary("memspeed");
    }

    public native String stringFromJNI(int size1);
    public native String stringFromJNI2(int size1);

    private TextView RAM_infoTxtView;

    private Context mContext;

    private ProgressDialog progressDialog;
    private int index = 0;

    private Button startRam_benchmark;
    private TextView HeapStack_txtView;

    private Map<String,Double> results_map = new HashMap<>();
    private RAM_ProgressDialog_Message message = new RAM_ProgressDialog_Message();


    private String version = " Android RandMem Benchmark 1.1 ";
    private Button mStartButton;

    private String version2 = " Android MemSpeed Benchmark 1.1 ";

    private Handler _myHandler;
    private Handler _myHandler2;
    private Runnable _myTask;
    private Runnable _myTask2;

    private TextView mDisplayDetails;
    private TextView mDisplayDetails2;

    private String[] xout = new String[20];
    private String[] xout2 = new String[20];

    private String x0;

    private String date$;

    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int testDone = 0;
    private static int size1;
    private int runs = 10; // 2 * 4 DP MW arrays = 2 * 32 MB

    private Typeface tf = Typeface.create("monospace", Typeface.NORMAL);

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
    }

    public RAMFragment()
    {

    }

    public String getMemoryInfo(){

        String mem_total = null;
        String cpuInfo = readMeminfo();
        String[] cpuInfoArray =cpuInfo.split(":");

        for( int i = 0 ; i< cpuInfoArray.length;i++){

            if(cpuInfoArray[i].contains("MemTotal")){

                mem_total = cpuInfoArray[i+1];
                break;

            }
        }

        if(mem_total != null) mem_total = mem_total.trim();

        return mem_total;
    }

    public String readMeminfo()
    {
        ProcessBuilder cmd;
        String result="";
        InputStream in = null;

        try{

            String[] args = {"/system/bin/cat", "/proc/meminfo"};

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

        message.addMessage();

        View rootView = inflater.inflate(R.layout.fragment_ram, container, false);

        HeapStack_txtView = rootView.findViewById(R.id.HeapStack_txtView);

        RAM_infoTxtView = rootView.findViewById(R.id.RamInfo_txtView);

        startRam_benchmark = rootView.findViewById(R.id.RAMBenchmark_btn);
        startRam_benchmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                progressRunner progressRunner_ = new progressRunner();
                progressRunner_.execute();
            }
        });

        String mem_total = getMemoryInfo();
        String mem_total_new = mem_total.replace("kB", "");
        mem_total_new = mem_total_new.replace("MemFree", "");
        mem_total_new = mem_total_new.trim();
        double memory = Double.parseDouble(mem_total_new);
        memory = memory / 1000000;
        DecimalFormat df = new DecimalFormat("#.##");
        RAM_infoTxtView.setText("RAM miktarı: " + df.format(memory) + " GB");

        mStartButton = rootView.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(this);

        clear();
        x0 = " Not run yet\n\n\n\n\n\n\n\n\n\n\n\n";
        testDone = 0;

        mDisplayDetails = rootView.findViewById(R.id.displayDetails);
        mDisplayDetails2 = rootView.findViewById(R.id.displayDetails2);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayDetails.setTypeface(tf);
        mDisplayDetails2.setTypeface(tf);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);

        mDisplayDetails.setTextColor(Color.rgb(0,0,255));
        mDisplayDetails2.setTextColor(Color.rgb(0,0,255));

        xout[0]   = version + date$ + "\n";
        xout[1]   = "\n";

        xout2[0]   = version2 + date$ + "\n";
        xout2[1]   = "\n";

        clear();

        //eski telefona ait kod
        int textSizeInSp = (int) getResources().getDimension(R.dimen.RAM);
        mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, convertSpToPixels(textSizeInSp , mContext));
        mDisplayDetails2.setTextSize(TypedValue.COMPLEX_UNIT_PX, convertSpToPixels(textSizeInSp , mContext));

        //note 4 e ait kod
        /*mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);
        mDisplayDetails2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);*/

        //annemin teli
        //mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        //mDisplayDetails2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

        displayAll();

        return rootView;
    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.startButton)
        {
            clear();
            _myTask = new myWhetTask();
            _myHandler = new Handler();
            xout[15] = " **************** Çalışıyor ****************\n";
            xout2[15] = " **************** Çalışıyor ****************\n";
            displayAll();
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            _myHandler.postDelayed(_myTask, 100);

        }

    }

    private void displayAll()
    {
        mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                xout[8]  + xout[9]  + xout[10] + xout[11] +
                xout[12]  + xout[13] + xout[14] +
                xout[15] + xout[16]);

        mDisplayDetails2.setText(xout2[0]  + xout2[1]  + xout2[2]  + xout2[3]  +
                xout2[4]  + xout2[5]  + xout2[6]  + xout2[7]  +
                xout2[8]  + xout2[9]  + xout2[10] + xout2[11] +
                xout2[12] + xout2[13] + xout2[14] + xout2[15] +
                xout2[16]);
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
        @Override
        public void run()
        {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = version + date$ + "\n";
            xout2[0] = version + date$ + "\n";

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            for (size1=0; size1<runs; size1++)
            {
                x0 = stringFromJNI(size1);
                xout[size1+5] = x0;
                endTest = System.currentTimeMillis();
                testTime = (double)(endTest - startTest) / 1000.0;
                if (testTime > 120) size1 = runs; //120 sn den fazla olursa testten çık
            }
            xout[15] = "\n";
            xout[16] = "          Toplam Geçen Süre  " + String.format("%5.1f", testTime)
                    + " saniye\n";

            //*******************************************************************************

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            for (size1=0; size1<runs; size1++)
            {
                x0 = stringFromJNI2(size1);
                xout2[size1+5] = x0;
                endTest = System.currentTimeMillis();
                testTime = (double)(endTest - startTest) / 1000.0;
                if (testTime > 120) size1 = runs; //120 sn den fazla olursa testten çık
            }
            xout2[15] = "\n";
            xout2[16] = "          Toplam Geçen Süre  " + String.format("%5.1f", testTime)
                    + " saniye\n";

            testDone = 1;
            displayAll();
            resetTest();
        }
    }

    public void clear()
    {
        xout[0]  = version + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = "    MBytes/Second Transferring 4 Byte Words  \n";
        xout[3]  = "   Memory     Serial.......     Random.......\n";
        xout[4]  = "   KBytes     Read   Rd/Wrt     Read   Rd/Wrt\n";
        xout[5]  = "\n";
        xout[6]  = "\n";
        xout[7]  = "\n";
        xout[8]  = "\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";
        xout[13] = "\n";
        xout[14] = "\n";
        xout[15] = "\n";
        xout[16] = "    Muhtemel çalışma süresi 10 ila 50 saniye\n";

        xout2[0]  = version + date$ + "\n";
        xout2[1]  = "\n";
        xout2[2]  = "              Reading Speed in MBytes/Second\n";
        xout2[3]  = "  Memory  x[m]=x[m]+s*y[m] Int+   x[m]=x[m]+y[m]\n";
        xout2[4]  = "  KBytes   Dble   Sngl    Int   Dble   Sngl    Int\n";
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
        xout2[15] = " ARMv7 hızlı FPU'ya sahip cihazlarda çalışma süresi 15 ila 25 saniye\n";
        xout2[16] = " Yavaş CPU'larda test 120 saniyeden sonra durur.\n";
        testDone = 0;
    }

    //***********************************************************************

    class progressRunner extends AsyncTask<URL, Integer, Long>
    {

        protected void onPreExecute()
        {
            try
            {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("RAM Benchmark");
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
        protected Long doInBackground(URL... urls) {

            Heap_Stack_Memory heap_stack_memory = new Heap_Stack_Memory();
            heap_stack_memory.Main();
            results_map.put("HeapTime", heap_stack_memory.elapsedTimeHeap);
            results_map.put("StackTime", heap_stack_memory.elapsedTimeStack);
            publishProgress(50);

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
                HeapStack_txtView.setText("\n" + "Heap Allocation: " + Double.toString(results_map.get("HeapTime")) + " milisaniye");
                HeapStack_txtView.append("\n" + " Stack Allocation: " + Double.toString(results_map.get("StackTime")) + " milisaniye");

                progressDialog.dismiss();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
