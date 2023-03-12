package com.vu.lorasensorapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    private List<String> field1List;

    public myAdapter(List<String> field1List) {
        this.field1List = field1List;
    }

    public void setDataList(List<String> field1List) {
        this.field1List = field1List;
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
        final String field1 = field1List.get(position);
        holder.dateTV.setText(field1);

        holder.dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), startSensor.class);
                // Retrieve the associated data for the clicked value
                String[] data = getDataForField1(field1);
                intent.putExtra("timeStamp", data[0]);
                intent.putExtra("longitude", data[1]);
                intent.putExtra("latitude", data[2]);
                intent.putExtra("altitude", data[3]);
                intent.putExtra("deviceID", data[4]);
                intent.putExtra("algorithm", data[5]);
                v.getContext().startActivity(intent);
            }
        });
    }

    private String[] getDataForField1(String field1) {
        // Retrieve data associated with field1
        // Replace this with your own logic to retrieve data based on field1 value
        // This is just an example
        if (field1.equals("Value 1")) {
            return new String[] {"2022-03-11 11:30:00", "50.1234", "30.5678", "100", "Device 1", "Algorithm A"};
        } else if (field1.equals("Value 2")) {
            return new String[] {"2022-03-11 12:00:00", "51.1234", "31.5678", "200", "Device 2", "Algorithm B"};
        } else {
            return new String[] {"", "", "", "", "", ""};
        }
    }

    @Override
    public int getItemCount() {
        return field1List.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.date_text_view);
        }
    }
}
