package com.laurensius_dede_suhardiman.voltagemonitoringsystem;

/**
 * Created by Laurensius D.S on 2/9/2018.
 */

public class LogBaterai {

    int icon;
    String id;
    String status;
    String voltage;
    String datetime;

    LogBaterai(int icon,String id,String status,String voltage,String datetime){
        this.icon = icon;
        this.id = id;
        this.status = status;
        this.voltage = voltage;
        this.datetime = datetime;
    }
}
