package com.fitzgerald.cs682.assignment1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrickStartActivity extends AppCompatActivity {

    private static final String TAG = "appLog TrickStartActiv";
    private TextView trickSeekBarValue;
    private List<String> tricks;
    private String dogName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_start);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        tricks = intent.getStringArrayListExtra("tricks");
        dogName = intent.getExtras().getString("dogName");

        if(tricks !=null && tricks.size()>0){
            Log.i(TAG, "The dog knows the tricks: " + android.text.TextUtils.join(", ", tricks));

            //Add the text message based on the number of tricks the user selected
            String trickMessage = dogName + " knows " + tricks.size() + " unique tricks!";
            TextView trickTextMessage = (TextView) findViewById(R.id.trickTextMessage);
            if(trickTextMessage != null){
                trickTextMessage.setText(trickMessage);
            }

            //set the seek bar max length based to be Nx2 the number of tricks the user entered
            final SeekBar trickSeekBar = (SeekBar) findViewById(R.id.trickSeekBar);
            if(trickSeekBar != null){
                trickSeekBar.setMax(tricks.size() * 2);

                //display the seek bar value as the user is picking it
                trickSeekBarValue = (TextView) findViewById(R.id.trickSeekBarValue);
                if(trickSeekBarValue !=null){
                    trickSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                            if(trickSeekBarValue != null){
                                trickSeekBarValue.setText(String.valueOf(progress));
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar){
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar){
                        }

                    });
                }
            }

            //create intent to instantiate new activity
            final Intent trickIntent = new Intent(this, TrickActivity.class);

            //Set up click event for button to navigate to next activity
            Button startTrainingButton = (Button) findViewById(R.id.startTrainingButton);
            if(startTrainingButton != null) {
                startTrainingButton.setOnClickListener(new View.OnClickListener() {
                    //when button is clicked, start the next activity
                    public void onClick(View v) {
                        Log.i(TAG, "startTrainingButton onClick");

                        //get the number of tricks they want to perform
                        int numTricks = 0;
                        trickSeekBarValue = (TextView) findViewById(R.id.trickSeekBarValue);
                        if(trickSeekBarValue !=null){
                            numTricks = trickSeekBar.getProgress();
                            Log.i(TAG, "num tricks = "+numTricks);
                        }

                        if (numTricks == 0){
                            createErrorAlert("Please select at least one trick.");
                        }else{
                            //generate the list of tricks based on the number they chose
                            ArrayList<String> toDoTricks = new ArrayList<>();
                            Random rand = new Random();
                            Log.i(TAG, "Random tricks list");
                            for( int i = 0; i < numTricks; i++){
                                toDoTricks.add(tricks.get(rand.nextInt(tricks.size())));
                                Log.i(TAG, "Random tricks = "+toDoTricks.get(i));
                            }

                            //pass the list of tricks to do to the next activity
                            trickIntent.putStringArrayListExtra("toDoTricks", toDoTricks);
                            trickIntent.putExtra("dogName", dogName);
                            startActivity(trickIntent);
                        }
                    }
                });
            }
        }else{
            createErrorAlert("Something went wrong.");
        }

    }

    /**
     * Helper method to create and show error dialog messages
     * @param message
     */
    private void createErrorAlert(String message){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // Create the AlertDialog object and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

}
