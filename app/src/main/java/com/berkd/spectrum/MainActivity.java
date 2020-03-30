package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static int sampleRate = 44100;
    static int freqOfTone = 20000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAudioFileSpinner();
        initAudioSamplesSpinner();
        initAudioSourceSpinner();
    }


    /**
     * About_Dialog
     * TODO: Hyperlink to my website for a more detailed.
     */
    public void aboutDialog() {
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(getSupportFragmentManager(), "about dialog");
    }

    /**
     * Help_Dialog
     * TODO: Need to create a nice tutorial on how to the use the app.
     */
    public void helpDialog() {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.show(getSupportFragmentManager(), "help dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }


    /**
     * WHEN OPTIONS MENU IS SELECT, CURRENTLY DOES NOT MUCH BUT WIP
     * TODO -> Get these functions things implemented (CURRENTLY WIP)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // item1
            case R.id.item1:
                aboutDialog();
                break;

            // item3
            case R.id.item2:
                aboutDialog();
                break;

            case R.id.item3:
                aboutDialog();
                break;

            case R.id.item4:
                aboutDialog();
                break;

            case R.id.item5:
                aboutDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * INIT AUDIO FILE SPINNER
     *  Initialise "Audio File" the spinner for use
     */
    public void initAudioFileSpinner() {
        Spinner audioFileSpinner = findViewById(R.id.spinner_audiofile);
        ArrayAdapter<CharSequence> audioFileAdapter = ArrayAdapter.createFromResource(this, R.array.audioFile,
                android.R.layout.simple_spinner_item);
        audioFileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audioFileSpinner.setAdapter(audioFileAdapter);
        audioFileSpinner.setOnItemSelectedListener(this);
    }

    /**
     * INIT AUDIO SAMPLES SPINNER
     *  Initialise "Audio Samples" the spinner for use
     */
    public void initAudioSamplesSpinner() {
        Spinner audioSampleSpinner = findViewById(R.id.spinner_samples);
        ArrayAdapter<CharSequence> audioSampleAdapter = ArrayAdapter.createFromResource(this, R.array.sampleRate,
                android.R.layout.simple_spinner_item);
        audioSampleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audioSampleSpinner.setAdapter(audioSampleAdapter);
        audioSampleSpinner.setOnItemSelectedListener(this);
    }

    /**
     * INIT AUDIO SOURCE SPINNER
     *  Initialise "Audio Source" the spinner for use
     */
    public void initAudioSourceSpinner() {
        Spinner audioSourceSpinner = findViewById(R.id.spinner_audiosource);
        ArrayAdapter<CharSequence> audioSourceAdapter = ArrayAdapter.createFromResource(this, R.array.sources,
                android.R.layout.simple_spinner_item);
        audioSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audioSourceSpinner.setAdapter(audioSourceAdapter);
        audioSourceSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
