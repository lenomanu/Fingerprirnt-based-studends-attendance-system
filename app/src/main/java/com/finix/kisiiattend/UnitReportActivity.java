package com.finix.kisiiattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UnitReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "UnitReport Activity";
    String unitCode, currentdate;
    Button dateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_report);
         Intent i = getIntent();
         unitCode = i.getStringExtra("unitCode");
         dateBtn = findViewById(R.id.date);
        Log.d(TAG, "onCreate: "+unitCode);
    }

    public void pickdate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    public void searchdate(View view) {
        Intent i = new Intent(getApplicationContext(),UnitRecyclerviewActivity.class);
        i.putExtra("date",currentdate);
        i.putExtra("unitCode",unitCode);
        startActivity(i);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E-MMMM-d");
        currentdate = dateFormat.format(c.getTime());
        dateBtn.setText(currentdate);
    }

    public void eligible(View view) {
        Intent intent = new Intent(getApplicationContext(),EligibleActivity.class);
        intent.putExtra("unitCode",unitCode);
        startActivity(intent);
    }

    public void noteligible(View view) {
        Intent intent = new Intent(getApplicationContext(),NoEligibleActivity.class);
        intent.putExtra("unitCode",unitCode);
        startActivity(intent);
    }
}