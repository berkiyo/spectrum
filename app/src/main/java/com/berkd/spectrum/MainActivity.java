package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
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

    MediaPlayer player;
    String audioFilePicked; // For the dropdown spinner (audioFile spinner)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAudioFileSpinner();
        initAudioSamplesSpinner();
        initAudioSourceSpinner();

        constructionDialog();

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

    public void constructionDialog() {
        ConstructionDialog constructionDialog = new ConstructionDialog();
        constructionDialog.show(getSupportFragmentManager(), "construction dialog");
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
                helpDialog();
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

    /**
     * WHEN ITEM SELECTED FOR SPINNERS
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        audioFilePicked = text; // Set the audio file picked for player

        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * PLAY SOUND
     * When play is pressed, play the sound until it is finished or STOP is pressed
     */
    public void playSound(View v) {
        // If created, we don't want to create another one
        if (player == null) {

            switch (audioFilePicked) {
                case "20kHz Sweep":
                    player = MediaPlayer.create(this, R.raw.twentythousand);
                    player.start();
                    break;
                case "5kHz Tone":
                    player = MediaPlayer.create(this, R.raw.fivekhz);
                    player.start();
                    break;
                case "8kHz Tone":
                    player = MediaPlayer.create(this, R.raw.eightkhz);
                    player.start();
                    break;

            }


            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }


    }

    public void stopSound(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Audio Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}
