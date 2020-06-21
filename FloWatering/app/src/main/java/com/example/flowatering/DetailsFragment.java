package com.example.flowatering;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailsFragment extends Fragment {

    private EditText name, species, extraInfo;
    private TextView lastWatering, nextWatering, freqValue, statement;
    private Button saveButton, editButton, confirmButton;
    private SeekBar freqBar;
    private ImageView image;
    private DataBaseHelper database;
    private String nameStr, photoStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        image = rootView.findViewById(R.id.image);
        name = rootView.findViewById(R.id.name);
        species = rootView.findViewById(R.id.species);
        statement = rootView.findViewById(R.id.statement);
        extraInfo = rootView.findViewById(R.id.extraInfo);
        lastWatering = rootView.findViewById(R.id.lastWatering);
        nextWatering = rootView.findViewById(R.id.nextWatering);
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

        database = new DataBaseHelper(getActivity());


        setAbilities(false,View.INVISIBLE,View.VISIBLE);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAbilities(true,View.VISIBLE,View.INVISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setAbilities(false,View.INVISIBLE,View.VISIBLE);
                updateData(false);
                updateView(nameStr);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                updateData(true);
                updateView(nameStr);
            }
        });
        return rootView;
    }

    void updateData(Boolean flag){
        Integer is_watered;
        if (flag) {
            is_watered = 1;
        }else{
            if(statement.getText().equals("Podlany!")){
                is_watered = 1;
            }else{
                is_watered = 0;
            }
        }
        Date date= new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String now = formatter.format(date);
        database.updateFlower(name.getText().toString(), species.getText().toString(),is_watered,now,Integer.parseInt(freqValue.getText().toString()),photoStr);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setText(String txt) {
        nameStr = txt;
        TextView view = (TextView) getView().findViewById(R.id.detailsText);
        view.setText(txt);
        updateView(nameStr);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateView(String txt){
        ArrayList<String> element = new ArrayList<String>();
        element = database.getData(txt);
        name.setText(txt);
        species.setText(element.get(0));
        if (element.get(1).equals("1")) {
            statement.setText("Podlany!");
            statement.setTextColor(Color.parseColor("#54A802"));
        }else{
            statement.setText("Niepodlany!!");
            statement.setTextColor(Color.parseColor("#8C031E"));
        }
        lastWatering.setText(element.get(2));
        String last = element.get(2);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            c.setTime(sdf.parse(last));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(element.get(3)));
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        nextWatering.setText(newDate);
        freqValue.setText(element.get(3));

        freqBar.setProgress(Integer.parseInt(element.get(3)));
        photoStr = element.get(4);
        new DownloadImageTask(image).execute(photoStr);
    }

    private void setAbilities(Boolean bool, int saveVisibility, int editVisibility){
        name.setEnabled(bool);
        species.setEnabled(bool);
        extraInfo.setEnabled(bool);
        freqBar.setEnabled(bool);
        saveButton.setVisibility(saveVisibility);
        editButton.setVisibility(editVisibility);
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bmImage.setImageBitmap(result);
        }
    }
}
