package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    /**
     * Database Stuff
     */
    DatabaseHelper myDB;
    Button buttonData;

    private MediaPlayer player;

    private Handler handler;
    private Recorder recorder;
    private AudioCalculator audioCalculator;

    private TextView textAmplitude;
    private TextView textFrequency;

    private Spinner spinner_datarate;
    private Spinner spinner_audiofile;
    private Spinner spinner_samplespeed;

    private int speed = 100;
    private int audioFilePicked = 1; // For the dropdown spinner (audioFile spinner)
    private int myDataPoints = 50; // used to set the precision of the amplitude accuracy.

    private boolean buttonFreezeClicked = false;

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeries;

    private TextView graphType; // used to store the amplitude OR frequency type.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDB = new DatabaseHelper(this); // setup and load database
        buttonData = findViewById(R.id.buttonData); // button declared here! o_o

        /**
         * Initialise the spinners
         */
        spinner_datarate = findViewById(R.id.spinner_datarate);
        initDataRateSpinner();
        spinner_audiofile = findViewById(R.id.spinner_audiofile);
        initAudioFileSpinner();
        spinner_samplespeed = findViewById(R.id.spinner_samplespeed);
        initSampleSpeedSpinner();


        //constructionDialog(); // TODO: Get rid of it once done (or just comment it out)

        recorder = new Recorder(callback);
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());


        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textFrequency = (TextView) findViewById(R.id.textFrequency);
        //textStatus = (TextView) findViewById(R.id.textStatus); // display status of program

        graphType = textAmplitude; // By default, this is set to amplitude.


        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);

        // Ask for microphone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

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

        runGraph();
        recorder.start();
        super.onResume();
    }

    @Override
    public void onPause() {
        pauseGraph();
        recorder.stop();
        super.onPause();
    }

    private double getFreqAmpVal() {
        return Double.parseDouble(graphType.getText().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    /*******************************************
     * Run Graph
     *  Run the graph. With a 50ms delay.
     */
    public void runGraph() {
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 0.25d;
                mSeries.appendData(new DataPoint(graphLastXValue, getFreqAmpVal()), true, myDataPoints);
                mHandler.postDelayed(this, speed);
            }
        };
        mHandler.postDelayed(mTimer, 50);
    }

    /****************************
     * Pause Graph
     *      Self Explantory.
     */
    public void pauseGraph() {
        mHandler.removeCallbacks(mTimer); // pause the graph
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    public void saveTheData(String input) {
        View inflatedView = getLayoutInflater().inflate(R.layout.saved_data_activity, null);
        ListView listView = findViewById(R.id.mainList);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(input);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                arrayList);

        listView.setAdapter(arrayAdapter);

    }

    /***********************************
     * Show the listview thingy
     */
    public void showDataSaved() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.saved_data_activity, null);


        pauseGraph();

        final EditText mEditText = (EditText) mView.findViewById(R.id.saveTextField);
        final String textEntered = mEditText.toString(); // convert to string

        mBuilder.setView(mView);



        mBuilder.setTitle("Saved Data");

        mBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Run timer again.
                runGraph();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    /**
     * WHEN OPTIONS MENU IS SELECT, CURRENTLY DOES NOT MUCH BUT WIP
     * TODO -> Get these functions things implemented (CURRENTLY WIP)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Save data
            case R.id.item1:
                newEntry();
                break;

            // help
            case R.id.item2:
                helpDialog();
                break;

            case R.id.item3:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Toast.makeText(MainActivity.this, "Keeping screen on!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item4:
                Intent intent = new Intent(MainActivity.this, ViewListContents.class);
                startActivity(intent);
                break;

            case R.id.item5:
                aboutDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * INIT AUDIO FILE SPINNER
     * Initialise "Audio File" the spinner for use
     */
    public void initAudioFileSpinner() {
        final ArrayAdapter<CharSequence> audioFileAdapter = ArrayAdapter.createFromResource(this, R.array.audioFile,
                android.R.layout.simple_spinner_item);
        audioFileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_audiofile.setAdapter(audioFileAdapter);


        // this is the listener, use this to read the selected item on the spinner
        spinner_audiofile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        audioFilePicked = 1;
                        break;
                    case 1:
                        audioFilePicked = 2;
                        break;
                    case 2:
                        audioFilePicked = 3;
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Feature coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        audioFilePicked = 1;
                        break;

                }             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    /**
     * INIT AUDIO DATA RATE SPINNER
     * Initialise "Audio Samples" the spinner for use
     */
    public void initDataRateSpinner() {
        ArrayAdapter<CharSequence> audioSampleAdapter = ArrayAdapter.createFromResource(this, R.array.graphtype,
                android.R.layout.simple_spinner_item);
        audioSampleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_datarate.setAdapter(audioSampleAdapter);


        // this is the listener, use this to read the selected item on the spinner
        spinner_datarate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // TODO: Implement the functionality.

                switch (position) {
                    case 0:
                        pauseGraph();
                        graphType = textAmplitude;
                        mSeries.setColor(Color.BLUE);
                        runGraph();
                        break;
                    case 1:
                        pauseGraph();
                        graphType = textFrequency;
                        mSeries.setColor(Color.RED);
                        runGraph();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Uh oh!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }




    /**
     * INIT AUDIO SAMPLES SPINNER
     * Initialise "Audio Samples" the spinner for use
     */
    public void initSampleSpeedSpinner() {
        ArrayAdapter<CharSequence> audioSampleAdapter = ArrayAdapter.createFromResource(this, R.array.speed,
                android.R.layout.simple_spinner_item);
        audioSampleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_samplespeed.setAdapter(audioSampleAdapter);

        // this is the listener, use this to read the selected item on the spinner
        spinner_samplespeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {
                    case 0:
                        speed = 10;
                        break;
                    case 1:
                        speed = 25;
                        break;
                    case 2:
                        speed = 50;
                        break;
                    case 3:
                        speed = 100;
                        break;
                    case 4:
                        speed = 200;
                        break;
                    case 5:
                        speed = 300;
                        break;
                    case 6:
                        speed = 500;
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "uhh!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
    // MEDIA STUFF


    /**
     * PLAY SOUND
     * When play is pressed, play the sound until it is finished or STOP is pressed
     */
    public void playSound(View v) {
        if (player == null) {

            switch (audioFilePicked) {

                case 1:
                    player = MediaPlayer.create(this, R.raw.twentythousand);
                    player.start();
                    break;


                case 2:
                    player = MediaPlayer.create(this, R.raw.fivekhz);
                    player.start();
                    break;


                case 3:
                    player = MediaPlayer.create(this, R.raw.eightkhz);
                    player.start();
                    break;

                default:
                    player = MediaPlayer.create(this, R.raw.twentythousand);
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


    /**
     * STOP SOUND
     * - Stop the sound.
     *
     * @param v
     */
    public void stopSound(View v) {
        stopPlayer();
    }


    /**
     * STOP PLAYER
     * - Stop the music player.
     */
    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Audio Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * STOP AUDIO
     * - Used to stop the audio playback
     */
    public void stopAudio(View view) {
        stopPlayer();
    }
    // END OF MEDIA STUFF
////////////////////////////////////////////////////////////////////////////////////////////////////






    // FREEZE CLICKED
    public void freezeClicked(View view) {

        if (buttonFreezeClicked) {
            runGraph();
            // set it back to normal state
            buttonFreezeClicked = false;

        } else {
            pauseGraph();
            buttonFreezeClicked = true;
        }

    }

    /**
     * CALLBACK
     * - Used to display the amplitude and frequency onto the screen.
     */
    private Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            int amplitude = audioCalculator.getAmplitude();
            double frequency = audioCalculator.getFrequency();

            final String amp = String.valueOf(amplitude);
            final String hz = String.valueOf(frequency);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    textAmplitude.setText(amp);
                    textFrequency.setText(hz);

                }
            });
        }
    };
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * DATABASE STUFF HERE
     */

    public void addData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if(insertData == true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }


    public void newEntry() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView = getLayoutInflater().inflate(R.layout.save_popup, null); // must be declared final!!!


        pauseGraph();

        final EditText mEditText = findViewById(R.id.saveTextField);
        mBuilder.setView(mView);
        mBuilder.setTitle("Save Graph");


        mBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance(); // Get current time and make it the subtext
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()); // format date

                EditText editText = mView.findViewById(R.id.saveTextField); // this is the way. Don't forget the mView part!

                // ADD THE TEXT!!!
                addData("\nTitle: " + editText.getText().toString() +"\n\nLast Frequency = " + textFrequency.getText().toString() +
                        "\nLast Amplitude = " + textAmplitude.getText().toString() +
                        "\n\n" + currentDate + "\n");


                runGraph();
                dialog.dismiss();

            }
        });

        mBuilder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                runGraph();
                dialog.dismiss();
            }

        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }



    /**
     * DATA VIEW BUTTON CLICKED
     *  - Show the data
     */
    public void buttonDBClicked(View v) {
        Intent intent = new Intent(MainActivity.this, ViewListContents.class);
        startActivity(intent);
    }

}
