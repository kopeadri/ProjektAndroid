package com.example.flowatering;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity implements PlantsListFragment.PlantsListFragmentActivityListener {

    private NotificationManagerCompat notificationManager;
    MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("com.example.MY_ACTION");
        registerReceiver(broadcastReceiver, filter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(String msg) {
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details);

        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(msg);
        }else {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("msg", msg);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


}
