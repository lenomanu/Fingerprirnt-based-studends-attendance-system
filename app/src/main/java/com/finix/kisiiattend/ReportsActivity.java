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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String unitName, unitCode;
    EditText search ;
    String Search, currentdate;
    Button pickdate;
    public static final String TAG = "Report Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Intent intent = getIntent();
        search = findViewById(R.id.registrationNumber);
        unitName = intent.getStringExtra("unitName");
        unitCode = intent.getStringExtra("unitCode");
        pickdate = findViewById(R.id.date);

        Log.d(TAG, "onCreate: "+unitCode+unitName);
    }

    public void searchRegNo(View view) {
        Log.d(TAG, "searchRegNo: Clicked");
        Search = search.getText().toString().trim();
        if(Search.isEmpty()){
            search.setError("Enter registration number");
            search.requestFocus();
            Toast.makeText(getApplicationContext(),"Enter registration number",Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(getApplicationContext(),RegSearchActivity.class);
            i.putExtra("regNo",Search);
            i.putExtra("unitCode",unitCode);
            startActivity(i);
        }
    }

    public void searchdate(View view) {
        Log.d(TAG, "searchDate: Clicked");
        Search = search.getText().toString().trim();
        if(Search.isEmpty()){
            search.setError("Enter registration number");
            search.requestFocus();
            Toast.makeText(getApplicationContext(),"Enter registration number",Toast.LENGTH_LONG).show();
        }else{
        Intent i = new Intent(getApplicationContext(),DateSearchActivity.class);
        i.putExtra("date",currentdate);
        i.putExtra("regNo",Search);
        i.putExtra("unitCode",unitCode);
        startActivity(i);}
    }

    public void pickdate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E-MMMM-d");
        currentdate = dateFormat.format(c.getTime());
        pickdate.setText(currentdate);

    }
}