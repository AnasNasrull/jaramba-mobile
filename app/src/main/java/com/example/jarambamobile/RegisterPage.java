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

import java.security.MessageDigest;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RegisterPage extends AppCompatActivity {

    private EditText etEmail, etNomor, etNama, etPassword;
    private Button btnRegister;

    private TextInputLayout txtPass;
    private ImageView img_logo;
    private TextView slogan, nextslogan, daftar, backto;
    private SubmitButton register;

    Animation rightin_anim,top_anim, bottom_anim;

    String outputString;
    String passwords;
    String AES = "AES";
    String pass = "testmypassword";

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

            try {
                outputString = encrypt(password, pass);
                passwords = outputString;
            } catch (Exception e) {
                e.printStackTrace();
            }


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
                                                    etEmail.setText("");
                                                    etNomor.setText("");
                                                    etNama.setText("");
                                                    etPassword.setText("");

                                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                                    String email = user.getEmail();
                                                    String uid = user.getUid();

                                                    HashMap <Object, String> hashMap = new HashMap<>();
                                                    hashMap.put("Email", email);
                                                    hashMap.put("Nomor_Handphone", number);
                                                    hashMap.put("Nama_Lengkap", username);
                                                    hashMap.put("Unique_ID", uid );
                                                    hashMap.put("Image", "");

                                                    try {
                                                        outputString = encrypt(etPassword.getText().toString().trim(), pass);
                                                        hashMap.put("Password", outputString);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference reference = database.getReference("Mobile_Apps");
                                                    reference.child("User").child(uid).setValue(hashMap);

                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                                                    builder.setIcon(R.drawable.ic_check_black_24dp);
                                                    builder.setTitle("Berhasil mendaftar");
                                                    builder.setMessage("Silahkan cek pesan pada email : " + email + "\nuntuk verifikasi pengguna");

                                                    builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
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


    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws  Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
