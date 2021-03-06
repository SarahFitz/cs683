package com.fitzgerald.cs682.assignment1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TrickSelectionActivity extends AppCompatActivity {
    private static final String TAG = "appLog TrickSelectActiv";
    private String dogName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_selection);

        //create intent to instantiate new activity
        final Intent selectDogIntent = new Intent(this, SelectDogActivity.class);

        //Set up click event for button to navigate to next activity
        Button submitTricksButton = (Button) findViewById(R.id.submitTricks);
        if(submitTricksButton != null) {
            submitTricksButton.setOnClickListener(new View.OnClickListener() {
                //when button is clicked, start the next activity
                public void onClick(View v) {
                    Log.i(TAG, "submitTrickButton onClick validate");
                    List<String> tricks = validateSubmission();

                    //add profile to internal file storage
                    boolean isNameValid = validateDogName();


                    //check that tricks were selected
                    if((tricks != null && tricks.size() > 0) && isNameValid){
                        Log.i(TAG, "submitTrickButton onClick startActivity");
                        addDogProfile(tricks, dogName);
                        startActivity(selectDogIntent);
                    }else if(!isNameValid){
                        createErrorAlert("Please provide a valid dog name.");
                    }else if(tricks == null || tricks.size() <= 0){
                        createErrorAlert("Please select at least one trick.");
                    }else{
                        createErrorAlert("Something went wrong. " +
                                "Please restart the app and try again.");
                    }
                }
            });
        }
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

    private boolean validateDogName(){
        EditText dogNameField = (EditText) findViewById(R.id.dogNameField);
            if(dogNameField != null){
                String name = dogNameField.getText().toString();

                if(name != null && !name.isEmpty()){
                    this.dogName = name;
                }else{
                    return false;
                }
        }

        return true;
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

    private void addDogProfile(List<String> tricks, String dogName){
        Log.i(TAG, "the dog name is: "+ dogName);

        String filename = "dogProfiles.txt";
        String string = dogName + "," + android.text.TextUtils.join(",", tricks)+"/n";
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND | Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
