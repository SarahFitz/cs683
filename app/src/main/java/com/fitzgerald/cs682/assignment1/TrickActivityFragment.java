package com.fitzgerald.cs682.assignment1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String TAG = "TrickActivityFragment";

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

    TrickListener activityCallback;

    public interface TrickListener {
        //TODO: Do something
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            activityCallback = (TrickListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
            + " must implement TrickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreate");
        Log.i(TAG, " bundle values: " + getArguments().toString());
        ArrayList<String> tricksList = getArguments().getStringArrayList("trickList");

        View view = inflater.inflate(R.layout.fragment_trick, container, false);

        if(trickImageMap != null && tricksList !=null && view!= null){
            currentTrickNum = 0;
            TextView trickName = (TextView) view.findViewById(R.id.trickName);
            if(trickName != null){
                trickName.setText(tricksList.get(currentTrickNum));
            }

            ImageView trickImage = (ImageView) view.findViewById(R.id.trickImage);
            trickImage.setImageResource(Integer.parseInt(trickImageMap.get(tricksList.get(currentTrickNum))));

        }

        return view;
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
