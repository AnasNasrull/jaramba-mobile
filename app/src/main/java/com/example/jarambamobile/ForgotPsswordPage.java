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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPsswordPage extends AppCompatActivity {

    private EditText etEmail;

    private AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pssword_page);


        //casting view
        etEmail = findViewById(R.id.et_email_konfirmasi);


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

    //ketika tombol Confirm ditekan maka link reset password akan di berikan melalui email
    public void forgotPassword(View view) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.et_email_konfirmasi,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        if(awesomeValidation.validate()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            firebaseAuth.sendPasswordResetEmail(etEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgotPsswordPage.this, "Sukses, silahkan buka Email/Gmail anda untuk ubah kata sandi", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ForgotPsswordPage.this, "Maaf, silahkan periksa kembali Email/Gmail anda, pastikan terhubung Internet", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });

        }
    }
}
