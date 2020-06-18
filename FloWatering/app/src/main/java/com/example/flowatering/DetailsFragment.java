package com.example.flowatering;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

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


public class DetailsFragment extends Fragment {

    private EditText name, species, extraInfo;
    private TextView lastWatering, nextWatering, freqValue;
    private Button saveButton, editButton, confirmButton;
    private SeekBar freqBar;
    private ImageView image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        image = rootView.findViewById(R.id.image);
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

        String url = "https://www.ikea.com/pl/pl/images/products/monstera-potted-plant__0653991_PE708220_S5.JPG?f=s";
        new DownloadImageTask(image).execute(url);

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

    void setText(String txt) {
        TextView view = (TextView) getView().findViewById(R.id.detailsText);
        view.setText(txt);
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
            bmImage.setImageBitmap(result);
        }
    }
}
