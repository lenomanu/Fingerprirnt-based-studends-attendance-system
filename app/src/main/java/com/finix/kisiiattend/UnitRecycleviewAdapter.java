package com.finix.kisiiattend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UnitRecycleviewAdapter extends RecyclerView.Adapter<UnitRecycleviewAdapter.MyViewHolder>{
    ArrayList<Attend> myAttendanceArrayList;
    Context context;

    public UnitRecycleviewAdapter(Context context,  ArrayList<Attend> myAttendanceArrayList) {
        this.context = context;
        this.myAttendanceArrayList = myAttendanceArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);

        }
    }

    @NonNull
    @Override
    public UnitRecycleviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dates, parent, false);
        return new UnitRecycleviewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitRecycleviewAdapter.MyViewHolder holder, int position) {
        Attend attend = myAttendanceArrayList.get(position);
        holder.date.setText(attend.id);
        holder.name.setText(attend.getName());
    }

    @Override
    public int getItemCount() {
        return myAttendanceArrayList.size();
    }

}