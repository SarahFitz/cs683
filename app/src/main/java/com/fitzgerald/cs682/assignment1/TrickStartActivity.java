package com.fitzgerald.cs682.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class TrickStartActivity extends AppCompatActivity {

    private static final String TAG = "TrickStartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_start);

        //get the data sent with the intent
        Intent intent = getIntent();
        List<String> tricks = intent.getStringArrayListExtra("tricks");
        //TODO: validate populated list and not null

        Log.i(TAG, "The dog knows the tricks: " + android.text.TextUtils.join(", ", tricks));

        String trickMessage = "Your dog knows " + tricks.size() + " unique tricks!";
        TextView trickTextMessage = (TextView) findViewById(R.id.trickTextMessage);
        trickTextMessage.setText(trickMessage);

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
