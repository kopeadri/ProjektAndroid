package com.example.flowatering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlantsListFragment extends Fragment {

    private static PlantsListFragmentActivityListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.plantslist_fragment, container, false);

        ListData[] myListData = new ListData[] {
                new ListData("Email", android.R.drawable.ic_dialog_email),
                new ListData("Info", android.R.drawable.ic_dialog_info),
                new ListData("Delete", android.R.drawable.ic_delete),
                new ListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new ListData("Alert", android.R.drawable.ic_dialog_alert),
                new ListData("Map", android.R.drawable.ic_dialog_map),
                new ListData("Email", android.R.drawable.ic_dialog_email),
                new ListData("Info", android.R.drawable.ic_dialog_info),
                new ListData("Delete", android.R.drawable.ic_delete),
                new ListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new ListData("Alert", android.R.drawable.ic_dialog_alert),
                new ListData("Map", android.R.drawable.ic_dialog_map),
        };

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


}


