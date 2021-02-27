package com.example.androidbenchmarkapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StatFs;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class IOFragment extends Fragment implements View.OnClickListener {

    static
    {
        System.loadLibrary("drivespeed2");
    }

    private ProgressDialog progressDialog;
    private int index = 0;
    private IO_ProgressDialog_Message message;
    Map<String,String> results_map = new HashMap<>();
    private TextView SQLite_txtView;
    private Button SQLite_Bench_btn;

    public native String doIt(int size1, String path, int cacheIt, int driveToUse);

    private String version = " Android Depolama Birimi Benchmark 1.0 ";
    private String title2;
    private String driveUsed;
    private String driveMsg;
    private Button mStartButton;
    private Handler _myHandler;
    private Handler _myHandler2;
    private TextView mDisplayDetails;
    private Runnable _myTask;
    private String[] xout = new String[20];
    private String capacity;
    private String x0;
    private String date$;
    private String path = "/data/data/com.example.androidbenchmarkapp/files/";

    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int testDone = 0;
    private static int size1;
    private static int sizea = 0;
    private String[] args = new String[2];
    private int runs;
    private int driveToUse;
    private int delete = 1;

    private Typeface tf = Typeface.create("monospace", Typeface.NORMAL);
    private Context show;
    private boolean noDetails;
    private boolean hasDirectIO;

    private Context mContext;

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public IOFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        message = new IO_ProgressDialog_Message();
        message.addMessage();

        View rootView = inflater.inflate(R.layout.fragment_io, container, false);

        SQLite_txtView = rootView.findViewById(R.id.SQLite_txtView);
        mStartButton = rootView.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(this);
        SQLite_Bench_btn = rootView.findViewById(R.id.SQLite_Bench_btn);
        SQLite_Bench_btn.setOnClickListener(this);

        x0 = " Not run yet\n\n\n\n\n\n\n\n\n\n\n\n";

        mDisplayDetails = rootView.findViewById(R.id.displayDetails);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayDetails.setTypeface(tf);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);
        title2 = version + date$ + "\n";

        mDisplayDetails.setTextColor(Color.rgb(0,0,255));

        xout[0]   = title2;
        xout[1]   = "\n";

        clear();

        //note 4
        /*int textSizeInSp = (int) getResources().getDimension(R.dimen.IO);
        mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, convertSpToPixels(textSizeInSp , mContext));*/

        //eski telefona ait kod
        int textSizeInSp = (int) getResources().getDimension(R.dimen.CPU);
        mDisplayDetails.setTextSize(convertSpToPixels(textSizeInSp , mContext));

        //annemin teli
        //mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

        displayAll();

        return rootView;
    }

// char filePath[1000] = "/data/data/com.drivespeed2/";
// char filePath[1000] = "/data/data/com/drivespeed2/files/";
// char filePath[1000] = "/sdcard/";
// char filePath[1000] = "/LocalDisk/";

    public void onClick(View v)
    {
        if(v.getId() == R.id.startButton)
        {
            driveToUse = 0;
            clear();
            noDetails = false;

            try
            {
                StatFs stat_fs2 = new StatFs(path);

                double total_int_space = (double)stat_fs2.getBlockCount() *(double)stat_fs2.getBlockSize();
                double free_int_space = (double)stat_fs2.getFreeBlocks() *(double)stat_fs2.getBlockSize();

                int MB_Tt2 = (int)(total_int_space / 1048576);
                int MB_Fr2 = (int)(free_int_space / 1048576);
                capacity = String.format("%7d", MB_Tt2) +
                        " Free " +
                        String.format("%7d", MB_Fr2) +
                        "\n";
            }
            catch(Exception e)
            {
                capacity = " Not Found\n";
                noDetails = true;
                Toast.makeText(mContext,"Cannot Find Drive Details",Toast.LENGTH_LONG).show();
            }

            _myTask = new myWhetTask();
            _myHandler = new Handler();
            xout[14] = " ****** Test Çalışıyor - 1 dakikadan uzun sürebilir.\n";
            xout[15] = " ****** Dosya Yolu " + path + "\n";
            xout[16] = " ****** Depolama kapasitesi MB " + capacity;
            displayAll();
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            if (noDetails)
            {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
            else
            {
                _myHandler.postDelayed(_myTask, 100);
            }
        }

        else if (v.getId() == R.id.SQLite_Bench_btn)
        {
            index = 0;
            ProgressRunner progressRunner = new ProgressRunner();
            progressRunner.execute();
        }
    }

    private void displayAll()
    {
        mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                xout[8]  + xout[9]  + xout[10] + xout[11] +
                xout[12] + xout[13] + xout[14] +
                xout[15] + xout[16]);
    }

    private void resetTest()
    {
        if(_myHandler != null)
        {
            _myHandler.removeCallbacks(_myTask);
        }
    }

    private class myWhetTask implements Runnable
    {

        public void run()
        {
            int cacheIt = 0;
            hasDirectIO = true;
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = title2;

            startTest = System.currentTimeMillis();
            runs =15;

            x0 = doIt(-1, path, cacheIt, driveToUse);
            if (x0.compareTo(" OK") != 0)
            {
                cacheIt = 1;
                hasDirectIO = false;
                driveMsg = " Data Cached";
                driveUsed = "           " + driveMsg + "\n";
            }
            else
            {
                driveMsg = " Data Not Cached";
                driveUsed = "          " + driveMsg + "\n";
            }
            if (sizea == 88) driveUsed = "      " + " Read Only" + "\n";

            TimeUsed = 0.0;
            xout[2] = "\n";
            xout[3] = "\n";
            xout[5] = "\n";
            xout[9] = "\n";
            xout[13] = "\n\n";
            if (sizea == 88)
            {
                x0 = doIt(sizea, path, cacheIt, driveToUse);
                xout[2] = x0;
                endTest = System.currentTimeMillis();
                testTime = (double)(endTest - startTest) / 1000.0;
            }
            else
            {
                for (size1=0; size1<runs; size1++)
                {
                    int use;
                    if (size1 == 2 || size1 == 3 || size1 == 5 || size1 == 9 || size1 == 13)
                    {
                        use = size1;
                        if (size1 == 2)
                        {
                            use = 5;
                        }
                        else if (size1 == 5)
                        {
                            use = 2;
                        }
                        x0 = doIt(size1, path, cacheIt, driveToUse);
                        xout[use] = x0;
                        endTest = System.currentTimeMillis();
                        testTime = (double)(endTest - startTest) / 1000.0;
                        if (testTime > 120) size1 = runs;
                    }
                }
            }
            xout[14] = " No delete\n";
            if (delete == 1)
            {
                size1 = 99;
                x0 = doIt(size1, path, cacheIt, driveToUse);
                xout[14] = "\n";
            }
            xout[0]  = "                     MBytes/Second\n";
            xout[1]  = "  MB    Write1 Write2 Write3  Read1  Read2  Read3\n";


            xout[4]  = " Cached\n";

            xout[6] = "\n";
            xout[7]  = " Random      Write                Read\n";
            xout[8]  = " From MB     4      8     16      4      8     16\n";

            xout[10] = "\n";
            xout[11] = " 200 Files   Write                Read            Delete \n";
            xout[12] = " File KB     4      8     16      4      8     16   secs \n";


            xout[14] = "       Toplam geçen süre  " + String.format("%5.1f", testTime)
                    + " saniye\n";
            xout[15] = "       Kullanılan Dosya Yolu - " + path + "\n";
            xout[16] = "  ";
            xout[17] = "       Depolama kapasitesi MB " + capacity;
            testDone = 1;
            displayAll();
            resetTest();
        }
    }


    public void clear()
    {
        xout[0]  = title2;
        xout[1]  = "\n";
        xout[2]  = " Test 1 - 3 adet 8 ve 16 MB boyutlarındaki dosyaları yazıp oku\n";
        xout[3]  = " Test 2 - 8 MB yaz, RAM'de önbelleğe alınabilir\n";
        xout[4]  = " Test 3 - 4-16 MB boyutundaki dosyalardan 1 Kb veri rastgele yazıp oku\n";
        xout[5]  = " Test 4 - 4-16 KB boyutunda 200 tane dosya yazıp oku \n";
        xout[6]  = "\n";;
        xout[7]  = " ------------------------------\n";
        xout[8]  = " Yeniden Başlatmak için dosya yolu girin\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";
        xout[13] = "\n";
        xout[14] = "\n";
        xout[15] = "\n";
        xout[16] = "\n";
        xout[17] = "\n";
        testDone = 0;
    }

    class ProgressRunner extends AsyncTask<URL, Integer, Long>
    {
        protected void onPreExecute()
        {
            try
            {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("IO Benchmark");
                progressDialog.setMessage("Başlıyor...");
                progressDialog.setCancelable(false);
                progressDialog.setProgress(0);
                progressDialog.setIndeterminate(false);
                progressDialog.show();
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

            try {

                Benchmark sqlite_bench = new Benchmark(mContext);
                sqlite_bench.runBenchmark();
                results_map.put("Android_SQLite", sqlite_bench.Android_Results);
                results_map.put("Requery_SQLite", sqlite_bench.Requery_Results);
                publishProgress(55);
                sleep(1500);

            }catch (Exception e){
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
                SQLite_txtView.setText("*******Android*******\n");
                SQLite_txtView.append(results_map.get("Android_SQLite") + "\n \n");
                SQLite_txtView.append("*******Requery*******\n");
                SQLite_txtView.append(results_map.get("Requery_SQLite"));

                progressDialog.dismiss();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
