package com.example.jarambamobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class History extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private DatabaseReference database;
    FirebaseAuth firebaseAuth;

    TextView tvTrip, tvWith;
    ConstraintLayout greetImg;
    ImageView imgLogo;

    private ArrayList<getAllHistory> getAllHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_history);

        recyclerView = findViewById(R.id.rv_dtlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        tvTrip = findViewById(R.id.txtTrip);
        tvWith = findViewById(R.id.txtWith);
        greetImg = findViewById(R.id.layoutHeader);
        imgLogo = findViewById(R.id.imgLogo);

        greeting();

        database.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllHistory.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    getAllHistory data = new getAllHistory();
                    data = noteDataSnapshot.getValue(getAllHistory.class);
                    data.setKey(noteDataSnapshot.getKey());

                    getAllHistory.add(data);
                }

                Collections.reverse(getAllHistory);
                recyclerAdapter = new RecyclerAdapter(getAllHistory);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        ChipNavigationBar bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.nav_history,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_home:
                        startActivity(new Intent(History.this, HomeActivity.class));
                        finish();
                        break;
                    case R.id.nav_trip:
                        startActivity(new Intent(History.this, TripUserHome.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(History.this, ProfilePage.class));
                        finish();
                        break;
                }
            }
        });
    }

    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18){
            greetImg.setBackgroundResource(R.drawable.header_morning);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            tvTrip.setTextColor(Color.parseColor("#FFFFFF"));
            tvWith.setTextColor(Color.parseColor("#FFFFFF"));
            greetImg.setBackgroundResource(R.drawable.header_night);
            imgLogo.setImageResource(R.drawable.jaramba_logo_night);
        }
    }
}