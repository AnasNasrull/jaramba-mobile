package com.example.jarambamobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingProfilePage extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_page);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Pengaturan");

    }

    public void changeUsername(View view) {

    }

    public void infoApps(View view) {
        startActivity(new Intent(SettingProfilePage.this, AboutAppsActivity.class));

    }
}
