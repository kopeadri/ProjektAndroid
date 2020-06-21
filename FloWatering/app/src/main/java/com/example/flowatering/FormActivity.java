package com.example.flowatering;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class FormActivity extends AppCompatActivity {

    private EditText name, species, photo, extraInfo;
    private TextView freqValue;
    private Button saveButton;
    private DataBaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        database = new DataBaseHelper(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new AssertionError();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        species = findViewById(R.id.species);
        photo = findViewById(R.id.photo);
        extraInfo = findViewById(R.id.extraInfo);
        freqValue = findViewById(R.id.freqValue);
        saveButton = findViewById(R.id.saveButton);
        SeekBar freqBar = findViewById(R.id.seekBar);


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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getData(v);
            }
        });
    }




    //DODAC SPRAWDZAJACE IFY - WYSYLAJACE ODPOWIEDNIA MESSAGE W SENDBROADCAST
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getData(View v){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String name_ = name.getText().toString();
        String species_ = species.getText().toString();
        Integer is_watered = 1;

        String last_watered = sdf.format(date);
        Integer how_often = Integer.parseInt(freqValue.getText().toString());
        String photo_ = photo.getText().toString();
        database.insertFlower(name_,species_,is_watered,last_watered,how_often,photo_);
        sendBroadcast(v,"Zapisano nową roślinkę");
        finish();
    }

    public void sendBroadcast(View v, String message) {
        Intent intent = new Intent("com.example.MY_ACTION");
        intent.putExtra("com.example.MY_ACTION", message);
        sendBroadcast(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}
