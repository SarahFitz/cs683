package com.fitzgerald.cs682.assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectDogActivity extends Activity {
    private static final String TAG = "appLog SelectDogActiv";
    //TODO: remove this eventually
    ArrayList<String> dogNames;
    ArrayList<ArrayList <String>> dogProfiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dog);

        setSpinnerData();

        //Set up click event for ADD dog button
        Button addNewDog = (Button) findViewById(R.id.addNewDogButton);

        addNewDog.setOnClickListener(new View.OnClickListener() {
            //when button is clicked, start the next activity
            public void onClick(View v) {
                Log.i(TAG, "addNewDog onClick");
                addDog();
            }
        });

        //Set up click event for SELECT dog button
        Button selectDogButton = (Button) findViewById(R.id.selectDogButton);
        selectDogButton.setOnClickListener(new View.OnClickListener() {
            //when button is clicked, start the next activity
            public void onClick(View v) {
                Log.i(TAG, "selectDog onClick");
                selectDog();
            }
        });

        //Set up click event for DELETE dog button
        Button deleteDogButton = (Button) findViewById(R.id.deleteDogButton);
        deleteDogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "deleteDog onClick");
                deleteDog();
            }
        });
    }

    private void addDog(){
        EditText dogNameField = (EditText) findViewById(R.id.newDogName);
        String name = dogNameField.getText().toString();

        if(name == null || name.isEmpty()){
            //TODO: error
        }else{
            final Intent trickSelectIntent = new Intent(this, TrickSelectionActivity.class);
            trickSelectIntent.putExtra("dogName", name);
            startActivity(trickSelectIntent);
        }

    }

    private void deleteDog(){
        //get selected dog for deleting
        Spinner spinner = (Spinner) findViewById(R.id.dogSpinner);
        String name = this.dogNames.get(spinner.getSelectedItemPosition());
        dogNames.remove(spinner.getSelectedItemPosition());

        String filename = "dogProfiles.txt";
        FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                for(ArrayList<String> profile : this.dogProfiles){
                    //only write the profiles to the file which don't match the one for deletion
                    if(!profile.get(0).equals(name)){
                        String profileString = android.text.TextUtils.join(",", profile)+"/n";
                        outputStream.write(profileString.getBytes());
                    }
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        //update the spinner data
        setSpinnerData();
    }

    private void selectDog(){
        //get selected dog for deleting
        Spinner spinner = (Spinner) findViewById(R.id.dogSpinner);
        String name = this.dogNames.get(spinner.getSelectedItemPosition());

        final Intent trickStartIntent = new Intent(this, TrickStartActivity.class);
        trickStartIntent.putExtra("dogName", name);

        //get the list of tricks for the selected dog
        ArrayList<String> tricks = new ArrayList<>();
        for(ArrayList<String> dog : this.dogProfiles){
            if(dog.get(0).equals(name)){
                tricks = dog;
                tricks.remove(0);
            }
        }
        trickStartIntent.putStringArrayListExtra("tricks", (ArrayList) tricks);
        startActivity(trickStartIntent);
    }

    private void setSpinnerData(){
        //get the list of dog names from the file storage
        this.dogNames = getDogNames();

        Spinner spinner = (Spinner) findViewById(R.id.dogSpinner);
        Button deleteDogButton = (Button) findViewById(R.id.deleteDogButton);
        Button selectDogButton = (Button) findViewById(R.id.selectDogButton);

        if(dogNames.size() <= 0){
            //if no dogs exist, hide these features
            spinner.setVisibility(View.INVISIBLE);
            deleteDogButton.setVisibility(View.INVISIBLE);
            selectDogButton.setVisibility(View.INVISIBLE);
        }else{
            //dogs exist, so show these features
            spinner.setVisibility(View.VISIBLE);
            deleteDogButton.setVisibility(View.VISIBLE);
            selectDogButton.setVisibility(View.VISIBLE);

            //set the dog profile names as the data for the spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dogNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    /**
     * Get a list of only the dog names from the dog profiles
     * @return
     */
    public ArrayList<String> getDogNames(){
        ArrayList<ArrayList <String>> dogProfiles = getDogProfiles();
        ArrayList<String> dogNames = new ArrayList<>();

        if(!dogProfiles.isEmpty()){
            for(ArrayList<String> profile : dogProfiles){
                dogNames.add(profile.get(0));
            }
        }

        return dogNames;
    }

    /**
     * Get a list of all the dog profiles
     * @return
     */
    public ArrayList< ArrayList <String>> getDogProfiles(){
        ArrayList< ArrayList <String>> dogProfiles = new ArrayList<>();

        try {
            InputStream inputStream = openFileInput("dogProfiles.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                //add each dog profile from the file to the array list
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String[] profiles = android.text.TextUtils.split(receiveString, "/n");
                    for(int i = 0; i<profiles.length-1; i++){
                        String[] profile = android.text.TextUtils.split(profiles[i], ",");
                        Log.i(TAG, "Dog profile from FILE : " + profile[i]);
                        dogProfiles.add(new ArrayList<>(Arrays.asList(profile)));
                    }
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        this.dogProfiles = dogProfiles;
        return dogProfiles;
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
