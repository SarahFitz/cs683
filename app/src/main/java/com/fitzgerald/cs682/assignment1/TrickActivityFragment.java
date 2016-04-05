package com.fitzgerald.cs682.assignment1;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrickActivityFragment extends Fragment {
    private int currentTrickNum;
    private static final Map<String, String> trickImageMap;
    private static final String TAG = "appLog TrickActivFrag";
    private View view;
    private ArrayList<String> tricksList;
    private ArrayList<ArrayList <String>> trickCompleteList = new ArrayList<>();
    private String dogName;

    //static maps which stores the name of the trick,
    // paired with the integer id of the image
    static {
        Map<String, String> map = new HashMap<>();
        map.put("Sit", String.valueOf(R.drawable.sit));
        map.put("Stand", String.valueOf(R.drawable.stand));
        map.put("Stay", String.valueOf(R.drawable.stay));
        map.put("Beg", String.valueOf(R.drawable.beg));
        map.put("Down", String.valueOf(R.drawable.down));
        map.put("Recall (Come)", String.valueOf(R.drawable.recall));
        trickImageMap = Collections.unmodifiableMap(map);
    }

    //Listener to be implemented by the activity
    TrickListener activityCallback;


    public interface TrickListener {
        //This passes the completed trick info back to the activity
        void tricksCompleted(ArrayList<ArrayList <String>> trickCompleteList);
    }

    //This is created, but not implemented yet
    //will be used to handle button clicks
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            activityCallback = (TrickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
            + " must implement TrickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreate");
        Log.i(TAG, " bundle values: " + getArguments().toString());

        //get the values from the bundle
        this.tricksList = getArguments().getStringArrayList("trickList");
        this.view = inflater.inflate(R.layout.fragment_trick, container, false);

        //set up the UI data to display the first trick in the list
        if(trickImageMap != null && this.tricksList !=null && view!= null){
            this.currentTrickNum = 0;
            setTrickData(currentTrickNum);
        }

        //add button listeners for the next and skip buttons
        assert this.view != null;
        Button nextButton = (Button) this.view.findViewById(R.id.nextButton);
        if(nextButton != null) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "nextButton onClick");
                    nextTrick();
                }
            });
        }

        Button skipButton = (Button) this.view.findViewById(R.id.skipButton);
        if(skipButton != null) {
            skipButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "skipButton onClick");
                    skipTrick();
                }
            });
        }

        return view;
    }

    /**
     * THis method will handle progression to the next
     * trick in the routine. It will increment the current
     * trick value and display the new trick.
     */
    public void nextTrick(){
        addToTrickCompleteList("Success");

        //if the last trick was finished go to the next activity
        if(this.currentTrickNum+1 == this.tricksList.size()){
            Log.i(TAG, "that was the last trick!");
            Log.i(TAG, "tricks completed list: "+ android.text.TextUtils.join(", ", this.trickCompleteList));
            activityCallback.tricksCompleted(this.trickCompleteList);
        }else{
            //increment the trick number and set the UI data
            this.currentTrickNum++;
            Log.i(TAG, "new trick number is: " + this.currentTrickNum);
            setTrickData(this.currentTrickNum);
        }
    }

    public void skipTrick(){
        addToTrickCompleteList("Skipped");

        //if the last trick was finished go to the next activity
        if(this.currentTrickNum+1 == this.tricksList.size()){
            Log.i(TAG, "that was the last trick!");
            Log.i(TAG, "tricks completed list: "+ android.text.TextUtils.join(", ", this.trickCompleteList));
            activityCallback.tricksCompleted(this.trickCompleteList);
        }else{
            //increment the trick number and set the UI data
            this.currentTrickNum++;
            Log.i(TAG, "new trick number is: " + this.currentTrickNum);
            setTrickData(this.currentTrickNum);
        }
    }

    private void addToTrickCompleteList(String message){
        ArrayList<String> trickCompleteData = new ArrayList<>();
        trickCompleteData.add(this.tricksList.get(currentTrickNum));
        trickCompleteData.add(message);
        this.trickCompleteList.add(trickCompleteData);
    }

    /**
     * Sets the image source and the trick name
     * based on the current trickNum from the tricklist
     * @param trickNum
     */
    private void setTrickData(int trickNum){
        //set the name of the initial trick
        TextView trickName = (TextView) this.view.findViewById(R.id.trickName);
        if(trickName != null){
            trickName.setText(this.tricksList.get(trickNum));
            Log.i(TAG, "New trick name: " + this.tricksList.get(trickNum));
            Log.i(TAG, "Trick number is: " + this.currentTrickNum);
        }

        //set the trick number
        TextView trickNumber = (TextView) this.view.findViewById(R.id.trickNumber);
        if(trickNumber!=null){
            trickNumber.setText("Trick "+ (this.currentTrickNum+1) + " out of " + this.tricksList.size() + ".");
        }

        //set the image of the initial trick
        ImageView trickImage = (ImageView) this.view.findViewById(R.id.trickImage);
        trickImage.setImageResource(Integer.parseInt(trickImageMap.get(this.tricksList.get(trickNum))));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }
}
