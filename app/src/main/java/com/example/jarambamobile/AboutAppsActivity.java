package com.example.jarambamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class AboutAppsActivity extends AppCompatActivity {


    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_apps);

        imgLogo = findViewById(R.id.logo_colorful);

        Glide.with(AboutAppsActivity.this).load(R.drawable.jaramba_logo_04).into(imgLogo);


    }
}
