package com.example.jarambamobile;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference database;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    ConstraintLayout greetImg;
    TextView tvUsername, tvWelcome;
    ImageView imgLogo;

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

        tvUsername = findViewById(R.id.txtUsername);
        getNamaUser();


        ChipNavigationBar  bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.nav_home,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
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
            }
        });

        greetImg = findViewById(R.id.layoutHeader);
        tvWelcome = findViewById(R.id.txtWelcome);
        imgLogo = findViewById(R.id.imgLogo);

        greeting();

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
            greetImg.setBackgroundResource(R.drawable.header_morning);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetImg.setBackgroundResource(R.drawable.header_night);
            tvWelcome.setTextColor(Color.parseColor("#FFFFFF"));
            tvUsername.setTextColor(Color.parseColor("#FFFFFF"));
            imgLogo.setImageResource(R.drawable.jaramba_logo_night);
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

    public void clickRide(View view) {
        startActivity(new Intent(this, UnderConstructionScreen.class));
    }
  
}

