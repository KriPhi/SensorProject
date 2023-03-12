package com.vu.lorasensorapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;


public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    private static List<String> timeStampList;
    private static OnItemClickListener listener;
    private int selectedItemPosition = RecyclerView.NO_POSITION;

    public myAdapter(List<String> timeStampList, OnItemClickListener listener) {
        this.timeStampList = timeStampList;
        this.listener = listener;
    }

    public void setDataList(List<String> field1List) {
        this.timeStampList = field1List;
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
        holder.bind(timeStampList.get(position), position == selectedItemPosition);
    }

    @Override
    public int getItemCount() {
        return timeStampList.size();
    }

    public void setSelectedItemPosition(int position) {
        int previousSelectedItemPosition = selectedItemPosition;
        selectedItemPosition = position;
        notifyItemChanged(previousSelectedItemPosition);
        notifyItemChanged(selectedItemPosition);
    }

    public interface OnItemClickListener {
        void onItemClick(String field1, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.date_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(String field1, boolean isSelected) {
            dateTV.setText(field1);

            // Highlight the selected item
            if (isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.mayablue));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ((myAdapter) ((RecyclerView) itemView.getParent()).getAdapter()).setSelectedItemPosition(position);
                listener.onItemClick(timeStampList.get(position), position);
            }
        }

    }
}