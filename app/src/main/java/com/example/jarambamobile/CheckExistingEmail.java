package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Set;

public class CheckExistingEmail extends AppCompatActivity {

    EditText etEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_existing_email);

        auth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_emailll);


    }

    public void checkEmail(View view) {
        auth.fetchSignInMethodsForEmail(etEmail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if(!check) {
                            Toast.makeText(CheckExistingEmail.this, "kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CheckExistingEmail.this, "ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
