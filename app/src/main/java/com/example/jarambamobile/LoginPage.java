package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.math.BigInteger;
import java.util.HashMap;

public class LoginPage extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextInputLayout txtPass;
    private ImageView img_logo;
    private TextView slogan, quotes, lupa_pass, havent_account, go_register;
    private SubmitButton login;


    Animation rightin_anim,top_anim, bottom_anim;
    AwesomeValidation awesomeValidation;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference database;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            startActivity (new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("Mobile_Apps").child("User");

        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);


        rightin_anim = AnimationUtils.loadAnimation(this,R.anim.right_in);
        top_anim = AnimationUtils.loadAnimation(this,R.anim.splash_top);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.splash_bottom);

        img_logo = findViewById(R.id.img_logo);
        slogan = findViewById(R.id.txt_slogan);
        quotes = findViewById(R.id.txt_quote);
        lupa_pass = findViewById(R.id.txt_lupa);
        havent_account = findViewById(R.id.txt_havent);
        go_register = findViewById(R.id.txt_register);
        login = findViewById(R.id.btn_login);
        txtPass = findViewById(R.id.txt_password);

        img_logo.setAnimation(top_anim);
        slogan.setAnimation(top_anim);
        quotes.setAnimation(top_anim);

        etEmail.setAnimation(rightin_anim);
        etPassword.setAnimation(rightin_anim);


        lupa_pass.setAnimation(rightin_anim);
        havent_account.setAnimation(rightin_anim);
        go_register.setAnimation(rightin_anim);
        login.setAnimation(rightin_anim);
        txtPass.setAnimation(rightin_anim);

    }

    public void lupaPassword(View view) {
        startActivity(new Intent(this, ForgotPsswordPage.class));
        finish();
    }

    public void mendaftar(View view) {
        startActivity(new Intent(this, RegisterPage.class));
        finish();
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi keluar aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin keluar aplikasi ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void login(View view) {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("Mobile_Apps").child("User");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.et_email_login,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.et_password_login,
                ".{6,}", R.string.invalid_password);

        if(awesomeValidation.validate()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(LoginPage.this, "Maaf, Email atau password anda salah, dan Pastikan Anda Terhubung dengan Internet", Toast.LENGTH_SHORT).show();
                            } else {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                    //encrypt password
                                    byte[] inputData = etPassword.getText().toString().getBytes();
                                    byte[] outputData = new byte[0];

                                    try {
                                        outputData = sha.encryptSHA(inputData, "SHA-256");
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    BigInteger shaData = new BigInteger(1, outputData);
                                    final String passwords = shaData.toString(16);


                                    //get current password
                                    Query query = databaseReference.orderByChild("Email").equalTo(etEmail.getText().toString().trim());
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //load password
                                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                                //get current password from database
                                                String pwd = ""+ds.child("Password").getValue();
                                                String UID = ""+ds.child("Unique_ID").getValue();
                                               // Toast.makeText(LoginPage.this, UID, Toast.LENGTH_SHORT).show();

                                                //check if password different
                                                if(!passwords.equals(pwd)){
                                                    HashMap<String, Object> result = new HashMap<>();
                                                    result.put("Password", passwords);

                                                    databaseReference.child(UID).updateChildren(result)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    //updated, dismiss progress
                                                                    progressDialog.dismiss();
                                                                    //Toast.makeText(LoginPage.this, "Password anda telah diperbarui...", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(LoginPage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                    progressDialog.dismiss();
                                    Toast.makeText(LoginPage.this, "Selamat datang di Jaramba", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginPage.this, HomeActivity.class));
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginPage.this, "Silahkan verifikasi email anda terlebih dahulu, kami telah mengirim pesan pada email anda", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

        }
    }
}
