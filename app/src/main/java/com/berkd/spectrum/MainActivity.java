package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import static com.berkd.spectrum.R.id.textFrequency;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MediaPlayer player;
    private String audioFilePicked; // For the dropdown spinner (audioFile spinner)

    private Handler handler;
    private Recorder recorder;
    private AudioCalculator audioCalculator;

    private TextView textAmplitude;
    private TextView textDecibel;
    private TextView textFrequency;
    private TextView textStatus;
    private TextView textAverageFreq;
    private TextView textAverageAmp;
    private int count = 100;

    private int[] amplitudeVals = new int[count];        // Store the amplitude values (recording only)
    private double[] frequencyVals = new double[count];


    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAudioFileSpinner();
        initAudioSamplesSpinner();
        constructionDialog();


        recorder = new Recorder(callback);
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());

        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);
        //textStatus = (TextView) findViewById(R.id.textStatus); // display status of program

        textAverageAmp = findViewById(R.id.textAverageAmp);
        textAverageFreq = findViewById(R.id.textAverageFreq);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);
    }

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.getGridLabelRenderer().setLabelVerticalWidth(100);

        // first mSeries is a line
        mSeries = new LineGraphSeries<>();
        mSeries.setDrawDataPoints(true);
        mSeries.setDrawBackground(true);
        graph.addSeries(mSeries);
    }




    @Override
    public void onResume() {

        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 0.25d;
                mSeries.appendData(new DataPoint(graphLastXValue, getRandom()), true, 100);
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer, 500);
        recorder.start();
        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        recorder.stop();
        super.onPause();
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {

        //return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
        return Double.parseDouble(textAmplitude.getText().toString());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public void permissionDialog() {
        PermissionDialog permissionDialog = new PermissionDialog();
        permissionDialog.show(getSupportFragmentManager(), "permission dialog");
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






    /////////////////////////////////////////////////////////////////////////////////////////////////

    private Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            int amplitude = audioCalculator.getAmplitude();
            double decibel = audioCalculator.getDecibel();
            double frequency = audioCalculator.getFrequency();

            final String amp = String.valueOf(amplitude);
            final String db = String.valueOf(decibel);
            final String hz = String.valueOf(frequency);



            handler.post(new Runnable() {
                @Override
                public void run() {
                    textAmplitude.setText(amp);
                    textDecibel.setText(db);
                    textFrequency.setText(hz);

                }
            });
        }
    };









}
