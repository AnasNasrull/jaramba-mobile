package com.example.jarambamobile;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {

    ImageView greetImg;

    private DatabaseReference database;
    FirebaseAuth firebaseAuth;
    TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.home);

        greeting();

        nameUser = findViewById(R.id.name_user);

        database = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();

        database.child("Mobile_Apps").child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String name = ""+noteDataSnapshot.child("Nama_Lengkap").getValue();

                    nameUser.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18){
            greetImg.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }
    }

    public void clickDamri(View view) {
        startActivity(new Intent(this, TripUser.class));
    }
  
}

