package com.laurensius_dede_suhardiman.voltagemonitoringsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.anastr.speedviewlib.PointerSpeedometer;

public class ActivityMonitoring extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tampilkan grafik log ?", Snackbar.LENGTH_LONG)
                        .setAction("Ya", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ActivityMonitoring.this,"Menampilkan data / grafik log", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });

        PointerSpeedometer speedometer = findViewById(R.id.pointerSpeedometer);
        speedometer.speedTo(50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_monitoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
