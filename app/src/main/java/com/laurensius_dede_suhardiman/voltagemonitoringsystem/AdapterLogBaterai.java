package com.laurensius_dede_suhardiman.voltagemonitoringsystem;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Laurensius D.S on 2/9/2018.
 */

public class AdapterLogBaterai extends RecyclerView.Adapter<AdapterLogBaterai.HolderLogBaterai> {
    List<LogBaterai> logBaterai;
    AdapterLogBaterai(List<LogBaterai>logBaterai){
        this.logBaterai =logBaterai;
    }

    @Override
    public HolderLogBaterai onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item_log,viewGroup,false);
        HolderLogBaterai holderLogBaterai = new HolderLogBaterai(v);
        return holderLogBaterai;
    }

    @Override
    public void onBindViewHolder(HolderLogBaterai holderLogBaterai,int i){
        holderLogBaterai.ivIcon.setImageResource(logBaterai.get(i).icon);
        holderLogBaterai.tvId.setText(logBaterai.get(i).id);
        holderLogBaterai.tvStatus.setText("Status Baterai : " + logBaterai.get(i).status);
        holderLogBaterai.tvVoltage.setText("Tegangan Baterai : " + logBaterai.get(i).voltage);
        holderLogBaterai.tvDatetime.setText("Tanggal dan jam : " + logBaterai.get(i).datetime);
    }

    @Override
    public int getItemCount(){
        return logBaterai.size();
    }

    public LogBaterai getItem(int position){
        return logBaterai.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderLogBaterai extends  RecyclerView.ViewHolder{
        CardView cvLog;
        ImageView ivIcon;
        TextView tvId;
        TextView tvStatus;
        TextView tvVoltage;
        TextView tvDatetime;

        HolderLogBaterai(View itemView){
            super(itemView);
            cvLog = (CardView) itemView.findViewById(R.id.cv_log);
            ivIcon = (ImageView)itemView.findViewById(R.id.iv_icon);
            tvId = (TextView)itemView.findViewById(R.id.tv_id);
            tvStatus = (TextView)itemView.findViewById(R.id.tv_status);
            tvVoltage = (TextView)itemView.findViewById(R.id.tv_voltage);
            tvDatetime = (TextView)itemView.findViewById(R.id.tv_datetime);
        }

    }
}