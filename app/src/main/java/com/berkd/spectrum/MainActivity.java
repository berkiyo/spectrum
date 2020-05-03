package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.InterruptedIOException;
import java.util.Random;

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
    private double[] frequencyVals = new double[count];        // Store the frequency values (recording only)

    // GRAPH VIEW
    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;



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


        // Initialise the chart / graph
        mChart = findViewById(R.id.graph);
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("Frequency vs Time Plot");
        mChart.setTouchEnabled(false); // disable touch functionality.
        mChart.setDragEnabled(false);
        //mChart.setScaleEnabled(false);
        //mChart.setDrawGridBackground(true);
        //mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);       // put the data into the chart

        startPlot();

        /**
         * Check if the microphone permission has been granted.
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

        }

    }

    public void startPlot() {
        if (thread != null) {
            thread.interrupt();
        }
        // Create a new thread if it is already running
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    addEntry();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Used to add an entry to the graph
     * @param frequency
     */
    public void addEntry() {
        LineData data = mChart.getData();

        if (data != null) {
            // append new data to the existing one
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            Random rand = new Random();
            data.addEntry(new Entry(set.getEntryCount(),  rand.nextInt()+ 5), 0); // add new data
            data.notifyDataChanged();
            mChart.setMaxVisibleValueCount(150);
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    public LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // smooths
        set.setCubicIntensity(0.2f);
        return set;
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


    /**
     * START RECORDING
     *  When the red button is pressed, it will start logging all the frequency and amplitude.
     *    - Store the amplitude and and frequency in two separate arrays
     *    - Once the recording is finished (5 seconds?) display the array onto the graph.
     */
    public void startRecord(View v) {

        int averageAmp = 0;
        double averageFreq = 0;
        String averageAmpStr;
        String averageFreqStr;

        // USING 50 points for now
        for (int i = 0; i < count; i++) {

            amplitudeVals[i] = audioCalculator.getAmplitude();
            frequencyVals[i] = audioCalculator.getFrequency();
        }

        // calculate the average
        for (int i = 0; i < amplitudeVals.length; i++) {
            averageAmp = averageAmp + amplitudeVals[i];
            averageAmp = averageAmp/count;

            averageFreq = averageFreq + frequencyVals[i];
            averageFreq = averageFreq/count;
        }


        averageAmpStr = "Average Amplitude = " + averageAmp;
        averageFreqStr = "Average Frequency = " + averageFreq;

        textAverageAmp.setText(averageAmpStr);
        textAverageFreq.setText(averageFreqStr);

        /**
         * GRAPH GOES HERE
         */






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

    @Override
    protected void onResume() {
        super.onResume();
        recorder.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }








}
