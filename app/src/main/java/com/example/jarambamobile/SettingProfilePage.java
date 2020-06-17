package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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


    ActionBar actionBar;
    ProgressDialog progressDialog;

    String outputString;
    String password, passwords;
    String AES = "AES";
    String pass = "testpassword";


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

        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext()
                                ,History.class));
                        overridePendingTransition(0,0);
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
                        return true;

                }
                return false;
            }
        });

    }

    public void changeEmail(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Memperbarui Email");
        builder.setMessage("Masukkan kata sandi sebagai autentikasi pengubahan email anda");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //add edit text
        final EditText editText2 = new EditText(this);
        editText2.setHint("Masukkan email baru");
        linearLayout.addView(editText2);

        builder.setView(linearLayout);

        builder.setPositiveButton("Perbarui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                progressDialog();
                final String value = editText2.getText().toString().trim();
                final String email= user.getEmail();

//                Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                            //get data
//                            password = ""+ds.child("password").getValue();
//
//                            try {
//                                outputString = decrypt(password, pass);
//                                //decrypted password
//                                passwords = outputString;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });





                if(!TextUtils.isEmpty(value)){
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, "ganteng");

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


                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SettingProfilePage.this, "Tolong masukkan email anda dengan benar", Toast.LENGTH_SHORT).show();
                }


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Memperbarui kata sandi");
        builder.setMessage("Jaramba akan mengirimkan pesan melalui email anda, kemudian anda dapat mengubah kata sandi melalui pesan tersebut");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //add edit text
        final EditText editText = new EditText(this);
        editText.setHint("Masukkan email anda");
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Perbarui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //input text from edit text
                final String value = editText.getText().toString().trim();

                if(!TextUtils.isEmpty(value)) {
                    progressDialog = new ProgressDialog(SettingProfilePage.this);
                    progressDialog();

                    firebaseAuth.sendPasswordResetEmail(editText.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingProfilePage.this, "Sukses, silahkan buka Email/Gmail anda untuk ubah kata sandi", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SettingProfilePage.this, "Maaf, silahkan periksa kembali Email/Gmail anda, pastikan terhubung Internet", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(SettingProfilePage.this, "Tolong masukkan email anda dengan benar", Toast.LENGTH_SHORT).show();
                }
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

    public void changeUsername(View view) {
        showNamePhoneUpdateDialog("Nama Lengkap");
    }

    public void changePhoneNumber(View view) {
        showNamePhoneUpdateDialog("Nomor handphone");
    }

    private void showNamePhoneUpdateDialog(final String key) {
        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Memperbarui " + key);

        //set Layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //add edit text
        final EditText editText = new EditText(this);
        editText.setHint("Masukkan " +key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add buttons in dialog
        builder.setPositiveButton("Perbarui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from editText
                final String value = editText.getText().toString().trim();

                //validate if user has enterd something or not
                if(!TextUtils.isEmpty(value)) {
                    progressDialog();
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
                    Toast.makeText(SettingProfilePage.this, "tolong masukkan " + key, Toast.LENGTH_SHORT).show();
                }
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

    public void infoApps(View view) {
        startActivity(new Intent(SettingProfilePage.this, AboutAppsActivity.class));

    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }



}
