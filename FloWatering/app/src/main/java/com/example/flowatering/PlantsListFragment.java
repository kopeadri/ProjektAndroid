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
                new ListData("Kwiatek 1", R.drawable.ic_good),
                new ListData("Kwiatek 2", R.drawable.ic_neutral),
                new ListData("Kwiatek 3", R.drawable.ic_bad)
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


