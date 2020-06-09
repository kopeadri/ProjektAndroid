package com.example.flowatering;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private EditText name, species, extraInfo;
    private TextView lastWatering, nextWatering, freqValue;
    private Button saveButton, editButton, confirmButton;
    private SeekBar freqBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        name = rootView.findViewById(R.id.name);
        species = rootView.findViewById(R.id.species);
        extraInfo = rootView.findViewById(R.id.extraInfo);
        freqBar = rootView.findViewById(R.id.seekBar);
        saveButton = rootView.findViewById(R.id.saveButton);
        editButton = rootView.findViewById(R.id.editButton);
        confirmButton = rootView.findViewById(R.id.confirmButton);
        freqValue = rootView.findViewById(R.id.freqValue);
        freqBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                freqValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        setAbilities(false,View.INVISIBLE,View.VISIBLE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAbilities(true,View.VISIBLE,View.INVISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAbilities(false,View.INVISIBLE,View.VISIBLE);
            }
        });
        return rootView;
    }

    public void setText(String txt) {
        TextView view = (TextView) getView().findViewById(R.id.detailsText);
        view.setText(txt);
    }

    public void setAbilities(Boolean bool, int saveVisibility, int editVisibility){
        name.setEnabled(bool);
        species.setEnabled(bool);
        extraInfo.setEnabled(bool);
        freqBar.setEnabled(bool);
        saveButton.setVisibility(saveVisibility);
        editButton.setVisibility(editVisibility);
    }
}
