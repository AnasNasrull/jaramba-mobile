package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spark.submitbutton.SubmitButton;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RegisterPage extends AppCompatActivity {

    private EditText etEmail, etNomor, etNama, etPassword;

    private TextInputLayout txtPass;
    private ImageView img_logo;
    private TextView slogan, nextslogan, daftar, backto;
    private SubmitButton register;

    Animation rightin_anim,top_anim, bottom_anim;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation = new AwesomeValidation(
            ValidationStyle.BASIC);

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //casting view
        etEmail = findViewById(R.id.et_email_register);
        etNomor = findViewById(R.id.et_nomor_register);
        etNama = findViewById(R.id.et_nama_register);
        etPassword = findViewById(R.id.et_password_register);


        databaseReference = FirebaseDatabase.getInstance().getReference("Mobile_Apps");
        firebaseAuth = FirebaseAuth.getInstance();

        rightin_anim = AnimationUtils.loadAnimation(this,R.anim.right_in);
        top_anim = AnimationUtils.loadAnimation(this,R.anim.splash_top);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.splash_bottom);

        img_logo = findViewById(R.id.img_logo);
        daftar = findViewById(R.id.txt_daftar);
        slogan = findViewById(R.id.txt_slogan);
        nextslogan = findViewById(R.id.txt_nextslogan);
        txtPass = findViewById(R.id.txt_password);

        register = findViewById(R.id.btn_register);
        backto = findViewById(R.id.txt_backto);

        img_logo.setAnimation(top_anim);
        daftar.setAnimation(top_anim);
        slogan.setAnimation(top_anim);
        nextslogan.setAnimation(top_anim);

        etEmail.setAnimation(rightin_anim);
        etNomor.setAnimation(rightin_anim);
        etNama.setAnimation(rightin_anim);
        etPassword.setAnimation(rightin_anim);
        txtPass.setAnimation(rightin_anim);

        register.setAnimation(rightin_anim);
        backto.setAnimation(rightin_anim);

    }



    public void backToLogin(View view) {
        startActivity(new Intent(this, LoginPage.class));
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

    public void btnPendaftaran(View view) {
        awesomeValidation.addValidation(this, R.id.et_nama_register,
                RegexTemplate.NOT_EMPTY,R.string.invalid_name);

        awesomeValidation.addValidation(this, R.id.et_email_register,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.et_password_register,
                ".{6,}",R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.et_nomor_register,
                RegexTemplate.NOT_EMPTY,R.string.invalid_number);



        if(awesomeValidation.validate()){
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            final  String email  = etEmail.getText().toString().trim();
            final String number = etNomor.getText().toString().trim();
            final String username = etNama.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                                    String email = user.getEmail();
                                                    String uid = user.getUid();

                                                    HashMap <Object, String> hashMap = new HashMap<>();
                                                    hashMap.put("Email", email);
                                                    hashMap.put("Nomor_Handphone", number);
                                                    hashMap.put("Nama_Lengkap", username);
                                                    hashMap.put("Unique_ID", uid );
                                                    hashMap.put("Image", "");

                                                    byte[] inputData = etPassword.getText().toString().getBytes();
                                                    byte[] outputData = new byte[0];

                                                    try {
                                                        outputData = sha.encryptSHA(inputData, "SHA-256");
                                                    } catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                    BigInteger shaData = new BigInteger(1, outputData);
                                                    String shaValue = shaData.toString(16);
                                                    hashMap.put("Password", shaValue);

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference reference = database.getReference("Mobile_Apps");
                                                    reference.child("User").child(uid).setValue(hashMap);

                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                                                    builder.setIcon(R.drawable.ic_check_black_24dp);
                                                    builder.setTitle("Berhasil mendaftar");
                                                    builder.setMessage("Silahkan cek pesan pada email : " + email + "\nuntuk verifikasi pengguna");

                                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            startActivity(new Intent(RegisterPage.this, LoginPage.class));
                                                            finish();
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();

                                                } else {
                                                    Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterPage.this, "Maaf terdapat gangguan pada sistem", Toast.LENGTH_LONG).show();
                            }
                        }
                    });



        }



    }
}
