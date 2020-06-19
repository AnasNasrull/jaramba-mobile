package com.example.jarambamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TripUserHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Calendar calendar;

    TextView tvEditDate;
    Spinner etStartCity, etStartArea, etDestinationCity, etDestinationArea;

    String StartCity, StartArea, DestinationCity, DestinationArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_user_home);


        etStartCity = findViewById(R.id.btn_start_city);
        etStartCity.setOnItemSelectedListener(this);

        etStartArea = findViewById(R.id.btn_start_area);

        etDestinationCity = findViewById(R.id.btn_dest_city);
        etDestinationCity.setOnItemSelectedListener(this);

        etDestinationArea = findViewById(R.id.btn_dest_area);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.area, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etStartArea.setAdapter(adapter);
        etDestinationArea.setAdapter(adapter);
        etStartArea.setOnItemSelectedListener(this);
        etDestinationArea.setOnItemSelectedListener(this);


    }


    public void btnGo(View view) {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void datePicker(View view) {

    }

}
