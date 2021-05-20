package com.finix.kisiiattend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{
    ArrayList<TotalAttandance> studentsArrayList;
    Context context;

    public AttendanceAdapter(Context context, ArrayList<TotalAttandance> studentsArrayList) {
        this.context = context;
        this.studentsArrayList = studentsArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView regNo, name,percentage;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            regNo = itemView.findViewById(R.id.regno);
            name = itemView.findViewById(R.id.name);
            percentage = itemView.findViewById(R.id.percentage);
            imageView = itemView.findViewById(R.id.icon);

        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.MyViewHolder holder, int position) {
        TotalAttandance totalAttandance = studentsArrayList.get(position);
        holder.percentage.setText(totalAttandance.getTimeAttended()+"%");
        holder.regNo.setText(totalAttandance.getRegNo());
        holder.name.setText(totalAttandance.getsName());
        if(Integer.parseInt(totalAttandance.getTimeAttended()) > 79){
            holder.imageView.setImageResource(R.drawable.done);
        }

//        holder.regNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context, UnitDetailsActivity.class);
//                intent.putExtra("unitName", unit.getUnitName());
//                intent.putExtra("unitCode", unit.getUnitCode());
//                context.startActivity(intent);
//
//            }
//        });
//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UnitDetailsActivity.class);
//                intent.putExtra("unitName", unit.getUnitName());
//                intent.putExtra("unitCode", unit.getUnitCode());
//                intent.putExtra("occupation", user.getOccupation());
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return studentsArrayList.size();
    }

}
