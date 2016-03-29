package com.fitzgerald.cs682.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class TrickStartActivity extends AppCompatActivity {

    private static final String TAG = "TrickStartActivity";
    private TextView trickSeekBarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_start);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        List<String> tricks = intent.getStringArrayListExtra("tricks");
        if(tricks !=null && tricks.size()>0){
            //TODO: validate populated list and not null

            Log.i(TAG, "The dog knows the tricks: " + android.text.TextUtils.join(", ", tricks));

            //Add the text message based on the number of tricks the user selected
            String trickMessage = "Your dog knows " + tricks.size() + " unique tricks!";
            TextView trickTextMessage = (TextView) findViewById(R.id.trickTextMessage);
            if(trickTextMessage != null){
                trickTextMessage.setText(trickMessage);
            }

            //set the seek bar max length based to be Nx2 the number of tricks the user entered
            SeekBar trickSeekBar = (SeekBar) findViewById(R.id.trickSeekBar);
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
        }
        //TODO: setup click event for next states
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
