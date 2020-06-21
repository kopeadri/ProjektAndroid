package com.example.flowatering;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("com.example.MY_ACTION".equals(intent.getAction())) {
            String receivedText = intent.getStringExtra("com.example.MY_ACTION");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}
