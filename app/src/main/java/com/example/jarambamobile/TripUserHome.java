package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarambamobile.fragment.DatePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

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

    TextView tvUsername, tvWelcome, tvEnterStart, tvEnterDestination, tvEnterDate;
    ImageView imgLogo;
    ConstraintLayout greetImg;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference database;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String StartCity, StartArea, DestinationCity, DestinationArea, Tanggal="", Hari="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_user_home);

        ChipNavigationBar bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.nav_trip,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
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
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("Mobile_Apps").child("User");

        //init progress dialog
        progressDialog = new ProgressDialog(TripUserHome.this);

        tvEditDate = findViewById(R.id.edit_date);
        tvEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        tvWelcome = findViewById(R.id.txtWelcome);
        tvEnterStart = findViewById(R.id.txtEnterPoint);
        tvEnterDestination = findViewById(R.id.txtEnterDestination);
        tvEnterDate = findViewById(R.id.txtEnterDate);
        imgLogo = findViewById(R.id.imgLogo);
        tvUsername = findViewById(R.id.txtUsername);
        getNamaUser();

        etStartArea = findViewById(R.id.btn_start_area);
        etDestinationArea = findViewById(R.id.btn_dest_area);
        etDestinationCity = findViewById(R.id.btn_dest_city);
        etStartCity = findViewById(R.id.btn_start_city);

        greetImg = findViewById(R.id.layoutHeader);
        greeting();

        btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!Tanggal.equals(""))&&(!Hari.equals(""))){
                    StartCity = etStartCity.getSelectedItem().toString().trim();
                    StartArea = etStartArea.getSelectedItem().toString().trim();
                    DestinationCity = etDestinationCity.getSelectedItem().toString().trim();
                    DestinationArea = etDestinationArea.getSelectedItem().toString().trim();
                    if(!StartArea.equals(DestinationArea)){
                        Intent intent = new Intent(getApplicationContext(),DamriStartTrip.class);
                        intent.putExtra("start_address", StartCity + ", " + StartArea);
                        intent.putExtra("destination_address", DestinationCity + ", " + DestinationArea);
                        intent.putExtra("From", "Trip User Home");
                        intent.putExtra("Tanggal", Tanggal);
                        intent.putExtra("Hari", Hari);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Tujuan tidak valid, silahkan ulangi !", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Tentukan Tanggal Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18){
            greetImg.setBackgroundResource(R.drawable.header_morning);

            ArrayAdapter<CharSequence> adapter_area = ArrayAdapter.createFromResource(this, R.array.area, R.layout.spinner_view_morning );
            ArrayAdapter<CharSequence> adapter_city = ArrayAdapter.createFromResource(this, R.array.city, R.layout.spinner_view_morning );

            adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etStartCity.setAdapter(adapter_city);
            etDestinationCity.setAdapter(adapter_city);
            etDestinationCity.setOnItemSelectedListener(this);
            etStartCity.setOnItemSelectedListener(this);

            adapter_area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etStartArea.setAdapter(adapter_area);
            etDestinationArea.setAdapter(adapter_area);
            etStartArea.setOnItemSelectedListener(this);
            etDestinationArea.setOnItemSelectedListener(this);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {

            tvWelcome.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnterStart.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnterDestination.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnterDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvEditDate.setTextColor(Color.parseColor("#FFFFFF"));
            imgLogo.setImageResource(R.drawable.jaramba_logo_night);
            tvUsername.setTextColor(Color.parseColor("#FFFFFF"));
            greetImg.setBackgroundResource(R.drawable.header_night);

            ArrayAdapter<CharSequence> adapter_area = ArrayAdapter.createFromResource(this, R.array.area, R.layout.spinner_view_night );
            ArrayAdapter<CharSequence> adapter_city = ArrayAdapter.createFromResource(this, R.array.city, R.layout.spinner_view_night );

            adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etStartCity.setAdapter(adapter_city);
            etDestinationCity.setAdapter(adapter_city);
            etDestinationCity.setOnItemSelectedListener(this);
            etStartCity.setOnItemSelectedListener(this);

            adapter_area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etStartArea.setAdapter(adapter_area);
            etDestinationArea.setAdapter(adapter_area);
            etStartArea.setOnItemSelectedListener(this);
            etDestinationArea.setOnItemSelectedListener(this);
        }
    }

    private void getNamaUser() {
        //progress dialog
        progressDialog();

        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = ""+ds.child("Nama_Lengkap").getValue();

                    //set data
                    tvUsername.setText(name);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        String nowDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvEditDate.setText(nowDate);
        Tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
        Hari = new SimpleDateFormat("EEEE").format(calendar.getTime());
    }
}
