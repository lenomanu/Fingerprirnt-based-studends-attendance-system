package com.finix.kisiiattend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DateSearchAdapter extends RecyclerView.Adapter<DateSearchAdapter.MyViewHolder>{
    ArrayList<Attend> myAttendanceArrayList;
    Context context;

    public DateSearchAdapter(Context context,  ArrayList<Attend> myAttendanceArrayList) {
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
    public DateSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myattendance, parent, false);
        return new DateSearchAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DateSearchAdapter.MyViewHolder holder, int position) {
        Attend attend = myAttendanceArrayList.get(position);
        holder.date.setText(attend.getDate());
        holder.name.setText(attend.getName());
    }

    @Override
    public int getItemCount() {
        return myAttendanceArrayList.size();
    }

}
