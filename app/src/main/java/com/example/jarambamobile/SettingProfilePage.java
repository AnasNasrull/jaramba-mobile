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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Set;

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


                if(!TextUtils.isEmpty(value)){


//                        AuthCredential credential = EmailAuthProvider
//                                .getCredential(email, "gantengin");
//
//                        user.reauthenticate(credential)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                        user.updateEmail(value)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(SettingProfilePage.this, "updated", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                });
//
//                                    }
//                                });


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
