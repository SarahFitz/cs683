package com.fitzgerald.cs682.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class TrickSelectionActivity extends AppCompatActivity {
    private static final String TAG = "TrickSelectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_selection);

        //Set up click event for button to navigate to next activity
        Button submitTricksButton = (Button) findViewById(R.id.submitTricks);
        //create intent to instantiate new activity
        final Intent trickStartIntent = new Intent(this, TrickStartActivity.class);
        submitTricksButton.setOnClickListener(new View.OnClickListener() {
            //when button is clicked, start the next activity
            public void onClick(View v) {
                Log.i(TAG, "submitTrickButton onClick validate");
                List<String> tricks = validateSubmission();

                //check that tricks were selected
                if(tricks.size() > 0){
                    Log.i(TAG, "submitTrickButton onClick startActivity");
                    //add the list of tricks to the intent to send to next activity
                    trickStartIntent.putStringArrayListExtra("tricks", (ArrayList)tricks);
                    startActivity(trickStartIntent);
                }else{
                    //TODO: some error scenario
                }

            }
        });
    }


    private List<String> validateSubmission(){
        List<String> tricks = new ArrayList<>();
        List<CheckBox> checkBoxes = getCheckBoxes();

        //check if each checkbox is checked, if yes add the trick to the list
        for(CheckBox check : checkBoxes){
            if (check.isChecked()){
                Log.i(TAG, "User checked the box: " + check.getText());
                tricks.add((String)check.getText());
            }
        }

        //send back the list of tricks
        return tricks;
    }

    @NonNull
    private List<CheckBox> getCheckBoxes() {
        List<CheckBox> checkBoxes = new ArrayList<>();

        checkBoxes.add((CheckBox) findViewById(R.id.sitCheckBox));
        checkBoxes.add((CheckBox) findViewById(R.id.stayCheckBox));
        checkBoxes.add((CheckBox) findViewById(R.id.downCheckBox));
        checkBoxes.add((CheckBox) findViewById(R.id.standCheckBox));
        checkBoxes.add((CheckBox) findViewById(R.id.begCheckBox));
        checkBoxes.add((CheckBox) findViewById(R.id.comeCheckBox));
        return checkBoxes;
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
