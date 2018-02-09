package com.laurensius_dede_suhardiman.voltagemonitoringsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class GrafikLog extends AppCompatActivity {

    private LinearLayout llError,llSuccess;
    private LineChart lcRecent;
    private RecyclerView rvLog;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdapterLogBaterai adapterLogBaterai;
    private Context context = this;
    private List<LogBaterai> logBaterai;
    boolean loaddata;
    private String JSON_data;
    private String url;
    private String[] array_id;
    private String[] array_datetime;
    private String[] array_status;
    private static String TAG;
    private int ln;
    private int loaddata_false = 0;
    private float[] array_voltage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_log);
        llError = (LinearLayout)findViewById(R.id.ll_error);
        llError.setVisibility(View.GONE);
        llSuccess = (LinearLayout)findViewById(R.id.ll_success);
        llSuccess.setVisibility(View.VISIBLE);
        lcRecent = (LineChart)findViewById(R.id.lc_recent);
        rvLog = (RecyclerView)findViewById(R.id.rv_log);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncGrafikRecent async= new AsyncGrafikRecent();
                            async.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    @Override
     public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(GrafikLog.this,Monitoring.class);
        startActivity(i);
        finish();
    }

    private class AsyncGrafikRecent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            url = getResources().getString(R.string.api_endpoint)
                    .concat(getResources().getString(R.string.recent))
                    .concat(getResources().getString(R.string.limit_recent_10))
                    .concat(getResources().getString(R.string.desc));
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            Log.d(TAG, "JSON data : " + JSON_data);
            if(JSON_data!=null){
                try {
                    JSONObject raw_data = new JSONObject(JSON_data);
                    JSONObject obj_response = raw_data.getJSONObject("response");
                    JSONArray array_data = obj_response.getJSONArray("data");
                    ln = array_data.length();
                    if(ln > 0){
                        array_id = new String[ln];
                        array_datetime= new String[ln];
                        array_voltage = new float[ln];
                        array_status = new String[ln];
                        for(int x=0;x<ln;x++){
                            JSONObject obj_data = array_data.getJSONObject(x);
                            array_id[x] = obj_data.getString("id");
                            array_datetime[x] = obj_data.getString("datetime");
                            array_voltage[x] = Float.parseFloat(obj_data.getString("voltage"));
                            array_status[x] = obj_data.getString("status");
                        }
                        loaddata = true;
                    }else{
                        loaddata = false;
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                    loaddata = false;
                }
            }
            else{
                loaddata=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(loaddata){
                llSuccess.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                List<Entry> entries = new ArrayList<Entry>();
                if(array_voltage.length>0){
                    logBaterai = new ArrayList<>();
                    int icon = 0;
                    for(int x=0;x<array_voltage.length;x++){
                        entries.add(new Entry(x, array_voltage[x]));
                        if(array_status[x].equals("GOOD")){
                            icon = R.mipmap.batre;
                        }else
                        if(array_status[x].equals("WEAK")){
                            icon = R.mipmap.weakbatre;
                        }else
                        if(array_status[x].equals("UNDEFINED")){
                            icon = R.mipmap.undefinedbatre;
                        }
                        logBaterai.add(new LogBaterai(icon,array_id[x],array_status[x],String.valueOf(array_voltage[x]),array_datetime[x]));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Voltase");
                    dataSet.setColor(Color.parseColor("#123123"));
                    dataSet.setValueTextColor(Color.parseColor("#123123"));
                    LineData lineData = new LineData(dataSet);
                    lcRecent.setData(lineData);
                    lcRecent.setContentDescription("Grafik Log");
                    lcRecent.invalidate();

                    rvLog.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(GrafikLog.this);
                    rvLog.setLayoutManager(mLayoutManager);
                    adapterLogBaterai = new AdapterLogBaterai(logBaterai);
                    rvLog.setAdapter(adapterLogBaterai);
                }
                loaddata_false = 0;
            }else {
                if(loaddata_false >= 3){
                    llSuccess.setVisibility(View.GONE);
                    llError.setVisibility(View.VISIBLE);
                }
                loaddata_false++;
            }
        }
    }
}
