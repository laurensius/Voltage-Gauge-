package com.laurensius_dede_suhardiman.voltagemonitoringsystem;

import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Monitoring extends AppCompatActivity {

    private PointerSpeedometer psVolstase;
    private TextView tvVoltage,tvStatus,tvTanggal,tvJam;
    private ImageView ivBatre, ivStatus;
    private LinearLayout llError, llSuccess;
    boolean loaddata;
    private String JSON_data;
    private String url;
    private String id;
    private String datetime;
    private String voltage;
    private String status;
    private static String TAG;
    private int ln;
    private int loaddata_false = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        TAG = getResources().getString(R.string.tag);
        tvVoltage = (TextView)findViewById(R.id.tv_voltage);
        tvStatus = (TextView)findViewById(R.id.tv_status);
        tvTanggal = (TextView)findViewById(R.id.tv_tanggal);
        tvJam = (TextView)findViewById(R.id.tv_jam);
        ivBatre = (ImageView)findViewById(R.id.iv_batre);
        ivStatus = (ImageView)findViewById(R.id.iv_status);
        llError = (LinearLayout)findViewById(R.id.ll_error);
        llError.setVisibility(View.GONE);
        llSuccess = (LinearLayout)findViewById(R.id.ll_success);
        llSuccess.setVisibility(View.VISIBLE);
        psVolstase = (PointerSpeedometer)findViewById(R.id.ps_voltase);
        psVolstase.setWithTremble(false);
        psVolstase.setTicks(0,5,10,15,20,25);
        psVolstase.setSpeedAt(0);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncMonitoring async= new AsyncMonitoring();
                            async.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    private class AsyncMonitoring extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            url = getResources().getString(R.string.api_endpoint)
                    .concat(getResources().getString(R.string.recent))
                    .concat(getResources().getString(R.string.limit_recent_1))
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
                        JSONObject obj_data = array_data.getJSONObject(0);
                        id = obj_data.getString("id");
                        datetime = obj_data.getString("datetime");
                        voltage = obj_data.getString("voltage");
                        status = obj_data.getString("status");
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
                String[] splited = datetime.split(" ");
                tvTanggal.setText(splited[0]);
                tvJam.setText(splited[1] + " WIB");
                Float float_voltage = Float.parseFloat(voltage);
                psVolstase.setSpeedAt(float_voltage);
                tvVoltage.setText(String.valueOf(float_voltage) + " V");
                tvStatus.setText(status);
                if(status.equals("GOOD")){
                    ivBatre.setImageResource(R.mipmap.batre);
                    ivStatus.setImageResource(R.mipmap.good);
                    psVolstase.setSpeedometerColor(Color.parseColor("#64DD17"));
                    psVolstase.setCenterCircleColor(Color.parseColor("#B2FF59"));
                }else
                if(status.equals("WEAK")){
                    ivBatre.setImageResource(R.mipmap.weakbatre);
                    ivStatus.setImageResource(R.mipmap.weak);
                    psVolstase.setSpeedometerColor(Color.parseColor("#D50000"));
                    psVolstase.setCenterCircleColor(Color.parseColor("#D50000"));
                }else
                if(status.equals("UNDEFINED")){
                    ivBatre.setImageResource(R.mipmap.undefinedbatre);
                    ivStatus.setImageResource(R.mipmap.undefined);
                    psVolstase.setSpeedometerColor(Color.parseColor("#D50000"));
                    psVolstase.setCenterCircleColor(Color.parseColor("#D50000"));
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
