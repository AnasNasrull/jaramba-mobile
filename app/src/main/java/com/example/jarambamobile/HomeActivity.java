package com.example.jarambamobile;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.Calendar;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference database;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    ImageView greetImg;
    TextView tvUsername;

//    TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("Mobile_Apps").child("User");

        //init progress dialog
        progressDialog = new ProgressDialog(HomeActivity.this);

        tvUsername = findViewById(R.id.name_user);
        getNamaUser();


        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_trip:
                        startActivity(new Intent(HomeActivity.this, TripUserHome.class));
                        finish();
                        break;
                    case R.id.nav_history:
                        startActivity(new Intent(HomeActivity.this, History.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(HomeActivity.this, ProfilePage.class));
                        finish();
                        break;
                }

                return false;
            }
        });

        greetImg = findViewById(R.id.greeting_img);

        greeting();

//        nameUser = findViewById(R.id.name_user);

//        database = FirebaseDatabase.getInstance().getReference();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        String uid = user.getUid();
//
//        database.child("Mobile_Apps").child("User").child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    String name = ""+noteDataSnapshot.child("Nama_Lengkap").getValue();
//
//                    nameUser.setText(name);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                //Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

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

    public void clickTrain(View view) {
        startActivity(new Intent(this, UnderConstructionScreen.class));
    }

    public void clickAngkot(View view) {
        startActivity(new Intent(this, UnderConstructionScreen.class));
    }

    public void clickMonorail(View view) {
        startActivity(new Intent(this, UnderConstructionScreen.class));
    }

    public void clickTravel(View view) {
        startActivity(new Intent(this, UnderConstructionScreen.class));
    }
  
}

