package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarambamobile.fragment.DatePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripUserHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    Calendar calendar;
    TextView tvEditDate;
    Spinner etStartCity, etStartArea, etDestinationCity, etDestinationArea;
    Button btnGo;

    String StartCity, StartArea, DestinationCity, DestinationArea, Tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_user_home);

        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi_trip);
        bottomNavigationView.setSelectedItemId(R.id.nav_trip);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(TripUserHome.this, HomeActivity.class));
                        finish();
                        break;
                    case R.id.nav_history:
                        startActivity(new Intent(TripUserHome.this, History.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(TripUserHome.this, ProfilePage.class));
                        finish();
                        break;
                }

                return false;
            }
        });

        tvEditDate = findViewById(R.id.edit_date);
        tvEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

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

        StartCity = etStartCity.getSelectedItem().toString().trim();
        StartArea = etStartArea.getSelectedItem().toString().trim();
        DestinationCity = etDestinationCity.getSelectedItem().toString().trim();
        DestinationArea = etDestinationArea.getSelectedItem().toString().trim();

        btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DamriStartTrip.class);
                intent.putExtra("start_address", StartCity + ", " + StartArea);
                intent.putExtra("destination_address", DestinationCity + ", " + DestinationArea);
                intent.putExtra("From", "Trip User Home");
                intent.putExtra("Tanggal", Tanggal.toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String nowDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        tvEditDate.setText(nowDate);
        Tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
    }
}
