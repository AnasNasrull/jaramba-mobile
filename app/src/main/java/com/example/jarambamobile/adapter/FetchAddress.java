package com.example.jarambamobile.adapter;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

public class FetchAddress extends IntentService {
    private ResultReceiver resultReceiver;

    public FetchAddress(){
        super("FetchAddress");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void deliverResultToReceiver(int resultCode, String addressMessage) {

    }
}
