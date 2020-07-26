package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class SettingProfilePage extends AppCompatActivity {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    Dialog dialog;


    ActionBar actionBar;
    ProgressDialog progressDialog;

    Button btnConfChangePassword, btnConfNameNumber, btnDismissChangeEmail, btnDismissNameNumber, btnConfEmail, btnDismissEmail;
    EditText etChangePassword, etChangeName, etChangeNumber, etChangeEmail, etPasswordValidateChangeEmail;
    TextView tvKeteranganubahNamaNomor;
    ImageView icUsername, icEmail, icHandphone, icPassword, icAbout, whiteLogo08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Mobile_Apps").child("User");
        storageReference = FirebaseStorage.getInstance().getReference(); //firebase storage refeernce

        actionBar = getSupportActionBar();
        actionBar.setTitle("Pengaturan");

        icUsername = findViewById(R.id.icon_username);
        icEmail = findViewById(R.id.icon_email);
        icHandphone = findViewById(R.id.icon_phone);
        icPassword = findViewById(R.id.icon_password);
        icAbout = findViewById(R.id.icon_about_app);
        whiteLogo08 = findViewById(R.id.logo_white);

        Glide.with(SettingProfilePage.this).load(R.drawable.ic_person_black_24dp).into(icUsername);
        Glide.with(SettingProfilePage.this).load(R.drawable.ic_email_black_24dp).into(icEmail);
        Glide.with(SettingProfilePage.this).load(R.drawable.ic_local_phone_black_24dp).into(icHandphone);
        Glide.with(SettingProfilePage.this).load(R.drawable.ic_lock_black_24dp).into(icPassword);
        Glide.with(SettingProfilePage.this).load(R.drawable.ic_info_black_24dp).into(icAbout);
        Glide.with(SettingProfilePage.this).load(R.drawable.logo08).into(whiteLogo08);

        progressDialog  = new ProgressDialog(SettingProfilePage.this);

    }

    public void changeEmail(View view) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ubah_email);
        etPasswordValidateChangeEmail = dialog.findViewById(R.id.et_validatepassword);
        etChangeEmail = dialog.findViewById(R.id.et_ubahemail);
        btnConfEmail = dialog.findViewById(R.id.btn_conf_email);
        btnDismissEmail = dialog.findViewById(R.id.btn_dismiss_email);



        btnDismissEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        btnConfEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!Patterns.EMAIL_ADDRESS.matcher(etChangeEmail.getText().toString().trim()).matches()){
                    etChangeEmail.setError("Maaf email anda tidak valid");
                    etChangeEmail.setFocusable(true);
                }

                else {
                    progressDialog();
                    final String value1= etPasswordValidateChangeEmail.getText().toString().trim();
                    final String value = etChangeEmail.getText().toString().trim();
                    final String email= user.getEmail();


                    Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                //get data
                                final String password = ""+ds.child("Password").getValue();
                                final String passwords;

                                byte[] inputData = etPasswordValidateChangeEmail.getText().toString().getBytes();
                                byte[] outputData = new byte[0];

                                try {
                                    outputData = sha.encryptSHA(inputData, "SHA-256");
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                BigInteger shaData = new BigInteger(1, outputData);
                                passwords = shaData.toString(16);

                              //  Toast.makeText(SettingProfilePage.this, password + "\n\n" + passwords, Toast.LENGTH_LONG).show();

                                if(!TextUtils.isEmpty(value)){
                                    firebaseAuth.fetchSignInMethodsForEmail(value)
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                    boolean check = !task.getResult().getSignInMethods().isEmpty();

                                                    if(!check) {
                                                        if (password.equals(passwords)) {


                                                            AuthCredential credential = EmailAuthProvider
                                                                    .getCredential(email, passwords);

                                                            user.reauthenticate(credential)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                            user.updateEmail(value)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                firebaseAuth = FirebaseAuth.getInstance();
                                                                                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if(task.isSuccessful()) {
                                                                                                                    dialog.dismiss();
                                                                                                                    progressDialog.dismiss();
                                                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfilePage.this);
                                                                                                                    builder.setIcon(R.drawable.ic_check_black_24dp);
                                                                                                                    builder.setTitle("Berhasil mengubah email");
                                                                                                                    builder.setMessage("Silahkan cek pesan pada email terbaru anda untuk verifikasi pengguna");

                                                                                                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                            FirebaseAuth.getInstance().signOut();
                                                                                                                            startActivity(new Intent(SettingProfilePage.this, LoginPage.class));
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    });
                                                                                                                    AlertDialog alertDialog = builder.create();
                                                                                                                    alertDialog.show();

                                                                                                                } else {
                                                                                                                    Toast.makeText(SettingProfilePage.this, "Maaf, aplikasi gagal mengirim pesan ke email anda", Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }
                                                                                                        });

                                                                                            } else {
                                                                                                dialog.dismiss();
                                                                                                progressDialog.dismiss();
                                                                                                Toast.makeText(SettingProfilePage.this, "Maaf, aplikasi tidak dapat mengirim pesan ke email anda", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });

                                                            final String key = "Email";
                                                            HashMap<String, Object> result = new HashMap<>();
                                                            result.put(key, value);

                                                            databaseReference.child(user.getUid()).updateChildren(result)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            //updated, dismiss progress
//                                                                            progressDialog.dismiss();
                                                                            dialog.dismiss();
                                                                            Toast.makeText(SettingProfilePage.this, "Data " + key + " anda diperbarui...", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(SettingProfilePage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });





                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(SettingProfilePage.this, "Maaf, kata sandi tidak sesuai", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(SettingProfilePage.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingProfilePage.this, "Tolong masukkan email anda dengan benar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    public void changePassword(View view) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ubah_password);
        etChangePassword = dialog.findViewById(R.id.et_konfirmasi);
        btnConfChangePassword = dialog.findViewById(R.id.btn_ubahpassword);
        btnDismissChangeEmail = dialog.findViewById(R.id.btn_dismiss_ubahpassword);

        btnDismissChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = etChangePassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etChangePassword.setError("Maaf email anda tidak valid");
                    etChangePassword.setFocusable(true);
                } else {

                    progressDialog();
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                        //Toast.makeText(SettingProfilePage.this, "Email anda terkonfirmasi, silahkan cek email anda untuk ubah kata sandi", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfilePage.this);
                                        builder.setIcon(R.drawable.ic_check_black_24dp);
                                        builder.setTitle("Email anda terkonfirmasi");
                                        builder.setMessage("silahkan cek email anda untuk ubah kata sandi");

                                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(SettingProfilePage.this, LoginPage.class));
                                                finish();
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingProfilePage.this, "Email anda tidak terkonfirmasi, cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingProfilePage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void changeUsername(View view) {
        showNamePhoneUpdateDialog("Nama_Lengkap");
    }

    public void changePhoneNumber(View view) {
        showNamePhoneUpdateDialog("Nomor_Handphone");
    }

    @SuppressLint("SetTextI18n")
    private void showNamePhoneUpdateDialog(final String key) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ubah_nama_nomor);
        btnConfNameNumber = dialog.findViewById(R.id.btn_ubah);
        btnDismissNameNumber = dialog.findViewById(R.id.btn_dismiss_ubah);
        etChangeName = dialog.findViewById(R.id.et_ubahnama);
        etChangeNumber = dialog.findViewById(R.id.et_ubahnomor);
        tvKeteranganubahNamaNomor = dialog.findViewById(R.id.tv_keterangan_ubahnamanomor);

        String tv = tvKeteranganubahNamaNomor.getText().toString().trim();


        if(key.equals("Nama_Lengkap")) {
            etChangeNumber.setVisibility(View.GONE);
            tvKeteranganubahNamaNomor.setText(tv + " nama lengkap baru ");

        } else {
            etChangeName.setVisibility(View.GONE);
            tvKeteranganubahNamaNomor.setText(tv +  " nomor handphone baru");
        }

        btnDismissNameNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfNameNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name = etChangeName.getText().toString().trim();
               String number = etChangeNumber.getText().toString().trim();

               if(!name.isEmpty() || !number.isEmpty()){
                   progressDialog();
                   String value;

                   if(name.isEmpty()) {
                       value = number;
                   } else {
                       value = name;
                   }

                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //updated, dismiss progress
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(SettingProfilePage.this,  "data anda diperbarui...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingProfilePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
               } else {
                   Toast.makeText(SettingProfilePage.this, "Mohon masukkan " + key + " dengan benar", Toast.LENGTH_SHORT).show();
               }

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void infoApps(View view) {
        startActivity(new Intent(SettingProfilePage.this, AboutAppsActivity.class));

    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }



}
