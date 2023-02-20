package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    public List<HelperClass2> dataList;

    public myAdapter() {
        this.dataList = dataList;
    }

    public void setDataList(List<HelperClass2> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HelperClass2 data = dataList.get(position);
        holder.dateTV.setText(data.getLongi());
        holder.longitudeTV.setText(String.valueOf(data.getDate()));
        holder.latitutdeTV.setText(String.valueOf(data.getDate()));
        holder.altitudeTV.setText(data.getAlti());
        holder.deviceIDTV.setText(data.getDevice());
        holder.algorithmTV.setText(data.getAlgo());
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV, longitudeTV, latitutdeTV, altitudeTV, deviceIDTV, algorithmTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.date_text_view);
            longitudeTV = itemView.findViewById(R.id.longitude_text_view);
            latitutdeTV = itemView.findViewById(R.id.latitude_text_view);
            altitudeTV = itemView.findViewById(R.id.altitude_text_view);
            deviceIDTV = itemView.findViewById(R.id.deviceID_text_view);
            algorithmTV = itemView.findViewById(R.id.algorithm_text_view);
        }



    }
}