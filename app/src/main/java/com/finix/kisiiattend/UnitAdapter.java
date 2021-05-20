package com.finix.kisiiattend;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.MyViewHolder> {
    ArrayList<Unit> unitArrayList;
    Context context;

    public UnitAdapter(Context context, ArrayList<Unit> unitArrayList) {
        this.context = context;
        this.unitArrayList = unitArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView unitCode, unitName, unitTimes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unitCode = itemView.findViewById(R.id.unitCode);
            unitName = itemView.findViewById(R.id.unitName);
            unitTimes = itemView.findViewById(R.id.unitTimes);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Unit unit = unitArrayList.get(position);
        User user = new User();
        holder.unitName.setText(unit.getUnitName());
        holder.unitCode.setText(unit.getUnitCode());
        holder.unitTimes.setText(unit.getUnitTimes());

        holder.unitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UnitDetailsActivity.class);
                intent.putExtra("unitName", unit.getUnitName());
                intent.putExtra("unitCode", unit.getUnitCode());
                intent.putExtra("unitTimes",unit.getUnitTimes());
                intent.putExtra("occupation", user.getOccupation());
                context.startActivity(intent);

            }
        });
        holder.unitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UnitDetailsActivity.class);
                intent.putExtra("unitName", unit.getUnitName());
                intent.putExtra("unitCode", unit.getUnitCode());
                intent.putExtra("unitTimes",unit.getUnitTimes());
                intent.putExtra("occupation", user.getOccupation());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitArrayList.size();
    }

}