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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.util.HashMap;

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

    String outputString;
    String passwords;
    String AES = "AES";
    String pass = "testmypassword";

    Button btnConfChangePassword, btnConfNameNumber, btnDismissChangeEmail, btnDismissNameNumber;
    EditText etChangePassword, etChangeName, etChangeNumber;
    TextView tvKeteranganubahNamaNomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference(); //firebase storage refeernce

        actionBar = getSupportActionBar();
        actionBar.setTitle("Pengaturan");

        progressDialog  = new ProgressDialog(SettingProfilePage.this);

    }

    public void changeEmail(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Memperbarui Email");
        builder.setMessage("Masukkan kata sandi dan Email baru sebagai autentikasi pengubahan email anda");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //add edit text
        final EditText editText2 = new EditText(this);
        final EditText editText1 = new EditText(this);
        editText1.setHint("Masukkan kata sandi akun ini");
        editText1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(editText1);

        editText2.setHint("Masukkan email baru");
        linearLayout.addView(editText2);

        builder.setView(linearLayout);

        builder.setPositiveButton("Perbarui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                progressDialog();
                final String value1 = editText1.getText().toString().trim();
                final String value = editText2.getText().toString().trim();
                final String email= user.getEmail();



                Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            //get data
                           String password = ""+ds.child("password").getValue();

                            try {
                                outputString = decrypt(password, pass);
                                //decrypted password
                                passwords = outputString;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(!TextUtils.isEmpty(value) ){
                                if(value1.equals(passwords)){

                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email, passwords);

                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    user.updateEmail(value)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(SettingProfilePage.this, "updated", Toast.LENGTH_SHORT).show();
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
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingProfilePage.this,  key +" anda diperbarui...", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingProfilePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingProfilePage.this, "Tolong masukkan email anda dengan benar", Toast.LENGTH_SHORT).show();
                                }



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
        });

        //add cancel button in dialog
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //create and show dialog
        builder.create().show();
    }

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }


    private SecretKeySpec generateKey(String password) throws  Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
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
                                        Toast.makeText(SettingProfilePage.this, "Email anda terkonfirmasi, silahkan cek email anda untuk ubah kata sandi", Toast.LENGTH_SHORT).show();
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
        showNamePhoneUpdateDialog("Nama Lengkap");
    }

    public void changePhoneNumber(View view) {
        showNamePhoneUpdateDialog("Nomor handphone");
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


        if(key.equals("Nama Lengkap")) {
            etChangeNumber.setVisibility(View.GONE);
            tvKeteranganubahNamaNomor.setText(tv + " " + key + " baru");

        } else {
            etChangeName.setVisibility(View.GONE);
            tvKeteranganubahNamaNomor.setText(tv +  " " + key + " baru");
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
                                    Toast.makeText(SettingProfilePage.this,  key +" anda diperbarui...", Toast.LENGTH_SHORT).show();
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
