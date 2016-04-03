package com.fitzgerald.cs682.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class TrickActivity extends Activity implements  TrickActivityFragment.TrickListener{

    private static final String TAG = "appLog TrickActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        ArrayList<String> tricks = intent.getStringArrayListExtra("toDoTricks");

        Log.i(TAG, "The dog knows the tricks: " + android.text.TextUtils.join(", ", tricks));
        if (tricks != null && tricks.size() > 0) {

            Log.i(TAG, "The training list of tricks: " + TextUtils.join(", ", tricks));
            //setup the fragment
            TrickActivityFragment fragment = new TrickActivityFragment();

            //past the trick list to the fragment in a bundle
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("trickList", tricks);
            fragment.setArguments(bundle);
            Log.i(TAG, " bundle values: " + fragment.getArguments().toString());

            getFragmentManager().beginTransaction().add(R.id.activityTrick, fragment).commit();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
}
