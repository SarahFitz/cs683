package com.fitzgerald.cs682.assignment1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrickResultsActivity extends AppCompatActivity {
    private static final String TAG = "appLog TrickResultActiv";
    private int successfulTricks = 0;
    private int totalTricks = 0;
    private String dogName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_results);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        long routineId = intent.getExtras().getLong("routineId");
        Log.i(TAG, "dogId = " + routineId);

        if(routineId >= 0) {
            TrickDBHelper dbHelper = new TrickDBHelper(getApplicationContext());

            //query DB and parse trick results
            getTrickResults(routineId, dbHelper);

            TextView heading = (TextView) findViewById(R.id.trickResultsHeading);
            heading.setText(this.dogName + " completed " + this.successfulTricks + " out of " + this.totalTricks + "total tricks!");
        }
    }


    /**
     * Run query to return tricks results for the dog's routine
     * @param routineId
     * @param dbHelper
     * @return
     */
    private Cursor getTrickResults(long routineId, TrickDBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT trickRoutine.id, trickRoutine.dogName, trick.trickName, trick.trickResult FROM " +
                dbHelper.ROUTINE_TABLE_NAME+" INNER JOIN "+dbHelper.TRICK_TABLE_NAME+
                " ON trickRoutine.id=trick.dogId WHERE trick.dogId=?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(routineId)});

        parseTrickResults(c, dbHelper);

        db.close();

        return c;
    }

    /**
     * Step through the cursor to parse data
     * creates a text view and adds it to the layout for each row
     * gets the dog name and updates the total and successful number of tricks.
     * @param c
     * @param dbHelper
     */
    private void parseTrickResults(Cursor c, TrickDBHelper dbHelper){
        c.moveToFirst();
        this.dogName = c.getString(c.getColumnIndex(dbHelper.ROUTINE_FIELD2));
        View resultsLayout = findViewById(R.id.resultLayout);

        while (!c.isAfterLast()){
            //get the trick name and status
            String trickName = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD2));
            String trickResult = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD3));
            Log.i(TAG, "Trick data = " + trickName + " : " + trickResult);

            //create a text view and set custom style and text
            TextView result = new TextView(new ContextThemeWrapper(this, R.style.TrickResultTextView), null, 0);
            result.setBackground(getResources().getDrawable(R.drawable.trick_skipped_background));
            result.setText("The trick " + trickName + " was " + trickResult + "!");

            //set background based on trick result
            if(trickResult.equals("Skipped")){
                result.setTextColor(getResources().getColor(R.color.pink2));
            }else{
                result.setTextColor(getResources().getColor(R.color.green1));
                this.successfulTricks ++;
            }

            //add the trick results to the layout with the separator
            ((LinearLayout)resultsLayout).addView(result);
            this.totalTricks ++;

            c.moveToNext();
        }
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
