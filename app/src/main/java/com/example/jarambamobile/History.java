package com.example.jarambamobile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {
    private ImageView info;
    private ImageView rate;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        info = findViewById(R.id.iconinfo1);
        rate = findViewById(R.id.rate);
        status = findViewById(R.id.status_list);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), status);
                dropDownMenu.getMenuInflater().inflate(R.menu.list_status, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 = new Dialog(History.this);

                dialog1.setContentView(R.layout.history_detail);

                dialog1.show();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(History.this);

                dialog.setContentView(R.layout.history_rating);

                Button Submit = dialog.findViewById(R.id.submit_rate);
                final RatingBar Rating = dialog.findViewById(R.id.ratingBar);
                final EditText Komentar = dialog.findViewById(R.id.comment_rate);
                final TextView Harga = dialog.findViewById(R.id.harga_rate);
                final TextView Pembayaran = dialog.findViewById(R.id.crbyr_rate);

                Submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rating = "Rating is : " + Rating.getRating() +"\nKomentar : " + Komentar.getText();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("history_user_app");

                        myRef.setValue(new history_rating_data(Rating.getRating(), Komentar.getText().toString(), Harga.getText().toString(), Pembayaran.getText().toString()));

                        Toast.makeText(History.this, rating, Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.trip:
                        startActivity(new Intent(getApplicationContext()
                                ,TripUser.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,ProfilePage.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }
}
