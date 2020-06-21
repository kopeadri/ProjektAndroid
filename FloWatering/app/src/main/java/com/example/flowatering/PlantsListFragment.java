package com.example.flowatering;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.flowatering.MyApplication.CHANNEL_2_ID;
import static com.example.flowatering.MyApplication.getAppContext;

public class PlantsListFragment extends Fragment {
    private NotificationManagerCompat notificationManager;

    private static PlantsListFragmentActivityListener listener;

    View rootView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBaseHelper database = new DataBaseHelper(getActivity());

        database.clean();
        database.insertFlower("Storczyk","Storczyk",1,"2020-06-21",5,"https://kwiaciarniaegzotyka.pl/wp-content/uploads/2018/10/DSC_3951-e1563400035543.jpg");
        database.insertFlower("Kaktusik","Kaktus",0,"2020-06-17",3,"https://1.bonami.pl/images/products/90/dd/90ddf805e79cada71e26c9733895cf84822f2074-1000x1000.jpeg");

        rootView = inflater.inflate(R.layout.plantslist_fragment, container, false);

        ListData[] myListData = new ListData[database.numberOfRows()];
        List<List<String>> dataList = database.getAllFlowers();
        int pic;

        for (int i=0; i<database.numberOfRows(); i++) {
            Log.d("pic",dataList.get(i).get(1));
            if (dataList.get(i).get(1).equals("1")){
                pic = R.drawable.ic_good;
            }else{
                pic = R.drawable.ic_bad;
            }
            myListData[i] = new ListData( dataList.get(i).get(0), pic);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        ListAdapter adapter = new ListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        FloatingActionButton add = rootView.findViewById(R.id.addButton);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FormActivity.class);
                    startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateView(){
        DataBaseHelper database = new DataBaseHelper(getActivity());
        ListData[] myListData = new ListData[database.numberOfRows()];
        List<List<String>> dataList = database.getAllFlowers();
        int pic;

        for (int i=0; i<database.numberOfRows(); i++) {
            Log.d("pic",dataList.get(i).get(1));
            if (dataList.get(i).get(1).equals("1")){
                pic = R.drawable.ic_good;
            }else{
                pic = R.drawable.ic_bad;
                notificationManager = NotificationManagerCompat.from(getAppContext());
                String plant_name = dataList.get(i).get(0);
                plantWarningNotif(plant_name);
            }
            myListData[i] = new ListData( dataList.get(i).get(0), pic);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        ListAdapter adapter = new ListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    // interfejs, który będzie implementować aktywność
    public interface PlantsListFragmentActivityListener {
        void onItemSelected(String msg);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (PlantsListFragmentActivityListener) activity;
    }

    // metoda wysyła dane do aktywności
    static void updateDetail(String msg) {
        listener.onItemSelected(msg);
    }

    public void plantWarningNotif(String plantname) {
        String title = plantname+" potrzebuje wody!";
        String message = "Twoja roślinka o imieniu "+plantname+" już dawno nie była podlewana";

        Intent activityIntent = new Intent(getAppContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getAppContext(),
                0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(getAppContext(), CHANNEL_2_ID)
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


