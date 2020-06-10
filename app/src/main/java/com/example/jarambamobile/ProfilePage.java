package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilePage extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    private TextView nameTv, emailTv, phoneTv;
    private ImageView avatarIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");



        nameTv = findViewById(R.id.tv_profil_username);
        emailTv = findViewById(R.id.tv_profil_email);
        phoneTv = findViewById(R.id.tv_profil_phone);
        avatarIv = findViewById(R.id.img_profile_page);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = ""+ds.child("Nama Lengkap").getValue();
                    String email = ""+ds.child("Email").getValue();
                    String phone = ""+ds.child("Nomor handphone").getValue();
                    String image = ""+ds.child("Image").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

//                    try {
//                        //if image is received then set
//                        Picasso.get().load(image).into(avatarIv);
//                    } catch (Exception e) {
//                        //if there is any exception while getting image then set default
//                        Picasso.get().load(R.drawable.ic_face_black_24dp).into(avatarIv);
//                    }



                    progressDialog.dismiss();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginPage.class));
        finish();
    }

    public void userSettings(View view) {
        startActivity(new Intent(this, SettingProfilePage.class));
    }

    public void changePictureProfileUser(View view) {
        String option[] = {"Kamera","Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih mode pengambilan gambar");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //kamera clicked
                    Toast.makeText(ProfilePage.this, "Ada memilih kamera", Toast.LENGTH_SHORT).show();
                } else if (which == 1) {
                    Toast.makeText(ProfilePage.this, "Ada memilih galeri", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.create().show();
    }
}
