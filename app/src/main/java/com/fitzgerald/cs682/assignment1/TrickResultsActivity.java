package com.fitzgerald.cs682.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrickResultsActivity extends Activity {
    private static final String TAG = "appLog TrickResultActiv";

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
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String query = "SELECT trickRoutine.id, trickRoutine.dogName, trick.trickName, trick.trickResult FROM " +
                    dbHelper.ROUTINE_TABLE_NAME+" INNER JOIN "+dbHelper.TRICK_TABLE_NAME+
                    " ON trickRoutine.id=trick.dogId WHERE trick.dogId=?";
            Cursor c = db.rawQuery(query, new String[]{String.valueOf(routineId)});

            c.moveToFirst();
            String dogName = c.getString(c.getColumnIndex(dbHelper.ROUTINE_FIELD2));
            View resultsLayout = findViewById(R.id.resultLayout);

            while (!c.isAfterLast()){
                //get the trick name and status
                String trickName = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD2));
                String trickResult = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD3));
                Log.i(TAG, "Trick data = " + trickName + " : " + trickResult);

                //create a text view to display the trick results
                TextView result = new TextView(this);
                result.setText("The trick " + trickName + " was " + trickResult + "!");
                if(trickResult.equals("Skipped")){
                    result.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else{
                    result.setBackgroundColor(getResources().getColor(R.color.success));
                }

                //add the trick results to the layout
                ((LinearLayout)resultsLayout).addView(result);

                c.moveToNext();
            }

            db.close();
        }
    }

}
