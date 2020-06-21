package com.example.flowatering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.flowatering.MyApplication.CHANNEL_1_ID;
import static com.example.flowatering.MyApplication.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity implements PlantsListFragment.PlantsListFragmentActivityListener {
    private NotificationManagerCompat notificationManager;
    MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = NotificationManagerCompat.from(this);
        IntentFilter filter = new IntentFilter("com.example.MY_ACTION");
        registerReceiver(broadcastReceiver, filter);

        NotifyToCheckAllPlants();
        plantWarningNotif("Kaktus Krzyś");
    }

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

    public void NotifyToCheckAllPlants() {
        String title = "Twoje roślinki potrzebują uwagi!";
        String message = "Sprawdź stan nawodnienia swoich roślin";

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }

    public void plantWarningNotif(String plantname) {
        String title = plantname+" potrzebuje wody!";
        String message = "Twoja roślinka o imieniu "+plantname+" już dawno nie była podlewana";

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(2, notification);
    }

}
