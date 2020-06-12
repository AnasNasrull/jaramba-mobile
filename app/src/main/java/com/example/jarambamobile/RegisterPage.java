package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    private EditText etEmail, etNomor, etNama, etPassword;
    private Button btnRegister;

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


        databaseReference = FirebaseDatabase.getInstance().getReference(
                "User");
        firebaseAuth = FirebaseAuth.getInstance();

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
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                String email = user.getEmail();
                                String uid = user.getUid();

                                HashMap <Object, String> hashMap = new HashMap<>();
                                hashMap.put("Email", email);
                                hashMap.put("Nomor handphone", number);
                                hashMap.put("Nama Lengkap", username);
                                hashMap.put("Unique ID", uid );
                                hashMap.put("image", "");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("User");
                                reference.child(uid).setValue(hashMap);

                                progressDialog.dismiss();
                                Toast.makeText(RegisterPage.this, "Email anda : " + email + "\nSukses terdaftar pada sistem", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                finish();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterPage.this, "Maaf terdapat gangguan pada sistem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }



    }
}
